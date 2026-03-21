package com.dec.decisland.item.custom

import com.dec.decisland.mana.ManaManager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

abstract class BedrockProjectileStaff(properties: Properties) : ProjectileStaff(properties) {
    override fun judge(level: Level, player: Player): Boolean {
        return ManaManager.getCurrentMana(player) >= manaCost
    }
}
