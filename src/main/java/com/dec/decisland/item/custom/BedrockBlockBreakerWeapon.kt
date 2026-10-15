package com.dec.decisland.item.custom

import com.dec.decisland.item.compat.asEquipmentSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/** Server-side implementation of the two Bedrock block-destruction functions. */
class BedrockBlockBreakerWeapon(
    properties: Properties,
    private val allowedBlocks: Set<String>,
    private val offsets: List<Offset> = DEFAULT_OFFSETS,
) : MagicWeapon(properties) {
    override fun judge(level: Level, player: Player): Boolean = true

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source !is Player) return
        offsets.forEach { offset ->
            val pos = BlockPos.containing(source.x + offset.x, source.y + offset.y, source.z + offset.z)
            val state = serverLevel.getBlockState(pos)
            val id = BuiltInRegistries.BLOCK.getKey(state.block).path
            if (id in allowedBlocks && !state.isAir) {
                serverLevel.destroyBlock(pos, true, source)
            }
        }
        stack.hurtAndBreak(1, source, source.usedItemHand?.asEquipmentSlot() ?: net.minecraft.world.entity.EquipmentSlot.MAINHAND)
    }

    data class Offset(val x: Double, val y: Double, val z: Double)

    companion object {
        val DEFAULT_OFFSETS: List<Offset> = buildList {
            add(Offset(0.4, 0.0, 0.0))
            add(Offset(-0.4, 0.0, 0.0))
            add(Offset(0.0, 0.0, 0.4))
            add(Offset(0.0, 0.0, -0.4))
            add(Offset(0.4, 0.0, 0.4))
            add(Offset(0.4, 0.0, -0.4))
            add(Offset(-0.4, 0.0, 0.4))
            add(Offset(-0.4, 0.0, -0.4))
            add(Offset(0.4, 1.0, 0.0))
            add(Offset(-0.4, 1.0, 0.0))
            add(Offset(0.0, 1.0, 0.4))
            add(Offset(0.0, 1.0, -0.4))
            add(Offset(0.4, 1.0, 0.4))
            add(Offset(0.4, 1.0, -0.4))
            add(Offset(-0.4, 1.0, 0.4))
            add(Offset(-0.4, 1.0, -0.4))
        }

        val DESTROY_STAFF_BLOCKS: Set<String> = setOf("stone", "sandstone", "red_sandstone", "cobblestone")

        val GOD_OF_DESTROY_BLOCKS: Set<String> = setOf(
            "stone",
            "sandstone",
            "red_sandstone",
            "cobblestone",
            "cobbled_deepslate",
            "dirt",
            "grass_block",
            "end_stone",
            "gravel",
            "netherrack",
            "soul_sand",
            "packed_ice",
            "ice",
            "snow",
            "snow_block",
            "sand",
            "cactus",
            "chorus_flower",
            "chorus_plant",
            "oak_log",
            "spruce_log",
            "birch_log",
            "jungle_log",
            "acacia_log",
            "dark_oak_log",
            "oak_leaves",
            "spruce_leaves",
            "birch_leaves",
            "jungle_leaves",
            "acacia_leaves",
            "dark_oak_leaves",
            "short_grass",
            "tall_grass",
            "poppy",
            "dandelion",
        )
    }
}
