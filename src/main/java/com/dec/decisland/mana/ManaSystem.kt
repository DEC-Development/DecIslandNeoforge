package com.dec.decisland.mana

import com.dec.decisland.DecIsland
import com.dec.decisland.attachment.ModAttachments
import com.dec.decisland.network.ManaSyncPayload
import com.dec.decisland.network.Networking
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object ManaSystem {
    const val WAIT_TICK: Int = 60
    private const val K2: Double = 0.001
    private const val K1: Double = 1 - K2 + 0.01
    private const val G_MIN: Double = (1 - K1) / K2
    private const val MAGICGAIN_MAP_K: Double = 1.05
    private const val MAGICGAIN_MAP_K2: Double = (-G_MIN + 1) / MAGICGAIN_MAP_K
    private val MAGIC_INCREASE_PARTICLE_ID: Identifier =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "magic_increase_particle")
    private val MAGIC_DECREASE_PARTICLE_ID: Identifier =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "magic_decrease_particle")

    @SubscribeEvent
    @JvmStatic
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        initManaData(event.entity)
    }

    @SubscribeEvent
    @JvmStatic
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        initManaData(event.entity)
    }

    private fun initManaData(player: Player) {
        if (!player.level().isClientSide) {
            val maxMana = player.getData(ModAttachments.MAX_MANA.get())
            val currentMana = player.getData(ModAttachments.CURRENT_MANA.get())
            player.setData(ModAttachments.PREV_MAGIC.get(), currentMana)
            player.setData(ModAttachments.MAGIC_GAP.get(), WAIT_TICK)
            PacketDistributor.sendToPlayer(player as ServerPlayer, ManaSyncPayload(currentMana, maxMana))
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onPlayerTick(event: PlayerTickEvent.Post) {
        val player = event.entity
        if (player.level().isClientSide) return

        val maxMana = player.getData(ModAttachments.MAX_MANA.get())
        val magicGain = player.getData(ModAttachments.MANA_GAIN_LEVEL.get())
        val magicReckon = player.getData(ModAttachments.MANA_RECKON.get())
        val currentMana = player.getData(ModAttachments.CURRENT_MANA.get())
        val magicGap = player.getData(ModAttachments.MAGIC_GAP.get())
        val prevMagic = player.getData(ModAttachments.PREV_MAGIC.get())

        if (currentMana < maxMana) {
            if (magicGap <= 0) {
                val newMana = min(currentMana + 1.0f, maxMana)
                player.setData(ModAttachments.CURRENT_MANA.get(), newMana)
                PacketDistributor.sendToPlayer(player as ServerPlayer, ManaSyncPayload(newMana, maxMana))
                spawnManaParticle(player, "increase")

                val mappedGain = magicGainMap(magicGain)
                val base = 1.0 / (K1 + K2 * mappedGain)
                var newGap = (WAIT_TICK * base.pow(magicReckon.toDouble())).toInt()
                if (newGap < 1) newGap = 1
                player.setData(ModAttachments.MAGIC_GAP.get(), newGap)
            } else {
                player.setData(ModAttachments.MAGIC_GAP.get(), magicGap - 1)
            }
            player.setData(ModAttachments.MANA_RECKON.get(), magicReckon + 1)
        } else if (magicReckon != 0) {
            player.setData(ModAttachments.MANA_RECKON.get(), 0)
            player.setData(ModAttachments.MAGIC_GAP.get(), WAIT_TICK)
        }

        if (prevMagic > currentMana && abs(prevMagic - currentMana) > 0.001f) {
            PacketDistributor.sendToPlayer(player as ServerPlayer, ManaSyncPayload(currentMana, maxMana))
            player.setData(ModAttachments.MANA_RECKON.get(), 0)
            if (magicReckon >= WAIT_TICK) {
                spawnManaParticle(player, "decrease")
            }
        }

        player.setData(ModAttachments.PREV_MAGIC.get(), currentMana)
    }

    private fun magicGainMap(magicGain: Float): Double =
        if (magicGain < 1.0f) {
            MAGICGAIN_MAP_K2 * MAGICGAIN_MAP_K.pow(magicGain.toDouble()) + G_MIN
        } else {
            magicGain.toDouble()
        }

    private fun spawnManaParticle(player: Player, type: String) {
        val level = player.level()
        if (level is ServerLevel) {
            val particleId = if (type == "increase") MAGIC_INCREASE_PARTICLE_ID else MAGIC_DECREASE_PARTICLE_ID
            Networking.sendBedrockEmitterToNearby(level, particleId, player.position(), 64.0, 2)
        }
    }
}
