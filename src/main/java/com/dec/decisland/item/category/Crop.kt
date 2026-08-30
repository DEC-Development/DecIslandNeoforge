package com.dec.decisland.item.category

import com.dec.decisland.block.ModBlocks
import com.dec.decisland.block.custom.BizarreChilliCropBlock
import com.dec.decisland.block.custom.CornCropBlock
import com.dec.decisland.block.custom.ExperienceFlowerCropBlock
import com.dec.decisland.block.custom.FritillaryCropBlock
import com.dec.decisland.block.custom.HouttuyniaCropBlock
import com.dec.decisland.block.custom.LeekCropBlock
import com.dec.decisland.block.custom.LavaFlowerCropBlock
import com.dec.decisland.block.custom.MooliCropBlock
import com.dec.decisland.block.custom.RiceCropBlock
import com.dec.decisland.block.custom.SimpleCropBlock
import com.dec.decisland.block.custom.SoybeanCropBlock
import com.dec.decisland.item.CustomItemProperties
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Function
import java.util.function.Supplier

object Crop {
    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_CROPS_TAB

    private val registeredCrops = mutableListOf<DeferredBlock<SimpleCropBlock>>()

    private fun registerCropFood(
        name: String,
        nutrition: Int,
        saturation: Float,
        compostableChance: Float,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .props {
                    Item.Properties().food(
                        FoodProperties(nutrition, saturation, false),
                        Consumables.defaultFood().consumeSeconds(1.6f).build(),
                    )
                }
                .customProp(CustomItemProperties.Builder().compostableChance(compostableChance).build())
                .creativeTab(creativeTab)
                .build(),
        )

    private fun registerSeeds(
        name: String,
        cropBlock: Supplier<out Block>,
        compostableChance: Float,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .func { props -> BlockItem(cropBlock.get(), props) }
                .customProp(CustomItemProperties.Builder().compostableChance(compostableChance).build())
                .creativeTab(creativeTab)
                .build(),
        )

    private fun registerSimpleCrop(
        name: String,
        factory: Function<net.minecraft.world.level.block.state.BlockBehaviour.Properties, out SimpleCropBlock>,
        ageToModelStage: IntArray,
        seedItem: Supplier<out Item>,
        cropItem: Supplier<out Item>,
        crossModel: Boolean = false,
    ): DeferredBlock<SimpleCropBlock> =
        ModBlocks.registerSimpleCrop(
            name = name,
            factory = factory,
            ageToModelStage = ageToModelStage,
            seedItem = seedItem,
            cropItem = cropItem,
            crossModel = crossModel,
        ).also(registeredCrops::add)

    @JvmStatic
    fun allBlocks(): Array<SimpleCropBlock> = registeredCrops.map { it.get() }.toTypedArray()

    @JvmField
    val LEEK_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "leek_crop",
        factory = Function { properties -> LeekCropBlock(properties, Supplier { LEEK_SEEDS.get() }) },
        ageToModelStage = intArrayOf(0, 0, 1, 1, 1, 2, 2, 2),
        seedItem = Supplier { LEEK_SEEDS.get() },
        cropItem = Supplier { LEEK.get() },
    )

    @JvmField
    val LEEK: DeferredItem<Item> = registerCropFood(
        name = "leek",
        nutrition = 1,
        saturation = 0.6f,
        compostableChance = 0.5f,
    )

    @JvmField
    val LEEK_COOKED: DeferredItem<Item> = registerCropFood(
        name = "leek_cooked",
        nutrition = 3,
        saturation = 0.6f,
        compostableChance = 0.0f,
    )

    @JvmField
    val LEEK_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "leek_seeds",
        cropBlock = Supplier { LEEK_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmField
    val MOOLI_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "mooli_crop",
        factory = Function { properties -> MooliCropBlock(properties, Supplier { MOOLI_SEEDS.get() }) },
        ageToModelStage = intArrayOf(0, 0, 1, 1, 2, 2, 3, 3),
        seedItem = Supplier { MOOLI_SEEDS.get() },
        cropItem = Supplier { MOOLI.get() },
        crossModel = true,
    )

    @JvmField
    val MOOLI: DeferredItem<Item> = registerCropFood(
        name = "mooli",
        nutrition = 4,
        saturation = 0.4f,
        compostableChance = 0.5f,
    )

    @JvmField
    val MOOLI_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "mooli_seeds",
        cropBlock = Supplier { MOOLI_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmField
    val RICE_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "rice_crop",
        factory = Function { properties -> RiceCropBlock(properties, Supplier { RICE_SEEDS.get() }) },
        ageToModelStage = intArrayOf(0, 0, 1, 2, 3, 3, 4, 5),
        seedItem = Supplier { RICE_SEEDS.get() },
        cropItem = Supplier { ModItems.RICE.get() },
    )

    @JvmField
    val RICE_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "rice_seeds",
        cropBlock = Supplier { RICE_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmField
    val SOYBEAN_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "soybean_crop",
        factory = Function { properties -> SoybeanCropBlock(properties, Supplier { SOYBEAN.get() }) },
        ageToModelStage = intArrayOf(0, 0, 1, 1, 2, 3, 3, 4),
        seedItem = Supplier { SOYBEAN.get() },
        cropItem = Supplier { SOYBEAN.get() },
    )

    @JvmField
    val SOYBEAN: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("soybean")
            .func { props -> BlockItem(SOYBEAN_CROP.get(), props) }
            .props {
                Item.Properties().food(
                    FoodProperties(1, 0.5f, false),
                    Consumables.defaultFood().build(),
                )
            }
            .customProp(CustomItemProperties.Builder().compostableChance(0.3f).build())
            .creativeTab(creativeTab)
            .build(),
    )

    @JvmField
    val HOUTTUYNIA_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "houttuynia_crop",
        factory = Function { properties -> HouttuyniaCropBlock(properties, Supplier { HOUTTUYNIA_SEEDS.get() }) },
        ageToModelStage = intArrayOf(0, 0, 1, 1, 2, 2, 3, 3),
        seedItem = Supplier { HOUTTUYNIA_SEEDS.get() },
        cropItem = Supplier { Food.HOUTTUYNIA.get() },
        crossModel = true,
    )

    @JvmField
    val HOUTTUYNIA_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "houttuynia_seeds",
        cropBlock = Supplier { HOUTTUYNIA_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmField
    val FRITILLARY_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "fritillary_crop",
        factory = Function { properties -> FritillaryCropBlock(properties, Supplier { FRITILLARY_SEEDS.get() }) },
        ageToModelStage = intArrayOf(0, 0, 1, 1, 2, 3, 3, 4),
        seedItem = Supplier { FRITILLARY_SEEDS.get() },
        cropItem = Supplier { Food.FRITILLARY.get() },
        crossModel = true,
    )

    @JvmField
    val FRITILLARY_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "fritillary_seeds",
        cropBlock = Supplier { FRITILLARY_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmField
    val BIZARRE_CHILLI_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "bizarre_chilli_crop",
        factory = Function { properties -> BizarreChilliCropBlock(properties, Supplier { BIZARRE_CHILLI_SEEDS.get() }) },
        ageToModelStage = intArrayOf(0, 0, 1, 2, 3, 4, 5, 6),
        seedItem = Supplier { BIZARRE_CHILLI_SEEDS.get() },
        cropItem = Supplier { ModItems.BIZARRE_CHILLI.get() },
        crossModel = true,
    )

    @JvmField
    val BIZARRE_CHILLI_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "bizarre_chilli_seeds",
        cropBlock = Supplier { BIZARRE_CHILLI_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmField
    val EXPERIENCE_FLOWER_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "experience_flower_crop",
        factory = Function { properties -> ExperienceFlowerCropBlock(properties, Supplier { EXPERIENCE_FLOWER_SEEDS.get() }) },
        ageToModelStage = intArrayOf(0, 0, 0, 1, 1, 1, 2, 2),
        seedItem = Supplier { EXPERIENCE_FLOWER_SEEDS.get() },
        cropItem = Supplier { EXPERIENCE_FLOWER_SEEDS.get() },
        crossModel = true,
    )

    @JvmField
    val EXPERIENCE_FLOWER_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "experience_flower_seeds",
        cropBlock = Supplier { EXPERIENCE_FLOWER_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmField
    val LAVA_FLOWER_CROP: DeferredBlock<SimpleCropBlock> = registerSimpleCrop(
        name = "lava_flower_crop",
        factory = Function { properties -> LavaFlowerCropBlock(properties, Supplier { LAVA_FLOWER_SEEDS.get() }) },
        ageToModelStage = intArrayOf(0, 0, 1, 1, 2, 3, 3, 4),
        seedItem = Supplier { LAVA_FLOWER_SEEDS.get() },
        cropItem = Supplier { Material.LAVA_ESSENCE.get() },
        crossModel = true,
    )

    @JvmField
    val LAVA_FLOWER_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "lava_flower_seeds",
        cropBlock = Supplier { LAVA_FLOWER_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmField
    val CORN_CROP: DeferredBlock<CornCropBlock> = ModBlocks.registerCornCrop()

    @JvmField
    val CORN_SEEDS: DeferredItem<Item> = registerSeeds(
        name = "corn_seeds",
        cropBlock = Supplier { CORN_CROP.get() },
        compostableChance = 0.3f,
    )

    @JvmStatic
    fun load() {
    }
}
