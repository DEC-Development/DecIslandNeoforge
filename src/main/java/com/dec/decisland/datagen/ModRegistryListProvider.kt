package com.dec.decisland.datagen

import com.dec.decisland.DecIsland
import com.dec.decisland.attachment.ModAttachments
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.effect.ModEffects
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.particles.ModParticles
import com.dec.decisland.worldgen.ModWorldgen
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

class ModRegistryListProvider(
    private val output: PackOutput,
) : DataProvider {
    override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
        val listRoot = resolveListRoot(output)
        Files.createDirectories(listRoot)

        writeList(listRoot.resolve("items.txt"), ModItems.ITEMS.getEntries().map { it.id.path })
        writeList(listRoot.resolve("blocks.txt"), ModBlocks.getBlockConfigs().map { it.name })
        writeList(listRoot.resolve("recipes.txt"), collectRecipeIds())
        writeList(listRoot.resolve("entities.txt"), ModEntities.ENTITY_TYPES.getEntries().map { it.id.path })
        writeList(listRoot.resolve("particles.txt"), ModParticles.getParticleConfigs().map { it.name })
        writeList(listRoot.resolve("effects.txt"), ModEffects.MOB_EFFECTS.getEntries().map { it.id.path })
        writeList(listRoot.resolve("attachments.txt"), ModAttachments.ATTACHMENT_TYPES.getEntries().map { it.id.path })
        writeList(listRoot.resolve("creative_tabs.txt"), ModCreativeModeTabs.getTabConfigs().map { it.name })
        writeList(listRoot.resolve("biome_sources.txt"), ModWorldgen.BIOME_SOURCES.getEntries().map { it.id.path })
        writeList(listRoot.resolve("chunk_generators.txt"), ModWorldgen.CHUNK_GENERATORS.getEntries().map { it.id.path })

        return CompletableFuture.completedFuture(null)
    }

    override fun getName(): String = "DecIsland Registry Lists"

    private fun writeList(
        path: Path,
        ids: Iterable<String>,
    ) {
        val content = ids
            .distinct()
            .sorted()
            .joinToString(separator = "\n", postfix = "\n")

        Files.writeString(
            path,
            content,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE,
        )
    }

    private fun resolveListRoot(output: PackOutput): Path {
        val outputFolder = output.outputFolder.toAbsolutePath().normalize()
        var current: Path? = outputFolder

        while (current != null) {
            if (current.fileName?.toString() == "src") {
                return current.parent.resolve("list")
            }
            current = current.parent
        }

        return outputFolder.parent.resolve("list")
    }

    private fun collectRecipeIds(): List<String> {
        val projectRoot = resolveListRoot(output).parent
        val recipeDirectories = listOf(
            projectRoot.resolve("src/main/resources/data/${DecIsland.MOD_ID}/recipe"),
            output.outputFolder.resolve("data/${DecIsland.MOD_ID}/recipe"),
        )

        return recipeDirectories
            .flatMap(::collectJsonIds)
            .distinct()
            .sorted()
    }

    private fun collectJsonIds(directory: Path): List<String> {
        if (!Files.exists(directory)) {
            return emptyList()
        }

        Files.walk(directory).use { paths ->
            return paths
                .filter { path -> Files.isRegularFile(path) && path.fileName.toString().endsWith(".json") }
                .map { path ->
                    directory.relativize(path)
                        .toString()
                        .replace('\\', '/')
                        .removeSuffix(".json")
                }
                .collect(Collectors.toList())
        }
    }
}
