package com.dec.decisland.item

import com.dec.decisland.DecIsland
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.item.category.Accessory
import com.dec.decisland.item.category.Mask
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.category.Weapon
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
            mapOf("en_us" to "DecIsland Materials", "zh_cn" to "DecIsland \u6750\u6599"),
        ).iconItem { Material.STEEL_INGOT.get() }.build(),
    )

    @JvmField
    val DECISLAND_FOODS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_foods_tab",
            mapOf("en_us" to "DecIsland Foods", "zh_cn" to "DecIsland \u98df\u7269"),
        ).iconItem { ModItems.A_BOWL_OF_RICE.get() }.build(),
    )

    @JvmField
    val DECISLAND_WEAPONS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_weapons_tab",
            mapOf("en_us" to "DecIsland Weapons", "zh_cn" to "DecIsland \u6b66\u5668"),
        ).iconItem { Weapon.ABSOLUTE_ZERO.get() }.build(),
    )

    @JvmField
    val DECISLAND_ACCESSORIES_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_accessories_tab",
            mapOf("en_us" to "DecIsland Accessories", "zh_cn" to "DecIsland \u9970\u54c1"),
        ).iconItem { Accessory.HEART_RING.get() }.build(),
    )

    @JvmField
    val DECISLAND_MISC_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_misc_tab",
            mapOf("en_us" to "DecIsland Misc", "zh_cn" to "DecIsland \u6742\u7269"),
        ).iconItem { ModItems.AUTUMN_LEAVES.get() }.build(),
    )

    @JvmField
    val DECISLAND_CROPS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_crops_tab",
            mapOf("en_us" to "DecIsland Crops", "zh_cn" to "DecIsland \u4f5c\u7269"),
        ).iconItem { ModItems.BIZARRE_CHILLI.get() }.build(),
    )

    @JvmField
    val DECISLAND_MASKS_TAB: Supplier<CreativeModeTab> = registerTab(
        CreativeTabConfig.Builder(
            "decisland_masks_tab",
            mapOf("en_us" to "DecIsland Masks", "zh_cn" to "DecIsland \u9762\u5177"),
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
