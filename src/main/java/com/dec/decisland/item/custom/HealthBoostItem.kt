package com.dec.decisland.item.custom

import com.dec.decisland.attachment.ModAttachments
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.attachment.AttachmentType

open class HealthBoostItem(properties: Properties) : Item(properties) {
    open fun getMinAttachmentLevel(): Int = 0

    open fun getMaxAttachmentLevel(): Int = 2

    open fun getAttachment(): AttachmentType<Int> = ModAttachments.MAX_HEALTH_LEVEL.get()

    open fun getAttribute(): Holder<Attribute> = Attributes.MAX_HEALTH

    open fun calAttributeValue(newLevel: Int): Int = 10 + 2 * newLevel

    open fun getNutrition(): Int = 2

    open fun getSaturationModifier(): Float = 1.0f

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        if (livingEntity is Player && !level.isClientSide) {
            val currentLevel = livingEntity.getData(getAttachment())
            if (currentLevel in getMinAttachmentLevel()..getMaxAttachmentLevel()) {
                val newLevel = currentLevel + 1
                livingEntity.setData(getAttachment(), newLevel)
                livingEntity.getAttribute(getAttribute())!!.baseValue = calAttributeValue(newLevel).toDouble()
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
