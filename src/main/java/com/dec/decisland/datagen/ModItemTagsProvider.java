package com.dec.decisland.datagen;

import com.dec.decisland.DecIsland;
import com.dec.decisland.block.ModBlocks;
import com.dec.decisland.item.ModItems;
import com.dec.decisland.tag.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DecIsland.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 给ModItem添加tag
        ModItems.getItemConfigs().forEach(config -> {
            if (config.tags == null) return;
            Item item = ModItems.getItemByConfig(config);
            config.tags.forEach(item_tag ->{
                tag(item_tag).add(item);
            });
        });
        // 给原版物品添加tag
        tag(ModItemTags.REPAIRS_AMETHYST_ARMOR)
                .add(Items.AMETHYST_SHARD)
                .add(Items.BUDDING_AMETHYST)
                .add(Items.AMETHYST_BLOCK)
                .add(Items.AMETHYST_CLUSTER);

        tag(ModItemTags.REPAIRS_FASHION)
                .add(Items.LEATHER)
                .add(Items.STRING)
                .add(Items.SLIME_BALL)
                .addTag(ItemTags.WOOL);
    }
}
