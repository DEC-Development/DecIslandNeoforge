package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.world.portal.SnowPortalShape
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object PortalEvents {
    @SubscribeEvent
    @JvmStatic
    fun onRightClickBlock(event: PlayerInteractEvent.RightClickBlock) {
        if (event.level.isClientSide) {
            return
        }
        if (!event.itemStack.`is`(Items.SNOWBALL)) {
            return
        }
        val clicked = event.pos
        val front = clicked.relative(event.face ?: return)
        if (!SnowPortalShape.tryCreatePortal(event.level, clicked) && !SnowPortalShape.tryCreatePortal(event.level, front)) {
            return
        }

        if (!event.entity.abilities.instabuild) {
            event.itemStack.shrink(1)
        }
        event.level.playSound(null, clicked, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0f, 1.0f)
        event.isCanceled = true
        event.cancellationResult = InteractionResult.SUCCESS
    }
}
