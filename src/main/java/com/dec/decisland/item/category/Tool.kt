package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.ModToolMaterial
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.ToolMaterial
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Tool {
    private const val PICKAXE_ATTACK_SPEED: Float = -2.8f

    @JvmField
    val AMETHYST_PICKAXE: DeferredItem<Item> = registerPickaxe(
        "amethyst_pickaxe",
        ModToolMaterial.AMETHYST_TOOL,
        attackDamage = 2.0f,
        repairItem = Supplier { Items.AMETHYST_SHARD },
    )

    @JvmField
    val CORAL_PICKAXE: DeferredItem<Item> = registerPickaxe(
        "coral_pickaxe",
        ModToolMaterial.CORAL_TOOL,
        attackDamage = 3.0f,
        repairItem = Supplier { Material.CORAL_INGOT.get() },
    )

    @JvmField
    val EMERALD_PICKAXE: DeferredItem<Item> = registerPickaxe(
        "emerald_pickaxe",
        ModToolMaterial.EMERALD_TOOL,
        attackDamage = 3.0f,
        repairItem = Supplier { Items.EMERALD },
    )

    @JvmField
    val GLASS_PICKAXE: DeferredItem<Item> = registerPickaxe(
        "glass_pickaxe",
        ModToolMaterial.GLASS_TOOL,
        attackDamage = 4.0f,
        repairItem = Supplier { Material.GLASS_INGOT.get() },
    )

    @JvmField
    val SLIME_PICKAXE: DeferredItem<Item> = registerPickaxe(
        "slime_pickaxe",
        ModToolMaterial.SLIME_TOOL,
        attackDamage = 2.0f,
        repairItem = Supplier { Items.SLIME_BALL },
    )

    @JvmField
    val AMETHYST_AXE: DeferredItem<Item> = registerAxe(
        "amethyst_axe",
        ModToolMaterial.AMETHYST_TOOL,
        attackDamage = 3.0f,
        attackSpeed = -2.9f,
        repairItem = Supplier { Items.AMETHYST_SHARD },
    )

    @JvmField
    val CORAL_AXE: DeferredItem<Item> = registerAxe(
        "coral_axe",
        ModToolMaterial.CORAL_TOOL,
        attackDamage = 4.0f,
        attackSpeed = -3.2f,
        repairItem = Supplier { Material.CORAL_INGOT.get() },
    )

    @JvmField
    val EMERALD_AXE: DeferredItem<Item> = registerAxe(
        "emerald_axe",
        ModToolMaterial.EMERALD_TOOL,
        attackDamage = 4.0f,
        attackSpeed = -3.0f,
        repairItem = Supplier { Items.EMERALD },
    )

    @JvmField
    val GLASS_AXE: DeferredItem<Item> = registerAxe(
        "glass_axe",
        ModToolMaterial.GLASS_TOOL,
        attackDamage = 6.0f,
        attackSpeed = -2.9f,
        repairItem = Supplier { Material.GLASS_INGOT.get() },
    )

    @JvmField
    val SLIME_AXE: DeferredItem<Item> = registerAxe(
        "slime_axe",
        ModToolMaterial.SLIME_TOOL,
        attackDamage = 0.0f,
        attackSpeed = -3.2f,
        repairItem = Supplier { Items.SLIME_BALL },
    )

    @JvmStatic
    fun load() {
    }

    private fun registerPickaxe(
        name: String,
        material: ToolMaterial,
        attackDamage: Float,
        repairItem: Supplier<Item>,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .props {
                    Item.Properties()
                        .pickaxe(material, attackDamage, PICKAXE_ATTACK_SPEED)
                        .stacksTo(1)
                        .repairable(repairItem.get())
                }
                .tags(listOf(ItemTags.PICKAXES))
                .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        )

    private fun registerAxe(
        name: String,
        material: ToolMaterial,
        attackDamage: Float,
        attackSpeed: Float,
        repairItem: Supplier<Item>,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .props {
                    Item.Properties()
                        .axe(material, attackDamage, attackSpeed)
                        .stacksTo(1)
                        .repairable(repairItem.get())
                }
                .tags(listOf(ItemTags.AXES))
                .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        )
}
