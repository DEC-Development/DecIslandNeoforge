package com.dec.decisland.item;

import com.dec.decisland.DecIsland;
import com.dec.decisland.block.ModBlocks;
import com.dec.decisland.item.category.Mask;
import com.dec.decisland.item.category.Material;
import com.dec.decisland.item.category.Weapon;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DecIsland.MOD_ID);
    private static final List<CreativeTabConfig> TAB_CONFIGS = new ArrayList<>();

    public static final Supplier<CreativeModeTab> DECISLAND_MATERIALS_TAB = registerTab(
            new CreativeTabConfig.Builder("decisland_materials_tab", Map.of(
                    "en_us", "DecIsland Materials",
                    "zh_cn", "DecIsland 材料"
            ))
                    .iconItem(() -> Material.STEEL_INGOT.get())
                    .build()
    );

    public static final Supplier<CreativeModeTab> DECISLAND_FOODS_TAB = registerTab(
            new CreativeTabConfig.Builder("decisland_foods_tab", Map.of(
                    "en_us", "DecIsland Foods",
                    "zh_cn", "DecIsland 食物"
            ))
                    .iconItem(() -> ModItems.A_BOWL_OF_RICE.get())
                    .build()
    );

    public static final Supplier<CreativeModeTab> DECISLAND_WEAPONS_TAB = registerTab(
            new CreativeTabConfig.Builder("decisland_weapons_tab", Map.of(
                    "en_us", "DecIsland Weapons",
                    "zh_cn", "DecIsland 武器"
            ))
                    .iconItem(() -> Weapon.ABSOLUTE_ZERO.get())
                    .build()
    );

    public static final Supplier<CreativeModeTab> DECISLAND_MISC_TAB = registerTab(
            new CreativeTabConfig.Builder("decisland_misc_tab", Map.of(
                    "en_us", "DecIsland Misc",
                    "zh_cn", "DecIsland 杂物"
            ))
                    .iconItem(() -> ModItems.AUTUMN_LEAVES.get())
                    .build()
    );

    public static final Supplier<CreativeModeTab> DECISLAND_CROPS_TAB = registerTab(
            new CreativeTabConfig.Builder("decisland_crops_tab", Map.of(
                    "en_us", "DecIsland Crops",
                    "zh_cn", "DecIsland 作物"
            ))
                    .iconItem(() -> ModItems.BIZARRE_CHILLI.get())
                    .build()
    );

    public static final Supplier<CreativeModeTab> DECISLAND_MASKS_TAB = registerTab(
            new CreativeTabConfig.Builder("decisland_masks_tab", Map.of(
                    "en_us", "DecIsland Masks",
                    "zh_cn", "DecIsland 面具"
            ))
                    .iconItem(() -> Mask.FRANK_MASK.get())
                    .build()
    );

    /**
     * 将指定物品栏的物品添加到输出列表
     *
     * @param tabConfig 物品栏配置
     * @param output    输出列表
     */
    private static void addItemsToTab(CreativeTabConfig tabConfig, CreativeModeTab.Output output) {
        ModItems.getItemConfigs().forEach(config -> {
            // 检查物品配置中的物品栏是否匹配
            if (config.creativeTab != null &&
                    config.creativeTab.get() == CREATIVE_MOD_TABS.getEntries().stream()
                            .filter(entry -> entry.getId().getPath().equals(tabConfig.name))
                            .findFirst()
                            .map(Holder::value)
                            .orElse(null)) {
                // 根据物品名称查找对应的物品
                ModItems.ITEMS.getEntries().stream()
                        .filter(entry -> entry.getId().getPath().equals(config.name))
                        .map(Holder::value)
                        .findFirst()
                        .ifPresent(output::accept);
            }
        });
        // 添加方块
        ModBlocks.getBlockConfigs().forEach(config -> {
            // 检查物品配置中的物品栏是否匹配
            if (config.creativeTab != null &&
                    config.creativeTab.get() == CREATIVE_MOD_TABS.getEntries().stream()
                            .filter(entry -> entry.getId().getPath().equals(tabConfig.name))
                            .findFirst()
                            .map(Holder::value)
                            .orElse(null)) {
                // 根据物品名称查找对应的物品
                ModBlocks.BLOCKS.getEntries().stream()
                        .filter(entry -> entry.getId().getPath().equals(config.name))
                        .map(Holder::value)
                        .findFirst()
                        .ifPresent(output::accept);
            }
        });
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }

    public static Supplier<CreativeModeTab> registerTab(CreativeTabConfig config) {
        TAB_CONFIGS.add(config);
        return CREATIVE_MOD_TABS.register(config.name, () -> CreativeModeTab.builder()
                .icon(() -> new ItemStack(config.iconItem.get()))
                .title(Component.translatable("itemGroup." + config.name))
                .displayItems((parameters, output) -> {
                    // 自动添加所有属于此物品栏的物品
                    addItemsToTab(config, output);
                }).build()
        );
    }

    public static List<CreativeTabConfig> getTabConfigs() {
        return TAB_CONFIGS;
    }
}
