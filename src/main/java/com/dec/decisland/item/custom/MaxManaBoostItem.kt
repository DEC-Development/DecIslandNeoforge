package com.dec.decisland.item.custom

import com.dec.decisland.attachment.ModAttachments
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.attachment.AttachmentType

open class MaxManaBoostItem(properties: Properties) : Item(properties) {
    open fun getMinAttachmentLevel(): Int = 20

    open fun getMaxAttachmentLevel(): Int = 39

    open fun getAttachment(): AttachmentType<Float> = ModAttachments.MAX_MANA.get()

    open fun getNutrition(): Int = 0

    open fun getSaturationModifier(): Float = 0.0f

    open fun getBoostValue(): Float = 2.0f

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        if (livingEntity is Player && !level.isClientSide) {
            val currentLevel = livingEntity.getData(getAttachment())
            if (currentLevel >= getMinAttachmentLevel() && currentLevel <= getMaxAttachmentLevel()) {
                livingEntity.setData(getAttachment(), currentLevel + getBoostValue())
                livingEntity.foodData.eat(getNutrition(), getSaturationModifier())
                stack.shrink(1)
                livingEntity.playSound(SoundEvents.GENERIC_EAT.value(), 1.0f, 1.0f)
            } else {
                return stack
            }
        }
        return stack
    }
}
