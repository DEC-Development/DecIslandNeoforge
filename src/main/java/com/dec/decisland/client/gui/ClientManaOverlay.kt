package com.dec.decisland.client.gui

import com.dec.decisland.DecIsland
import com.dec.decisland.mixin.client.GuiGraphicsInvoker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min

object ClientManaOverlay {
    private var lastFullManaTime: Long = 0
    private var isFadingOut: Boolean = false
    private const val FADE_DELAY_MS: Long = 3000
    private const val FADE_DURATION_MS: Long = 3000
    private val manaIcons: Array<ResourceLocation> = Array(5) { i ->
        ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/gui/sprites/mana/mana_$i.png")
    }
    private val manaMaxBorderIcons: Array<ResourceLocation> = Array(5) { i ->
        ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/gui/sprites/mana/mana_max_$i.png")
    }
    private var cachedCurrentMana: Float = 0.0f
    private var cachedMaxMana: Float = 20.0f
    private var targetCurrentMana: Float = 0.0f
    private var transitionCurrentMana: Float = 0.0f
    private var lastUpdateTime: Long = 0
    private const val TRANSITION_DURATION_MS: Long = 500
    private const val MANA_ICON_STEP: Float = 20.0f
    private const val LAST_MANA_ICON_INDEX: Int = 4

    @JvmStatic
    fun render(guiGraphics: GuiGraphics, screenWidth: Int, screenHeight: Int) {
        val mc = Minecraft.getInstance()
        val player = mc.player ?: return
        updateTransitionMana()

        val currentMana = transitionCurrentMana
        val maxMana = cachedMaxMana
        if (maxMana <= 0.0f) return

        var fadeAlpha = 1.0f
        if (currentMana >= maxMana && maxMana > 0.0f) {
            val currentTime = System.currentTimeMillis()
            val elapsedSinceFull = currentTime - lastFullManaTime
            if (elapsedSinceFull > FADE_DELAY_MS) {
                isFadingOut = true
                if (elapsedSinceFull > FADE_DELAY_MS + FADE_DURATION_MS) {
                    return
                }
                val fadeElapsed = elapsedSinceFull - FADE_DELAY_MS
                val fadeProgress = fadeElapsed.toFloat() / FADE_DURATION_MS
                fadeAlpha = 1.0f - fadeProgress
            }
        } else {
            isFadingOut = false
        }

        // The five textures are consecutive mana states (0, 20, 40, 60 and
        // 80+).  Calculate the position continuously so the two neighbouring
        // states can be blended instead of switching at an integer boundary.
        val iconPosition = (currentMana / MANA_ICON_STEP)
            .coerceIn(0.0f, LAST_MANA_ICON_INDEX.toFloat())
        val lowIndex = floor(iconPosition).toInt().coerceIn(0, LAST_MANA_ICON_INDEX)
        val highIndex = (lowIndex + 1).coerceAtMost(LAST_MANA_ICON_INDEX)
        val alphaHigh = if (lowIndex == highIndex) {
            1.0f
        } else {
            interpolate(iconPosition - lowIndex.toFloat())
        }

        val iconSize = 16
        val x = (screenWidth - iconSize) / 2
        val y = screenHeight - 49

        blitManaTexture(
            guiGraphics,
            manaIcons[lowIndex],
            x,
            y,
            iconSize,
            iconSize,
            fadeAlpha,
        )

        if (lowIndex != highIndex) {
            val combinedAlpha = alphaHigh * fadeAlpha
            blitManaTexture(
                guiGraphics,
                manaIcons[highIndex],
                x,
                y,
                iconSize,
                iconSize,
                combinedAlpha,
            )
        }

        val maxManaBorderIndex = getMaxManaBorderIndex(maxMana)
        blitManaTexture(
            guiGraphics,
            manaMaxBorderIcons[maxManaBorderIndex],
            x,
            y,
            iconSize,
            iconSize,
            fadeAlpha,
        )
    }

    private fun blitManaTexture(
        guiGraphics: GuiGraphics,
        texture: ResourceLocation,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        alpha: Float,
    ) {
        val clampedAlpha = alpha.coerceIn(0.0f, 1.0f)
        if (clampedAlpha <= 0.0f) return

        (guiGraphics as GuiGraphicsInvoker).decislandInnerBlit(
            texture,
            x,
            x + width,
            y,
            y + height,
            0,
            0.0f,
            1.0f,
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            clampedAlpha,
        )
    }

    private fun updateTransitionMana() {
        val currentTime = System.currentTimeMillis()
        val deltaTime = if (lastUpdateTime == 0L) {
            0L
        } else {
            (currentTime - lastUpdateTime).coerceIn(0L, 100L)
        }
        lastUpdateTime = currentTime
        if (abs(targetCurrentMana - transitionCurrentMana) > 0.1f) {
            val progress = deltaTime.toFloat() / TRANSITION_DURATION_MS
            transitionCurrentMana += (targetCurrentMana - transitionCurrentMana) * min(1.0f, progress * 4.0f)
        } else {
            transitionCurrentMana = targetCurrentMana
        }
    }

    private fun getMaxManaBorderIndex(maxMana: Float): Int =
        when {
            maxMana < 30.0f -> 0
            maxMana < 40.0f -> 1
            maxMana < 50.0f -> 2
            maxMana < 60.0f -> 3
            else -> 4
        }

    private fun interpolate(x: Float): Float {
        val t = x * x
        return (0.5 * kotlin.math.sin(Math.PI * t - Math.PI / 2) + 0.5).toFloat()
    }

    @JvmStatic
    fun updateManaValues(current: Float, max: Float) {
        val manaChanged = abs(current - cachedCurrentMana) > 0.001f || abs(max - cachedMaxMana) > 0.001f
        val now = System.currentTimeMillis()

        cachedCurrentMana = current
        cachedMaxMana = max
        targetCurrentMana = current

        if (lastUpdateTime == 0L) {
            transitionCurrentMana = current
            lastUpdateTime = now
        }

        if (manaChanged) {
            lastFullManaTime = now
            isFadingOut = false
        }

        if (current >= max && max > 0.0f) {
            if (!isFadingOut) {
                lastFullManaTime = now
            }
        } else {
            isFadingOut = false
        }
    }

    @JvmStatic
    fun getCurrentMana(): Float = cachedCurrentMana

    @JvmStatic
    fun getMaxMana(): Float = cachedMaxMana
}
