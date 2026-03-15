package com.dec.decisland.item.gun

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.FlintlockBulletEntity
import com.dec.decisland.item.category.Weapon
import com.dec.decisland.network.Networking
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

data class GunShot(
    val speed: Float,
    val inaccuracy: Float,
)

data class GunConfig(
    val cooldownTicks: Int,
    val baseDamage: Float,
    val knockback: Double,
    val shots: List<GunShot>,
    val bulletType: net.minecraft.world.entity.EntityType<out FlintlockBulletEntity>,
    val bulletBounces: Int = 0,
    val projectilePower: Double = 2.5,
    val projectileInertia: Double = 1.1,
    val projectileLiquidInertia: Double = 0.95,
    val projectileGravity: Double = 0.02,
    val movementSpeedAddition: Double = 0.0,
    val sneakMovementSpeedAddition: Double = 0.0,
    val recoilPitchUpDegrees: Float = 2.5f,
    val recoilYawDegrees: Float = 1.0f,
    val muzzleParticleId: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "flintlock_smoke_particle"),
    val shootSound: SoundEvent,
    val shootSoundVolume: Float = 1.0f,
    val shootSoundPitchMin: Float = 0.85f,
    val shootSoundPitchMax: Float = 1.05f,
)

open class GunItem(
    properties: Properties,
    private val config: GunConfig,
) : Item(properties) {
    private val movementSpeedModifierId: Identifier = run {
        val key = BuiltInRegistries.ENTITY_TYPE.getKey(config.bulletType)
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "gun_movement_speed/${key.namespace}/${key.path}")
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(usedHand)
        if (player.cooldowns.isOnCooldown(stack)) {
            return InteractionResult.FAIL
        }

        if (!level.isClientSide) {
            val serverLevel = level as? ServerLevel ?: return InteractionResult.FAIL
            if (!consumeAmmo(player)) {
                return InteractionResult.FAIL
            }
            shoot(serverLevel, player, usedHand, stack)
        }

        player.swing(usedHand, true)
        return if (level.isClientSide) InteractionResult.SUCCESS else InteractionResult.SUCCESS_SERVER
    }

    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: net.minecraft.world.entity.Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)
        val player = entity as? Player ?: return
        if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
            removeMovementModifier(player)
            return
        }

        val amount = if (player.isCrouching) config.sneakMovementSpeedAddition else config.movementSpeedAddition
        if (amount == 0.0) {
            removeMovementModifier(player)
            return
        }

        val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        val existing = attribute.getModifier(movementSpeedModifierId)
        if (existing != null && existing.amount == amount) {
            return
        }
        if (existing != null) {
            attribute.removeModifier(movementSpeedModifierId)
        }

        attribute.addTransientModifier(
            AttributeModifier(
                movementSpeedModifierId,
                amount,
                AttributeModifier.Operation.ADD_VALUE,
            ),
        )
    }

    private fun removeMovementModifier(player: Player) {
        val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        if (attribute.getModifier(movementSpeedModifierId) != null) {
            attribute.removeModifier(movementSpeedModifierId)
        }
    }

    protected open fun consumeAmmo(player: Player): Boolean {
        if (player.abilities.instabuild) {
            return true
        }

        val bulletItem = Weapon.FLINTLOCK_BULLET.get()
        for (stack in player.inventory.nonEquipmentItems) {
            if (!stack.isEmpty() && stack.`is`(bulletItem)) {
                stack.shrink(1)
                return true
            }
        }
        val offhand = player.inventory.getItem(40)
        if (!offhand.isEmpty() && offhand.`is`(bulletItem)) {
            offhand.shrink(1)
            return true
        }
        return false
    }

    protected open fun shoot(serverLevel: ServerLevel, player: Player, usedHand: InteractionHand, stack: ItemStack) {
        player.cooldowns.addCooldown(stack, config.cooldownTicks)

        val random = player.random
        val pitch = config.shootSoundPitchMin + (random.nextFloat() * (config.shootSoundPitchMax - config.shootSoundPitchMin))
        serverLevel.playSound(
            null,
            player.x,
            player.y,
            player.z,
            config.shootSound,
            SoundSource.PLAYERS,
            config.shootSoundVolume,
            pitch,
        )

        spawnMuzzle(serverLevel, player)
        spawnProjectiles(serverLevel, player)
        sendRecoil(player)
        val slot = if (usedHand == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND
        stack.hurtAndBreak(1, player, slot)
    }

    private fun spawnMuzzle(serverLevel: ServerLevel, player: Player) {
        val eyePos = player.eyePosition
        val look = player.lookAngle
        val position = eyePos.add(look.scale(1.0))
        if (player is ServerPlayer) {
            Networking.sendBedrockEmitter(player, config.muzzleParticleId, position, 40)
        } else {
            serverLevel.sendParticles(
                net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE,
                position.x,
                position.y,
                position.z,
                15,
                0.15,
                0.08,
                0.15,
                0.02,
            )
        }
    }

    private fun spawnProjectiles(serverLevel: ServerLevel, player: Player) {
        val spawn = player.eyePosition.add(player.lookAngle.scale(0.3))

        config.shots.forEach { shot ->
            val bullet = FlintlockBulletEntity(config.bulletType, serverLevel)
            bullet.setOwner(player)
            bullet.setPos(spawn.x, spawn.y, spawn.z)
            bullet.baseDamage = config.baseDamage
            bullet.knockback = config.knockback
            bullet.bouncesRemaining = config.bulletBounces
            bullet.power = config.projectilePower
            bullet.inertia = config.projectileInertia
            bullet.liquidInertia = config.projectileLiquidInertia
            bullet.gravityAmount = config.projectileGravity

            val effectiveSpeed = (shot.speed * (config.projectilePower / 2.5)).toFloat()
            bullet.shootFromRotation(player, player.xRot, player.yRot, 0.0f, effectiveSpeed, shot.inaccuracy)
            serverLevel.addFreshEntity(bullet)
        }
    }

    private fun sendRecoil(player: Player) {
        if (player !is ServerPlayer) {
            return
        }
        if (config.recoilPitchUpDegrees == 0.0f && config.recoilYawDegrees == 0.0f) {
            return
        }

        val shotCount = config.shots.size.coerceAtLeast(1)
        val pitch = config.recoilPitchUpDegrees * (1.0f + (shotCount - 1) * 0.25f)
        val yaw = ((player.random.nextFloat() * 2.0f) - 1.0f) * config.recoilYawDegrees
        Networking.sendRecoil(player, pitch, yaw)
    }
}
