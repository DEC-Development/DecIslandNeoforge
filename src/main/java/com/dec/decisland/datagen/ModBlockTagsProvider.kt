package com.dec.decisland.datagen

import com.dec.decisland.DecIsland
import com.dec.decisland.block.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.BlockTagsProvider
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : BlockTagsProvider(output, lookupProvider, DecIsland.MOD_ID) {
    override fun addTags(provider: HolderLookup.Provider) {
        ModBlocks.getBlockConfigs().forEach { config ->
            val block = ModBlocks.getBlockByConfig(config).value()
            config.tags?.forEach { blockTag ->
                tag(blockTag).add(block)
            }
        }
    }
}
