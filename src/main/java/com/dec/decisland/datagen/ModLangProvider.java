package com.dec.decisland.datagen;

import com.dec.decisland.DecIsland;
import com.dec.decisland.block.ModBlocks;
import com.dec.decisland.item.ModCreativeModeTabs;
import com.dec.decisland.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

// 通用语言提供者基类
public abstract class ModLangProvider extends LanguageProvider {
    protected final String locale;
    protected ModLangProvider(PackOutput output, String locale) {
        super(output, DecIsland.MOD_ID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        // 物品翻译
        ModItems.getItemConfigs().forEach(config -> {
            Item item = ModItems.getItemByConfig(config);

            if (item != null && config.langMap != null) {
                String translation = config.langMap.get(this.locale);
                if (translation != null) {
                    add(item, translation);
                }
            }
        });

        // 方块翻译
        ModBlocks.getBlockConfigs().forEach(config -> {
            Block block = ModBlocks.getBlockByConfig(config).value();

            if (block != null && config.langMap != null) {
                String translation = config.langMap.get(this.locale);
                if (translation != null) {
                    add(block, translation);
                }
            }
        });

        // 物品组翻译
        ModCreativeModeTabs.getTabConfigs().forEach(config -> {
            if (config.langMap == null) return;
            String translation = config.langMap.get(this.locale);
            if (translation == null) return;
            add("itemGroup." + config.name, translation);
        });
    }
}
