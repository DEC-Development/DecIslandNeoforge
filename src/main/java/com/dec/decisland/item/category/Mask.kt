package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.custom.Mask as MaskItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Mask {
    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_MASKS_TAB

    @JvmField
    val ABYSSAL_CONTROLLER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("abyssal_controller_mask", mapOf("en_us" to "Abyssal Controller Mask", "zh_cn" to "深海牢笼面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val AGENT_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("agent_mask", mapOf("en_us" to "Agent Mask", "zh_cn" to "机器人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ALEX_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("alex_mask", mapOf("en_us" to "Alex Mask", "zh_cn" to "艾利克斯面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ALLAY_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("allay_mask", mapOf("en_us" to "Allay Mask", "zh_cn" to "悦灵面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ARCTIC_FOX_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("arctic_fox_mask", mapOf("en_us" to "Arcitc Fox Mask", "zh_cn" to "雪狐面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ASH_BLAZE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("ash_blaze_mask", mapOf("en_us" to "Ash Blaze Mask", "zh_cn" to "灰烬烈焰人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ASH_KNIGHT_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("ash_knight_mask", mapOf("en_us" to "Ash Knight Mask", "zh_cn" to "灰烬骑士面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ASH_PUFFERFISH_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("ash_pufferfish_mask", mapOf("en_us" to "Ash Pufferfish Mask", "zh_cn" to "灰烬河豚面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val AXOLOTL_BLUE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("axolotl_blue_mask", mapOf("en_us" to "Axolotl Blue Mask", "zh_cn" to "蓝色美西螈面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val AXOLOTL_CYAN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("axolotl_cyan_mask", mapOf("en_us" to "Axolotl Cyan Mask", "zh_cn" to "青色美西螈面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val AXOLOTL_GOLD_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("axolotl_gold_mask", mapOf("en_us" to "Axolotl Gold Mask", "zh_cn" to "金色美西螈面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val AXOLOTL_LUCY_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("axolotl_lucy_mask", mapOf("en_us" to "Axolotl Lucy Mask", "zh_cn" to "粉红色美西螈面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val AXOLOTL_WILD_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("axolotl_wild_mask", mapOf("en_us" to "Axolotl Wild Mask", "zh_cn" to "棕色面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val BAT_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("bat_mask", mapOf("en_us" to "Bat Mask", "zh_cn" to "蝙蝠面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val BEE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("bee_mask", mapOf("en_us" to "Bee Mask", "zh_cn" to "蜜蜂面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val BLAZE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("blaze_mask", mapOf("en_us" to "Blaze Mask", "zh_cn" to "烈焰人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val BOOMBER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("boomber_mask", mapOf("en_us" to "Bommber Mask", "zh_cn" to "轰炸者面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val BROWN_BEAR_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("brown_bear_mask", mapOf("en_us" to "Brown Bear Mask", "zh_cn" to "棕熊面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val BROWN_MOOSHROOM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("brown_mooshroom_mask", mapOf("en_us" to "Brown Mooshroom Mask", "zh_cn" to "棕色蘑菇牛面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val CAVE_SPIDER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("cave_spider_mask", mapOf("en_us" to "Cave Spider Mask", "zh_cn" to "洞穴蜘蛛面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val CHICKEN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("chicken_mask", mapOf("en_us" to "Chicken Mask", "zh_cn" to "小鸡面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val COW_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("cow_mask", mapOf("en_us" to "Cow Mask", "zh_cn" to "牛面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val CREEPER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("creeper_mask", mapOf("en_us" to "Creeper Mask", "zh_cn" to "苦力怕面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val CRIMSON_SLIME_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("crimson_slime_mask", mapOf("en_us" to "Crimson Slime Mask", "zh_cn" to "猩红史莱姆面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val DARK_SNOW_MAN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("dark_snow_man_mask", mapOf("en_us" to "Dark Snow Man Mask", "zh_cn" to "暗黑雪人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val DARK_WEREWOLF_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("dark_werewolf_mask", mapOf("en_us" to "Dark Werewolf Mask", "zh_cn" to "暗狼人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val DARK_ZOMBIE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("dark_zombie_mask", mapOf("en_us" to "Dark Zombie Mask", "zh_cn" to "暗黑僵尸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val DOLPHIN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("dolphin_mask", mapOf("en_us" to "Dolphin Mask", "zh_cn" to "海豚面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val DROWNED_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("drowned_mask", mapOf("en_us" to "Drowned Mask", "zh_cn" to "溺尸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ELF_OF_ASH_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("elf_of_ash_mask", mapOf("en_us" to "Elf Of Ash Mask", "zh_cn" to "灰烬精灵面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ELF_OF_CHAOS_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("elf_of_chaos_mask", mapOf("en_us" to "Elf Of Chaos Mask", "zh_cn" to "混乱精灵面具面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ELF_OF_DEEP_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("elf_of_deep_mask", mapOf("en_us" to "Elf Of Deep Mask", "zh_cn" to "深渊精灵面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ELF_OF_LEAVES_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("elf_of_leaves_mask", mapOf("en_us" to "Elf Of Leaves Mask", "zh_cn" to "绿叶精灵面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val END_STONE_GOLEM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("end_stone_golem_mask", mapOf("en_us" to "End Stone Golem Mask", "zh_cn" to "末地石傀儡面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ENDER_WITCH_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("ender_witch_mask", mapOf("en_us" to "Ender Witch Mask", "zh_cn" to "末影女巫面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ENDERMAN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("enderman_mask", mapOf("en_us" to "Enderman Mask", "zh_cn" to "末影人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ENTITY_SOUL_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("entity_soul_mask", mapOf("en_us" to "Entity Soul Mask", "zh_cn" to "实体灵魂面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val EVERLASTING_WINTER_GHAST_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("everlasting_winter_ghast_mask", mapOf("en_us" to "Everlasting Winter Ghast Mask", "zh_cn" to "永冬恶魂面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val EVIL_SNOW_MAN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("evil_snow_man_mask", mapOf("en_us" to "Evil Snow Man Mask", "zh_cn" to "邪恶雪人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val FOX_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("fox_mask", mapOf("en_us" to "Fox Mask", "zh_cn" to "狐狸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val FRANK_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("frank_mask", mapOf("en_us" to "Frank Mask", "zh_cn" to "弗兰克面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val FROZEN_SPIDER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("frozen_spider_mask", mapOf("en_us" to "Frozen Spider Mask", "zh_cn" to "冰冻蜘蛛面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val GARGOYLE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("gargoyle_mask", mapOf("en_us" to "Gargoyle Mask", "zh_cn" to "石像鬼面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val GHAST_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("ghast_mask", mapOf("en_us" to "Ghast Mask", "zh_cn" to "恶魂面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val GINGERBREAD_MAN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("gingerbread_man_mask", mapOf("en_us" to "Gingerbread Man Mask", "zh_cn" to "姜饼人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val GOBLIN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("goblin_mask", mapOf("en_us" to "Goblin Mask", "zh_cn" to "哥布林面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val GUARDIAN_ELDER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("guardian_elder_mask", mapOf("en_us" to "Guardian Elder Mask", "zh_cn" to "守卫者面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val GUARDIAN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("guardian_mask", mapOf("en_us" to "Guardian Mask", "zh_cn" to "远古守卫者面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val HOST_OF_DEEP_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("host_of_deep_mask", mapOf("en_us" to "Host Of Deep Mask", "zh_cn" to "深渊之主面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val HUSK_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("husk_mask", mapOf("en_us" to "Huck Mask", "zh_cn" to "剥皮者面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ICE_BLAZE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("ice_blaze_mask", mapOf("en_us" to "Ice Blaze Mask", "zh_cn" to "寒霜人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ICE_ZOMBIE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("ice_zombie_mask", mapOf("en_us" to "Ice Zombie Mask", "zh_cn" to "寒冰僵尸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ILLAGER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("illager_mask", mapOf("en_us" to "Illager Mask", "zh_cn" to "刌民面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val IRON_GOLEM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("iron_golem_mask", mapOf("en_us" to "Iron Golem Mask", "zh_cn" to "铁傀儡面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val LEAVES_GOLEM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("leaves_golem_mask", mapOf("en_us" to "Leaves Golem Mask", "zh_cn" to "绿叶精华面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val MAGMA_CUBE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("magma_cube_mask", mapOf("en_us" to "Magma Cube Mask", "zh_cn" to "岩浆怪面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val MOOSHROOM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("mooshroom_mask", mapOf("en_us" to "Mooshroom Mask", "zh_cn" to "蘑菇牛面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val MUMMY_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("mummy_mask", mapOf("en_us" to "Mummy Mask", "zh_cn" to "木乃伊面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val MUMMY_SKELETON_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("mummy_skeleton_mask", mapOf("en_us" to "Mummy Skeleton Mask", "zh_cn" to "干尸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val MUSHROOM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("mushroom_mask", mapOf("en_us" to "Mushroom Mask", "zh_cn" to "蘑菇面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val MYSTICAL_SKELETON_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("mystical_skeleton_mask", mapOf("en_us" to "Mystical Skeleton Mask", "zh_cn" to "神秘骷髅面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val NETHER_CREEPER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("nether_creeper_mask", mapOf("en_us" to "Nether Creeper Mask", "zh_cn" to "地狱苦力怕面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val NETHER_GOLEM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("nether_golem_mask", mapOf("en_us" to "Nether Golem Mask", "zh_cn" to "地狱傀儡面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val NETHER_PHANTOM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("nether_phantom_mask", mapOf("en_us" to "Nether Phantom Mask", "zh_cn" to "地狱幻翼面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val NETHER_SKELETON_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("nether_skeleton_mask", mapOf("en_us" to "Nether Skeleton Mask", "zh_cn" to "地狱骷髅面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val NETHER_SKELETON_WIZARD_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("nether_skeleton_wizard_mask", mapOf("en_us" to "Nether Skeleton Wizard Mask", "zh_cn" to "地狱骷髅法师面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val OBSIDIAN_GOLEM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("obsidian_golem_mask", mapOf("en_us" to "Obsdian Golem Mask", "zh_cn" to "黑曜石傀儡面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val OLD_MONSTER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("old_monster_mask", mapOf("en_us" to "Old Monster Mask", "zh_cn" to "老怪物面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val PHANTOM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("phantom_mask", mapOf("en_us" to "Phantom Mask", "zh_cn" to "幻翼面具面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val PIG_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("pig_mask", mapOf("en_us" to "Pig Mask", "zh_cn" to "猪面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val PIGZOMBIE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("pigzombie_mask", mapOf("en_us" to "Pigzombie Mask", "zh_cn" to "僵尸猪人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val PLAYER_GHOST_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("player_ghost_mask", mapOf("en_us" to "Player Ghost Mask", "zh_cn" to "鬼魂面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val POLAR_WOLF_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("polar_wolf_mask", mapOf("en_us" to "Polar Wolf Mask", "zh_cn" to "北极狼面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val POLARBEAR_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("polarbear_mask", mapOf("en_us" to "Polarbear Mask", "zh_cn" to "北极熊面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val PREDATORS_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("predators_mask", mapOf("en_us" to "Predators Mask", "zh_cn" to "扑食者面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val PREHISTORIC_MONSTER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("prehistoric_monster_mask", mapOf("en_us" to "Prehistoric Monster Mask", "zh_cn" to "远古怪物面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val PUFFERFISH_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("pufferfish_mask", mapOf("en_us" to "Pufferfish Mask", "zh_cn" to "河豚面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val RADIATE_CREEPER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("radiate_creeper_mask", mapOf("en_us" to "Radiate Creeper Mask", "zh_cn" to "辐射苦力怕面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val RADIATE_ENDERMAN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("radiate_enderman_mask", mapOf("en_us" to "Radiate Enderman Mask", "zh_cn" to "辐射末影人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val RUMORER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("rumorer_mask", mapOf("en_us" to "Rumorer Mask", "zh_cn" to "灵异面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SEA_TURTLE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("sea_turtle_mask", mapOf("en_us" to "Sea Turtle Mask", "zh_cn" to "海龟面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SHADOW_CREEPER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("shadow_creeper_mask", mapOf("en_us" to "Shadow Creeper Mask", "zh_cn" to "暗影苦力怕面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SHADOW_OF_SEA_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("shadow_of_sea_mask", mapOf("en_us" to "Shadow Of Sea Mask", "zh_cn" to "幻翼海妖面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SHADOW_SKELETON_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("shadow_skeleton_mask", mapOf("en_us" to "Shadow Skeleton Mask", "zh_cn" to "幻翼骷髅面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SHADOW_ZOMBIE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("shadow_zombie_mask", mapOf("en_us" to "Shadow Zombie Mask", "zh_cn" to "幻翼僵尸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SHULKER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("shulker_mask", mapOf("en_us" to "Shulker Mask", "zh_cn" to "潜影贝面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SKELETON_ASSASSIN_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("skeleton_assassin_mask", mapOf("en_us" to "Skeleton Assassin Mask", "zh_cn" to "骷髅刺客面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SKELETON_KNIGHT_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("skeleton_knight_mask", mapOf("en_us" to "Skeleton Knight Mask", "zh_cn" to "骷髅骑士面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SKELETON_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("skeleton_mask", mapOf("en_us" to "Skeleton Mask", "zh_cn" to "骷髅面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SLIME_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("slime_mask", mapOf("en_us" to "Slime Mask", "zh_cn" to "史莱姆面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SOUL_BLAZE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("soul_blaze_mask", mapOf("en_us" to "Soul Blaze Mask", "zh_cn" to "灵魂烈焰人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SOUL_SKELETON_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("soul_skeleton_mask", mapOf("en_us" to "Soul Skeleton Mask", "zh_cn" to "灵魂骷髅面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SOUL_ZOMBIE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("soul_zombie_mask", mapOf("en_us" to "Soul Zombie Mask", "zh_cn" to "灵魂僵尸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SPIDER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("spider_mask", mapOf("en_us" to "Spider Mask", "zh_cn" to "蜘蛛面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SQUID_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("squid_mask", mapOf("en_us" to "Squid Mask", "zh_cn" to "鱿鱼面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val STEVE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("steve_mask", mapOf("en_us" to "Steve Mask", "zh_cn" to "史蒂夫面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val STRAY_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("stray_mask", mapOf("en_us" to "Stray Mask", "zh_cn" to "流浪者面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val STRIDER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("strider_mask", mapOf("en_us" to "Strider Mask", "zh_cn" to "炽足兽面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SWAMP_DROWNED_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("swamp_drowned_mask", mapOf("en_us" to "Swamp Drowned Mask", "zh_cn" to "淤骸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val SWAMP_GOLEM_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("swamp_golem_mask", mapOf("en_us" to "Swamp Golem Mask", "zh_cn" to "沼泽巨怪面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val TNT_CREEPER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("tnt_creeper_mask", mapOf("en_us" to "Tnt Creeper Mask", "zh_cn" to "TNT苦力怕面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val VENGEFUL_GHOST_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("vengeful_ghost_mask", mapOf("en_us" to "Vengeful Ghost Mask", "zh_cn" to "怨魂面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val VEX_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("vex_mask", mapOf("en_us" to "Vex Mask", "zh_cn" to "恼鬼面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val VILLAGER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("villager_mask", mapOf("en_us" to "Villager Mask", "zh_cn" to "村民面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val WANDERING_TRADER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("wandering_trader_mask", mapOf("en_us" to "Wandering Trader Mask", "zh_cn" to "流浪商人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val WARPED_SKELETON_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("warped_skeleton_mask", mapOf("en_us" to "Warped Skeleton Mask", "zh_cn" to "扭曲骷髅面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val WEREWOLF_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("werewolf_mask", mapOf("en_us" to "Werewolf Mask", "zh_cn" to "狼人面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val WITCH_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("witch_mask", mapOf("en_us" to "Witch Mask", "zh_cn" to "女巫面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val WITHER_INVULNERABLE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("wither_invulnerable_mask", mapOf("en_us" to "Wither Invulnerable Mask", "zh_cn" to "坚固凋灵面具面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val WITHER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("wither_mask", mapOf("en_us" to "Wither Mask", "zh_cn" to "凋零面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val WITHER_SKELETON_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("wither_skeleton_mask", mapOf("en_us" to "Wither Skeleton Mask", "zh_cn" to "凋灵骷髅面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val WOLF_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("wolf_mask", mapOf("en_us" to "Wolf Mask", "zh_cn" to "狼面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ZOMBIE_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("zombie_mask", mapOf("en_us" to "Zombie Mask", "zh_cn" to "僵尸面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ZOMBIE_SUMMONER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("zombie_summoner_mask", mapOf("en_us" to "Zombie Summoner Mask", "zh_cn" to "僵尸召唤师面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ZOMBIE_VILLAGER_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("zombie_villager_mask", mapOf("en_us" to "Zombie Villager Mask", "zh_cn" to "僵尸村民面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

    @JvmField
    val ZOMBIE_WARRIOR_MASK: DeferredItem<Item> = ModItems.registerItem(
            ItemConfig.Builder("zombie_warrior_mask", mapOf("en_us" to "Zombie Warrior Mask", "zh_cn" to "僵尸战士面具")).func(::MaskItem)
                    .creativeTab(creativeTab).build()
    )

// 以下纹理文件在语言文件中没有对应的条目：
// _mask

    @JvmStatic
    fun load() {

    }
}
