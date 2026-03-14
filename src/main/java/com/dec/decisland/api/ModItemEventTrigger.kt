package com.dec.decisland.api

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

/**
 * 物品实现此接口后，可以在特定事件触发时执行自定义逻辑。
 * 所有方法都有默认空实现，因此实现类无需实现所有方法。
 */
interface ModItemEventTrigger {
    /**
     * 当玩家使用此物品攻击实体时触发。
     * @param source 攻击的实体
     * @param target 被攻击的实体
     * @param stack  使用的物品栈
     */
    fun onAttack(source: LivingEntity?, target: Entity?, stack: ItemStack?, hand: InteractionHand?): Boolean {
        // 默认为空，子类可覆盖
        return false
    } // 你可以在未来添加更多可选方法，例如：
    // default void onRightClickBlock(...) { }
    // default void onTick(...) { }
}