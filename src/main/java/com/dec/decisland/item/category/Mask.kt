package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.lang.Lang
import com.dec.decisland.item.custom.Mask as MaskItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Mask {
    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_MASKS_TAB

    private fun registerMask(name: String): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name, Lang.item.get(name))
                .func(::MaskItem)
                .creativeTab(creativeTab)
                .build(),
        )

    @JvmField
    val ABYSSAL_CONTROLLER_MASK: DeferredItem<Item> = registerMask("abyssal_controller_mask")

    @JvmField
    val AGENT_MASK: DeferredItem<Item> = registerMask("agent_mask")

    @JvmField
    val ALEX_MASK: DeferredItem<Item> = registerMask("alex_mask")

    @JvmField
    val ALLAY_MASK: DeferredItem<Item> = registerMask("allay_mask")

    @JvmField
    val ARCTIC_FOX_MASK: DeferredItem<Item> = registerMask("arctic_fox_mask")

    @JvmField
    val ASH_BLAZE_MASK: DeferredItem<Item> = registerMask("ash_blaze_mask")

    @JvmField
    val ASH_KNIGHT_MASK: DeferredItem<Item> = registerMask("ash_knight_mask")

    @JvmField
    val ASH_PUFFERFISH_MASK: DeferredItem<Item> = registerMask("ash_pufferfish_mask")

    @JvmField
    val AXOLOTL_BLUE_MASK: DeferredItem<Item> = registerMask("axolotl_blue_mask")

    @JvmField
    val AXOLOTL_CYAN_MASK: DeferredItem<Item> = registerMask("axolotl_cyan_mask")

    @JvmField
    val AXOLOTL_GOLD_MASK: DeferredItem<Item> = registerMask("axolotl_gold_mask")

    @JvmField
    val AXOLOTL_LUCY_MASK: DeferredItem<Item> = registerMask("axolotl_lucy_mask")

    @JvmField
    val AXOLOTL_WILD_MASK: DeferredItem<Item> = registerMask("axolotl_wild_mask")

    @JvmField
    val BAT_MASK: DeferredItem<Item> = registerMask("bat_mask")

    @JvmField
    val BEE_MASK: DeferredItem<Item> = registerMask("bee_mask")

    @JvmField
    val BLAZE_MASK: DeferredItem<Item> = registerMask("blaze_mask")

    @JvmField
    val BOOMBER_MASK: DeferredItem<Item> = registerMask("boomber_mask")

    @JvmField
    val BROWN_BEAR_MASK: DeferredItem<Item> = registerMask("brown_bear_mask")

    @JvmField
    val BROWN_MOOSHROOM_MASK: DeferredItem<Item> = registerMask("brown_mooshroom_mask")

    @JvmField
    val CAVE_SPIDER_MASK: DeferredItem<Item> = registerMask("cave_spider_mask")

    @JvmField
    val CHICKEN_MASK: DeferredItem<Item> = registerMask("chicken_mask")

    @JvmField
    val COW_MASK: DeferredItem<Item> = registerMask("cow_mask")

    @JvmField
    val CREEPER_MASK: DeferredItem<Item> = registerMask("creeper_mask")

    @JvmField
    val CRIMSON_SLIME_MASK: DeferredItem<Item> = registerMask("crimson_slime_mask")

    @JvmField
    val DARK_SNOW_MAN_MASK: DeferredItem<Item> = registerMask("dark_snow_man_mask")

    @JvmField
    val DARK_WEREWOLF_MASK: DeferredItem<Item> = registerMask("dark_werewolf_mask")

    @JvmField
    val DARK_ZOMBIE_MASK: DeferredItem<Item> = registerMask("dark_zombie_mask")

    @JvmField
    val DOLPHIN_MASK: DeferredItem<Item> = registerMask("dolphin_mask")

    @JvmField
    val DROWNED_MASK: DeferredItem<Item> = registerMask("drowned_mask")

    @JvmField
    val ELF_OF_ASH_MASK: DeferredItem<Item> = registerMask("elf_of_ash_mask")

    @JvmField
    val ELF_OF_CHAOS_MASK: DeferredItem<Item> = registerMask("elf_of_chaos_mask")

    @JvmField
    val ELF_OF_DEEP_MASK: DeferredItem<Item> = registerMask("elf_of_deep_mask")

    @JvmField
    val ELF_OF_LEAVES_MASK: DeferredItem<Item> = registerMask("elf_of_leaves_mask")

    @JvmField
    val END_STONE_GOLEM_MASK: DeferredItem<Item> = registerMask("end_stone_golem_mask")

    @JvmField
    val ENDER_WITCH_MASK: DeferredItem<Item> = registerMask("ender_witch_mask")

    @JvmField
    val ENDERMAN_MASK: DeferredItem<Item> = registerMask("enderman_mask")

    @JvmField
    val ENTITY_SOUL_MASK: DeferredItem<Item> = registerMask("entity_soul_mask")

    @JvmField
    val EVERLASTING_WINTER_GHAST_MASK: DeferredItem<Item> = registerMask("everlasting_winter_ghast_mask")

    @JvmField
    val EVIL_SNOW_MAN_MASK: DeferredItem<Item> = registerMask("evil_snow_man_mask")

    @JvmField
    val FOX_MASK: DeferredItem<Item> = registerMask("fox_mask")

    @JvmField
    val FRANK_MASK: DeferredItem<Item> = registerMask("frank_mask")

    @JvmField
    val FROZEN_SPIDER_MASK: DeferredItem<Item> = registerMask("frozen_spider_mask")

    @JvmField
    val GARGOYLE_MASK: DeferredItem<Item> = registerMask("gargoyle_mask")

    @JvmField
    val GHAST_MASK: DeferredItem<Item> = registerMask("ghast_mask")

    @JvmField
    val GINGERBREAD_MAN_MASK: DeferredItem<Item> = registerMask("gingerbread_man_mask")

    @JvmField
    val GOBLIN_MASK: DeferredItem<Item> = registerMask("goblin_mask")

    @JvmField
    val GUARDIAN_ELDER_MASK: DeferredItem<Item> = registerMask("guardian_elder_mask")

    @JvmField
    val GUARDIAN_MASK: DeferredItem<Item> = registerMask("guardian_mask")

    @JvmField
    val HOST_OF_DEEP_MASK: DeferredItem<Item> = registerMask("host_of_deep_mask")

    @JvmField
    val HUSK_MASK: DeferredItem<Item> = registerMask("husk_mask")

    @JvmField
    val ICE_BLAZE_MASK: DeferredItem<Item> = registerMask("ice_blaze_mask")

    @JvmField
    val ICE_ZOMBIE_MASK: DeferredItem<Item> = registerMask("ice_zombie_mask")

    @JvmField
    val ILLAGER_MASK: DeferredItem<Item> = registerMask("illager_mask")

    @JvmField
    val IRON_GOLEM_MASK: DeferredItem<Item> = registerMask("iron_golem_mask")

    @JvmField
    val LEAVES_GOLEM_MASK: DeferredItem<Item> = registerMask("leaves_golem_mask")

    @JvmField
    val MAGMA_CUBE_MASK: DeferredItem<Item> = registerMask("magma_cube_mask")

    @JvmField
    val MOOSHROOM_MASK: DeferredItem<Item> = registerMask("mooshroom_mask")

    @JvmField
    val MUMMY_MASK: DeferredItem<Item> = registerMask("mummy_mask")

    @JvmField
    val MUMMY_SKELETON_MASK: DeferredItem<Item> = registerMask("mummy_skeleton_mask")

    @JvmField
    val MUSHROOM_MASK: DeferredItem<Item> = registerMask("mushroom_mask")

    @JvmField
    val MYSTICAL_SKELETON_MASK: DeferredItem<Item> = registerMask("mystical_skeleton_mask")

    @JvmField
    val NETHER_CREEPER_MASK: DeferredItem<Item> = registerMask("nether_creeper_mask")

    @JvmField
    val NETHER_GOLEM_MASK: DeferredItem<Item> = registerMask("nether_golem_mask")

    @JvmField
    val NETHER_PHANTOM_MASK: DeferredItem<Item> = registerMask("nether_phantom_mask")

    @JvmField
    val NETHER_SKELETON_MASK: DeferredItem<Item> = registerMask("nether_skeleton_mask")

    @JvmField
    val NETHER_SKELETON_WIZARD_MASK: DeferredItem<Item> = registerMask("nether_skeleton_wizard_mask")

    @JvmField
    val OBSIDIAN_GOLEM_MASK: DeferredItem<Item> = registerMask("obsidian_golem_mask")

    @JvmField
    val OLD_MONSTER_MASK: DeferredItem<Item> = registerMask("old_monster_mask")

    @JvmField
    val PHANTOM_MASK: DeferredItem<Item> = registerMask("phantom_mask")

    @JvmField
    val PIG_MASK: DeferredItem<Item> = registerMask("pig_mask")

    @JvmField
    val PIGZOMBIE_MASK: DeferredItem<Item> = registerMask("pigzombie_mask")

    @JvmField
    val PLAYER_GHOST_MASK: DeferredItem<Item> = registerMask("player_ghost_mask")

    @JvmField
    val POLAR_WOLF_MASK: DeferredItem<Item> = registerMask("polar_wolf_mask")

    @JvmField
    val POLARBEAR_MASK: DeferredItem<Item> = registerMask("polarbear_mask")

    @JvmField
    val PREDATORS_MASK: DeferredItem<Item> = registerMask("predators_mask")

    @JvmField
    val PREHISTORIC_MONSTER_MASK: DeferredItem<Item> = registerMask("prehistoric_monster_mask")

    @JvmField
    val PUFFERFISH_MASK: DeferredItem<Item> = registerMask("pufferfish_mask")

    @JvmField
    val RADIATE_CREEPER_MASK: DeferredItem<Item> = registerMask("radiate_creeper_mask")

    @JvmField
    val RADIATE_ENDERMAN_MASK: DeferredItem<Item> = registerMask("radiate_enderman_mask")

    @JvmField
    val RUMORER_MASK: DeferredItem<Item> = registerMask("rumorer_mask")

    @JvmField
    val SEA_TURTLE_MASK: DeferredItem<Item> = registerMask("sea_turtle_mask")

    @JvmField
    val SHADOW_CREEPER_MASK: DeferredItem<Item> = registerMask("shadow_creeper_mask")

    @JvmField
    val SHADOW_OF_SEA_MASK: DeferredItem<Item> = registerMask("shadow_of_sea_mask")

    @JvmField
    val SHADOW_SKELETON_MASK: DeferredItem<Item> = registerMask("shadow_skeleton_mask")

    @JvmField
    val SHADOW_ZOMBIE_MASK: DeferredItem<Item> = registerMask("shadow_zombie_mask")

    @JvmField
    val SHULKER_MASK: DeferredItem<Item> = registerMask("shulker_mask")

    @JvmField
    val SKELETON_ASSASSIN_MASK: DeferredItem<Item> = registerMask("skeleton_assassin_mask")

    @JvmField
    val SKELETON_KNIGHT_MASK: DeferredItem<Item> = registerMask("skeleton_knight_mask")

    @JvmField
    val SKELETON_MASK: DeferredItem<Item> = registerMask("skeleton_mask")

    @JvmField
    val SLIME_MASK: DeferredItem<Item> = registerMask("slime_mask")

    @JvmField
    val SOUL_BLAZE_MASK: DeferredItem<Item> = registerMask("soul_blaze_mask")

    @JvmField
    val SOUL_SKELETON_MASK: DeferredItem<Item> = registerMask("soul_skeleton_mask")

    @JvmField
    val SOUL_ZOMBIE_MASK: DeferredItem<Item> = registerMask("soul_zombie_mask")

    @JvmField
    val SPIDER_MASK: DeferredItem<Item> = registerMask("spider_mask")

    @JvmField
    val SQUID_MASK: DeferredItem<Item> = registerMask("squid_mask")

    @JvmField
    val STEVE_MASK: DeferredItem<Item> = registerMask("steve_mask")

    @JvmField
    val STRAY_MASK: DeferredItem<Item> = registerMask("stray_mask")

    @JvmField
    val STRIDER_MASK: DeferredItem<Item> = registerMask("strider_mask")

    @JvmField
    val SWAMP_DROWNED_MASK: DeferredItem<Item> = registerMask("swamp_drowned_mask")

    @JvmField
    val SWAMP_GOLEM_MASK: DeferredItem<Item> = registerMask("swamp_golem_mask")

    @JvmField
    val TNT_CREEPER_MASK: DeferredItem<Item> = registerMask("tnt_creeper_mask")

    @JvmField
    val VENGEFUL_GHOST_MASK: DeferredItem<Item> = registerMask("vengeful_ghost_mask")

    @JvmField
    val VEX_MASK: DeferredItem<Item> = registerMask("vex_mask")

    @JvmField
    val VILLAGER_MASK: DeferredItem<Item> = registerMask("villager_mask")

    @JvmField
    val WANDERING_TRADER_MASK: DeferredItem<Item> = registerMask("wandering_trader_mask")

    @JvmField
    val WARPED_SKELETON_MASK: DeferredItem<Item> = registerMask("warped_skeleton_mask")

    @JvmField
    val WEREWOLF_MASK: DeferredItem<Item> = registerMask("werewolf_mask")

    @JvmField
    val WITCH_MASK: DeferredItem<Item> = registerMask("witch_mask")

    @JvmField
    val WITHER_INVULNERABLE_MASK: DeferredItem<Item> = registerMask("wither_invulnerable_mask")

    @JvmField
    val WITHER_MASK: DeferredItem<Item> = registerMask("wither_mask")

    @JvmField
    val WITHER_SKELETON_MASK: DeferredItem<Item> = registerMask("wither_skeleton_mask")

    @JvmField
    val WOLF_MASK: DeferredItem<Item> = registerMask("wolf_mask")

    @JvmField
    val ZOMBIE_MASK: DeferredItem<Item> = registerMask("zombie_mask")

    @JvmField
    val ZOMBIE_SUMMONER_MASK: DeferredItem<Item> = registerMask("zombie_summoner_mask")

    @JvmField
    val ZOMBIE_VILLAGER_MASK: DeferredItem<Item> = registerMask("zombie_villager_mask")

    @JvmField
    val ZOMBIE_WARRIOR_MASK: DeferredItem<Item> = registerMask("zombie_warrior_mask")

    @JvmStatic
    fun load() {
    }
}
