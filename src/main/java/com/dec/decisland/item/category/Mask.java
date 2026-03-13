package com.dec.decisland.item.category;

import com.dec.decisland.item.ItemConfig;
import com.dec.decisland.item.ModCreativeModeTabs;
import com.dec.decisland.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;
import java.util.function.Supplier;

public final class Mask {
    public static final Supplier<CreativeModeTab> creativeTab = ModCreativeModeTabs.DECISLAND_MASKS_TAB;

    public static final DeferredItem<Item> ABYSSAL_CONTROLLER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("abyssal_controller_mask", Map.of(
                    "en_us", "Abyssal Controller Mask",
                    "zh_cn", "深海牢笼面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> AGENT_MASK = ModItems.registerItem(
            new ItemConfig.Builder("agent_mask", Map.of(
                    "en_us", "Agent Mask",
                    "zh_cn", "机器人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ALEX_MASK = ModItems.registerItem(
            new ItemConfig.Builder("alex_mask", Map.of(
                    "en_us", "Alex Mask",
                    "zh_cn", "艾利克斯面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ALLAY_MASK = ModItems.registerItem(
            new ItemConfig.Builder("allay_mask", Map.of(
                    "en_us", "Allay Mask",
                    "zh_cn", "悦灵面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ARCTIC_FOX_MASK = ModItems.registerItem(
            new ItemConfig.Builder("arctic_fox_mask", Map.of(
                    "en_us", "Arcitc Fox Mask",
                    "zh_cn", "雪狐面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ASH_BLAZE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("ash_blaze_mask", Map.of(
                    "en_us", "Ash Blaze Mask",
                    "zh_cn", "灰烬烈焰人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ASH_KNIGHT_MASK = ModItems.registerItem(
            new ItemConfig.Builder("ash_knight_mask", Map.of(
                    "en_us", "Ash Knight Mask",
                    "zh_cn", "灰烬骑士面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ASH_PUFFERFISH_MASK = ModItems.registerItem(
            new ItemConfig.Builder("ash_pufferfish_mask", Map.of(
                    "en_us", "Ash Pufferfish Mask",
                    "zh_cn", "灰烬河豚面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> AXOLOTL_BLUE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("axolotl_blue_mask", Map.of(
                    "en_us", "Axolotl Blue Mask",
                    "zh_cn", "蓝色美西螈面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> AXOLOTL_CYAN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("axolotl_cyan_mask", Map.of(
                    "en_us", "Axolotl Cyan Mask",
                    "zh_cn", "青色美西螈面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> AXOLOTL_GOLD_MASK = ModItems.registerItem(
            new ItemConfig.Builder("axolotl_gold_mask", Map.of(
                    "en_us", "Axolotl Gold Mask",
                    "zh_cn", "金色美西螈面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> AXOLOTL_LUCY_MASK = ModItems.registerItem(
            new ItemConfig.Builder("axolotl_lucy_mask", Map.of(
                    "en_us", "Axolotl Lucy Mask",
                    "zh_cn", "粉红色美西螈面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> AXOLOTL_WILD_MASK = ModItems.registerItem(
            new ItemConfig.Builder("axolotl_wild_mask", Map.of(
                    "en_us", "Axolotl Wild Mask",
                    "zh_cn", "棕色面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> BAT_MASK = ModItems.registerItem(
            new ItemConfig.Builder("bat_mask", Map.of(
                    "en_us", "Bat Mask",
                    "zh_cn", "蝙蝠面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> BEE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("bee_mask", Map.of(
                    "en_us", "Bee Mask",
                    "zh_cn", "蜜蜂面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> BLAZE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("blaze_mask", Map.of(
                    "en_us", "Blaze Mask",
                    "zh_cn", "烈焰人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> BOOMBER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("boomber_mask", Map.of(
                    "en_us", "Bommber Mask",
                    "zh_cn", "轰炸者面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> BROWN_BEAR_MASK = ModItems.registerItem(
            new ItemConfig.Builder("brown_bear_mask", Map.of(
                    "en_us", "Brown Bear Mask",
                    "zh_cn", "棕熊面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> BROWN_MOOSHROOM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("brown_mooshroom_mask", Map.of(
                    "en_us", "Brown Mooshroom Mask",
                    "zh_cn", "棕色蘑菇牛面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> CAVE_SPIDER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("cave_spider_mask", Map.of(
                    "en_us", "Cave Spider Mask",
                    "zh_cn", "洞穴蜘蛛面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> CHICKEN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("chicken_mask", Map.of(
                    "en_us", "Chicken Mask",
                    "zh_cn", "小鸡面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> COW_MASK = ModItems.registerItem(
            new ItemConfig.Builder("cow_mask", Map.of(
                    "en_us", "Cow Mask",
                    "zh_cn", "牛面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> CREEPER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("creeper_mask", Map.of(
                    "en_us", "Creeper Mask",
                    "zh_cn", "苦力怕面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> CRIMSON_SLIME_MASK = ModItems.registerItem(
            new ItemConfig.Builder("crimson_slime_mask", Map.of(
                    "en_us", "Crimson Slime Mask",
                    "zh_cn", "猩红史莱姆面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> DARK_SNOW_MAN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("dark_snow_man_mask", Map.of(
                    "en_us", "Dark Snow Man Mask",
                    "zh_cn", "暗黑雪人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> DARK_WEREWOLF_MASK = ModItems.registerItem(
            new ItemConfig.Builder("dark_werewolf_mask", Map.of(
                    "en_us", "Dark Werewolf Mask",
                    "zh_cn", "暗狼人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> DARK_ZOMBIE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("dark_zombie_mask", Map.of(
                    "en_us", "Dark Zombie Mask",
                    "zh_cn", "暗黑僵尸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> DOLPHIN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("dolphin_mask", Map.of(
                    "en_us", "Dolphin Mask",
                    "zh_cn", "海豚面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> DROWNED_MASK = ModItems.registerItem(
            new ItemConfig.Builder("drowned_mask", Map.of(
                    "en_us", "Drowned Mask",
                    "zh_cn", "溺尸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ELF_OF_ASH_MASK = ModItems.registerItem(
            new ItemConfig.Builder("elf_of_ash_mask", Map.of(
                    "en_us", "Elf Of Ash Mask",
                    "zh_cn", "灰烬精灵面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ELF_OF_CHAOS_MASK = ModItems.registerItem(
            new ItemConfig.Builder("elf_of_chaos_mask", Map.of(
                    "en_us", "Elf Of Chaos Mask",
                    "zh_cn", "混乱精灵面具面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ELF_OF_DEEP_MASK = ModItems.registerItem(
            new ItemConfig.Builder("elf_of_deep_mask", Map.of(
                    "en_us", "Elf Of Deep Mask",
                    "zh_cn", "深渊精灵面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ELF_OF_LEAVES_MASK = ModItems.registerItem(
            new ItemConfig.Builder("elf_of_leaves_mask", Map.of(
                    "en_us", "Elf Of Leaves Mask",
                    "zh_cn", "绿叶精灵面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> END_STONE_GOLEM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("end_stone_golem_mask", Map.of(
                    "en_us", "End Stone Golem Mask",
                    "zh_cn", "末地石傀儡面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ENDER_WITCH_MASK = ModItems.registerItem(
            new ItemConfig.Builder("ender_witch_mask", Map.of(
                    "en_us", "Ender Witch Mask",
                    "zh_cn", "末影女巫面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ENDERMAN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("enderman_mask", Map.of(
                    "en_us", "Enderman Mask",
                    "zh_cn", "末影人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ENTITY_SOUL_MASK = ModItems.registerItem(
            new ItemConfig.Builder("entity_soul_mask", Map.of(
                    "en_us", "Entity Soul Mask",
                    "zh_cn", "实体灵魂面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> EVERLASTING_WINTER_GHAST_MASK = ModItems.registerItem(
            new ItemConfig.Builder("everlasting_winter_ghast_mask", Map.of(
                    "en_us", "Everlasting Winter Ghast Mask",
                    "zh_cn", "永冬恶魂面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> EVIL_SNOW_MAN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("evil_snow_man_mask", Map.of(
                    "en_us", "Evil Snow Man Mask",
                    "zh_cn", "邪恶雪人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> FOX_MASK = ModItems.registerItem(
            new ItemConfig.Builder("fox_mask", Map.of(
                    "en_us", "Fox Mask",
                    "zh_cn", "狐狸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> FRANK_MASK = ModItems.registerItem(
            new ItemConfig.Builder("frank_mask", Map.of(
                    "en_us", "Frank Mask",
                    "zh_cn", "弗兰克面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> FROZEN_SPIDER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("frozen_spider_mask", Map.of(
                    "en_us", "Frozen Spider Mask",
                    "zh_cn", "冰冻蜘蛛面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> GARGOYLE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("gargoyle_mask", Map.of(
                    "en_us", "Gargoyle Mask",
                    "zh_cn", "石像鬼面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> GHAST_MASK = ModItems.registerItem(
            new ItemConfig.Builder("ghast_mask", Map.of(
                    "en_us", "Ghast Mask",
                    "zh_cn", "恶魂面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> GINGERBREAD_MAN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("gingerbread_man_mask", Map.of(
                    "en_us", "Gingerbread Man Mask",
                    "zh_cn", "姜饼人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> GOBLIN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("goblin_mask", Map.of(
                    "en_us", "Goblin Mask",
                    "zh_cn", "哥布林面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> GUARDIAN_ELDER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("guardian_elder_mask", Map.of(
                    "en_us", "Guardian Elder Mask",
                    "zh_cn", "守卫者面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> GUARDIAN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("guardian_mask", Map.of(
                    "en_us", "Guardian Mask",
                    "zh_cn", "远古守卫者面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> HOST_OF_DEEP_MASK = ModItems.registerItem(
            new ItemConfig.Builder("host_of_deep_mask", Map.of(
                    "en_us", "Host Of Deep Mask",
                    "zh_cn", "深渊之主面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> HUSK_MASK = ModItems.registerItem(
            new ItemConfig.Builder("husk_mask", Map.of(
                    "en_us", "Huck Mask",
                    "zh_cn", "剥皮者面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ICE_BLAZE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("ice_blaze_mask", Map.of(
                    "en_us", "Ice Blaze Mask",
                    "zh_cn", "寒霜人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ICE_ZOMBIE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("ice_zombie_mask", Map.of(
                    "en_us", "Ice Zombie Mask",
                    "zh_cn", "寒冰僵尸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ILLAGER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("illager_mask", Map.of(
                    "en_us", "Illager Mask",
                    "zh_cn", "刌民面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> IRON_GOLEM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("iron_golem_mask", Map.of(
                    "en_us", "Iron Golem Mask",
                    "zh_cn", "铁傀儡面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> LEAVES_GOLEM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("leaves_golem_mask", Map.of(
                    "en_us", "Leaves Golem Mask",
                    "zh_cn", "绿叶精华面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> MAGMA_CUBE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("magma_cube_mask", Map.of(
                    "en_us", "Magma Cube Mask",
                    "zh_cn", "岩浆怪面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> MOOSHROOM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("mooshroom_mask", Map.of(
                    "en_us", "Mooshroom Mask",
                    "zh_cn", "蘑菇牛面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> MUMMY_MASK = ModItems.registerItem(
            new ItemConfig.Builder("mummy_mask", Map.of(
                    "en_us", "Mummy Mask",
                    "zh_cn", "木乃伊面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> MUMMY_SKELETON_MASK = ModItems.registerItem(
            new ItemConfig.Builder("mummy_skeleton_mask", Map.of(
                    "en_us", "Mummy Skeleton Mask",
                    "zh_cn", "干尸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> MUSHROOM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("mushroom_mask", Map.of(
                    "en_us", "Mushroom Mask",
                    "zh_cn", "蘑菇面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> MYSTICAL_SKELETON_MASK = ModItems.registerItem(
            new ItemConfig.Builder("mystical_skeleton_mask", Map.of(
                    "en_us", "Mystical Skeleton Mask",
                    "zh_cn", "神秘骷髅面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> NETHER_CREEPER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("nether_creeper_mask", Map.of(
                    "en_us", "Nether Creeper Mask",
                    "zh_cn", "地狱苦力怕面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> NETHER_GOLEM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("nether_golem_mask", Map.of(
                    "en_us", "Nether Golem Mask",
                    "zh_cn", "地狱傀儡面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> NETHER_PHANTOM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("nether_phantom_mask", Map.of(
                    "en_us", "Nether Phantom Mask",
                    "zh_cn", "地狱幻翼面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> NETHER_SKELETON_MASK = ModItems.registerItem(
            new ItemConfig.Builder("nether_skeleton_mask", Map.of(
                    "en_us", "Nether Skeleton Mask",
                    "zh_cn", "地狱骷髅面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> NETHER_SKELETON_WIZARD_MASK = ModItems.registerItem(
            new ItemConfig.Builder("nether_skeleton_wizard_mask", Map.of(
                    "en_us", "Nether Skeleton Wizard Mask",
                    "zh_cn", "地狱骷髅法师面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> OBSIDIAN_GOLEM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("obsidian_golem_mask", Map.of(
                    "en_us", "Obsdian Golem Mask",
                    "zh_cn", "黑曜石傀儡面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> OLD_MONSTER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("old_monster_mask", Map.of(
                    "en_us", "Old Monster Mask",
                    "zh_cn", "老怪物面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> PHANTOM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("phantom_mask", Map.of(
                    "en_us", "Phantom Mask",
                    "zh_cn", "幻翼面具面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> PIG_MASK = ModItems.registerItem(
            new ItemConfig.Builder("pig_mask", Map.of(
                    "en_us", "Pig Mask",
                    "zh_cn", "猪面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> PIGZOMBIE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("pigzombie_mask", Map.of(
                    "en_us", "Pigzombie Mask",
                    "zh_cn", "僵尸猪人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> PLAYER_GHOST_MASK = ModItems.registerItem(
            new ItemConfig.Builder("player_ghost_mask", Map.of(
                    "en_us", "Player Ghost Mask",
                    "zh_cn", "鬼魂面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> POLAR_WOLF_MASK = ModItems.registerItem(
            new ItemConfig.Builder("polar_wolf_mask", Map.of(
                    "en_us", "Polar Wolf Mask",
                    "zh_cn", "北极狼面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> POLARBEAR_MASK = ModItems.registerItem(
            new ItemConfig.Builder("polarbear_mask", Map.of(
                    "en_us", "Polarbear Mask",
                    "zh_cn", "北极熊面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> PREDATORS_MASK = ModItems.registerItem(
            new ItemConfig.Builder("predators_mask", Map.of(
                    "en_us", "Predators Mask",
                    "zh_cn", "扑食者面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> PREHISTORIC_MONSTER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("prehistoric_monster_mask", Map.of(
                    "en_us", "Prehistoric Monster Mask",
                    "zh_cn", "远古怪物面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> PUFFERFISH_MASK = ModItems.registerItem(
            new ItemConfig.Builder("pufferfish_mask", Map.of(
                    "en_us", "Pufferfish Mask",
                    "zh_cn", "河豚面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> RADIATE_CREEPER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("radiate_creeper_mask", Map.of(
                    "en_us", "Radiate Creeper Mask",
                    "zh_cn", "辐射苦力怕面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> RADIATE_ENDERMAN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("radiate_enderman_mask", Map.of(
                    "en_us", "Radiate Enderman Mask",
                    "zh_cn", "辐射末影人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> RUMORER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("rumorer_mask", Map.of(
                    "en_us", "Rumorer Mask",
                    "zh_cn", "灵异面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SEA_TURTLE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("sea_turtle_mask", Map.of(
                    "en_us", "Sea Turtle Mask",
                    "zh_cn", "海龟面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SHADOW_CREEPER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("shadow_creeper_mask", Map.of(
                    "en_us", "Shadow Creeper Mask",
                    "zh_cn", "暗影苦力怕面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SHADOW_OF_SEA_MASK = ModItems.registerItem(
            new ItemConfig.Builder("shadow_of_sea_mask", Map.of(
                    "en_us", "Shadow Of Sea Mask",
                    "zh_cn", "幻翼海妖面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SHADOW_SKELETON_MASK = ModItems.registerItem(
            new ItemConfig.Builder("shadow_skeleton_mask", Map.of(
                    "en_us", "Shadow Skeleton Mask",
                    "zh_cn", "幻翼骷髅面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SHADOW_ZOMBIE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("shadow_zombie_mask", Map.of(
                    "en_us", "Shadow Zombie Mask",
                    "zh_cn", "幻翼僵尸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SHULKER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("shulker_mask", Map.of(
                    "en_us", "Shulker Mask",
                    "zh_cn", "潜影贝面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SKELETON_ASSASSIN_MASK = ModItems.registerItem(
            new ItemConfig.Builder("skeleton_assassin_mask", Map.of(
                    "en_us", "Skeleton Assassin Mask",
                    "zh_cn", "骷髅刺客面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SKELETON_KNIGHT_MASK = ModItems.registerItem(
            new ItemConfig.Builder("skeleton_knight_mask", Map.of(
                    "en_us", "Skeleton Knight Mask",
                    "zh_cn", "骷髅骑士面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SKELETON_MASK = ModItems.registerItem(
            new ItemConfig.Builder("skeleton_mask", Map.of(
                    "en_us", "Skeleton Mask",
                    "zh_cn", "骷髅面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SLIME_MASK = ModItems.registerItem(
            new ItemConfig.Builder("slime_mask", Map.of(
                    "en_us", "Slime Mask",
                    "zh_cn", "史莱姆面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SOUL_BLAZE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("soul_blaze_mask", Map.of(
                    "en_us", "Soul Blaze Mask",
                    "zh_cn", "灵魂烈焰人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SOUL_SKELETON_MASK = ModItems.registerItem(
            new ItemConfig.Builder("soul_skeleton_mask", Map.of(
                    "en_us", "Soul Skeleton Mask",
                    "zh_cn", "灵魂骷髅面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SOUL_ZOMBIE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("soul_zombie_mask", Map.of(
                    "en_us", "Soul Zombie Mask",
                    "zh_cn", "灵魂僵尸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SPIDER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("spider_mask", Map.of(
                    "en_us", "Spider Mask",
                    "zh_cn", "蜘蛛面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SQUID_MASK = ModItems.registerItem(
            new ItemConfig.Builder("squid_mask", Map.of(
                    "en_us", "Squid Mask",
                    "zh_cn", "鱿鱼面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> STEVE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("steve_mask", Map.of(
                    "en_us", "Steve Mask",
                    "zh_cn", "史蒂夫面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> STRAY_MASK = ModItems.registerItem(
            new ItemConfig.Builder("stray_mask", Map.of(
                    "en_us", "Stray Mask",
                    "zh_cn", "流浪者面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> STRIDER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("strider_mask", Map.of(
                    "en_us", "Strider Mask",
                    "zh_cn", "炽足兽面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SWAMP_DROWNED_MASK = ModItems.registerItem(
            new ItemConfig.Builder("swamp_drowned_mask", Map.of(
                    "en_us", "Swamp Drowned Mask",
                    "zh_cn", "淤骸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> SWAMP_GOLEM_MASK = ModItems.registerItem(
            new ItemConfig.Builder("swamp_golem_mask", Map.of(
                    "en_us", "Swamp Golem Mask",
                    "zh_cn", "沼泽巨怪面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> TNT_CREEPER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("tnt_creeper_mask", Map.of(
                    "en_us", "Tnt Creeper Mask",
                    "zh_cn", "TNT苦力怕面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> VENGEFUL_GHOST_MASK = ModItems.registerItem(
            new ItemConfig.Builder("vengeful_ghost_mask", Map.of(
                    "en_us", "Vengeful Ghost Mask",
                    "zh_cn", "怨魂面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> VEX_MASK = ModItems.registerItem(
            new ItemConfig.Builder("vex_mask", Map.of(
                    "en_us", "Vex Mask",
                    "zh_cn", "恼鬼面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> VILLAGER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("villager_mask", Map.of(
                    "en_us", "Villager Mask",
                    "zh_cn", "村民面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> WANDERING_TRADER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("wandering_trader_mask", Map.of(
                    "en_us", "Wandering Trader Mask",
                    "zh_cn", "流浪商人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> WARPED_SKELETON_MASK = ModItems.registerItem(
            new ItemConfig.Builder("warped_skeleton_mask", Map.of(
                    "en_us", "Warped Skeleton Mask",
                    "zh_cn", "扭曲骷髅面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> WEREWOLF_MASK = ModItems.registerItem(
            new ItemConfig.Builder("werewolf_mask", Map.of(
                    "en_us", "Werewolf Mask",
                    "zh_cn", "狼人面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> WITCH_MASK = ModItems.registerItem(
            new ItemConfig.Builder("witch_mask", Map.of(
                    "en_us", "Witch Mask",
                    "zh_cn", "女巫面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> WITHER_INVULNERABLE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("wither_invulnerable_mask", Map.of(
                    "en_us", "Wither Invulnerable Mask",
                    "zh_cn", "坚固凋灵面具面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> WITHER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("wither_mask", Map.of(
                    "en_us", "Wither Mask",
                    "zh_cn", "凋零面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> WITHER_SKELETON_MASK = ModItems.registerItem(
            new ItemConfig.Builder("wither_skeleton_mask", Map.of(
                    "en_us", "Wither Skeleton Mask",
                    "zh_cn", "凋灵骷髅面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> WOLF_MASK = ModItems.registerItem(
            new ItemConfig.Builder("wolf_mask", Map.of(
                    "en_us", "Wolf Mask",
                    "zh_cn", "狼面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ZOMBIE_MASK = ModItems.registerItem(
            new ItemConfig.Builder("zombie_mask", Map.of(
                    "en_us", "Zombie Mask",
                    "zh_cn", "僵尸面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ZOMBIE_SUMMONER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("zombie_summoner_mask", Map.of(
                    "en_us", "Zombie Summoner Mask",
                    "zh_cn", "僵尸召唤师面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ZOMBIE_VILLAGER_MASK = ModItems.registerItem(
            new ItemConfig.Builder("zombie_villager_mask", Map.of(
                    "en_us", "Zombie Villager Mask",
                    "zh_cn", "僵尸村民面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

    public static final DeferredItem<Item> ZOMBIE_WARRIOR_MASK = ModItems.registerItem(
            new ItemConfig.Builder("zombie_warrior_mask", Map.of(
                    "en_us", "Zombie Warrior Mask",
                    "zh_cn", "僵尸战士面具"
            )).func(com.dec.decisland.item.custom.Mask::new)
                    .creativeTab(creativeTab).build()
    );

// 以下纹理文件在语言文件中没有对应的条目：
// _mask

    public static void load() {

    }
}
