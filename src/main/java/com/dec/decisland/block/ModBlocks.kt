package com.dec.decisland.block

import com.dec.decisland.DecIsland
import com.dec.decisland.block.category.SimplePlant
import com.dec.decisland.block.custom.FlowerGhostBlock
import com.dec.decisland.block.custom.NightmareBlock
import com.dec.decisland.block.custom.SimpleCropBlock
import com.dec.decisland.block.custom.SimplePlantBlock
import com.dec.decisland.block.custom.ShapedBlock
import com.dec.decisland.block.custom.SnowPortalBlock
import com.dec.decisland.block.custom.CornCropBlock
import com.dec.decisland.datagen.ModBlockLootTablesProvider
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.category.Food
import com.dec.decisland.item.category.Crop
import com.dec.decisland.item.category.Weapon
import com.dec.decisland.item.category.Fashion
import net.minecraft.core.Direction
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.DelegatedModel
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.PropertyDispatch
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

object ModBlocks {
    @JvmField
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(DecIsland.MOD_ID)

    private val blockConfigs = mutableListOf<BlockConfig>()

    private data class BlockModelSpec(
        val kind: Kind,
        val sideTexture: String? = null,
        val topTexture: String? = null,
        val bottomTexture: String? = null,
        val texture: String? = null,
        val customPath: String? = null,
    ) {
        enum class Kind {
            CUBE_ALL,
            CUBE_BOTTOM_TOP,
            COLUMN,
            CUSTOM,
        }

        companion object {
            fun cubeAll(): BlockModelSpec = BlockModelSpec(Kind.CUBE_ALL)

            fun cubeAll(texture: String): BlockModelSpec = BlockModelSpec(Kind.CUBE_ALL, texture = texture)

            fun cubeBottomTop(
                sideTexture: String,
                topTexture: String,
                bottomTexture: String,
            ): BlockModelSpec = BlockModelSpec(Kind.CUBE_BOTTOM_TOP, sideTexture, topTexture, bottomTexture)

            fun column(
                sideTexture: String,
                topTexture: String,
            ): BlockModelSpec = BlockModelSpec(Kind.COLUMN, sideTexture, topTexture)

            fun custom(path: String): BlockModelSpec = BlockModelSpec(Kind.CUSTOM, customPath = path)
        }
    }

    private data class SimpleBlockSpec(
        val name: String,
        val destroyTime: Float,
        val explosionResistance: Float,
        val sound: SoundType,
        val lightLevel: Int = 0,
        val friction: Float? = null,
        val requiresCorrectTool: Boolean = false,
        val noOcclusion: Boolean = false,
        val tags: List<TagKey<Block>> = emptyList(),
        val model: BlockModelSpec = BlockModelSpec.cubeAll(),
        val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_BLOCKS_TAB,
        val factory: Function<BlockBehaviour.Properties, out Block> = Function(::Block),
        val loot: LootSpec = LootSpec.self(),
    )

    private data class SimplePlantSpec(
        val name: String,
        val textureName: String = name,
        val langMap: Map<String, String>,
        val selectionWidth: Double,
        val selectionHeight: Double,
        val placement: (BlockState) -> Boolean,
        val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_NATURE_TAB,
        val lightLevel: Int = 0,
    )

    private data class SimpleCropSpec(
        val name: String,
        val factory: Function<BlockBehaviour.Properties, out SimpleCropBlock>,
        val ageToModelStage: IntArray,
        val seedItem: Supplier<out ItemLike>,
        val cropItem: Supplier<out ItemLike>,
        val crossModel: Boolean,
    )

    private data class WeightedDrop(
        val item: Supplier<out ItemLike>,
        val weight: Int,
        val maxCount: Int = 1,
    )

    private data class LootSpec(
        val dropItem: Supplier<out ItemLike>? = null,
        val minCount: Float = 1.0f,
        val maxCount: Float = 1.0f,
        val silkTouch: Boolean = false,
        val chance: Float? = null,
        val weighted: List<WeightedDrop>? = null,
        val noDrop: Boolean = false,
    ) {
        companion object {
            fun self(): LootSpec = LootSpec()

            fun none(): LootSpec = LootSpec(noDrop = true)

            fun drop(
                item: Supplier<out ItemLike>,
                minCount: Float = 1.0f,
                maxCount: Float = 1.0f,
                silkTouch: Boolean = false,
            ): LootSpec = LootSpec(item, minCount, maxCount, silkTouch)

            fun chance(
                item: Supplier<out ItemLike>,
                chance: Float,
            ): LootSpec = LootSpec(item, chance = chance)

            fun weighted(entries: List<WeightedDrop>): LootSpec = LootSpec(weighted = entries)
        }
    }

    @JvmField
    val ASH: DeferredBlock<Block> = registerBlock(
        BlockConfig.Builder("ash")
            .props { BlockBehaviour.Properties.of().strength(0.2f, 0.5f).sound(SoundType.SAND).noLootTable() }
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .blockLootTableGenerator {}
            .build(),
    )

    @JvmField
    val ANCIENT_ICE: DeferredBlock<Block> = registerBlock(
        BlockConfig.Builder("ancient_ice")
            .props { BlockBehaviour.Properties.of().strength(2.8f, 2.8f).friction(0.991f).requiresCorrectToolForDrops().sound(SoundType.GLASS) }
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .tags(pickaxeDiamondTags())
            .build(),
    )

    @JvmField
    val AMETHYST_LANTERN: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "amethyst_lantern",
            destroyTime = 2.0f,
            explosionResistance = 10.0f,
            sound = SoundType.GLASS,
            lightLevel = 15,
        ),
    )

    @JvmField
    val BLACK_FLOWER_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("black_flower_block", 0.2f, 0.0f, SoundType.GRASS),
    )

    @JvmField
    val BLUE_FLOWER_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("blue_flower_block", 0.2f, 0.0f, SoundType.GRASS),
    )

    /*
    @JvmField
    val BABY_BLUE_EYES_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        SimplePlantSpec(
            name = "baby_blue_eyes_flower",
            langMap = mapOf("en_us" to "Baby Blue Eyes Flower", "zh_cn" to "喜林草"),
            selectionWidth = 9.0,
            selectionHeight = 16.0,
            placement = placementOf(Blocks.GRASS_BLOCK, Blocks.DIRT),
        ),
    )

    @JvmField
    val BALLOON_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        SimplePlantSpec(
            name = "balloon_flower",
            langMap = mapOf("en_us" to "Balloon Flower", "zh_cn" to "桔梗花"),
            selectionWidth = 7.0,
            selectionHeight = 15.0,
            placement = placementOf(Blocks.STONE, Blocks.SNOW_BLOCK, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRAVEL),
        ),
    )

    @JvmField
    val BUTTER_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        SimplePlantSpec(
            name = "butter_flower",
            langMap = mapOf("en_us" to "Butter Flower", "zh_cn" to "黄油郁金香"),
            selectionWidth = 9.0,
            selectionHeight = 15.0,
            placement = placementOf(Blocks.GRASS_BLOCK, Blocks.DIRT),
        ),
    )

    @JvmField
    val ROSEMARY: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        SimplePlantSpec(
            name = "rosemary",
            langMap = mapOf("en_us" to "Rosemary", "zh_cn" to "迷迭香"),
            selectionWidth = 10.0,
            selectionHeight = 15.0,
            placement = placementOf(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.PODZOL),
        ),
    )

    @JvmField
    val THYME: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        SimplePlantSpec(
            name = "thyme",
            langMap = mapOf("en_us" to "Thyme", "zh_cn" to "百里香"),
            selectionWidth = 10.0,
            selectionHeight = 15.0,
            placement = placementOf(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.PODZOL),
        ),
    )

    @JvmField
    val SNOW_LOTUS: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        SimplePlantSpec(
            name = "snow_lotus",
            langMap = mapOf("en_us" to "Snow Lotus", "zh_cn" to "雪莲花"),
            selectionWidth = 12.0,
            selectionHeight = 9.0,
            placement = placementOf(Blocks.STONE, Blocks.SNOW_BLOCK, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRAVEL),
        ),
    )

    @JvmField
    val WITHER_CONE: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        SimplePlantSpec(
            name = "wither_cone",
            langMap = mapOf("en_us" to "Wither Cone", "zh_cn" to "枯萎锥"),
            selectionWidth = 10.0,
            selectionHeight = 12.0,
            placement = placementOf(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND, Blocks.GRAVEL),
        ),
    )
    */

    @JvmField
    val BABY_BLUE_EYES_FLOWER: DeferredBlock<SimplePlantBlock> = SimplePlant.BABY_BLUE_EYES_FLOWER

    @JvmField
    val BALLOON_FLOWER: DeferredBlock<SimplePlantBlock> = SimplePlant.BALLOON_FLOWER

    @JvmField
    val BUTTER_FLOWER: DeferredBlock<SimplePlantBlock> = SimplePlant.BUTTER_FLOWER

    @JvmField
    val ROSEMARY: DeferredBlock<SimplePlantBlock> = SimplePlant.ROSEMARY

    @JvmField
    val THYME: DeferredBlock<SimplePlantBlock> = SimplePlant.THYME

    @JvmField
    val SNOW_LOTUS: DeferredBlock<SimplePlantBlock> = SimplePlant.SNOW_LOTUS

    @JvmField
    val WITHER_CONE: DeferredBlock<SimplePlantBlock> = SimplePlant.WITHER_CONE

    @JvmField
    val BLUE_ICE_BRICK_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "blue_ice_brick_block",
            destroyTime = 3.5f,
            explosionResistance = 20.0f,
            sound = SoundType.GLASS,
            friction = 0.52f,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val CANDY_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "candy_block",
            destroyTime = 1.5f,
            explosionResistance = 5.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val CHISELED_ICE_BRICK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "chiseled_ice_brick",
            destroyTime = 1.9f,
            explosionResistance = 10.0f,
            sound = SoundType.GLASS,
            friction = 0.55f,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val COMPRESS_FLOWER_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("compress_flower_block", 0.3f, 5.0f, SoundType.GRASS),
    )

    @JvmField
    val COMPRESSED_ICE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "compressed_ice",
            destroyTime = 0.5f,
            explosionResistance = 0.5f,
            sound = SoundType.GLASS,
            friction = 0.97f,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val BLUE_GEM_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "blue_gem_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Material.BLUE_GEM_DEBRIS.get() }, 1.0f, 3.0f, silkTouch = true),
        ),
    )

    @JvmField
    val RED_GEM_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "red_gem_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Material.RED_GEM_DEBRIS.get() }, 1.0f, 3.0f, silkTouch = true),
        ),
    )

    @JvmField
    val URANIUM_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "uranium_ore",
            tags = pickaxeStoneTags(),
        ),
    )

    @JvmField
    val STREAM_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "stream_ore",
            tags = pickaxeIronTags(),
        ),
    )

    @JvmField
    val LAVA_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "lava_ore",
            tags = pickaxeIronTags(),
        ),
    )

    @JvmField
    val END_COAL_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "end_coal_ore",
            tags = pickaxeTags(),
            loot = LootSpec.drop(Supplier { Material.COAL_NUGGET.get() }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val END_DIAMOND_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "end_diamond_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Material.DIAMOND_NUGGET.get() }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val END_EMERALD_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "end_emerald_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Material.EMERALD_NUGGET.get() }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val END_GOLD_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "end_gold_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Items.GOLD_NUGGET }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val END_IRON_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "end_iron_ore",
            tags = pickaxeStoneTags(),
            loot = LootSpec.drop(Supplier { Items.IRON_NUGGET }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val END_LAPIS_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "end_lapis_ore",
            tags = pickaxeStoneTags(),
            loot = LootSpec.drop(Supplier { Material.LAPIS_NUGGET.get() }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val END_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "end_ore",
            destroyTime = 3.0f,
            explosionResistance = 9.0f,
            tags = pickaxeTags(),
            loot = LootSpec.drop(Supplier { Material.ENDER_POWDER.get() }, 1.0f, 4.0f, silkTouch = true),
        ),
    )

    @JvmField
    val END_REDSTONE_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "end_redstone_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Items.REDSTONE }, 2.0f, 4.0f, silkTouch = true),
        ),
    )

    @JvmField
    val NETHER_COAL_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "nether_coal_ore",
            tags = pickaxeTags(),
            loot = LootSpec.drop(Supplier { Material.COAL_NUGGET.get() }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val NETHER_DIAMOND_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "nether_diamond_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Material.DIAMOND_NUGGET.get() }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val NETHER_EMERALD_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "nether_emerald_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Material.EMERALD_NUGGET.get() }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val NETHER_IRON_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "nether_iron_ore",
            tags = pickaxeStoneTags(),
            loot = LootSpec.drop(Supplier { Items.IRON_NUGGET }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val NETHER_LAPIS_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "nether_lapis_ore",
            tags = pickaxeStoneTags(),
            loot = LootSpec.drop(Supplier { Material.LAPIS_NUGGET.get() }, 3.0f, 5.0f, silkTouch = true),
        ),
    )

    @JvmField
    val NETHER_REDSTONE_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "nether_redstone_ore",
            tags = pickaxeIronTags(),
            loot = LootSpec.drop(Supplier { Items.REDSTONE }, 2.0f, 4.0f, silkTouch = true),
        ),
    )

    @JvmField
    val SOUL_SOIL_DIAMOND_ORE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        oreSpec(
            name = "soul_soil_diamond_ore",
            tags = pickaxeIronTags(),
            sound = SoundType.SOUL_SOIL,
            loot = LootSpec.drop(Supplier { Material.DIAMOND_NUGGET.get() }, 1.0f, 4.0f, silkTouch = true),
        ),
    )

    @JvmField
    val CRIMSON_LAMP: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "crimson_lamp",
            destroyTime = 2.3f,
            explosionResistance = 15.0f,
            sound = SoundType.STONE,
            lightLevel = 15,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("crimson_lamp_side", "crimson_lamp_top", "crimson_lamp_bottom"),
        ),
    )

    @JvmField
    val GOLDEN_LAMP: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "golden_lamp",
            destroyTime = 1.5f,
            explosionResistance = 5.0f,
            sound = SoundType.STONE,
            lightLevel = 15,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("golden_lamp_side", "golden_lamp_top", "golden_lamp_bottom"),
        ),
    )

    @JvmField
    val ICE_BRICK_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "ice_brick_block",
            destroyTime = 1.7f,
            explosionResistance = 10.0f,
            sound = SoundType.GLASS,
            friction = 0.52f,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val ICE_LANTERN: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "ice_lantern",
            destroyTime = 2.0f,
            explosionResistance = 10.0f,
            sound = SoundType.GLASS,
            lightLevel = 15,
        ),
    )

    @JvmField
    val ICE_MIXED_BRICK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "ice_mixed_brick",
            destroyTime = 1.5f,
            explosionResistance = 10.0f,
            sound = SoundType.GLASS,
            friction = 0.54f,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val LIGHT_OBSIDIAN: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "light_obsidian",
            destroyTime = 2.0f,
            explosionResistance = 100.0f,
            sound = SoundType.GLASS,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val LURK_LOG: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "lurk_log",
            destroyTime = 1.0f,
            explosionResistance = 20.0f,
            sound = SoundType.WOOD,
            tags = axeTags(),
            model = BlockModelSpec.column("lurk_log_side", "lurk_log_top"),
            factory = Function(::RotatedPillarBlock),
        ),
    )

    @JvmField
    val PINK_FLOWER_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("pink_flower_block", 0.2f, 0.0f, SoundType.GRASS),
    )

    @JvmField
    val RED_FLOWER_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("red_flower_block", 0.2f, 0.0f, SoundType.GRASS),
    )

    @JvmField
    val RED_STONEBRICK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "red_stonebrick",
            destroyTime = 2.1f,
            explosionResistance = 10.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val ROTTEN_FLESH_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("rotten_flesh_block", 2.0f, 0.0f, SoundType.GRAVEL),
    )

    @JvmField
    val SMOOTH_AMETHYST_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "smooth_amethyst_block",
            destroyTime = 2.3f,
            explosionResistance = 10.0f,
            sound = SoundType.GLASS,
            lightLevel = 1,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val SOLIDIFIED_LAVA_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "solidified_lava_block",
            destroyTime = 2.5f,
            explosionResistance = 10.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val STEEL_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "steel_block",
            destroyTime = 3.5f,
            explosionResistance = 10.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val URANIUM_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "uranium_block",
            destroyTime = 3.5f,
            explosionResistance = 10.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val WHITE_FLOWER_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("white_flower_block", 0.2f, 0.0f, SoundType.GRASS),
    )

    @JvmField
    val YELLOW_FLOWER_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("yellow_flower_block", 0.2f, 0.0f, SoundType.GRASS),
    )

    @JvmField
    val SNOW_PORTAL: DeferredBlock<SnowPortalBlock> = registerBlock(
        "snow_portal",
        ::SnowPortalBlock,
        Supplier {
            BlockBehaviour.Properties.of()
                .noOcclusion()
                .strength(-1.0f)
                .lightLevel { 11 }
                .sound(SoundType.GLASS)
                .noLootTable()
                .pushReaction(PushReaction.BLOCK)
        },
        false,
    )

    @JvmField
    val NIGHTMARE_BLOCK: DeferredBlock<NightmareBlock> = registerBlock(
        BlockConfig.Builder("nightmare_block")
            .func(::NightmareBlock)
            .props {
                BlockBehaviour.Properties.of()
                    .strength(1.0f, 50.0f)
                    .lightLevel { 3 }
                    .noLootTable()
            }
            .shouldRegistryBlockItem(false)
            .blockLootTableGenerator {}
            .build(),
    )

    @JvmField
    val FLOWER_GHOST_BLOCK: DeferredBlock<FlowerGhostBlock> = registerBlock(
        BlockConfig.Builder("flower_ghost_block")
            .func(::FlowerGhostBlock)
            .props {
                BlockBehaviour.Properties.of()
                    .strength(0.01f, 0.0f)
                    .noLootTable()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
            }
            .shouldRegistryBlockItem(false)
            .blockLootTableGenerator {}
            .build(),
    )

    // ==================== 基岩版移植方块（第二波：笼子/灯笼/书架/特殊掉落） ====================

    // 红灯笼碰撞箱：基岩版 origin [-5,1,-5] size [10,12,10]
    private val RED_LANTERN_SHAPE: VoxelShape = Shapes.box(3.0 / 16, 1.0 / 16, 3.0 / 16, 13.0 / 16, 13.0 / 16, 13.0 / 16)
    // 圣诞礼物碰撞箱：origin [-4,0,-4] size [8,8,8]
    private val CHRISTMAS_GIFT_SHAPE: VoxelShape = Shapes.box(4.0 / 16, 0.0, 4.0 / 16, 12.0 / 16, 8.0 / 16, 12.0 / 16)
    // 金链碰撞箱：origin [-2.5,0,-2.5] size [5,16,5]
    private val GOLDEN_CHAIN_SHAPE: VoxelShape = Shapes.box(5.5 / 16, 0.0, 5.5 / 16, 10.5 / 16, 1.0, 10.5 / 16)
    // 灯罩碰撞箱：origin [-8,0,5] size [16,16,3]（贴背板的竖直板）
    private val LAMPSHADE_SHAPE: VoxelShape = Shapes.box(0.0, 0.0, 13.0 / 16, 1.0, 1.0, 1.0)
    // 石堆碰撞箱：origin [-6,0,-6] size [13,2,13]
    private val STONE_HEAP_SHAPE: VoxelShape = Shapes.box(2.0 / 16, 0.0, 2.0 / 16, 15.0 / 16, 2.0 / 16, 15.0 / 16)
    // 石板路碰撞箱：origin [-8,0,-8] size [16,2,16]
    private val STONE_ROAD_SHAPE: VoxelShape = Shapes.box(0.0, 0.0, 0.0, 1.0, 2.0 / 16, 1.0)
    // 小石子碰撞箱：origin [-2,0,-2] size [3,2,3]
    private val SMALL_STONE_SHAPE: VoxelShape = Shapes.box(6.0 / 16, 0.0, 6.0 / 16, 9.0 / 16, 2.0 / 16, 9.0 / 16)

    @JvmField
    val ASH_CAGE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "ash_cage",
            destroyTime = 3.2f, explosionResistance = 500.0f,
            sound = SoundType.METAL,
            requiresCorrectTool = true, noOcclusion = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val FROZEN_CAGE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "frozen_cage",
            destroyTime = 3.2f, explosionResistance = 500.0f,
            sound = SoundType.GLASS, friction = 0.55f,
            requiresCorrectTool = true, noOcclusion = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val RED_LANTERN: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "red_lantern",
            destroyTime = 0.01f, explosionResistance = 0.0f,
            sound = SoundType.GLASS,
            lightLevel = 15, noOcclusion = true,
            factory = Function { properties -> ShapedBlock(properties, RED_LANTERN_SHAPE) },
            model = BlockModelSpec.custom("red_lantern"),
        ),
    )

    @JvmField
    val ICE_BOOKSHELF: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "ice_bookshelf",
            destroyTime = 1.5f, explosionResistance = 10.0f,
            sound = SoundType.GLASS, friction = 0.55f,
            model = BlockModelSpec.cubeBottomTop("ice_bookshelf_side", "ice_brick_block", "ice_brick_block"),
            loot = LootSpec.weighted(
                listOf(
                    WeightedDrop(Supplier { Items.BOOK }, 40, 3),
                    WeightedDrop(Supplier { Weapon.SNOWBALL_MAGIC_BOOK.get() }, 1),
                    WeightedDrop(Supplier { Material.OLD_BOOK.get() }, 3),
                    WeightedDrop(Supplier { Weapon.LAPIS_MAGIC_BOOK.get() }, 1),
                    WeightedDrop(Supplier { ModItems.EXPERIENCE_BOOK_EMPTY.get() }, 1),
                    WeightedDrop(Supplier { ModItems.EXPERIENCE_BOOK.get() }, 1),
                ),
            ),
        ),
    )

    @JvmField
    val LURK_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "lurk_block",
            destroyTime = 0.5f, explosionResistance = 20.0f,
            sound = SoundType.GRASS, noOcclusion = true,
            loot = LootSpec.chance(Supplier { SimplePlant.LURK_SPRING.get() }, 0.1f),
        ),
    )

    @JvmField
    val LURK_END_STONE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "lurk_end_stone",
            destroyTime = 3.0f, explosionResistance = 30.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("lurk_end_stone_side", "lurk_end_stone_up", "minecraft:end_stone"),
            loot = LootSpec.weighted(
                listOf(
                    WeightedDrop(Supplier { ModBlocks.LURK_END_STONE.get() }, 1),
                    WeightedDrop(Supplier { Items.END_STONE }, 4),
                ),
            ),
        ),
    )

    @JvmField
    val RADIATE_STONE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "radiate_stone",
            destroyTime = 1.5f, explosionResistance = 15.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true, noOcclusion = true,
            tags = pickaxeTags(),
            loot = LootSpec.weighted(
                listOf(
                    WeightedDrop(Supplier { ModBlocks.RADIATE_STONE.get() }, 1),
                    WeightedDrop(Supplier { Material.SMALL_STONE.get() }, 1),
                    WeightedDrop(Supplier { Items.COBBLESTONE }, 1),
                ),
            ),
        ),
    )

    @JvmField
    val RADIATE_DIRT: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "radiate_dirt",
            destroyTime = 1.0f, explosionResistance = 5.0f,
            sound = SoundType.GRAVEL, noOcclusion = true,
            loot = LootSpec.weighted(
                listOf(
                    WeightedDrop(Supplier { ModBlocks.RADIATE_DIRT.get() }, 1),
                    WeightedDrop(Supplier { Items.DIRT }, 1),
                ),
            ),
        ),
    )

    @JvmField
    val RADIATE_STONEBRICK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "radiate_stonebrick",
            destroyTime = 1.5f, explosionResistance = 15.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true, noOcclusion = true,
            tags = pickaxeTags(),
            // 基岩版 radiate_stonebrick 的战利品表就是 radiate_stone.json（权重三选一）
            loot = LootSpec.weighted(
                listOf(
                    WeightedDrop(Supplier { ModBlocks.RADIATE_STONE.get() }, 1),
                    WeightedDrop(Supplier { Material.SMALL_STONE.get() }, 1),
                    WeightedDrop(Supplier { Items.COBBLESTONE }, 1),
                ),
            ),
        ),
    )

    @JvmField
    val RED_PATTERNED_STONEBRICK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "red_patterned_stonebrick",
            destroyTime = 2.1f, explosionResistance = 10.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true, noOcclusion = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeAll("red_patterned_stonebrick_side"),
        ),
    )

    // ==================== 基岩版移植方块（第三波：建筑/装饰/储物） ====================

    @JvmField
    val BROKEN_DIRT: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("broken_dirt", 0.7f, 0.0f, SoundType.GRAVEL, noOcclusion = true),
    )

    @JvmField
    val FLESH_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "flesh_block",
            destroyTime = 1.0f, explosionResistance = 0.0f,
            sound = SoundType.HONEY_BLOCK, noOcclusion = true,
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val SCALE_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "scale_block",
            destroyTime = 4.0f, explosionResistance = 0.0f,
            sound = SoundType.METAL,
            lightLevel = 3, noOcclusion = true,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val SWEET_BERRIES_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec("sweet_berries_block", 0.3f, 0.0f, SoundType.GRASS, noOcclusion = true),
    )

    @JvmField
    val SNOW_BRICK_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "snow_brick_block",
            destroyTime = 1.5f, explosionResistance = 10.0f,
            sound = SoundType.SNOW,
            requiresCorrectTool = true, noOcclusion = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val GHOST_ICE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "ghost_ice",
            destroyTime = 1.0f, explosionResistance = 10.0f,
            sound = SoundType.GLASS, friction = 0.98f,
            noOcclusion = true,
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val DIRT_GHOST_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "dirt_ghost_block",
            destroyTime = 0.01f, explosionResistance = 0.0f,
            sound = SoundType.GRAVEL, noOcclusion = true,
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val FUSE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "fuse",
            destroyTime = 0.3f, explosionResistance = 0.0f,
            sound = SoundType.WOOL, noOcclusion = true,
        ),
    )

    @JvmField
    val EYE_OF_NATURE_LOG: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "eye_of_nature_log",
            destroyTime = 3.0f, explosionResistance = 0.0f,
            sound = SoundType.WOOD, noOcclusion = true,
            tags = axeTags(),
            loot = LootSpec.weighted(
                listOf(
                    WeightedDrop(Supplier { Material.EYE_OF_NATURE.get() }, 1, 1),
                    WeightedDrop(Supplier { Items.OAK_LOG }, 1, 1),
                ),
            ),
        ),
    )

    @JvmField
    val LACE_BLOCK_BLACK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "lace_block_black",
            destroyTime = 1.0f, explosionResistance = 0.0f,
            sound = SoundType.WOOL, noOcclusion = true,
            model = BlockModelSpec.cubeAll("lace_block_black_side"),
        ),
    )

    @JvmField
    val LACE_BLOCK_LIGHT_RED: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "lace_block_light_red",
            destroyTime = 1.0f, explosionResistance = 0.0f,
            sound = SoundType.WOOL, noOcclusion = true,
            model = BlockModelSpec.cubeAll("lace_block_light_red_side"),
        ),
    )

    @JvmField
    val THIN_ROPE_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "thin_rope_block",
            destroyTime = 0.01f, explosionResistance = 0.0f,
            sound = SoundType.WOOL, noOcclusion = true,
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val ICE_ROPE_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "ice_rope_block",
            destroyTime = 0.01f, explosionResistance = 0.0f,
            sound = SoundType.GLASS, noOcclusion = true,
            model = BlockModelSpec.cubeAll("ice_rope_block_0"),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val CRATE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "crate",
            destroyTime = 2.0f, explosionResistance = 10.0f,
            sound = SoundType.WOOD,
            tags = axeTags(),
            model = BlockModelSpec.cubeAll("crate_locked"),
        ),
    )

    @JvmField
    val FROZEN_CRATE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "frozen_crate",
            destroyTime = 2.0f, explosionResistance = 10.0f,
            sound = SoundType.WOOD,
            tags = axeTags(),
            model = BlockModelSpec.cubeBottomTop("frozen_crate_locked", "frozen_crate_up", "crate_locked"),
        ),
    )

    @JvmField
    val CAVE_CRATE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "cave_crate",
            destroyTime = 2.2f, explosionResistance = 10.0f,
            sound = SoundType.WOOD,
            tags = axeTags(),
            model = BlockModelSpec.cubeAll("cave_crate_locked"),
        ),
    )

    @JvmField
    val GOLDEN_FENCE_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "golden_fence_block",
            destroyTime = 2.0f, explosionResistance = 30.0f,
            sound = SoundType.METAL,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val GOLDEN_BOOKSHELF: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "golden_bookshelf",
            destroyTime = 1.5f, explosionResistance = 30.0f,
            sound = SoundType.WOOD,
            lightLevel = 3,
            tags = axeTags(),
            // 前/后/侧面各不相同，使用手写六面模型
            model = BlockModelSpec.custom("golden_bookshelf"),
        ),
    )

    @JvmField
    val GOLDEN_BOOKSHELF_FRAME: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "golden_bookshelf_frame",
            destroyTime = 1.5f, explosionResistance = 30.0f,
            sound = SoundType.WOOD,
            tags = axeTags(),
            model = BlockModelSpec.custom("golden_bookshelf_frame"),
        ),
    )

    @JvmField
    val NUKE_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "nuke_block",
            destroyTime = 1.0f, explosionResistance = 10.0f,
            sound = SoundType.METAL,
            model = BlockModelSpec.cubeBottomTop("nuke_side", "minecraft:tnt_top", "minecraft:tnt_bottom"),
        ),
    )

    @JvmField
    val END_ALTAR: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "end_altar",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("end_altar_side", "end_altar_up", "end_altar_down"),
        ),
    )

    // ==================== 基岩版移植方块（第四波：召唤器/机器） ====================

    @JvmField
    val LIGHTNING_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "lightning_summoner",
            destroyTime = 3.0f, explosionResistance = 10.0f,
            sound = SoundType.METAL,
            model = BlockModelSpec.cubeBottomTop("lightning_summoner", "weather_console_up", "weather_console_down"),
        ),
    )

    @JvmField
    val RAINING_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "raining_summoner",
            destroyTime = 3.0f, explosionResistance = 10.0f,
            sound = SoundType.METAL,
            model = BlockModelSpec.cubeBottomTop("raining_summoner", "weather_console_up", "weather_console_down"),
        ),
    )

    @JvmField
    val SUNNY_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "sunny_summoner",
            destroyTime = 3.0f, explosionResistance = 10.0f,
            sound = SoundType.METAL,
            model = BlockModelSpec.cubeBottomTop("sunny_summoner", "weather_console_up", "weather_console_down"),
        ),
    )

    @JvmField
    val PURPUR_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "purpur_summoner",
            destroyTime = 5.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("purpur_summoner_side", "purpur_summoner_up", "purpur_summoner_up"),
        ),
    )

    @JvmField
    val ENCHANTED_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "enchanted_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("enchanted_summoner_side", "enchanted_summoner_up", "enchanted_summoner_up"),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val PLAIN_TOWER_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "plain_tower_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("plain_tower_summoner_side", "plain_tower_summoner_up", "plain_tower_summoner_up"),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val ASH_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "ash_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val ABYSSAL_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "abyssal_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val BAT_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "bat_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val DEEP_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "deep_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val FOREST_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "forest_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val SOUL_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "soul_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            loot = LootSpec.none(),
        ),
    )

    @JvmField
    val EVERLASTING_WINTER_SUMMONER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "everlasting_winter_summoner",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
        ),
    )

    @JvmField
    val MONITOR: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "monitor",
            destroyTime = 3.0f, explosionResistance = 50.0f,
            sound = SoundType.METAL,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("monitor", "lurk_log_up", "lurk_log_up"),
        ),
    )

    @JvmField
    val MONITOR_ACTIVATED: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "monitor_activated",
            destroyTime = 3.0f, explosionResistance = 50.0f,
            sound = SoundType.METAL,
            lightLevel = 1,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("monitor_activated", "lurk_log_up", "lurk_log_up"),
            // 激活态掉落普通监视器
            loot = LootSpec.drop(Supplier { MONITOR.get() }),
        ),
    )

    @JvmField
    val ITEM_PICKER: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "item_picker",
            destroyTime = 3.0f, explosionResistance = 10.0f,
            sound = SoundType.METAL,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("item_picker_side", "item_picker_up", "item_picker_down"),
        ),
    )

    @JvmField
    val MINING_MACHINE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "mining_machine",
            destroyTime = 3.0f, explosionResistance = 10.0f,
            sound = SoundType.METAL,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("mining_machine_side", "mining_machine_up", "mining_machine_down"),
        ),
    )

    @JvmField
    val MINE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "mine",
            destroyTime = 1.2f, explosionResistance = 7.0f,
            sound = SoundType.METAL,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("mine", "mine_top", "mine"),
        ),
    )

    @JvmField
    val MAGIC_LETTER_BOX: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "magic_letter_box",
            destroyTime = 1.0f, explosionResistance = 30.0f,
            sound = SoundType.WOOD,
            tags = axeTags(),
            // 正面投信口与侧面不同，使用手写六面模型
            model = BlockModelSpec.custom("magic_letter_box"),
        ),
    )

    @JvmField
    val FLOWING_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "flowing_block",
            destroyTime = 3.0f, explosionResistance = 100.0f,
            sound = SoundType.STONE,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            model = BlockModelSpec.cubeBottomTop("flowing_block_side_0", "flowing_block_up", "flowing_block_down"),
        ),
    )

    // ==================== 基岩版移植方块（第五波：自定义碰撞箱） ====================

    @JvmField
    val CHRISTMAS_GIFT_BLOCK: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "christmas_gift_block",
            destroyTime = 0.3f, explosionResistance = 0.0f,
            sound = SoundType.WOOL, noOcclusion = true,
            factory = Function { properties -> ShapedBlock(properties, CHRISTMAS_GIFT_SHAPE) },
            model = BlockModelSpec.cubeAll("christmas_gift_block"),
            loot = LootSpec.weighted(
                listOf(
                    WeightedDrop(Supplier { Fashion.CHRISTMAS_CAP.get() }, 3),
                    WeightedDrop(Supplier { Food.GINGERBREAD_MAN.get() }, 5),
                    WeightedDrop(Supplier { Weapon.GINGERBREAD_SWORD.get() }, 2),
                    WeightedDrop(Supplier { Weapon.CANDY_CANE.get() }, 3),
                ),
            ),
        ),
    )

    @JvmField
    val GOLDEN_CHAIN: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "golden_chain",
            destroyTime = 1.7f, explosionResistance = 40.0f,
            sound = SoundType.CHAIN, noOcclusion = true,
            requiresCorrectTool = true,
            tags = pickaxeTags(),
            factory = Function { properties -> ShapedBlock(properties, GOLDEN_CHAIN_SHAPE) },
        ),
    )

    @JvmField
    val LAMPSHADE: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "lampshade",
            destroyTime = 1.0f, explosionResistance = 20.0f,
            sound = SoundType.WOOL, noOcclusion = true,
            factory = Function { properties -> ShapedBlock(properties, LAMPSHADE_SHAPE) },
        ),
    )

    @JvmField
    val STONE_HEAP: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "stone_heap",
            destroyTime = 0.2f, explosionResistance = 0.0f,
            sound = SoundType.STONE, noOcclusion = true,
            factory = Function { properties -> ShapedBlock(properties, STONE_HEAP_SHAPE) },
            model = BlockModelSpec.cubeAll("small_stone"),
            loot = LootSpec.drop(Supplier { Material.SMALL_STONE.get() }, 2.0f, 5.0f),
        ),
    )

    @JvmField
    val STONE_ROAD: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "stone_road",
            destroyTime = 0.2f, explosionResistance = 0.0f,
            sound = SoundType.STONE, noOcclusion = true,
            factory = Function { properties -> ShapedBlock(properties, STONE_ROAD_SHAPE) },
            model = BlockModelSpec.cubeAll("small_stone"),
            loot = LootSpec.drop(Supplier { Material.SMALL_STONE.get() }, 2.0f, 5.0f),
        ),
    )

    @JvmField
    val SMALL_STONE_BLOCK_ENTITY: DeferredBlock<Block> = registerSimpleBedrockBlock(
        SimpleBlockSpec(
            name = "small_stone_block_entity",
            destroyTime = 0.01f, explosionResistance = 0.0f,
            sound = SoundType.STONE, noOcclusion = true,
            factory = Function { properties -> ShapedBlock(properties, SMALL_STONE_SHAPE) },
            model = BlockModelSpec.cubeAll("small_stone"),
            loot = LootSpec.drop(Supplier { Material.SMALL_STONE.get() }),
        ),
    )

    private fun <T : Block> registerBlock(
        name: String,
        func: Function<BlockBehaviour.Properties, out T>,
        props: Supplier<BlockBehaviour.Properties>,
        shouldRegistryBlockItem: Boolean,
    ): DeferredBlock<T> {
        val block = BLOCKS.registerBlock(name, func, props.get())
        if (shouldRegistryBlockItem) {
            ModItems.ITEMS.registerSimpleBlockItem(block)
        }
        return block
    }

    private fun <T : Block> registerBlock(
        name: String,
        func: Function<BlockBehaviour.Properties, out T>,
        shouldRegistryBlockItem: Boolean,
    ): DeferredBlock<T> {
        val block = BLOCKS.registerBlock(name, func)
        if (shouldRegistryBlockItem) {
            ModItems.ITEMS.registerSimpleBlockItem(block)
        }
        return block
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Block> registerBlock(config: BlockConfig): DeferredBlock<T> {
        val block = BLOCKS.registerBlock(config.name, config.func, config.props.get()) as DeferredBlock<T>
        blockConfigs.add(config)
        if (config.shouldRegistryBlockItem) {
            ModItems.ITEMS.registerSimpleBlockItem(block)
        }
        return block
    }

    private fun registerSimpleBedrockBlock(spec: SimpleBlockSpec): DeferredBlock<Block> {
        val builder = BlockConfig.Builder(spec.name)
            .func(spec.factory)
            .props { simpleProperties(spec) }
            .creativeTab(spec.creativeTab)
            .blockModelGenerator { blockModels -> generateSimpleBlockModel(spec, blockModels) }
            .blockLootTableGenerator { lootTables -> generateSimpleBlockLoot(spec, lootTables) }

        if (spec.tags.isNotEmpty()) {
            builder.tags(spec.tags)
        }

        return registerBlock(builder.build())
    }

    @JvmStatic
    fun registerSimplePlant(
        name: String,
        selectionWidth: Double,
        selectionHeight: Double,
        placement: (BlockState) -> Boolean,
        creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_NATURE_TAB,
        lightLevel: Int = 0,
    ): DeferredBlock<SimplePlantBlock> =
        registerSimplePlant(
            SimplePlantSpec(
                name = name,
                langMap = emptyMap(),
                selectionWidth = selectionWidth,
                selectionHeight = selectionHeight,
                placement = placement,
                creativeTab = creativeTab,
                lightLevel = lightLevel,
            ),
        )

    @JvmStatic
    fun registerSimpleCrop(
        name: String,
        factory: Function<BlockBehaviour.Properties, out SimpleCropBlock>,
        ageToModelStage: IntArray,
        seedItem: Supplier<out ItemLike>,
        cropItem: Supplier<out ItemLike>,
        crossModel: Boolean = false,
    ): DeferredBlock<SimpleCropBlock> =
        registerSimpleCrop(
            SimpleCropSpec(
                name = name,
                factory = factory,
                ageToModelStage = ageToModelStage,
                seedItem = seedItem,
                cropItem = cropItem,
                crossModel = crossModel,
            ),
        )

    private fun registerSimplePlant(spec: SimplePlantSpec): DeferredBlock<SimplePlantBlock> {
        val builder = BlockConfig.Builder(spec.name, spec.langMap)
            .func { properties -> SimplePlantBlock(properties, spec.placement, spec.selectionWidth, spec.selectionHeight) }
            .props { simplePlantProperties(spec) }
            .creativeTab(spec.creativeTab)
            .blockModelGenerator { blockModels -> generateSimplePlantModel(spec, blockModels) }

        return registerBlock(builder.build())
    }

    private fun registerSimpleCrop(spec: SimpleCropSpec): DeferredBlock<SimpleCropBlock> {
        val builder = BlockConfig.Builder(spec.name)
            .func(spec.factory)
            .props { simpleCropProperties() }
            .shouldRegistryBlockItem(false)
            .blockModelGenerator { blockModels -> generateSimpleCropModel(spec, blockModels) }
            .blockLootTableGenerator { lootTables -> generateSimpleCropLoot(spec, lootTables) }

        return registerBlock(builder.build())
    }

    @JvmStatic
    fun registerCornCrop(): DeferredBlock<CornCropBlock> {
        val builder = BlockConfig.Builder("corn_crop")
            .func(Function { properties -> CornCropBlock(properties) })
            .props { simpleCropProperties().mapColor(MapColor.PLANT) }
            .shouldRegistryBlockItem(false)
            .blockModelGenerator { blockModels -> generateCornCropModel(blockModels) }
            .blockLootTableGenerator { lootTables -> generateCornCropLoot(lootTables) }

        return registerBlock(builder.build())
    }

    private fun simpleProperties(spec: SimpleBlockSpec): BlockBehaviour.Properties {
        val properties = BlockBehaviour.Properties.of()
            .strength(spec.destroyTime, spec.explosionResistance)
            .sound(spec.sound)

        if (spec.lightLevel > 0) {
            properties.lightLevel { spec.lightLevel }
        }
        if (spec.friction != null) {
            properties.friction(spec.friction)
        }
        if (spec.requiresCorrectTool) {
            properties.requiresCorrectToolForDrops()
        }
        if (spec.noOcclusion) {
            properties.noOcclusion()
        }

        return properties
    }

    private fun simplePlantProperties(spec: SimplePlantSpec): BlockBehaviour.Properties {
        val properties = BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY)

        if (spec.lightLevel > 0) {
            properties.lightLevel { spec.lightLevel }
        }

        return properties
    }

    private fun simpleCropProperties(): BlockBehaviour.Properties =
        BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
            .pushReaction(PushReaction.DESTROY)

    private fun generateSimpleBlockModel(
        spec: SimpleBlockSpec,
        blockModels: BlockModelGenerators,
    ) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.${spec.name}").value()
        when (spec.model.kind) {
            BlockModelSpec.Kind.CUBE_ALL -> {
                val texture = spec.model.texture
                if (texture != null) {
                    val model = ModelTemplates.CUBE_ALL.create(
                        block,
                        TextureMapping.cube(blockTexture(texture)),
                        blockModels.modelOutput,
                    )
                    blockModels.blockStateOutput.accept(simpleBlock(block, model))
                    delegateItemModel(blockModels, block, model)
                } else {
                    blockModels.createTrivialCube(block)
                    // createTrivialCube 只生成方块模型与 blockstate，需要为 BlockItem 补一个委托物品模型
                    if (block.asItem() != Items.AIR) {
                        delegateItemModel(blockModels, block, ModelLocationUtils.getModelLocation(block))
                    }
                }
            }

            BlockModelSpec.Kind.CUSTOM -> {
                // 手写模型放在 assets/decisland/models/block/ 与 blockstates/ 下，这里仅委托物品模型
                delegateItemModel(
                    blockModels,
                    block,
                    ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "block/" + spec.model.customPath),
                )
            }

            BlockModelSpec.Kind.CUBE_BOTTOM_TOP -> {
                val model = ModelTemplates.CUBE_BOTTOM_TOP.create(
                    block,
                    TextureMapping()
                        .put(TextureSlot.SIDE, blockTexture(spec.model.sideTexture!!))
                        .put(TextureSlot.TOP, blockTexture(spec.model.topTexture!!))
                        .put(TextureSlot.BOTTOM, blockTexture(spec.model.bottomTexture!!)),
                    blockModels.modelOutput,
                )
                blockModels.blockStateOutput.accept(simpleBlock(block, model))
                delegateItemModel(blockModels, block, model)
            }

            BlockModelSpec.Kind.COLUMN -> {
                val model = ModelTemplates.CUBE_COLUMN.create(
                    block,
                    TextureMapping.column(
                        blockTexture(spec.model.sideTexture!!),
                        blockTexture(spec.model.topTexture!!),
                    ),
                    blockModels.modelOutput,
                )
                blockModels.blockStateOutput.accept(axisAlignedPillarBlock(block, model))
                delegateItemModel(blockModels, block, model)
            }
        }
    }

    private fun generateSimplePlantModel(
        spec: SimplePlantSpec,
        blockModels: BlockModelGenerators,
    ) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.${spec.name}").value()
        val model = ModelTemplates.CROSS.create(
            block,
            TextureMapping.cross(blockTexture(spec.textureName)),
            blockModels.modelOutput,
        )
        blockModels.blockStateOutput.accept(simpleBlock(block, model))
        if (block.asItem() != Items.AIR) {
            ModelTemplates.FLAT_ITEM.create(
                ModelLocationUtils.getModelLocation(block.asItem()),
                TextureMapping.layer0(blockTexture(spec.textureName)),
                blockModels.modelOutput,
            )
        }
    }

    private fun generateSimpleCropModel(
        spec: SimpleCropSpec,
        blockModels: BlockModelGenerators,
    ) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.${spec.name}").value()
        if (spec.crossModel) {
            createSimpleFlatItemModel(blockModels, spec.seedItem.get().asItem())
            val stageModels = mutableMapOf<Int, ResourceLocation>()
            blockModels.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block).with(
                    PropertyDispatch.property(CropBlock.AGE).generate { age ->
                        val stage = spec.ageToModelStage[age.toInt()]
                        val model = stageModels.getOrPut(stage) {
                            val suffix = "_stage$stage"
                            ModelTemplates.CROSS.createWithSuffix(
                                block,
                                suffix,
                                TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix)),
                                blockModels.modelOutput,
                            )
                        }
                        Variant.variant().with(VariantProperties.MODEL, model)
                    },
                ),
            )
        } else {
            createCropBlockModel(blockModels, block, CropBlock.AGE, spec.ageToModelStage)
        }
    }

    private fun blockTexture(name: String): ResourceLocation =
        if (':' in name) {
            val (namespace, path) = name.split(':', limit = 2)
            ResourceLocation.fromNamespaceAndPath(namespace, "block/$path")
        } else {
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "block/$name")
        }

    private fun generateSimpleBlockLoot(
        spec: SimpleBlockSpec,
        lootTables: ModBlockLootTablesProvider,
    ) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.${spec.name}").value()
        val dropItem = spec.loot.dropItem?.get()
        when {
            spec.loot.noDrop -> lootTables.addNoDrop(block)
            spec.loot.weighted != null ->
                lootTables.addWeightedDrop(
                    block,
                    spec.loot.weighted.map { Triple(it.item.get(), it.weight, it.maxCount) },
                )
            spec.loot.chance != null && dropItem != null -> lootTables.addChanceDrop(block, dropItem, spec.loot.chance)
            dropItem == null -> lootTables.addDropSelf(block)
            spec.loot.silkTouch -> lootTables.addSilkTouchRangeDrop(block, dropItem, spec.loot.minCount, spec.loot.maxCount)
            spec.loot.minCount == 1.0f && spec.loot.maxCount == 1.0f -> lootTables.addSingleItemDrop(block, dropItem)
            else -> lootTables.addRangeDrop(block, dropItem, spec.loot.minCount, spec.loot.maxCount)
        }
    }

    private fun generateSimpleCropLoot(
        spec: SimpleCropSpec,
        lootTables: ModBlockLootTablesProvider,
    ) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.${spec.name}").value()
        lootTables.addCropDrop(
            block,
            spec.cropItem.get().asItem(),
            spec.seedItem.get().asItem(),
            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(
                    StatePropertiesPredicate.Builder.properties()
                        .hasProperty(CropBlock.AGE, CropBlock.MAX_AGE),
                ),
        )
    }

    private fun generateCornCropModel(blockModels: BlockModelGenerators) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.corn_crop").value()
        createSimpleFlatItemModel(blockModels, Crop.CORN_SEEDS.get().asItem())
        val stageModels = mutableMapOf<String, ResourceLocation>()
        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block).with(
                PropertyDispatch.properties(CornCropBlock.AGE, DoublePlantBlock.HALF).generate { age, half ->
                    val stage =
                        if (half == DoubleBlockHalf.UPPER) {
                            age.toInt().coerceAtMost(CornCropBlock.UPPER_MAX_AGE)
                        } else {
                            age.toInt()
                        }
                    val halfName = if (half == DoubleBlockHalf.UPPER) "upper" else "lower"
                    val suffix = "_${halfName}_stage$stage"
                    val model = stageModels.getOrPut(suffix) {
                        val textureMapping = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix))
                        ModelTemplates.CROSS.createWithSuffix(block, suffix, textureMapping, blockModels.modelOutput)
                    }
                    Variant.variant().with(VariantProperties.MODEL, model)
                },
            ),
        )
    }

    // 1.21.1 ports of BlockModelGenerators' private/package-private helpers built on the public data-model classes.
    private fun simpleBlock(block: Block, modelLocation: ResourceLocation): MultiVariantGenerator =
        MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, modelLocation))

    private fun axisAlignedPillarBlock(block: Block, modelLocation: ResourceLocation): MultiVariantGenerator =
        MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, modelLocation))
            .with(
                PropertyDispatch.property(BlockStateProperties.AXIS)
                    .select(Direction.Axis.Y, Variant.variant())
                    .select(Direction.Axis.Z, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                    .select(
                        Direction.Axis.X,
                        Variant.variant()
                            .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                            .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90),
                    ),
            )

    private fun delegateItemModel(blockModels: BlockModelGenerators, block: Block, modelLocation: ResourceLocation) {
        blockModels.modelOutput.accept(ModelLocationUtils.getModelLocation(block.asItem()), DelegatedModel(modelLocation))
    }

    private fun createSimpleFlatItemModel(blockModels: BlockModelGenerators, item: Item) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), blockModels.modelOutput)
    }

    private fun createCropBlockModel(
        blockModels: BlockModelGenerators,
        cropBlock: Block,
        ageProperty: IntegerProperty,
        ageToVisualStageMapping: IntArray,
    ) {
        require(ageProperty.possibleValues.size == ageToVisualStageMapping.size)
        val stageModels = mutableMapOf<Int, ResourceLocation>()
        val propertyDispatch = PropertyDispatch.property(ageProperty).generate { age ->
            val stage = ageToVisualStageMapping[age.toInt()]
            val model = stageModels.getOrPut(stage) {
                val suffix = "_stage$stage"
                ModelTemplates.CROP.createWithSuffix(
                    cropBlock,
                    suffix,
                    TextureMapping.crop(TextureMapping.getBlockTexture(cropBlock, suffix)),
                    blockModels.modelOutput,
                )
            }
            Variant.variant().with(VariantProperties.MODEL, model)
        }
        createSimpleFlatItemModel(blockModels, cropBlock.asItem())
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cropBlock).with(propertyDispatch))
    }

    private fun generateCornCropLoot(lootTables: ModBlockLootTablesProvider) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.corn_crop").value()
        lootTables.addCornCropDrop(block, Food.CORN.get())
    }

    private fun oreSpec(
        name: String,
        tags: List<TagKey<Block>>,
        loot: LootSpec = LootSpec.self(),
        destroyTime: Float = 3.0f,
        explosionResistance: Float = 3.0f,
        sound: SoundType = SoundType.STONE,
    ): SimpleBlockSpec =
        SimpleBlockSpec(
            name = name,
            destroyTime = destroyTime,
            explosionResistance = explosionResistance,
            sound = sound,
            requiresCorrectTool = true,
            tags = tags,
            creativeTab = ModCreativeModeTabs.DECISLAND_NATURE_TAB,
            loot = loot,
        )

    private fun pickaxeTags(): List<TagKey<Block>> = listOf(BlockTags.MINEABLE_WITH_PICKAXE)

    private fun pickaxeStoneTags(): List<TagKey<Block>> = listOf(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_STONE_TOOL)

    private fun pickaxeIronTags(): List<TagKey<Block>> = listOf(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)

    private fun pickaxeDiamondTags(): List<TagKey<Block>> = listOf(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)

    private fun axeTags(): List<TagKey<Block>> = listOf(BlockTags.MINEABLE_WITH_AXE)

    @JvmStatic
    fun placementOf(vararg allowedBlocks: Block): (BlockState) -> Boolean {
        val allowedSet = allowedBlocks.toSet()
        return { state -> state.block in allowedSet }
    }

    @JvmStatic
    fun placementOfPaths(vararg allowedPaths: String): (BlockState) -> Boolean {
        val allowedSet = allowedPaths.map(::normalizePlacementPath).toSet()
        return { state ->
            normalizePlacementPath(BuiltInRegistries.BLOCK.getKey(state.block).path) in allowedSet
        }
    }

    private fun normalizePlacementPath(path: String): String =
        when (path.substringAfter(':')) {
            "grass" -> "grass_block"
            "snow" -> "snow_block"
            else -> path.substringAfter(':')
        }

    @JvmStatic
    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
    }

    @JvmStatic
    fun getBlockByName(name: String): Holder<Block> =
        BLOCKS.getEntries()
            .firstOrNull { it.get().descriptionId == name }
            ?: throw NoSuchElementException(name)

    @JvmStatic
    fun getBlockByConfig(config: BlockConfig): Holder<Block> =
        getBlockByName("block.${DecIsland.MOD_ID}.${config.name}")

    @JvmStatic
    fun getBlockConfigs(): List<BlockConfig> = blockConfigs
}
