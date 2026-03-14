package com.dec.decisland.item

import com.dec.decisland.DecIsland
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.item.category.Mask
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.category.Weapon
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {
    @JvmField
    val CREATIVE_MOD_TABS: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DecIsland.MOD_ID)

    private val tabConfigs = mutableListOf<CreativeTabConfig>()

    @JvmField
    val DECISLAND_MATERIALS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_materials_tab",
            mapOf("en_us" to "DecIsland Materials", "zh_cn" to "DecIsland 材料"),
        ).iconItem { Material.STEEL_INGOT.get() }.build(),
    )

    @JvmField
    val DECISLAND_FOODS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_foods_tab",
            mapOf("en_us" to "DecIsland Foods", "zh_cn" to "DecIsland 食物"),
        ).iconItem { ModItems.A_BOWL_OF_RICE.get() }.build(),
    )

    @JvmField
    val DECISLAND_WEAPONS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_weapons_tab",
            mapOf("en_us" to "DecIsland Weapons", "zh_cn" to "DecIsland 武器"),
        ).iconItem { Weapon.ABSOLUTE_ZERO.get() }.build(),
    )

    @JvmField
    val DECISLAND_MISC_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_misc_tab",
            mapOf("en_us" to "DecIsland Misc", "zh_cn" to "DecIsland 杂物"),
        ).iconItem { ModItems.AUTUMN_LEAVES.get() }.build(),
    )

    @JvmField
    val DECISLAND_CROPS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_crops_tab",
            mapOf("en_us" to "DecIsland Crops", "zh_cn" to "DecIsland 作物"),
        ).iconItem { ModItems.BIZARRE_CHILLI.get() }.build(),
    )

    @JvmField
    val DECISLAND_MASKS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_masks_tab",
            mapOf("en_us" to "DecIsland Masks", "zh_cn" to "DecIsland 面具"),
        ).iconItem { Mask.FRANK_MASK.get() }.build(),
    )

    private fun addItemsToTab(tabConfig: CreativeTabConfig, output: CreativeModeTab.Output) {
        val currentTab = CREATIVE_MOD_TABS.getEntries()
            .firstOrNull { it.id.path == tabConfig.name }
            ?.value()

        ModItems.getItemConfigs().forEach { config ->
            if (config.creativeTab?.get() == currentTab) {
                ModItems.ITEMS.getEntries()
                    .firstOrNull { it.id.path == config.name }
                    ?.value()
                    ?.let(output::accept)
            }
        }

        ModBlocks.getBlockConfigs().forEach { config ->
            if (config.creativeTab?.get() == currentTab) {
                ModBlocks.BLOCKS.getEntries()
                    .firstOrNull { it.id.path == config.name }
                    ?.value()
                    ?.let(output::accept)
            }
        }
    }

    @JvmStatic
    fun register(eventBus: IEventBus) {
        CREATIVE_MOD_TABS.register(eventBus)
    }

    @JvmStatic
    fun registerTab(config: CreativeTabConfig): Supplier<CreativeModeTab> {
        tabConfigs.add(config)
        return CREATIVE_MOD_TABS.register(config.name, Supplier {
            CreativeModeTab.builder()
                .icon { ItemStack(config.iconItem!!.get()) }
                .title(Component.translatable("itemGroup.${config.name}"))
                .displayItems { _, output -> addItemsToTab(config, output) }
                .build()
        })
    }

    @JvmStatic
    fun getTabConfigs(): List<CreativeTabConfig> = tabConfigs
}
