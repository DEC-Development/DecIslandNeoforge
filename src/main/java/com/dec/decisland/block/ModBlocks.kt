package com.dec.decisland.block

import com.dec.decisland.DecIsland
import com.dec.decisland.block.category.SimplePlant
import com.dec.decisland.block.custom.FlowerGhostBlock
import com.dec.decisland.block.custom.NightmareBlock
import com.dec.decisland.block.custom.SimpleCropBlock
import com.dec.decisland.block.custom.SimplePlantBlock
import com.dec.decisland.block.custom.SnowPortalBlock
import com.dec.decisland.block.custom.CornCropBlock
import com.dec.decisland.datagen.ModBlockLootTablesProvider
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.category.Food
import com.dec.decisland.item.category.Crop
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Holder
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.CreativeModeTab
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
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.advancements.criterion.StatePropertiesPredicate
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
    ) {
        enum class Kind {
            CUBE_ALL,
            CUBE_BOTTOM_TOP,
            COLUMN,
        }

        companion object {
            fun cubeAll(): BlockModelSpec = BlockModelSpec(Kind.CUBE_ALL)

            fun cubeBottomTop(
                sideTexture: String,
                topTexture: String,
                bottomTexture: String,
            ): BlockModelSpec = BlockModelSpec(Kind.CUBE_BOTTOM_TOP, sideTexture, topTexture, bottomTexture)

            fun column(
                sideTexture: String,
                topTexture: String,
            ): BlockModelSpec = BlockModelSpec(Kind.COLUMN, sideTexture, topTexture)
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
        val tags: List<TagKey<Block>> = emptyList(),
        val model: BlockModelSpec = BlockModelSpec.cubeAll(),
        val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_MATERIALS_TAB,
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

    private data class LootSpec(
        val dropItem: Supplier<out ItemLike>? = null,
        val minCount: Float = 1.0f,
        val maxCount: Float = 1.0f,
        val silkTouch: Boolean = false,
    ) {
        companion object {
            fun self(): LootSpec = LootSpec()

            fun drop(
                item: Supplier<out ItemLike>,
                minCount: Float = 1.0f,
                maxCount: Float = 1.0f,
                silkTouch: Boolean = false,
            ): LootSpec = LootSpec(item, minCount, maxCount, silkTouch)
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
                .noCollision()
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

    private fun <T : Block> registerBlock(
        name: String,
        func: Function<BlockBehaviour.Properties, out T>,
        props: Supplier<BlockBehaviour.Properties>,
        shouldRegistryBlockItem: Boolean,
    ): DeferredBlock<T> {
        val block = BLOCKS.registerBlock(name, func, props)
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
        val block = BLOCKS.registerBlock(config.name, config.func, config.props) as DeferredBlock<T>
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
            BlockModelSpec.Kind.CUBE_ALL -> blockModels.createTrivialCube(block)

            BlockModelSpec.Kind.CUBE_BOTTOM_TOP -> {
                val model = ModelTemplates.CUBE_BOTTOM_TOP.create(
                    block,
                    TextureMapping()
                        .put(TextureSlot.SIDE, blockTexture(spec.model.sideTexture!!))
                        .put(TextureSlot.TOP, blockTexture(spec.model.topTexture!!))
                        .put(TextureSlot.BOTTOM, blockTexture(spec.model.bottomTexture!!)),
                    blockModels.modelOutput,
                )
                blockModels.blockStateOutput.accept(
                    BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)),
                )
                blockModels.registerSimpleItemModel(block, model)
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
                blockModels.blockStateOutput.accept(
                    BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)),
                )
                blockModels.registerSimpleItemModel(block, model)
            }
        }
    }

    private fun generateSimplePlantModel(
        spec: SimplePlantSpec,
        blockModels: BlockModelGenerators,
    ) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.${spec.name}").value()
        blockModels.createCrossBlock(
            block,
            BlockModelGenerators.PlantType.NOT_TINTED,
            TextureMapping.cross(blockTexture(spec.textureName)),
        )
        val itemModel =
            ModelTemplates.FLAT_ITEM.create(
                block.asItem(),
                TextureMapping.layer0(blockTexture(spec.textureName)),
                blockModels.modelOutput,
        )
        blockModels.registerSimpleItemModel(block, itemModel)
    }

    private fun generateSimpleCropModel(
        spec: SimpleCropSpec,
        blockModels: BlockModelGenerators,
    ) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.${spec.name}").value()
        if (spec.crossModel) {
            blockModels.registerSimpleFlatItemModel(spec.seedItem.get().asItem())
            val stageModels = mutableMapOf<Int, Identifier>()
            blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block).with(
                    PropertyDispatch.initial(CropBlock.AGE).generate { age ->
                        val stage = spec.ageToModelStage[age.toInt()]
                        val model = stageModels.getOrPut(stage) {
                            val suffix = "_stage$stage"
                            val textureMapping = TextureMapping.cross(TextureMapping.getBlockTexture(block, suffix))
                            BlockModelGenerators.PlantType.NOT_TINTED
                                .getCross()
                                .createWithSuffix(block, suffix, textureMapping, blockModels.modelOutput)
                        }
                        BlockModelGenerators.plainVariant(model)
                    },
                ),
            )
        } else {
            blockModels.createCropBlock(block, CropBlock.AGE, *spec.ageToModelStage)
        }
    }

    private fun blockTexture(name: String): Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "block/$name")

    private fun generateSimpleBlockLoot(
        spec: SimpleBlockSpec,
        lootTables: ModBlockLootTablesProvider,
    ) {
        val block = getBlockByName("block.${DecIsland.MOD_ID}.${spec.name}").value()
        val dropItem = spec.loot.dropItem?.get()
        when {
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
        blockModels.registerSimpleFlatItemModel(Crop.CORN_SEEDS.get().asItem())
        val stageModels = mutableMapOf<String, Identifier>()
        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(block).with(
                PropertyDispatch.initial(CornCropBlock.AGE, DoublePlantBlock.HALF).generate { age, half ->
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
                        BlockModelGenerators.PlantType.NOT_TINTED
                            .getCross()
                            .createWithSuffix(block, suffix, textureMapping, blockModels.modelOutput)
                    }
                    BlockModelGenerators.plainVariant(model)
                },
            ),
        )
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
