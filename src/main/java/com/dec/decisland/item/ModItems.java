package com.dec.decisland.item;

import com.dec.decisland.DecIsland;
import com.dec.decisland.item.custom.*;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.item.Items.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(DecIsland.MOD_ID);
    private static final List<ItemConfig> ITEM_CONFIGS = new ArrayList<>();
    //    public static final DeferredItem<Item> A_BOWL_OF_RICE =
//            ITEMS.registerSimpleItem("a_bowl_of_rice", props -> props.food(
//                    new FoodProperties(10, 10, true),
//                    Consumables.defaultFood().build()
//            ));
    public static final DeferredItem<Item> A_BOWL_OF_RICE = registerItem(
            new ItemConfig.Builder("a_bowl_of_rice", Map.of(
                    "en_us", "A Bowl of Rice",
                    "zh_cn", "米饭"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(10, 0.6F, false),
                    Consumables.defaultFood().build()
            ).usingConvertsTo(BOWL)).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );
    public static final DeferredItem<Item> A_PIECE_OF_SALMON = registerItem(
            new ItemConfig.Builder("a_piece_of_salmon", Map.of(
                    "en_us", "a Piece of Salmon",
                    "zh_cn", "生鱼片"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(1, 0.4F, false),
                    Consumables.defaultFood().build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> APPLE_PIE = registerItem(
            new ItemConfig.Builder("apple_pie", Map.of(
                    "en_us", "Apple Pie",
                    "zh_cn", "苹果派"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(12, 0.6F, false),
                    Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 2))).build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> APPLE_JUICE = registerItem(
            new ItemConfig.Builder("apple_juice", Map.of(
                    "en_us", "Apple Juice",
                    "zh_cn", "苹果汁"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(2, 0, false),
                    Consumables.defaultDrink().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 1))).build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> AMETHYST_AXE = registerItem(
            new ItemConfig.Builder("amethyst_axe", Map.of(
                    "en_us", "Amethyst Axe",
                    "zh_cn", "紫水晶斧"
            )).func(p -> new AxeItem(ToolMaterial.DIAMOND, 0.0F, -3.2F, p))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM).build()
    );

    public static final DeferredItem<Item> ASH_PUFFERFISH = registerItem(
            new ItemConfig.Builder("ash_pufferfish", Map.of(
                    "en_us", "Ash Pufferfish",
                    "zh_cn", "灰烬豚"
            )).func(AshPufferfish::new)
                    .props(() -> new Item.Properties().stacksTo(16).useCooldown(3.0f))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB).build()
    );

    public static final DeferredItem<Item> AUTUMN_LEAVES = registerItem(
            new ItemConfig.Builder("autumn_leaves", Map.of(
                    "en_us", "Autumn Leaves",
                    "zh_cn", "秋叶"
            )).customProp(new CustomItemProperties.Builder().compostableChance(0.3f).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB).build()
    );

    public static final DeferredItem<Item> BIZARRE_CHILLI = registerItem(
            new ItemConfig.Builder("bizarre_chilli", Map.of(
                    "en_us", "Bizarre Chilli",
                    "zh_cn", "奇异椒"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_CROPS_TAB).build()
    );

    public static final DeferredItem<Item> BLOOD_WORM = registerItem(
            new ItemConfig.Builder("blood_worm", Map.of(
                    "en_us", "Blood Worm",
                    "zh_cn", "血蠕虫"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> BRACKEN = registerItem(
            new ItemConfig.Builder("bracken", Map.of(
                    "en_us", "Bracken",
                    "zh_cn", "蕨菜"
            )).props(() -> (new Item.Properties()).food(
                            new FoodProperties(1, 0.1F, false),
                            Consumables.defaultFood().build()
                    )).customProp(new CustomItemProperties.Builder().compostableChance(0.3f).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> BLUE_JELLYFISH = registerItem(
            new ItemConfig.Builder("blue_jellyfish", Map.of(
                    "en_us", "Blue Jellyfish",
                    "zh_cn", "蓝水母"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(2, 0.2f, true),
                    Consumables.defaultFood()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f))
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 0), 0.5f))
                            .build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );


    public static final DeferredItem<Item> MOON_JELLYFISH = registerItem(
            new ItemConfig.Builder("moon_jellyfish", Map.of(
                    "en_us", "Moon Jellyfish",
                    "zh_cn", "海月水母"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(2, 0.2f, true),
                    Consumables.defaultFood()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f))
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 0), 0.75f))
                            .build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> PINK_JELLYFISH = registerItem(
            new ItemConfig.Builder("pink_jellyfish", Map.of(
                    "en_us", "Pink Jellyfish",
                    "zh_cn", "粉水母"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(2, 0.2f, true),
                    Consumables.defaultFood()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(MobEffects.POISON, 5 * 20, 3), 0.7f))
                            .build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> YELLOW_JELLYFISH = registerItem(
            new ItemConfig.Builder("yellow_jellyfish", Map.of(
                    "en_us", "Yellow Jellyfish",
                    "zh_cn", "黄水母"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(2, 0.2f, true),
                    Consumables.defaultFood()
                            .onConsume(new ApplyStatusEffectsConsumeEffect(
                                    new MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f))
                            .build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> CANDY = registerItem(
            new ItemConfig.Builder("candy", Map.of(
                    "en_us", "Candy",
                    "zh_cn", "糖果"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(1, 0.4f, true),
                    Consumables.defaultFood().build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> CHOCOLATE_COOKIE = registerItem(
            new ItemConfig.Builder("chocolate_cookie", Map.of(
                    "en_us", "Chocolate Cookie",
                    "zh_cn", "巧克力曲奇"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(3, 0.6f, false),
                    Consumables.defaultFood().build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> CHOCOLATES = registerItem(
            new ItemConfig.Builder("chocolates", Map.of(
                    "en_us", "Chocolates",
                    "zh_cn", "巧克力"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(7, 0.6f, false),
                    Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(
                            new MobEffectInstance(MobEffects.SPEED, 20 * 20, 0), 0.73f)).build()
            ).usingConvertsTo(PAPER)).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> COAL_FISH = registerItem(
            new ItemConfig.Builder("coal_fish", Map.of(
                    "en_us", "Coal Fish",
                    "zh_cn", "煤炭鱼"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(3, 0.4f, false),
                    Consumables.defaultFood().build()
            )).customProp(
                    new CustomItemProperties.Builder().burnTime(8).build()
            ).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> COB = registerItem(
            new ItemConfig.Builder("cob", Map.of(
                    "en_us", "Cob",
                    "zh_cn", "玉米棒"
            )).customProp(new CustomItemProperties.Builder().compostableChance(0.3f).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_CROPS_TAB).build()
    );


    public static final DeferredItem<Item> AMETHYST_HELMET = registerItem(
            new ItemConfig.Builder("amethyst_helmet", Map.of(
                    "en_us", "Amethyst Helmet",
                    "zh_cn", "紫水晶头盔"
            ))
                    .props(() -> (new Item.Properties().humanoidArmor(ModArmorMaterials.AMETHYST, ArmorType.HELMET)
                    ))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> AMETHYST_CHESTPLATE = registerItem(
            new ItemConfig.Builder("amethyst_chestplate", Map.of(
                    "en_us", "Amethyst Chestplate",
                    "zh_cn", "紫水晶胸甲"
            ))
                    .props(() -> (new Item.Properties().humanoidArmor(ModArmorMaterials.AMETHYST, ArmorType.CHESTPLATE)
                    ))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> AMETHYST_LEGGINGS = registerItem(
            new ItemConfig.Builder("amethyst_leggings", Map.of(
                    "en_us", "Amethyst Leggings",
                    "zh_cn", "紫水晶护腿"
            ))
                    .props(() -> (new Item.Properties().humanoidArmor(ModArmorMaterials.AMETHYST, ArmorType.LEGGINGS)
                    ))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> AMETHYST_BOOTS = registerItem(
            new ItemConfig.Builder("amethyst_boots", Map.of(
                    "en_us", "Amethyst Boots",
                    "zh_cn", "紫水晶靴子"
            ))
                    .props(() -> (new Item.Properties().humanoidArmor(ModArmorMaterials.AMETHYST, ArmorType.BOOTS)
                    ))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> FROZEN_HELMET = registerItem(
            new ItemConfig.Builder("frozen_helmet", Map.of(
                    "en_us", "Frozen Helmet",
                    "zh_cn", "霜冻头盔"
            ))
                    .props(() -> (new Item.Properties().humanoidArmor(ModArmorMaterials.FROZEN, ArmorType.HELMET)
                    ))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> FROZEN_CHESTPLATE = registerItem(
            new ItemConfig.Builder("frozen_chestplate", Map.of(
                    "en_us", "Frozen Chestplate",
                    "zh_cn", "霜冻甲"
            ))
                    .props(() -> (new Item.Properties().humanoidArmor(ModArmorMaterials.FROZEN, ArmorType.CHESTPLATE)
                    ))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> FROZEN_LEGGINGS = registerItem(
            new ItemConfig.Builder("frozen_leggings", Map.of(
                    "en_us", "Frozen Leggings",
                    "zh_cn", "霜冻护腿"
            ))
                    .props(() -> (new Item.Properties().humanoidArmor(ModArmorMaterials.FROZEN, ArmorType.LEGGINGS)
                    ))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> FROZEN_BOOTS = registerItem(
            new ItemConfig.Builder("frozen_boots", Map.of(
                    "en_us", "Frozen Boots",
                    "zh_cn", "霜冻靴"
            ))
                    .props(() -> (new Item.Properties().humanoidArmor(ModArmorMaterials.FROZEN, ArmorType.BOOTS)
                    ))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> FRIED_EGG = registerItem(
            new ItemConfig.Builder("fried_egg", Map.of(
                    "en_us", "Fried Egg",
                    "zh_cn", "煎蛋"
            )).props(() -> (new Item.Properties()).food(
                    new FoodProperties(5, 0.4f, false),
                    Consumables.defaultFood().build()
            )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> HEART_EGG = registerItem(
            new ItemConfig.Builder("heart_egg", Map.of(
                    "en_us", "Heart Egg",
                    "zh_cn", "生命蛋"
            )).func(HeartEgg::new)
                    .props(() -> (new Item.Properties()).food(
                            new FoodProperties(4, 1, true),
                            Consumables.defaultFood().build()
                    )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> SPURT_EGG = registerItem(
            new ItemConfig.Builder("spurt_egg", Map.of(
                    "en_us", "Spurt Egg",
                    "zh_cn", "迸发蛋"
            )).func(SpurtEgg::new)
                    .props(() -> (new Item.Properties()).food(
                            new FoodProperties(4, 1, true),
                            Consumables.defaultFood().build()
                    )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> ENDER_EGG = registerItem(
            new ItemConfig.Builder("ender_egg", Map.of(
                    "en_us", "Ender Egg",
                    "zh_cn", "末影蛋"
            )).func(EnderEgg::new)
                    .props(() -> (new Item.Properties()).food(
                            new FoodProperties(4, 1, true),
                            Consumables.defaultFood().build()
                    )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> ENERGY_RAY_STAFF = ModItems.registerItem(
            new ItemConfig.Builder("energy_ray_staff", Map.of(
                    "en_us", "Energy Ray Staff",
                    "zh_cn", "能量射线杖"
            )).func(EnergyRayStaff::new)
                    .props(
                            () -> new Item.Properties()
                                    .useCooldown(0.7F).stacksTo(1)
                    )
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> BLUE_GEM = registerItem(
            new ItemConfig.Builder("blue_gem", Map.of(
                    "en_us", "Blue Gem",
                    "zh_cn", "蓝宝石"
            )).func(BlueGem::new)
                    .props(() -> (new Item.Properties()).food(
                            new FoodProperties(0, 0, true),
                            Consumables.defaultFood().build()
                    )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> RED_GEM = registerItem(
            new ItemConfig.Builder("red_gem", Map.of(
                    "en_us", "Red Gem",
                    "zh_cn", "红宝石"
            )).func(RedGem::new)
                    .props(() -> (new Item.Properties()).food(
                            new FoodProperties(0, 0, true),
                            Consumables.defaultFood().build()
                    )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );

    public static final DeferredItem<Item> MAGIC_CRYSTAL = registerItem(
            new ItemConfig.Builder("magic_crystal", Map.of(
                    "en_us", "Magic Crystal",
                    "zh_cn", "魔法水晶"
            )).func(MagicCrystal::new)
                    .props(() -> (new Item.Properties()).food(
                            new FoodProperties(0, 0, true),
                            Consumables.defaultFood().build()
                    )).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    );


    public static DeferredItem<Item> registerItem(ItemConfig config) {
//        DecIsland.LOGGER.info("Registering item: {}", config.name);
        ITEM_CONFIGS.add(config);
        return ITEMS.registerItem(config.name, config.func, config.props);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static List<ItemConfig> getItemConfigs() {
        return ITEM_CONFIGS;
    }

    public static Item getItemByConfig(ItemConfig config) {
        return ModItems.ITEMS.getEntries().stream()
                .filter(entry -> entry.getId().getPath().equals(config.name))
                .map(Holder::value)
                .findFirst()
                .orElse(null);
    }
}
