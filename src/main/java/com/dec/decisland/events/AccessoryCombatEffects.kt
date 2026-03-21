package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.CustomItemProperties.CooldownBonus
import com.dec.decisland.item.ItemConfig.WeaponCooldownCategory
import com.dec.decisland.item.custom.accessory.AccessoryProcItem
import com.dec.decisland.mixin.ItemCooldownsAccessor
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import com.dec.decisland.tag.ModItemTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemCooldowns
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.EnumMap
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object AccessoryCombatEffects {
    private data class EquippedAccessory(
        val hand: InteractionHand,
        val stack: ItemStack,
        val bonus: CooldownBonus,
    )

    private data class AccessoryProcCooldownKey(
        val playerId: UUID,
        val procKey: String,
    )

    private data class CooldownStateKey(
        val playerId: UUID,
        val clientSide: Boolean,
    )

    private val cooldownAccumulators: MutableMap<CooldownStateKey, EnumMap<WeaponCooldownCategory, Double>> = ConcurrentHashMap()
    private val accessoryProcCooldowns: MutableMap<AccessoryProcCooldownKey, Long> = ConcurrentHashMap()

    private val cooldownInstanceClass: Class<*> = Class.forName("net.minecraft.world.item.ItemCooldowns\$CooldownInstance")
    private val cooldownStartTimeMethod: Method =
        cooldownInstanceClass.getDeclaredMethod("startTime").apply { isAccessible = true }
    private val cooldownEndTimeMethod: Method =
        cooldownInstanceClass.getDeclaredMethod("endTime").apply { isAccessible = true }
    private val cooldownInstanceConstructor: Constructor<*> =
        cooldownInstanceClass.getDeclaredConstructor(Int::class.javaPrimitiveType, Int::class.javaPrimitiveType).apply { isAccessible = true }

    @SubscribeEvent
    @JvmStatic
    fun onPlayerTick(event: PlayerTickEvent.Post) {
        val player = event.entity
        if (player.level().isClientSide) {
            return
        }

        tickCooldownAcceleration(player)
    }

    @JvmStatic
    fun tickClientPlayer(player: Player) {
        tickCooldownAcceleration(player)
    }

    @JvmStatic
    fun onSuccessfulWeaponUse(player: Player, weaponStack: ItemStack) {
        if (player.level().isClientSide) {
            return
        }

        if (getCooldownCategory(weaponStack) != WeaponCooldownCategory.MISSILE) {
            return
        }

        val serverLevel = player.level() as? ServerLevel ?: return
        val accessory = getBestAccessory(player, WeaponCooldownCategory.MISSILE) ?: return
        val procItem = accessory.stack.item as? AccessoryProcItem ?: return
        val cooldownKey = AccessoryProcCooldownKey(player.uuid, procItem.accessoryProcCooldownKey)
        val nextAllowedTick = accessoryProcCooldowns[cooldownKey] ?: 0L
        if (serverLevel.gameTime < nextAllowedTick) {
            return
        }

        if (player.random.nextDouble() >= procItem.accessoryProcChance) {
            return
        }

        accessoryProcCooldowns[cooldownKey] = serverLevel.gameTime + procItem.accessoryProcCooldownTicks
        procItem.triggerAccessoryProc(serverLevel, player, weaponStack, accessory.stack)
        accessory.stack.hurtAndBreak(1, player, accessory.hand.asEquipmentSlot())
    }

    @JvmStatic
    fun getCooldownCategory(stack: ItemStack): WeaponCooldownCategory? {
        if (stack.isEmpty) {
            return null
        }

        val item = stack.item
        return when {
            stack.`is`(ModItemTags.WEAPON_GUN) -> WeaponCooldownCategory.GUN
            stack.`is`(ModItemTags.WEAPON_CATAPULT) -> WeaponCooldownCategory.CATAPULT
            stack.`is`(ModItemTags.WEAPON_MAGIC_BOOK) -> WeaponCooldownCategory.MAGIC_BOOK
            stack.`is`(ModItemTags.WEAPON_MISSILE) -> WeaponCooldownCategory.MISSILE
            else -> null
        }
    }

    private fun tickCooldownAcceleration(player: Player) {
        val stateKey = CooldownStateKey(player.uuid, player.level().isClientSide)
        val accumulators = cooldownAccumulators.computeIfAbsent(stateKey) {
            EnumMap(WeaponCooldownCategory::class.java)
        }

        val seenCategories = mutableSetOf<WeaponCooldownCategory>()

        for (hand in InteractionHand.entries) {
            val stack = player.getItemInHand(hand)
            val category = getCooldownCategory(stack) ?: continue
            seenCategories += category

            val accessory = getBestAccessory(player, category)
            if (accessory == null || !player.cooldowns.isOnCooldown(stack)) {
                accumulators[category] = 0.0
                continue
            }

            val increment = accessory.bonus.ticksPerFourTicks / 4.0
            val updatedAccumulator = (accumulators[category] ?: 0.0) + increment
            accumulators[category] = shortenCooldown(player.cooldowns, stack, updatedAccumulator)
        }

        WeaponCooldownCategory.entries
            .filterNot(seenCategories::contains)
            .forEach { accumulators[it] = 0.0 }
    }

    private fun shortenCooldown(cooldowns: ItemCooldowns, stack: ItemStack, accumulator: Double): Double {
        val cooldownGroup = cooldowns.getCooldownGroup(stack)
        val accessor = cooldowns as ItemCooldownsAccessor
        val cooldownMap = accessor.`decisland$getCooldowns`()
        var instance = cooldownMap[cooldownGroup] ?: return 0.0
        val tickCount = accessor.`decisland$getTickCount`()
        var pending = accumulator

        while (pending >= 1.0) {
            val endTime = readCooldownEnd(instance)
            val remainingTicks = endTime - tickCount
            if (remainingTicks <= 1) {
                cooldowns.removeCooldown(cooldownGroup)
                return 0.0
            }

            val startTime = readCooldownStart(instance)
            instance = cooldownInstanceConstructor.newInstance(startTime, endTime - 1)
            cooldownMap[cooldownGroup] = instance
            pending -= 1.0
        }

        return pending
    }

    private fun readCooldownStart(instance: Any): Int = cooldownStartTimeMethod.invoke(instance) as Int

    private fun readCooldownEnd(instance: Any): Int = cooldownEndTimeMethod.invoke(instance) as Int

    private fun getBestAccessory(player: Player, category: WeaponCooldownCategory): EquippedAccessory? =
        InteractionHand.entries
            .mapNotNull { hand ->
                val stack = player.getItemInHand(hand)
                if (stack.isEmpty) {
                    return@mapNotNull null
                }

                val path = itemPath(stack)
                val bonus = ItemConfig.getConfigByRegisteredName(path)?.customProp?.accessoryCooldownBonus ?: return@mapNotNull null
                if (bonus.category != category) {
                    return@mapNotNull null
                }

                EquippedAccessory(hand, stack, bonus)
            }
            .maxByOrNull { it.bonus.ticksPerFourTicks }

    private fun itemPath(stack: ItemStack): String = BuiltInRegistries.ITEM.getKey(stack.item).path
}
