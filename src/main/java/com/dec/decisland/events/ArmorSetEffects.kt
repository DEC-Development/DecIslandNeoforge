package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.item.category.Armor
import com.dec.decisland.mana.ManaManager
import com.dec.decisland.network.Networking
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import java.util.function.Supplier

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object ArmorSetEffects {
    private val RUPERT_TEAR_PARTICLE: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "tear_from_rupert")

    private enum class ArmorSetId {
        AMETHYST,
        CRYING,
        EVERLASTING_WINTER,
        LAVA,
        RUPERT,
        WOOD,
    }

    private data class ArmorSetDefinition(
        val id: ArmorSetId,
        val head: Supplier<Item>,
        val chest: Supplier<Item>,
        val legs: Supplier<Item>,
        val boots: Supplier<Item>,
    )

    private val armorSets: List<ArmorSetDefinition> = listOf(
        ArmorSetDefinition(
            ArmorSetId.AMETHYST,
            Armor.AMETHYST_HELMET,
            Armor.AMETHYST_CHESTPLATE,
            Armor.AMETHYST_LEGGINGS,
            Armor.AMETHYST_BOOTS,
        ),
        ArmorSetDefinition(
            ArmorSetId.CRYING,
            Armor.CRYING_HELMET,
            Armor.CRYING_CHESTPLATE,
            Armor.CRYING_LEGGINGS,
            Armor.CRYING_BOOTS,
        ),
        ArmorSetDefinition(
            ArmorSetId.EVERLASTING_WINTER,
            Armor.EVERLASTING_WINTER_HELMET,
            Armor.EVERLASTING_WINTER_CHESTPLATE,
            Armor.EVERLASTING_WINTER_LEGGINGS,
            Armor.EVERLASTING_WINTER_BOOTS,
        ),
        ArmorSetDefinition(
            ArmorSetId.LAVA,
            Armor.LAVA_HELMET,
            Armor.LAVA_CHESTPLATE,
            Armor.LAVA_LEGGINGS,
            Armor.LAVA_BOOTS,
        ),
        ArmorSetDefinition(
            ArmorSetId.RUPERT,
            Armor.RUPERT_HELMET,
            Armor.RUPERT_CHESTPLATE,
            Armor.RUPERT_LEGGINGS,
            Armor.RUPERT_BOOTS,
        ),
        ArmorSetDefinition(
            ArmorSetId.WOOD,
            Armor.WOOD_HELMET,
            Armor.WOOD_CHESTPLATE,
            Armor.WOOD_LEGGINGS,
            Armor.WOOD_BOOTS,
        ),
    )

    @SubscribeEvent
    @JvmStatic
    fun onEntityTick(event: EntityTickEvent.Post) {
        val entity = event.entity as? LivingEntity ?: return
        if (entity.level().isClientSide) {
            return
        }

        val equippedSet = getEquippedSet(entity) ?: return
        when {
            entity.tickCount % 100 == 0 && equippedSet == ArmorSetId.WOOD && entity is Player -> {
                if (ManaManager.addMana(entity, 5.0f) > 0.0f) {
                    spawnParticles(entity, ParticleTypes.HAPPY_VILLAGER, 6, 0.35)
                }
            }

            entity.tickCount % 40 == 0 && equippedSet == ArmorSetId.RUPERT -> {
                spawnRupertParticles(entity)
            }

            entity.tickCount % 20 == 0 && equippedSet == ArmorSetId.AMETHYST && entity is Player -> {
                if (ManaManager.addMana(entity, 1.0f) > 0.0f) {
                    spawnParticles(entity, ParticleTypes.ENCHANT, 8, 0.4)
                }
            }
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onLivingDamaged(event: LivingDamageEvent.Post) {
        val entity = event.entity
        if (entity.level().isClientSide) {
            return
        }

        when (getEquippedSet(entity)) {
            ArmorSetId.RUPERT -> handleRupertSet(entity)
            ArmorSetId.LAVA -> handleLavaSet(entity)
            ArmorSetId.CRYING -> handleCryingSet(entity)
            ArmorSetId.EVERLASTING_WINTER -> handleEverlastingWinterSet(entity)
            else -> Unit
        }
    }

    private fun handleRupertSet(entity: LivingEntity) {
        if (!roll(entity, 20)) {
            return
        }

        entity.addEffect(MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 0))
        entity.addEffect(MobEffectInstance(MobEffects.SPEED, 5 * 20, 0))
        spawnRupertParticles(entity)
    }

    private fun handleLavaSet(entity: LivingEntity) {
        entity.addEffect(MobEffectInstance(MobEffects.FIRE_RESISTANCE, 4 * 20, 0))
        spawnParticles(entity, ParticleTypes.LAVA, 6, 0.25)
        spawnParticles(entity, ParticleTypes.FLAME, 6, 0.25)
    }

    private fun handleCryingSet(entity: LivingEntity) {
        val roll = entity.random.nextInt(100) + 1
        when {
            roll <= 10 -> entity.addEffect(MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 0))
            roll <= 20 -> entity.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 4 * 20, 0))
            roll <= 30 -> entity.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 5 * 20, 0))
            roll <= 40 -> entity.addEffect(MobEffectInstance(MobEffects.NAUSEA, 7 * 20, 0))
        }
    }

    private fun handleEverlastingWinterSet(entity: LivingEntity) {
        if (!roll(entity, 12)) {
            return
        }

        val nearbyTargets = entity.level()
            .getEntitiesOfClass(LivingEntity::class.java, entity.boundingBox.inflate(5.0)) { it != entity }

        nearbyTargets.forEach { target ->
            target.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 3 * 20, 1))
        }
        entity.addEffect(MobEffectInstance(MobEffects.HEALTH_BOOST, 30 * 20, 0))
        spawnParticles(entity, ParticleTypes.SNOWFLAKE, 12, 0.45)
    }

    private fun getEquippedSet(entity: LivingEntity): ArmorSetId? =
        armorSets.firstOrNull { matches(entity, it) }?.id

    private fun matches(entity: LivingEntity, definition: ArmorSetDefinition): Boolean =
        entity.getItemBySlot(EquipmentSlot.HEAD).`is`(definition.head.get()) &&
            entity.getItemBySlot(EquipmentSlot.CHEST).`is`(definition.chest.get()) &&
            entity.getItemBySlot(EquipmentSlot.LEGS).`is`(definition.legs.get()) &&
            entity.getItemBySlot(EquipmentSlot.FEET).`is`(definition.boots.get())

    private fun roll(entity: LivingEntity, chancePercent: Int): Boolean =
        entity.random.nextInt(100) < chancePercent

    private fun spawnParticles(entity: LivingEntity, particle: ParticleOptions, count: Int, spread: Double) {
        val level = entity.level() as? ServerLevel ?: return
        level.sendParticles(
            particle,
            entity.x,
            entity.y + 1.0,
            entity.z,
            count,
            spread,
            0.35,
            spread,
            0.01,
        )
    }

    private fun spawnRupertParticles(entity: LivingEntity) {
        val level = entity.level() as? ServerLevel ?: return
        Networking.sendBedrockEmitterToNearby(
            level,
            RUPERT_TEAR_PARTICLE,
            entity.position().add(0.0, 1.0, 0.0),
            radius = 64.0,
            durationTicks = 6,
        )
    }
}
