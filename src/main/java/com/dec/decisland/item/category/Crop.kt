package com.dec.decisland.item.category

import com.dec.decisland.block.ModBlocks
import com.dec.decisland.block.custom.LeekCropBlock
import com.dec.decisland.block.custom.MooliCropBlock
import com.dec.decisland.block.custom.SimpleCropBlock
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
    ): DeferredBlock<SimpleCropBlock> =
        ModBlocks.registerSimpleCrop(
            name = name,
            factory = factory,
            ageToModelStage = ageToModelStage,
            seedItem = seedItem,
            cropItem = cropItem,
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

    @JvmStatic
    fun load() {
    }
}
