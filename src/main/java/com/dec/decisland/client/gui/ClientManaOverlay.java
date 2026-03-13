package com.dec.decisland.client.gui;

import com.dec.decisland.DecIsland;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.RenderPipelines;

public class ClientManaOverlay {

    // 添加用于控制HUD淡出的变量
    private static long lastFullManaTime = 0; // 上次魔法值满的时间
    private static boolean isFadingOut = false; // 是否正在淡出
    private static final long FADE_DELAY_MS = 3000; // 淡出延迟时间 3秒
    private static final long FADE_DURATION_MS = 3000; // 淡出持续时间 3秒

    // 图标数组：索引0~4对应 mana_0 ~ mana_4
    private static final Identifier[] MANA_ICONS = new Identifier[5];
    // 边框图标数组：索引0~4对应 mana_max_0 ~ mana_max_4
    private static final Identifier[] MANA_MAX_BORDER_ICONS = new Identifier[5];
    static {
        for (int i = 0; i < 5; i++) {
            MANA_ICONS[i] = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID,
                    "textures/gui/sprites/mana/mana_" + i + ".png");
        }
        // 初始化边框图标数组
        for (int i = 0; i < 5; i++) {
            MANA_MAX_BORDER_ICONS[i] = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID,
                    "textures/gui/sprites/mana/mana_max_" + i + ".png");
        }
    }

    // 添加缓存变量来存储mana值，避免每次都从附件获取
    private static float cachedCurrentMana = 0.0f;
    private static float cachedMaxMana = 20.0f;
    
    // 添加过渡效果相关变量
    private static float targetCurrentMana = 0.0f; // 目标魔法值
    private static float transitionCurrentMana = 0.0f; // 过渡过程中的魔法值
    private static long lastUpdateTime = 0; // 上次更新时间
    private static final long TRANSITION_DURATION_MS = 500; // 过渡持续时间 500ms

    public static void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        // 更新过渡值
        updateTransitionMana();

        // 使用过渡后的mana值，而不是直接使用缓存值
        float currentMana = transitionCurrentMana;
        float maxMana = cachedMaxMana;

        if (maxMana <= 0) return;

        // 检查是否需要开始或继续淡出
        boolean shouldRender = true;
        float fadeAlpha = 1.0f; // 默认不透明

        // 检查是否魔法满了，并且已经超过了延迟时间
        if (currentMana >= maxMana && maxMana > 0) {
            long currentTime = System.currentTimeMillis();
            long elapsedSinceFull = currentTime - lastFullManaTime;

            if (elapsedSinceFull > FADE_DELAY_MS) {
                isFadingOut = true;

                if (elapsedSinceFull > FADE_DELAY_MS + FADE_DURATION_MS) {
                    // 超过了整个淡出时间，不再渲染
                    return;
                } else {
                    // 计算淡出透明度
                    long fadeElapsed = elapsedSinceFull - FADE_DELAY_MS;
                    float fadeProgress = (float) fadeElapsed / FADE_DURATION_MS;
                    fadeAlpha = 1.0f - fadeProgress; // 从1到0
                }
            }
        } else {
            // 魔法值未满，重置淡出状态
            isFadingOut = false;
        }

        // 根据当前魔法值的绝对数值计算显示效果，而不是比例
        // 0-20 -> mana_0 to mana_1
        // 20-40 -> mana_1 to mana_2
        // 40-60 -> mana_2 to mana_3
        // 60-80 -> mana_3 to mana_4
        // >=80 -> mana_4

        int lowIndex, highIndex;
        float alphaHigh = 0.0f; // 高阈值图标的透明度

        if (currentMana >= 80) { // >= 80 -> 直接用 mana_4
            lowIndex = 4;
            highIndex = 4;
            alphaHigh = 1.0f;
        } else if (currentMana <= 0) { // <= 0 -> 用 mana_0
            lowIndex = 0;
            highIndex = 0;
            alphaHigh = 0.0f;
        } else {
            // 根据当前魔法值确定应该使用哪两个图标进行插值
            if (currentMana < 20) {
                // 0 到 20 之间 -> 使用 mana_0 和 mana_1
                lowIndex = 0;
                highIndex = 1;
                float normalizedValue = (currentMana - 0) / (20 - 0); // 0.0 到 1.0
                alphaHigh = interpolate(normalizedValue); // 使用插值函数
            } else if (currentMana < 40) {
                // 20 到 40 之间 -> 使用 mana_1 和 mana_2
                lowIndex = 1;
                highIndex = 2;
                float normalizedValue = (currentMana - 20) / (40 - 20); // 0.0 到 1.0
                alphaHigh = interpolate(normalizedValue); // 使用插值函数
            } else if (currentMana < 60) {
                // 40 到 60 之间 -> 使用 mana_2 和 mana_3
                lowIndex = 2;
                highIndex = 3;
                float normalizedValue = (currentMana - 40) / (60 - 40); // 0.0 到 1.0
                alphaHigh = interpolate(normalizedValue); // 使用插值函数
            } else if (currentMana < 80) {
                // 60 到 80 之间 -> 使用 mana_3 和 mana_4
                lowIndex = 3;
                highIndex = 4;
                float normalizedValue = (currentMana - 60) / (80 - 60); // 0.0 到 1.0
                alphaHigh = interpolate(normalizedValue); // 使用插值函数
            } else {
                // >= 80 -> 使用 mana_4
                lowIndex = 4;
                highIndex = 4;
                alphaHigh = 1.0f;
            }
        }

        // 绘制位置：水平居中，垂直位于生命条上方（屏幕底部向上偏移49像素）
        int iconSize = 16;
        int x = (screenWidth - iconSize) / 2;
        int y = screenHeight - 49; // 原版生命条通常在 screenHeight - 39

        // 先绘制低值图标（作为底层）
        // 使用正确的blit方法签名
        int baseColorWithAlpha = ((int) (fadeAlpha * 255) << 24) | 0xFFFFFF;

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                MANA_ICONS[lowIndex],
                x, y,
                0, 0,
                iconSize, iconSize,
                iconSize, iconSize,
                iconSize, iconSize,
                baseColorWithAlpha
        );

        // 如果需要混合，则绘制高值图标（作为顶层），带透明度
        if (lowIndex != highIndex) {
            // 计算带透明度的颜色值
            float combinedAlpha = alphaHigh * fadeAlpha; // 结合插值和淡出效果
            int alphaInt = (int) (combinedAlpha * 255);
            int colorWithAlpha = (alphaInt << 24) | 0xFFFFFF; // 设置透明度，RGB保持白色

            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    MANA_ICONS[highIndex],
                    x, y,
                    0, 0,
                    iconSize, iconSize,
                    iconSize, iconSize,
                    iconSize, iconSize,
                    colorWithAlpha
            );
        }

        // 根据MaxMana决定边框类型并绘制边框
        int maxManaBorderIndex = getMaxManaBorderIndex(maxMana);
        if (maxManaBorderIndex >= 0 && maxManaBorderIndex < MANA_MAX_BORDER_ICONS.length) {
            // 绘制边框图标，覆盖在当前mana图标之上
            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    MANA_MAX_BORDER_ICONS[maxManaBorderIndex],
                    x, y,
                    0, 0,
                    iconSize, iconSize,
                    iconSize, iconSize,
                    iconSize, iconSize,
                    baseColorWithAlpha // 应用相同的透明度
            );
        }

        // 添加调试信息显示
        // 将当前mana值显示在mana图标旁边
//        String manaText = String.format("%.1f/%.1f", cachedCurrentMana, cachedMaxMana);
//        guiGraphics.drawString(mc.font, manaText, x + iconSize + 5, y + 5, 0xFFFFFFFF, true);
    }

    /**
     * 更新过渡过程中的魔法值
     */
    private static void updateTransitionMana() {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastUpdateTime;
        lastUpdateTime = currentTime;

        // 如果目标值与当前过渡值不同，则进行过渡更新
        if (Math.abs(targetCurrentMana - transitionCurrentMana) > 0.1f) {
            float progress = (float) deltaTime / TRANSITION_DURATION_MS;
            transitionCurrentMana = transitionCurrentMana + (targetCurrentMana - transitionCurrentMana) * Math.min(1.0f, progress * 4); // 加速过渡
        } else {
            transitionCurrentMana = targetCurrentMana;
        }
    }

    /**
     * 根据MaxMana值确定边框索引
     * 0-29 -> mana_max_0 (index 0)
     * 30-39 -> mana_max_1 (index 1)
     * 40-49 -> mana_max_2 (index 2)
     * 50-59 -> mana_max_3 (index 3)
     * 60+ -> mana_max_4 (index 4)
     */
    private static int getMaxManaBorderIndex(float maxMana) {
        if (maxMana < 30) {
            return 0;
        } else if (maxMana < 40) {
            return 1;
        } else if (maxMana < 50) {
            return 2;
        } else if (maxMana < 60) {
            return 3;
        } else {
            return 4;
        }
    }

    // 插值函数: 0.5*sin(π*t - π/2) + 0.5, 其中 t = x^2
    private static float interpolate(float x) {
        double t = x * x; // t = x^2
        return (float)(0.5 * Math.sin(Math.PI * t - Math.PI / 2) + 0.5);
    }

    // 用于从服务端更新mana值的方法
    public static void updateManaValues(float current, float max) {
        cachedCurrentMana = current;
        cachedMaxMana = max;
        targetCurrentMana = current; // 更新目标魔法值

        // 仅在当前过渡值为初始状态时设置它，避免重复设置
        if (transitionCurrentMana == 0.0f && cachedCurrentMana != 0.0f) {
            transitionCurrentMana = current;
        }

        // 检查是否魔法值满了，如果是且之前不是满的状态，则记录时间
        if (current >= max && max > 0) {
            if (!isFadingOut) {
                lastFullManaTime = System.currentTimeMillis();
            }
        } else {
            // 如果魔法值不满，重置状态
            isFadingOut = false;
        }
    }

    // 获取当前mana值，用于调试
    public static float getCurrentMana() {
        return cachedCurrentMana;
    }

    // 获取最大mana值，用于调试
    public static float getMaxMana() {
        return cachedMaxMana;
    }
}