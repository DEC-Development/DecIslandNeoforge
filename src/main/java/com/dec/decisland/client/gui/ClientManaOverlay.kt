package com.dec.decisland.client.gui

import com.dec.decisland.DecIsland
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sin

object ClientManaOverlay {
    private var lastFullManaTime: Long = 0
    private var isFadingOut: Boolean = false
    private const val FADE_DELAY_MS: Long = 3000
    private const val FADE_DURATION_MS: Long = 3000
    private val manaIcons: Array<Identifier> = Array(5) { i ->
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/gui/sprites/mana/mana_$i.png")
    }
    private val manaMaxBorderIcons: Array<Identifier> = Array(5) { i ->
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/gui/sprites/mana/mana_max_$i.png")
    }
    private var cachedCurrentMana: Float = 0.0f
    private var cachedMaxMana: Float = 20.0f
    private var targetCurrentMana: Float = 0.0f
    private var transitionCurrentMana: Float = 0.0f
    private var lastUpdateTime: Long = 0
    private const val TRANSITION_DURATION_MS: Long = 500

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

        val (lowIndex, highIndex, alphaHigh) = when {
            currentMana >= 80.0f -> Triple(4, 4, 1.0f)
            currentMana <= 0.0f -> Triple(0, 0, 0.0f)
            currentMana < 20.0f -> Triple(0, 1, interpolate((currentMana - 0.0f) / 20.0f))
            currentMana < 40.0f -> Triple(1, 2, interpolate((currentMana - 20.0f) / 20.0f))
            currentMana < 60.0f -> Triple(2, 3, interpolate((currentMana - 40.0f) / 20.0f))
            else -> Triple(3, 4, interpolate((currentMana - 60.0f) / 20.0f))
        }

        val iconSize = 16
        val x = (screenWidth - iconSize) / 2
        val y = screenHeight - 49
        val baseColorWithAlpha = ((fadeAlpha * 255).toInt() shl 24) or 0xFFFFFF

        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            manaIcons[lowIndex],
            x,
            y,
            0.0f,
            0.0f,
            iconSize,
            iconSize,
            iconSize,
            iconSize,
            iconSize,
            iconSize,
            baseColorWithAlpha,
        )

        if (lowIndex != highIndex) {
            val combinedAlpha = alphaHigh * fadeAlpha
            val colorWithAlpha = ((combinedAlpha * 255).toInt() shl 24) or 0xFFFFFF
            guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                manaIcons[highIndex],
                x,
                y,
                0.0f,
                0.0f,
                iconSize,
                iconSize,
                iconSize,
                iconSize,
                iconSize,
                iconSize,
                colorWithAlpha,
            )
        }

        val maxManaBorderIndex = getMaxManaBorderIndex(maxMana)
        guiGraphics.blit(
            RenderPipelines.GUI_TEXTURED,
            manaMaxBorderIcons[maxManaBorderIndex],
            x,
            y,
            0.0f,
            0.0f,
            iconSize,
            iconSize,
            iconSize,
            iconSize,
            iconSize,
            iconSize,
            baseColorWithAlpha,
        )
    }

    private fun updateTransitionMana() {
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastUpdateTime
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
        return (0.5 * sin(Math.PI * t - Math.PI / 2) + 0.5).toFloat()
    }

    @JvmStatic
    fun updateManaValues(current: Float, max: Float) {
        val manaChanged = abs(current - cachedCurrentMana) > 0.001f || abs(max - cachedMaxMana) > 0.001f

        cachedCurrentMana = current
        cachedMaxMana = max
        targetCurrentMana = current

        if (transitionCurrentMana == 0.0f && cachedCurrentMana != 0.0f) {
            transitionCurrentMana = current
        }

        if (manaChanged) {
            lastFullManaTime = System.currentTimeMillis()
            isFadingOut = false
        }

        if (current >= max && max > 0.0f) {
            if (!isFadingOut) {
                lastFullManaTime = System.currentTimeMillis()
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
