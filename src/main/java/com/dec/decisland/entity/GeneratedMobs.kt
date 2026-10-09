package com.dec.decisland.entity

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.custom.BedrockMob
import com.dec.decisland.entity.custom.MobConfig
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.SpawnPlacementTypes
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.level.levelgen.Heightmap
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.DeferredSpawnEggItem
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

/** 由 tools/entity_port/generate.mjs 生成：基岩版生物批量移植注册表 */
object GeneratedMobs {
    @JvmField
    val ENTITY_TYPES: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(Registries.ENTITY_TYPE, DecIsland.MOD_ID)

    @JvmField
    val SPAWN_EGGS: DeferredRegister.Items =
        DeferredRegister.createItems(DecIsland.MOD_ID)

    private fun <T : Entity> registerEntity(
        name: String,
        factory: (EntityType<T>, Level) -> T,
        category: MobCategory,
        configure: EntityType.Builder<T>.() -> Unit = {},
    ): DeferredHolder<EntityType<*>, EntityType<T>> =
        ENTITY_TYPES.register(name, Supplier { EntityType.Builder.of(factory, category).apply(configure).build(name) })

    private val CFG_ABYSSAL_CONTROLLER = MobConfig("abyssal_controller", health = 150.0, speed = 0.080, attackDamage = 3.0, followRange = 96.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "villager", "iron_golem", "snow_golem"))

    @JvmField
    val ABYSSAL_CONTROLLER: Supplier<EntityType<BedrockMob>> =
        registerEntity("abyssal_controller", { type, level -> BedrockMob(type, level, CFG_ABYSSAL_CONTROLLER) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val ABYSSAL_CONTROLLER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("abyssal_controller_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ABYSSAL_CONTROLLER.get() }, 14147036, 16777215, Item.Properties())
        })

    private val CFG_ABYSSAL_SHADOW = MobConfig("abyssal_shadow", health = 10.0, speed = 0.250, attackDamage = 3.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"), rangedProjectile = Supplier { ModEntities.WAVE_ENERGY.get() })

    @JvmField
    val ABYSSAL_SHADOW: Supplier<EntityType<BedrockMob>> =
        registerEntity("abyssal_shadow", { type, level -> BedrockMob(type, level, CFG_ABYSSAL_SHADOW) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val ABYSSAL_SHADOW_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("abyssal_shadow_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ABYSSAL_SHADOW.get() }, 6983555, 10605507, Item.Properties())
        })

    @JvmField
    val ANGRY_CHICKEN: Supplier<EntityType<AngryChicken>> =
        registerEntity("angry_chicken", { type, level -> AngryChicken(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 0.80f)
        }

    @JvmField
    val ANGRY_CHICKEN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("angry_chicken_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ANGRY_CHICKEN.get() }, 14535363, 16777215, Item.Properties())
        })

    @JvmField
    val ARCHER: Supplier<EntityType<Archer>> =
        registerEntity("archer", { type, level -> Archer(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ARCHER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("archer_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ARCHER.get() }, 7889494, 11835014, Item.Properties())
        })

    @JvmField
    val ASH_BLAZE: Supplier<EntityType<AshBlaze>> =
        registerEntity("ash_blaze", { type, level -> AshBlaze(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f).fireImmune()
        }

    @JvmField
    val ASH_BLAZE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_blaze_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_BLAZE.get() }, 3881531, 6447457, Item.Properties())
        })

    private val CFG_ASH_BOMB = MobConfig("ash_bomb", health = 10.0, speed = 0.250, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val ASH_BOMB: Supplier<EntityType<BedrockMob>> =
        registerEntity("ash_bomb", { type, level -> BedrockMob(type, level, CFG_ASH_BOMB) }, MobCategory.MONSTER) {
            sized(1.00f, 0.50f)
        }

    @JvmField
    val ASH_BOMB_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_bomb_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_BOMB.get() }, 11687220, 16745816, Item.Properties())
        })

    private val CFG_ASH_HORSE = MobConfig("ash_horse", health = 300.0, speed = 0.250, attackDamage = 3.0, followRange = 96.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = true, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "villager"))

    @JvmField
    val ASH_HORSE: Supplier<EntityType<BedrockMob>> =
        registerEntity("ash_horse", { type, level -> BedrockMob(type, level, CFG_ASH_HORSE) }, MobCategory.MONSTER) {
            sized(1.40f, 1.60f)
        }

    @JvmField
    val ASH_HORSE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_horse_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_HORSE.get() }, 3680038, 6178374, Item.Properties())
        })

    private val CFG_ASH_HORSE_HEAD = MobConfig("ash_horse_head", health = 150.0, speed = 0.080, attackDamage = 3.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = true, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem"))

    @JvmField
    val ASH_HORSE_HEAD: Supplier<EntityType<BedrockMob>> =
        registerEntity("ash_horse_head", { type, level -> BedrockMob(type, level, CFG_ASH_HORSE_HEAD) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val ASH_HORSE_HEAD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_horse_head_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_HORSE_HEAD.get() }, 3680038, 6178374, Item.Properties())
        })

    private val CFG_ASH_KNIGHT = MobConfig("ash_knight", health = 400.0, speed = 0.200, attackDamage = 3.0, followRange = 96.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = true, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "villager"))

    @JvmField
    val ASH_KNIGHT: Supplier<EntityType<BedrockMob>> =
        registerEntity("ash_knight", { type, level -> BedrockMob(type, level, CFG_ASH_KNIGHT) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ASH_KNIGHT_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_knight_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_KNIGHT.get() }, 2893093, 5129284, Item.Properties())
        })

    private val CFG_ASH_KNIGHT_HEAD = MobConfig("ash_knight_head", health = 300.0, speed = 0.250, attackDamage = 14.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = true, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "villager"))

    @JvmField
    val ASH_KNIGHT_HEAD: Supplier<EntityType<BedrockMob>> =
        registerEntity("ash_knight_head", { type, level -> BedrockMob(type, level, CFG_ASH_KNIGHT_HEAD) }, MobCategory.MONSTER) {
            sized(0.80f, 0.80f)
        }

    @JvmField
    val ASH_KNIGHT_HEAD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_knight_head_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_KNIGHT_HEAD.get() }, 2893093, 5129284, Item.Properties())
        })

    @JvmField
    val ASH_PUFFERFISH: Supplier<EntityType<AshPufferfish>> =
        registerEntity("ash_pufferfish", { type, level -> AshPufferfish(type, level) }, MobCategory.WATER_CREATURE) {
            sized(0.80f, 0.80f)
        }

    @JvmField
    val ASH_PUFFERFISH_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_pufferfish_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_PUFFERFISH.get() }, 11444882, 16641496, Item.Properties())
        })

    private val CFG_ASH_SWORD = MobConfig("ash_sword", health = 100.0, speed = 1.700, attackDamage = 10.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = true, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val ASH_SWORD: Supplier<EntityType<BedrockMob>> =
        registerEntity("ash_sword", { type, level -> BedrockMob(type, level, CFG_ASH_SWORD) }, MobCategory.MONSTER) {
            sized(1.00f, 0.50f)
        }

    @JvmField
    val ASH_SWORD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_sword_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_SWORD.get() }, 5967647, 9250364, Item.Properties())
        })

    private val CFG_ASH_SWORD_PHANTOM = MobConfig("ash_sword_phantom", health = 8.0, speed = 4.000, attackDamage = 10.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = true, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val ASH_SWORD_PHANTOM: Supplier<EntityType<BedrockMob>> =
        registerEntity("ash_sword_phantom", { type, level -> BedrockMob(type, level, CFG_ASH_SWORD_PHANTOM) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val ASH_SWORD_PHANTOM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ash_sword_phantom_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ASH_SWORD_PHANTOM.get() }, 5967647, 9250364, Item.Properties())
        })

    @JvmField
    val BABY_ENDER_DRAGON: Supplier<EntityType<BabyEnderDragon>> =
        registerEntity("baby_ender_dragon", { type, level -> BabyEnderDragon(type, level) }, MobCategory.MONSTER) {
            sized(3.00f, 1.50f).fireImmune()
        }

    @JvmField
    val BABY_ENDER_DRAGON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("baby_ender_dragon_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { BABY_ENDER_DRAGON.get() }, 2433830, 4538949, Item.Properties())
        })

    @JvmField
    val BAT_CHARGER: Supplier<EntityType<BatCharger>> =
        registerEntity("bat_charger", { type, level -> BatCharger(type, level) }, MobCategory.MONSTER) {
            sized(0.80f, 1.44f)
        }

    @JvmField
    val BAT_CHARGER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("bat_charger_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { BAT_CHARGER.get() }, 11678887, 16734451, Item.Properties())
        })

    private val CFG_BLACKSTONE_THORN = MobConfig("blackstone_thorn", health = 27.0, speed = 0.250, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = true, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val BLACKSTONE_THORN: Supplier<EntityType<BedrockMob>> =
        registerEntity("blackstone_thorn", { type, level -> BedrockMob(type, level, CFG_BLACKSTONE_THORN) }, MobCategory.MONSTER) {
            sized(1.00f, 2.00f)
        }

    @JvmField
    val BLACKSTONE_THORN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("blackstone_thorn_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { BLACKSTONE_THORN.get() }, 3453506, 5832555, Item.Properties())
        })

    @JvmField
    val BLOOD_ZOMBIE: Supplier<EntityType<BloodZombie>> =
        registerEntity("blood_zombie", { type, level -> BloodZombie(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.60f)
        }

    @JvmField
    val BLOOD_ZOMBIE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("blood_zombie_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { BLOOD_ZOMBIE.get() }, 7582260, 11403096, Item.Properties())
        })

    @JvmField
    val BOMBER: Supplier<EntityType<Bomber>> =
        registerEntity("bomber", { type, level -> Bomber(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f).fireImmune()
        }

    @JvmField
    val BOMBER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("bomber_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { BOMBER.get() }, 9190686, 13591867, Item.Properties())
        })

    @JvmField
    val BROWN_BEAR: Supplier<EntityType<BrownBear>> =
        registerEntity("brown_bear", { type, level -> BrownBear(type, level) }, MobCategory.CREATURE) {
            sized(1.30f, 1.40f)
        }

    @JvmField
    val BROWN_BEAR_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("brown_bear_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { BROWN_BEAR.get() }, 6566921, 10115102, Item.Properties())
        })

    @JvmField
    val CARNAGER: Supplier<EntityType<Carnager>> =
        registerEntity("carnager", { type, level -> Carnager(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val CARNAGER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("carnager_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { CARNAGER.get() }, 4146237, 6778212, Item.Properties())
        })

    private val CFG_CHEST_MONSTER = MobConfig("chest_monster", health = 50.0, speed = 0.400, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = true, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "villager", "iron_golem"))

    @JvmField
    val CHEST_MONSTER: Supplier<EntityType<BedrockMob>> =
        registerEntity("chest_monster", { type, level -> BedrockMob(type, level, CFG_CHEST_MONSTER) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val CHEST_MONSTER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("chest_monster_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { CHEST_MONSTER.get() }, 5521183, 8611644, Item.Properties())
        })

    private val CFG_CHESTER = MobConfig("chester", health = 20.0, speed = 0.300, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = true, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val CHESTER: Supplier<EntityType<BedrockMob>> =
        registerEntity("chester", { type, level -> BedrockMob(type, level, CFG_CHESTER) }, MobCategory.MONSTER) {
            sized(0.56f, 0.56f)
        }

    @JvmField
    val CHESTER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("chester_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { CHESTER.get() }, 5521183, 8611644, Item.Properties())
        })

    private val CFG_CLAM = MobConfig("clam", health = 20.0, speed = 0.080, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = true, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val CLAM: Supplier<EntityType<BedrockMob>> =
        registerEntity("clam", { type, level -> BedrockMob(type, level, CFG_CLAM) }, MobCategory.WATER_CREATURE) {
            sized(0.30f, 0.30f)
        }

    @JvmField
    val CLAM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("clam_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { CLAM.get() }, 15390422, 16777215, Item.Properties())
        })

    private val CFG_CRAB = MobConfig("crab", health = 20.0, speed = 0.150, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = true, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val CRAB: Supplier<EntityType<BedrockMob>> =
        registerEntity("crab", { type, level -> BedrockMob(type, level, CFG_CRAB) }, MobCategory.WATER_CREATURE) {
            sized(0.30f, 0.30f)
        }

    @JvmField
    val CRAB_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("crab_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { CRAB.get() }, 6250593, 9606037, Item.Properties())
        })

    @JvmField
    val CRIMSON_SLIME: Supplier<EntityType<CrimsonSlime>> =
        registerEntity("crimson_slime", { type, level -> CrimsonSlime(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val CRIMSON_SLIME_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("crimson_slime_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { CRIMSON_SLIME.get() }, 14579061, 16756912, Item.Properties())
        })

    @JvmField
    val DARK_SNOW_MAN: Supplier<EntityType<DarkSnowMan>> =
        registerEntity("dark_snow_man", { type, level -> DarkSnowMan(type, level) }, MobCategory.CREATURE) {
            sized(0.40f, 1.80f)
        }

    @JvmField
    val DARK_SNOW_MAN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("dark_snow_man_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { DARK_SNOW_MAN.get() }, 3439538, 5814527, Item.Properties())
        })

    private val CFG_DARK_WEREWOLF = MobConfig("dark_werewolf", health = 100.0, speed = 0.250, attackDamage = 7.0, followRange = 32.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = true, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val DARK_WEREWOLF: Supplier<EntityType<BedrockMob>> =
        registerEntity("dark_werewolf", { type, level -> BedrockMob(type, level, CFG_DARK_WEREWOLF) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val DARK_WEREWOLF_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("dark_werewolf_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { DARK_WEREWOLF.get() }, 2903402, 5077409, Item.Properties())
        })

    @JvmField
    val DOCTOR: Supplier<EntityType<Doctor>> =
        registerEntity("doctor", { type, level -> Doctor(type, level) }, MobCategory.CREATURE) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val DOCTOR_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("doctor_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { DOCTOR.get() }, 7757395, 11637122, Item.Properties())
        })

    @JvmField
    val ELF_OF_ASH: Supplier<EntityType<ElfOfAsh>> =
        registerEntity("elf_of_ash", { type, level -> ElfOfAsh(type, level) }, MobCategory.MONSTER) {
            sized(0.40f, 0.80f).fireImmune()
        }

    @JvmField
    val ELF_OF_ASH_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("elf_of_ash_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ELF_OF_ASH.get() }, 11579956, 16777048, Item.Properties())
        })

    @JvmField
    val ELF_OF_CHAOS: Supplier<EntityType<ElfOfChaos>> =
        registerEntity("elf_of_chaos", { type, level -> ElfOfChaos(type, level) }, MobCategory.CREATURE) {
            sized(0.60f, 2.00f).fireImmune()
        }

    @JvmField
    val ELF_OF_CHAOS_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("elf_of_chaos_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ELF_OF_CHAOS.get() }, 11678812, 16734350, Item.Properties())
        })

    @JvmField
    val ELF_OF_DEEP: Supplier<EntityType<ElfOfDeep>> =
        registerEntity("elf_of_deep", { type, level -> ElfOfDeep(type, level) }, MobCategory.MONSTER) {
            sized(0.40f, 0.80f).fireImmune()
        }

    @JvmField
    val ELF_OF_DEEP_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("elf_of_deep_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ELF_OF_DEEP.get() }, 8952233, 13230582, Item.Properties())
        })

    @JvmField
    val ELF_OF_DUST: Supplier<EntityType<ElfOfDust>> =
        registerEntity("elf_of_dust", { type, level -> ElfOfDust(type, level) }, MobCategory.MONSTER) {
            sized(0.40f, 0.80f).fireImmune()
        }

    @JvmField
    val ELF_OF_DUST_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("elf_of_dust_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ELF_OF_DUST.get() }, 9605778, 14145495, Item.Properties())
        })

    @JvmField
    val ENCHANT_ARMOR: Supplier<EntityType<EnchantArmor>> =
        registerEntity("enchant_armor", { type, level -> EnchantArmor(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val ENCHANT_ARMOR_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("enchant_armor_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENCHANT_ARMOR.get() }, 8106548, 12123992, Item.Properties())
        })

    @JvmField
    val ENCHANT_ARMOR_BY_BOSS: Supplier<EntityType<EnchantArmorByBoss>> =
        registerEntity("enchant_armor_by_boss", { type, level -> EnchantArmorByBoss(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val ENCHANT_ARMOR_BY_BOSS_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("enchant_armor_by_boss_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENCHANT_ARMOR_BY_BOSS.get() }, 3453614, 5832701, Item.Properties())
        })

    @JvmField
    val ENCHANT_DIAMOND_ARMOR: Supplier<EntityType<EnchantDiamondArmor>> =
        registerEntity("enchant_diamond_armor", { type, level -> EnchantDiamondArmor(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val ENCHANT_DIAMOND_ARMOR_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("enchant_diamond_armor_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENCHANT_DIAMOND_ARMOR.get() }, 3453586, 5832663, Item.Properties())
        })

    @JvmField
    val ENCHANT_ILLAGER: Supplier<EntityType<EnchantIllager>> =
        registerEntity("enchant_illager", { type, level -> EnchantIllager(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ENCHANT_ILLAGER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("enchant_illager_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENCHANT_ILLAGER.get() }, 3453603, 5832686, Item.Properties())
        })

    @JvmField
    val ENCHANT_ILLAGER_1: Supplier<EntityType<EnchantIllager1>> =
        registerEntity("enchant_illager_1", { type, level -> EnchantIllager1(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ENCHANT_ILLAGER_1_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("enchant_illager_1_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENCHANT_ILLAGER_1.get() }, 11678812, 16734350, Item.Properties())
        })

    @JvmField
    val ENCHANT_ILLAGER_2: Supplier<EntityType<EnchantIllager2>> =
        registerEntity("enchant_illager_2", { type, level -> EnchantIllager2(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ENCHANT_ILLAGER_2_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("enchant_illager_2_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENCHANT_ILLAGER_2.get() }, 3453584, 5832660, Item.Properties())
        })

    @JvmField
    val END_STONE_GOLEM: Supplier<EntityType<EndStoneGolem>> =
        registerEntity("end_stone_golem", { type, level -> EndStoneGolem(type, level) }, MobCategory.CREATURE) {
            sized(1.40f, 2.90f).fireImmune()
        }

    @JvmField
    val END_STONE_GOLEM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("end_stone_golem_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { END_STONE_GOLEM.get() }, 14277534, 16777192, Item.Properties())
        })

    private val CFG_ENDER_SNAIL = MobConfig("ender_snail", health = 30.0, speed = 0.200, attackDamage = 5.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val ENDER_SNAIL: Supplier<EntityType<BedrockMob>> =
        registerEntity("ender_snail", { type, level -> BedrockMob(type, level, CFG_ENDER_SNAIL) }, MobCategory.MONSTER) {
            sized(0.80f, 0.80f)
        }

    @JvmField
    val ENDER_SNAIL_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ender_snail_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENDER_SNAIL.get() }, 4928690, 7821567, Item.Properties())
        })

    private val CFG_ENDER_SNAKE = MobConfig("ender_snake", health = 30.0, speed = 0.080, attackDamage = 3.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val ENDER_SNAKE: Supplier<EntityType<BedrockMob>> =
        registerEntity("ender_snake", { type, level -> BedrockMob(type, level, CFG_ENDER_SNAKE) }, MobCategory.MONSTER) {
            sized(1.00f, 0.60f)
        }

    @JvmField
    val ENDER_SNAKE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ender_snake_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENDER_SNAKE.get() }, 6040668, 9391502, Item.Properties())
        })

    @JvmField
    val ENDER_WITCH: Supplier<EntityType<EnderWitch>> =
        registerEntity("ender_witch", { type, level -> EnderWitch(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ENDER_WITCH_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ender_witch_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENDER_WITCH.get() }, 3453519, 5832573, Item.Properties())
        })

    @JvmField
    val ENDER_WITCH_PUPPET: Supplier<EntityType<EnderWitchPuppet>> =
        registerEntity("ender_witch_puppet", { type, level -> EnderWitchPuppet(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ENDER_WITCH_PUPPET_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ender_witch_puppet_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENDER_WITCH_PUPPET.get() }, 4731208, 7558516, Item.Properties())
        })

    private val CFG_ENTITY_SOUL = MobConfig("entity_soul", health = 170.0, speed = 0.500, attackDamage = 3.0, followRange = 96.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = true, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val ENTITY_SOUL: Supplier<EntityType<BedrockMob>> =
        registerEntity("entity_soul", { type, level -> BedrockMob(type, level, CFG_ENTITY_SOUL) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val ENTITY_SOUL_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("entity_soul_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENTITY_SOUL.get() }, 8151882, 12163191, Item.Properties())
        })

    private val CFG_ENTITY_SOUL_1 = MobConfig("entity_soul_1", health = 100.0, speed = 0.500, attackDamage = 3.0, followRange = 96.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = true, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val ENTITY_SOUL_1: Supplier<EntityType<BedrockMob>> =
        registerEntity("entity_soul_1", { type, level -> BedrockMob(type, level, CFG_ENTITY_SOUL_1) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val ENTITY_SOUL_1_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("entity_soul_1_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ENTITY_SOUL_1.get() }, 8151882, 12163191, Item.Properties())
        })

    private val CFG_ESCAPED_SOUL_ENTITY = MobConfig("escaped_soul_entity", health = 400.0, speed = 0.320, attackDamage = 20.0, followRange = 32.0, knockbackResistance = 0.70, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem"))

    @JvmField
    val ESCAPED_SOUL_ENTITY: Supplier<EntityType<BedrockMob>> =
        registerEntity("escaped_soul_entity", { type, level -> BedrockMob(type, level, CFG_ESCAPED_SOUL_ENTITY) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val ESCAPED_SOUL_ENTITY_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("escaped_soul_entity_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ESCAPED_SOUL_ENTITY.get() }, 7649707, 11534329, Item.Properties())
        })

    private val CFG_EVERLASTING_WINTER_GHAST = MobConfig("everlasting_winter_ghast", health = 300.0, speed = 0.350, attackDamage = 2.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem"))

    @JvmField
    val EVERLASTING_WINTER_GHAST: Supplier<EntityType<BedrockMob>> =
        registerEntity("everlasting_winter_ghast", { type, level -> BedrockMob(type, level, CFG_EVERLASTING_WINTER_GHAST) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val EVERLASTING_WINTER_GHAST_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("everlasting_winter_ghast_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { EVERLASTING_WINTER_GHAST.get() }, 11914482, 16777215, Item.Properties())
        })

    @JvmField
    val EVERLASTING_WINTER_GHAST_1: Supplier<EntityType<EverlastingWinterGhast1>> =
        registerEntity("everlasting_winter_ghast_1", { type, level -> EverlastingWinterGhast1(type, level) }, MobCategory.MONSTER) {
            sized(5.00f, 5.00f).fireImmune()
        }

    @JvmField
    val EVERLASTING_WINTER_GHAST_1_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("everlasting_winter_ghast_1_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { EVERLASTING_WINTER_GHAST_1.get() }, 4630941, 7403238, Item.Properties())
        })

    private val CFG_EVERLASTING_WINTER_SHADOW = MobConfig("everlasting_winter_shadow", health = 50.0, speed = 0.250, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = true, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val EVERLASTING_WINTER_SHADOW: Supplier<EntityType<BedrockMob>> =
        registerEntity("everlasting_winter_shadow", { type, level -> BedrockMob(type, level, CFG_EVERLASTING_WINTER_SHADOW) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val EVERLASTING_WINTER_SHADOW_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("everlasting_winter_shadow_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { EVERLASTING_WINTER_SHADOW.get() }, 12768237, 16777215, Item.Properties())
        })

    @JvmField
    val EVIL_SNOW_MAN: Supplier<EntityType<EvilSnowMan>> =
        registerEntity("evil_snow_man", { type, level -> EvilSnowMan(type, level) }, MobCategory.CREATURE) {
            sized(0.40f, 1.80f)
        }

    @JvmField
    val EVIL_SNOW_MAN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("evil_snow_man_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { EVIL_SNOW_MAN.get() }, 11695924, 16757592, Item.Properties())
        })

    @JvmField
    val FIRE_STORM: Supplier<EntityType<FireStorm>> =
        registerEntity("fire_storm", { type, level -> FireStorm(type, level) }, MobCategory.MONSTER) {
            sized(0.30f, 0.90f).fireImmune()
        }

    @JvmField
    val FIRE_STORM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("fire_storm_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FIRE_STORM.get() }, 12684066, 16764480, Item.Properties())
        })

    private val CFG_FRIENDLY_LITTLE_SOUL = MobConfig("friendly_little_soul", health = 8.0, speed = 0.200, attackDamage = 4.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val FRIENDLY_LITTLE_SOUL: Supplier<EntityType<BedrockMob>> =
        registerEntity("friendly_little_soul", { type, level -> BedrockMob(type, level, CFG_FRIENDLY_LITTLE_SOUL) }, MobCategory.MONSTER) {
            sized(0.60f, 1.50f)
        }

    @JvmField
    val FRIENDLY_LITTLE_SOUL_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("friendly_little_soul_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FRIENDLY_LITTLE_SOUL.get() }, 15790320, 16777215, Item.Properties())
        })

    @JvmField
    val FRIENDLY_SPIDER: Supplier<EntityType<FriendlySpider>> =
        registerEntity("friendly_spider", { type, level -> FriendlySpider(type, level) }, MobCategory.MONSTER) {
            sized(0.70f, 0.45f)
        }

    @JvmField
    val FRIENDLY_SPIDER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("friendly_spider_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FRIENDLY_SPIDER.get() }, 4545366, 7310725, Item.Properties())
        })

    @JvmField
    val FROZEN_GHAST: Supplier<EntityType<FrozenGhast>> =
        registerEntity("frozen_ghast", { type, level -> FrozenGhast(type, level) }, MobCategory.MONSTER) {
            sized(2.00f, 2.00f).fireImmune()
        }

    @JvmField
    val FROZEN_GHAST_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("frozen_ghast_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FROZEN_GHAST.get() }, 15462126, 16777215, Item.Properties())
        })

    private val CFG_FROZEN_HEART = MobConfig("frozen_heart", health = 150.0, speed = 0.080, attackDamage = 3.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem"))

    @JvmField
    val FROZEN_HEART: Supplier<EntityType<BedrockMob>> =
        registerEntity("frozen_heart", { type, level -> BedrockMob(type, level, CFG_FROZEN_HEART) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val FROZEN_HEART_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("frozen_heart_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FROZEN_HEART.get() }, 5429721, 8454143, Item.Properties())
        })

    private val CFG_FROZEN_HEART_SLEEPING = MobConfig("frozen_heart_sleeping", health = 1000000.0, speed = 0.250, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = true, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val FROZEN_HEART_SLEEPING: Supplier<EntityType<BedrockMob>> =
        registerEntity("frozen_heart_sleeping", { type, level -> BedrockMob(type, level, CFG_FROZEN_HEART_SLEEPING) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val FROZEN_HEART_SLEEPING_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("frozen_heart_sleeping_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FROZEN_HEART_SLEEPING.get() }, 3438514, 5813247, Item.Properties())
        })

    @JvmField
    val FROZEN_MAN: Supplier<EntityType<FrozenMan>> =
        registerEntity("frozen_man", { type, level -> FrozenMan(type, level) }, MobCategory.CREATURE) {
            sized(0.40f, 1.80f)
        }

    @JvmField
    val FROZEN_MAN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("frozen_man_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FROZEN_MAN.get() }, 3442866, 5819135, Item.Properties())
        })

    private val CFG_FROZEN_SHADOW = MobConfig("frozen_shadow", health = 24.0, speed = 4.000, attackDamage = 10.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val FROZEN_SHADOW: Supplier<EntityType<BedrockMob>> =
        registerEntity("frozen_shadow", { type, level -> BedrockMob(type, level, CFG_FROZEN_SHADOW) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val FROZEN_SHADOW_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("frozen_shadow_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FROZEN_SHADOW.get() }, 14016755, 16777215, Item.Properties())
        })

    @JvmField
    val FROZEN_SPIDER: Supplier<EntityType<FrozenSpider>> =
        registerEntity("frozen_spider", { type, level -> FrozenSpider(type, level) }, MobCategory.MONSTER) {
            sized(1.40f, 0.90f)
        }

    @JvmField
    val FROZEN_SPIDER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("frozen_spider_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { FROZEN_SPIDER.get() }, 16382972, 16777215, Item.Properties())
        })

    @JvmField
    val GARGOYLE: Supplier<EntityType<Gargoyle>> =
        registerEntity("gargoyle", { type, level -> Gargoyle(type, level) }, MobCategory.MONSTER) {
            sized(1.00f, 1.80f)
        }

    @JvmField
    val GARGOYLE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("gargoyle_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GARGOYLE.get() }, 11709492, 16775768, Item.Properties())
        })

    private val CFG_GHOST_DIAMOND_AXE = MobConfig("ghost_diamond_axe", health = 40.0, speed = 0.200, attackDamage = 6.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "villager"))

    @JvmField
    val GHOST_DIAMOND_AXE: Supplier<EntityType<BedrockMob>> =
        registerEntity("ghost_diamond_axe", { type, level -> BedrockMob(type, level, CFG_GHOST_DIAMOND_AXE) }, MobCategory.MONSTER) {
            sized(0.40f, 1.80f)
        }

    @JvmField
    val GHOST_DIAMOND_AXE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ghost_diamond_axe_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GHOST_DIAMOND_AXE.get() }, 11678797, 16734330, Item.Properties())
        })

    private val CFG_GHOST_DIAMOND_STAFF = MobConfig("ghost_diamond_staff", health = 40.0, speed = 0.080, attackDamage = 3.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "villager", "iron_golem", "snow_golem"), rangedProjectile = Supplier { ModEntities.STREAM_ENERGY_BALL.get() })

    @JvmField
    val GHOST_DIAMOND_STAFF: Supplier<EntityType<BedrockMob>> =
        registerEntity("ghost_diamond_staff", { type, level -> BedrockMob(type, level, CFG_GHOST_DIAMOND_STAFF) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val GHOST_DIAMOND_STAFF_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ghost_diamond_staff_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GHOST_DIAMOND_STAFF.get() }, 5289642, 8323064, Item.Properties())
        })

    private val CFG_GHOST_DIAMOND_SWORD = MobConfig("ghost_diamond_sword", health = 40.0, speed = 1.400, attackDamage = 7.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val GHOST_DIAMOND_SWORD: Supplier<EntityType<BedrockMob>> =
        registerEntity("ghost_diamond_sword", { type, level -> BedrockMob(type, level, CFG_GHOST_DIAMOND_SWORD) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val GHOST_DIAMOND_SWORD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ghost_diamond_sword_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GHOST_DIAMOND_SWORD.get() }, 2251084, 4229753, Item.Properties())
        })

    private val CFG_GHOST_IRON_AXE = MobConfig("ghost_iron_axe", health = 20.0, speed = 0.200, attackDamage = 5.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "villager"))

    @JvmField
    val GHOST_IRON_AXE: Supplier<EntityType<BedrockMob>> =
        registerEntity("ghost_iron_axe", { type, level -> BedrockMob(type, level, CFG_GHOST_IRON_AXE) }, MobCategory.MONSTER) {
            sized(0.40f, 1.80f)
        }

    @JvmField
    val GHOST_IRON_AXE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ghost_iron_axe_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GHOST_IRON_AXE.get() }, 7169366, 10918534, Item.Properties())
        })

    private val CFG_GHOST_IRON_SWORD = MobConfig("ghost_iron_sword", health = 20.0, speed = 1.800, attackDamage = 6.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val GHOST_IRON_SWORD: Supplier<EntityType<BedrockMob>> =
        registerEntity("ghost_iron_sword", { type, level -> BedrockMob(type, level, CFG_GHOST_IRON_SWORD) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val GHOST_IRON_SWORD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ghost_iron_sword_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GHOST_IRON_SWORD.get() }, 5723474, 8881537, Item.Properties())
        })

    private val CFG_GHOST_WOODEN_STAFF = MobConfig("ghost_wooden_staff", health = 20.0, speed = 0.080, attackDamage = 3.0, followRange = 64.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "villager", "iron_golem", "snow_golem"), rangedProjectile = Supplier { ModEntities.ENERGY_BALL.get() })

    @JvmField
    val GHOST_WOODEN_STAFF: Supplier<EntityType<BedrockMob>> =
        registerEntity("ghost_wooden_staff", { type, level -> BedrockMob(type, level, CFG_GHOST_WOODEN_STAFF) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val GHOST_WOODEN_STAFF_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ghost_wooden_staff_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GHOST_WOODEN_STAFF.get() }, 8481601, 12624745, Item.Properties())
        })

    private val CFG_GHOST_WOODEN_SWORD = MobConfig("ghost_wooden_sword", health = 10.0, speed = 1.800, attackDamage = 4.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val GHOST_WOODEN_SWORD: Supplier<EntityType<BedrockMob>> =
        registerEntity("ghost_wooden_sword", { type, level -> BedrockMob(type, level, CFG_GHOST_WOODEN_SWORD) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val GHOST_WOODEN_SWORD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ghost_wooden_sword_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GHOST_WOODEN_SWORD.get() }, 4075282, 6705450, Item.Properties())
        })

    @JvmField
    val GINGERBREAD_MAN_BY_TOTEM: Supplier<EntityType<GingerbreadManByTotem>> =
        registerEntity("gingerbread_man_by_totem", { type, level -> GingerbreadManByTotem(type, level) }, MobCategory.MONSTER) {
            sized(0.30f, 0.54f)
        }

    @JvmField
    val GINGERBREAD_MAN_BY_TOTEM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("gingerbread_man_by_totem_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GINGERBREAD_MAN_BY_TOTEM.get() }, 3438514, 5813247, Item.Properties())
        })

    private val CFG_GOBLIN = MobConfig("goblin", health = 30.0, speed = 0.350, attackDamage = 2.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val GOBLIN: Supplier<EntityType<BedrockMob>> =
        registerEntity("goblin", { type, level -> BedrockMob(type, level, CFG_GOBLIN) }, MobCategory.MONSTER) {
            sized(0.80f, 1.30f)
        }

    @JvmField
    val GOBLIN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("goblin_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GOBLIN.get() }, 11678793, 16734325, Item.Properties())
        })

    private val CFG_GOBLIN_SNIPER = MobConfig("goblin_sniper", health = 20.0, speed = 0.200, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"), rangedProjectile = Supplier { ModEntities.BULLET_BY_FLINTLOCK_PRO.get() })

    @JvmField
    val GOBLIN_SNIPER: Supplier<EntityType<BedrockMob>> =
        registerEntity("goblin_sniper", { type, level -> BedrockMob(type, level, CFG_GOBLIN_SNIPER) }, MobCategory.MONSTER) {
            sized(0.80f, 1.30f)
        }

    @JvmField
    val GOBLIN_SNIPER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("goblin_sniper_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GOBLIN_SNIPER.get() }, 10040498, 14768383, Item.Properties())
        })

    private val CFG_GOBLIN_WIZARD = MobConfig("goblin_wizard", health = 20.0, speed = 0.250, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"), rangedProjectile = Supplier { ModEntities.GOLDEN_ENERGY_BALL.get() })

    @JvmField
    val GOBLIN_WIZARD: Supplier<EntityType<BedrockMob>> =
        registerEntity("goblin_wizard", { type, level -> BedrockMob(type, level, CFG_GOBLIN_WIZARD) }, MobCategory.MONSTER) {
            sized(0.80f, 1.30f)
        }

    @JvmField
    val GOBLIN_WIZARD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("goblin_wizard_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { GOBLIN_WIZARD.get() }, 10596916, 15466328, Item.Properties())
        })

    private val CFG_HOST_OF_DEEP = MobConfig("host_of_deep", health = 400.0, speed = 0.500, attackDamage = 3.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val HOST_OF_DEEP: Supplier<EntityType<BedrockMob>> =
        registerEntity("host_of_deep", { type, level -> BedrockMob(type, level, CFG_HOST_OF_DEEP) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val HOST_OF_DEEP_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("host_of_deep_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { HOST_OF_DEEP.get() }, 4603453, 7366244, Item.Properties())
        })

    @JvmField
    val ICE_BAT: Supplier<EntityType<IceBat>> =
        registerEntity("ice_bat", { type, level -> IceBat(type, level) }, MobCategory.MONSTER) {
            sized(0.50f, 0.90f)
        }

    @JvmField
    val ICE_BAT_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ice_bat_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ICE_BAT.get() }, 15003126, 16777215, Item.Properties())
        })

    @JvmField
    val ICE_BLAZE: Supplier<EntityType<IceBlaze>> =
        registerEntity("ice_blaze", { type, level -> IceBlaze(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f).fireImmune()
        }

    @JvmField
    val ICE_BLAZE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ice_blaze_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ICE_BLAZE.get() }, 7577584, 11397631, Item.Properties())
        })

    @JvmField
    val ICE_CREEPER: Supplier<EntityType<IceCreeper>> =
        registerEntity("ice_creeper", { type, level -> IceCreeper(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f).fireImmune()
        }

    @JvmField
    val ICE_CREEPER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ice_creeper_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ICE_CREEPER.get() }, 13492218, 16777215, Item.Properties())
        })

    private val CFG_ICE_MONSTER = MobConfig("ice_monster", health = 100.0, speed = 0.250, attackDamage = 15.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "villager"))

    @JvmField
    val ICE_MONSTER: Supplier<EntityType<BedrockMob>> =
        registerEntity("ice_monster", { type, level -> BedrockMob(type, level, CFG_ICE_MONSTER) }, MobCategory.MONSTER) {
            sized(1.20f, 2.90f)
        }

    @JvmField
    val ICE_MONSTER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ice_monster_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ICE_MONSTER.get() }, 13557757, 16777215, Item.Properties())
        })

    private val CFG_ICE_SPIRIT = MobConfig("ice_spirit", health = 20.0, speed = 0.300, attackDamage = 6.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "villager"))

    @JvmField
    val ICE_SPIRIT: Supplier<EntityType<BedrockMob>> =
        registerEntity("ice_spirit", { type, level -> BedrockMob(type, level, CFG_ICE_SPIRIT) }, MobCategory.MONSTER) {
            sized(0.60f, 0.60f)
        }

    @JvmField
    val ICE_SPIRIT_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ice_spirit_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ICE_SPIRIT.get() }, 12768504, 16777215, Item.Properties())
        })

    private val CFG_ICE_THORN = MobConfig("ice_thorn", health = 6.0, speed = 0.250, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = true, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val ICE_THORN: Supplier<EntityType<BedrockMob>> =
        registerEntity("ice_thorn", { type, level -> BedrockMob(type, level, CFG_ICE_THORN) }, MobCategory.MONSTER) {
            sized(1.00f, 2.00f)
        }

    @JvmField
    val ICE_THORN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ice_thorn_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ICE_THORN.get() }, 5354036, 8388440, Item.Properties())
        })

    private val CFG_ICE_WIZARD = MobConfig("ice_wizard", health = 120.0, speed = 0.500, attackDamage = 3.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val ICE_WIZARD: Supplier<EntityType<BedrockMob>> =
        registerEntity("ice_wizard", { type, level -> BedrockMob(type, level, CFG_ICE_WIZARD) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ICE_WIZARD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ice_wizard_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ICE_WIZARD.get() }, 9677781, 14285311, Item.Properties())
        })

    @JvmField
    val ICE_ZOMBIE: Supplier<EntityType<IceZombie>> =
        registerEntity("ice_zombie", { type, level -> IceZombie(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val ICE_ZOMBIE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("ice_zombie_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ICE_ZOMBIE.get() }, 14871273, 16777215, Item.Properties())
        })

    @JvmField
    val ILLUSION_ZOMBIE: Supplier<EntityType<IllusionZombie>> =
        registerEntity("illusion_zombie", { type, level -> IllusionZombie(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ILLUSION_ZOMBIE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("illusion_zombie_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ILLUSION_ZOMBIE.get() }, 6250335, 9605778, Item.Properties())
        })

    @JvmField
    val ILLUSIONER: Supplier<EntityType<Illusioner>> =
        registerEntity("illusioner", { type, level -> Illusioner(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ILLUSIONER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("illusioner_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ILLUSIONER.get() }, 3627638, 6065329, Item.Properties())
        })

    @JvmField
    val ILLUSIONER_PUPPET: Supplier<EntityType<IllusionerPuppet>> =
        registerEntity("illusioner_puppet", { type, level -> IllusionerPuppet(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ILLUSIONER_PUPPET_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("illusioner_puppet_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ILLUSIONER_PUPPET.get() }, 3627638, 6065329, Item.Properties())
        })

    @JvmField
    val JUNGLE_SPIDER: Supplier<EntityType<JungleSpider>> =
        registerEntity("jungle_spider", { type, level -> JungleSpider(type, level) }, MobCategory.MONSTER) {
            sized(0.40f, 0.30f)
        }

    @JvmField
    val JUNGLE_SPIDER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("jungle_spider_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { JUNGLE_SPIDER.get() }, 5661267, 8885891, Item.Properties())
        })

    @JvmField
    val KING_OF_PILLAGER: Supplier<EntityType<KingOfPillager>> =
        registerEntity("king_of_pillager", { type, level -> KingOfPillager(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 2.00f).fireImmune()
        }

    @JvmField
    val KING_OF_PILLAGER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("king_of_pillager_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { KING_OF_PILLAGER.get() }, 3453565, 5832635, Item.Properties())
        })

    private val CFG_LAVA_LIZARD = MobConfig("lava_lizard", health = 15.0, speed = 0.160, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val LAVA_LIZARD: Supplier<EntityType<BedrockMob>> =
        registerEntity("lava_lizard", { type, level -> BedrockMob(type, level, CFG_LAVA_LIZARD) }, MobCategory.MONSTER) {
            sized(1.20f, 0.60f)
        }

    @JvmField
    val LAVA_LIZARD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("lava_lizard_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { LAVA_LIZARD.get() }, 11089074, 16144639, Item.Properties())
        })

    @JvmField
    val LIFE_INSECT: Supplier<EntityType<LifeInsect>> =
        registerEntity("life_insect", { type, level -> LifeInsect(type, level) }, MobCategory.MONSTER) {
            sized(0.40f, 0.30f).fireImmune()
        }

    @JvmField
    val LIFE_INSECT_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("life_insect_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { LIFE_INSECT.get() }, 13583923, 16740695, Item.Properties())
        })

    @JvmField
    val LITTLE_BAT: Supplier<EntityType<LittleBat>> =
        registerEntity("little_bat", { type, level -> LittleBat(type, level) }, MobCategory.MONSTER) {
            sized(0.40f, 0.72f)
        }

    @JvmField
    val LITTLE_BAT_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("little_bat_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { LITTLE_BAT.get() }, 3942661, 6506777, Item.Properties())
        })

    private val CFG_LITTLE_SOUL = MobConfig("little_soul", health = 3.0, speed = 0.200, attackDamage = 6.0, followRange = 64.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val LITTLE_SOUL: Supplier<EntityType<BedrockMob>> =
        registerEntity("little_soul", { type, level -> BedrockMob(type, level, CFG_LITTLE_SOUL) }, MobCategory.MONSTER) {
            sized(0.60f, 1.50f)
        }

    @JvmField
    val LITTLE_SOUL_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("little_soul_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { LITTLE_SOUL.get() }, 15790320, 16777215, Item.Properties())
        })

    @JvmField
    val LURK_SKELETON: Supplier<EntityType<LurkSkeleton>> =
        registerEntity("lurk_skeleton", { type, level -> LurkSkeleton(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val LURK_SKELETON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("lurk_skeleton_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { LURK_SKELETON.get() }, 1214090, 2869197, Item.Properties())
        })

    @JvmField
    val LURK_ZOMBIE: Supplier<EntityType<LurkZombie>> =
        registerEntity("lurk_zombie", { type, level -> LurkZombie(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val LURK_ZOMBIE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("lurk_zombie_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { LURK_ZOMBIE.get() }, 1259580, 2907747, Item.Properties())
        })

    @JvmField
    val MUMMY: Supplier<EntityType<Mummy>> =
        registerEntity("mummy", { type, level -> Mummy(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val MUMMY_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("mummy_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { MUMMY.get() }, 14075565, 16777212, Item.Properties())
        })

    @JvmField
    val MUMMY_SKELETON: Supplier<EntityType<MummySkeleton>> =
        registerEntity("mummy_skeleton", { type, level -> MummySkeleton(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val MUMMY_SKELETON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("mummy_skeleton_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { MUMMY_SKELETON.get() }, 14075565, 16777211, Item.Properties())
        })

    @JvmField
    val MURLOC: Supplier<EntityType<Murloc>> =
        registerEntity("murloc", { type, level -> Murloc(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val MURLOC_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("murloc_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { MURLOC.get() }, 5012629, 7977434, Item.Properties())
        })

    private val CFG_MURLOC_WIZARD = MobConfig("murloc_wizard", health = 11.0, speed = 0.250, attackDamage = 5.0, followRange = 16.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"), rangedProjectile = Supplier { ModEntities.WAVE_ENERGY.get() })

    @JvmField
    val MURLOC_WIZARD: Supplier<EntityType<BedrockMob>> =
        registerEntity("murloc_wizard", { type, level -> BedrockMob(type, level, CFG_MURLOC_WIZARD) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val MURLOC_WIZARD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("murloc_wizard_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { MURLOC_WIZARD.get() }, 5148060, 8114404, Item.Properties())
        })

    private val CFG_MUSHROOM_MONSTER = MobConfig("mushroom_monster", health = 20.0, speed = 0.250, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = true, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val MUSHROOM_MONSTER: Supplier<EntityType<BedrockMob>> =
        registerEntity("mushroom_monster", { type, level -> BedrockMob(type, level, CFG_MUSHROOM_MONSTER) }, MobCategory.WATER_CREATURE) {
            sized(0.30f, 0.30f)
        }

    @JvmField
    val MUSHROOM_MONSTER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("mushroom_monster_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { MUSHROOM_MONSTER.get() }, 14310479, 16748413, Item.Properties())
        })

    @JvmField
    val MUSHROOM_ZOMBIE: Supplier<EntityType<MushroomZombie>> =
        registerEntity("mushroom_zombie", { type, level -> MushroomZombie(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val MUSHROOM_ZOMBIE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("mushroom_zombie_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { MUSHROOM_ZOMBIE.get() }, 3369827, 5743512, Item.Properties())
        })

    @JvmField
    val MYSTICAL_SKELETON: Supplier<EntityType<MysticalSkeleton>> =
        registerEntity("mystical_skeleton", { type, level -> MysticalSkeleton(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val MYSTICAL_SKELETON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("mystical_skeleton_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { MYSTICAL_SKELETON.get() }, 4666546, 7493887, Item.Properties())
        })

    private val CFG_NAUTILUS = MobConfig("nautilus", health = 10.0, speed = 0.080, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = true, friendly = true, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val NAUTILUS: Supplier<EntityType<BedrockMob>> =
        registerEntity("nautilus", { type, level -> BedrockMob(type, level, CFG_NAUTILUS) }, MobCategory.WATER_CREATURE) {
            sized(0.30f, 0.30f)
        }

    @JvmField
    val NAUTILUS_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("nautilus_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { NAUTILUS.get() }, 16329256, 16730696, Item.Properties())
        })

    @JvmField
    val NETHER_CREEPER: Supplier<EntityType<NetherCreeper>> =
        registerEntity("nether_creeper", { type, level -> NetherCreeper(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f).fireImmune()
        }

    @JvmField
    val NETHER_CREEPER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("nether_creeper_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { NETHER_CREEPER.get() }, 5316640, 8339006, Item.Properties())
        })

    @JvmField
    val NETHER_GOLEM: Supplier<EntityType<NetherGolem>> =
        registerEntity("nether_golem", { type, level -> NetherGolem(type, level) }, MobCategory.CREATURE) {
            sized(0.60f, 2.00f).fireImmune()
        }

    @JvmField
    val NETHER_GOLEM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("nether_golem_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { NETHER_GOLEM.get() }, 6497830, 9913669, Item.Properties())
        })

    @JvmField
    val NETHER_PHANTOM: Supplier<EntityType<NetherPhantom>> =
        registerEntity("nether_phantom", { type, level -> NetherPhantom(type, level) }, MobCategory.MONSTER) {
            sized(1.20f, 0.75f)
        }

    @JvmField
    val NETHER_PHANTOM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("nether_phantom_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { NETHER_PHANTOM.get() }, 14735059, 16777215, Item.Properties())
        })

    @JvmField
    val NETHER_SKELETON: Supplier<EntityType<NetherSkeleton>> =
        registerEntity("nether_skeleton", { type, level -> NetherSkeleton(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f).fireImmune()
        }

    @JvmField
    val NETHER_SKELETON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("nether_skeleton_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { NETHER_SKELETON.get() }, 5570560, 8720914, Item.Properties())
        })

    @JvmField
    val NETHER_SKELETON_WIZARD: Supplier<EntityType<NetherSkeletonWizard>> =
        registerEntity("nether_skeleton_wizard", { type, level -> NetherSkeletonWizard(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val NETHER_SKELETON_WIZARD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("nether_skeleton_wizard_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { NETHER_SKELETON_WIZARD.get() }, 5636096, 8786450, Item.Properties())
        })

    @JvmField
    val OBSIDIAN_GOLEM: Supplier<EntityType<ObsidianGolem>> =
        registerEntity("obsidian_golem", { type, level -> ObsidianGolem(type, level) }, MobCategory.CREATURE) {
            sized(1.40f, 2.90f).fireImmune()
        }

    @JvmField
    val OBSIDIAN_GOLEM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("obsidian_golem_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { OBSIDIAN_GOLEM.get() }, 3453523, 5832578, Item.Properties())
        })

    @JvmField
    val PILLAGER_BY_BOSS: Supplier<EntityType<PillagerByBoss>> =
        registerEntity("pillager_by_boss", { type, level -> PillagerByBoss(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val PILLAGER_BY_BOSS_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("pillager_by_boss_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { PILLAGER_BY_BOSS.get() }, 5392712, 8418675, Item.Properties())
        })

    @JvmField
    val PIRATE: Supplier<EntityType<Pirate>> =
        registerEntity("pirate", { type, level -> Pirate(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val PIRATE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("pirate_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { PIRATE.get() }, 10072628, 14810968, Item.Properties())
        })

    @JvmField
    val PLAYER_GHOST: Supplier<EntityType<PlayerGhost>> =
        registerEntity("player_ghost", { type, level -> PlayerGhost(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val PLAYER_GHOST_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("player_ghost_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { PLAYER_GHOST.get() }, 8092539, 12105912, Item.Properties())
        })

    @JvmField
    val POLAR_WOLF: Supplier<EntityType<PolarWolf>> =
        registerEntity("polar_wolf", { type, level -> PolarWolf(type, level) }, MobCategory.CREATURE) {
            sized(0.90f, 1.20f)
        }

    @JvmField
    val POLAR_WOLF_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("polar_wolf_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { POLAR_WOLF.get() }, 11089074, 16144639, Item.Properties())
        })

    @JvmField
    val PREDATORS: Supplier<EntityType<Predators>> =
        registerEntity("predators", { type, level -> Predators(type, level) }, MobCategory.MONSTER) {
            sized(2.50f, 4.50f).fireImmune()
        }

    @JvmField
    val PREDATORS_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("predators_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { PREDATORS.get() }, 9319602, 13785343, Item.Properties())
        })

    @JvmField
    val PUMPKIN_SLIME: Supplier<EntityType<PumpkinSlime>> =
        registerEntity("pumpkin_slime", { type, level -> PumpkinSlime(type, level) }, MobCategory.MONSTER) {
            sized(0.50f, 0.50f)
        }

    @JvmField
    val PUMPKIN_SLIME_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("pumpkin_slime_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { PUMPKIN_SLIME.get() }, 12414743, 16754738, Item.Properties())
        })

    @JvmField
    val RADIATE_CREEPER: Supplier<EntityType<RadiateCreeper>> =
        registerEntity("radiate_creeper", { type, level -> RadiateCreeper(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f).fireImmune()
        }

    @JvmField
    val RADIATE_CREEPER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("radiate_creeper_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { RADIATE_CREEPER.get() }, 999437, 2517027, Item.Properties())
        })

    @JvmField
    val RADIATE_ENDERMAN: Supplier<EntityType<RadiateEnderman>> =
        registerEntity("radiate_enderman", { type, level -> RadiateEnderman(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 2.90f)
        }

    @JvmField
    val RADIATE_ENDERMAN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("radiate_enderman_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { RADIATE_ENDERMAN.get() }, 858375, 2307099, Item.Properties())
        })

    @JvmField
    val RADIATE_SKELETON: Supplier<EntityType<RadiateSkeleton>> =
        registerEntity("radiate_skeleton", { type, level -> RadiateSkeleton(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f).fireImmune()
        }

    @JvmField
    val RADIATE_SKELETON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("radiate_skeleton_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { RADIATE_SKELETON.get() }, 10529697, 15399403, Item.Properties())
        })

    @JvmField
    val RADIATE_SPIDER: Supplier<EntityType<RadiateSpider>> =
        registerEntity("radiate_spider", { type, level -> RadiateSpider(type, level) }, MobCategory.MONSTER) {
            sized(1.68f, 1.08f)
        }

    @JvmField
    val RADIATE_SPIDER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("radiate_spider_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { RADIATE_SPIDER.get() }, 7499625, 11315360, Item.Properties())
        })

    @JvmField
    val REAL_SOUL: Supplier<EntityType<RealSoul>> =
        registerEntity("real_soul", { type, level -> RealSoul(type, level) }, MobCategory.MONSTER) {
            sized(1.20f, 3.00f)
        }

    @JvmField
    val REAL_SOUL_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("real_soul_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { REAL_SOUL.get() }, 15790320, 16777215, Item.Properties())
        })

    @JvmField
    val RUMORER: Supplier<EntityType<Rumorer>> =
        registerEntity("rumorer", { type, level -> Rumorer(type, level) }, MobCategory.MONSTER) {
            sized(0.70f, 1.20f)
        }

    @JvmField
    val RUMORER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("rumorer_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { RUMORER.get() }, 1184274, 2829099, Item.Properties())
        })

    @JvmField
    val SARDINE: Supplier<EntityType<Sardine>> =
        registerEntity("sardine", { type, level -> Sardine(type, level) }, MobCategory.WATER_CREATURE) {
            sized(0.60f, 0.30f)
        }

    @JvmField
    val SARDINE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("sardine_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SARDINE.get() }, 8495030, 12642559, Item.Properties())
        })

    private val CFG_SAUCER = MobConfig("saucer", health = 9.0, speed = 0.080, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = false, rangedInterval = 40, targets = listOf("player"))

    @JvmField
    val SAUCER: Supplier<EntityType<BedrockMob>> =
        registerEntity("saucer", { type, level -> BedrockMob(type, level, CFG_SAUCER) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f)
        }

    @JvmField
    val SAUCER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("saucer_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SAUCER.get() }, 6912397, 10532560, Item.Properties())
        })

    private val CFG_SEA_URCHIN = MobConfig("sea_urchin", health = 6.0, speed = 0.080, attackDamage = 4.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = true, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val SEA_URCHIN: Supplier<EntityType<BedrockMob>> =
        registerEntity("sea_urchin", { type, level -> BedrockMob(type, level, CFG_SEA_URCHIN) }, MobCategory.WATER_CREATURE) {
            sized(0.80f, 0.80f)
        }

    @JvmField
    val SEA_URCHIN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("sea_urchin_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SEA_URCHIN.get() }, 591383, 1972528, Item.Properties())
        })

    @JvmField
    val SHADOW_ARCHER: Supplier<EntityType<ShadowArcher>> =
        registerEntity("shadow_archer", { type, level -> ShadowArcher(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val SHADOW_ARCHER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_archer_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_ARCHER.get() }, 8220015, 12297640, Item.Properties())
        })

    @JvmField
    val SHADOW_CREEPER: Supplier<EntityType<ShadowCreeper>> =
        registerEntity("shadow_creeper", { type, level -> ShadowCreeper(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val SHADOW_CREEPER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_creeper_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_CREEPER.get() }, 10564786, 15423743, Item.Properties())
        })

    private val CFG_SHADOW_OF_DEEP = MobConfig("shadow_of_deep", health = 40.0, speed = 0.300, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem"))

    @JvmField
    val SHADOW_OF_DEEP: Supplier<EntityType<BedrockMob>> =
        registerEntity("shadow_of_deep", { type, level -> BedrockMob(type, level, CFG_SHADOW_OF_DEEP) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SHADOW_OF_DEEP_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_of_deep_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_OF_DEEP.get() }, 4603453, 7366244, Item.Properties())
        })

    @JvmField
    val SHADOW_OF_SEA: Supplier<EntityType<ShadowOfSea>> =
        registerEntity("shadow_of_sea", { type, level -> ShadowOfSea(type, level) }, MobCategory.WATER_CREATURE) {
            sized(0.90f, 0.50f)
        }

    @JvmField
    val SHADOW_OF_SEA_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_of_sea_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_OF_SEA.get() }, 14213085, 16777215, Item.Properties())
        })

    @JvmField
    val SHADOW_SKELETON: Supplier<EntityType<ShadowSkeleton>> =
        registerEntity("shadow_skeleton", { type, level -> ShadowSkeleton(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SHADOW_SKELETON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_skeleton_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_SKELETON.get() }, 11678786, 16734315, Item.Properties())
        })

    @JvmField
    val SHADOW_SKELETON_PUPPET: Supplier<EntityType<ShadowSkeletonPuppet>> =
        registerEntity("shadow_skeleton_puppet", { type, level -> ShadowSkeletonPuppet(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SHADOW_SKELETON_PUPPET_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_skeleton_puppet_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_SKELETON_PUPPET.get() }, 5056854, 8014213, Item.Properties())
        })

    private val CFG_SHADOW_SOUL_1 = MobConfig("shadow_soul_1", health = 200.0, speed = 0.150, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.70, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem"))

    @JvmField
    val SHADOW_SOUL_1: Supplier<EntityType<BedrockMob>> =
        registerEntity("shadow_soul_1", { type, level -> BedrockMob(type, level, CFG_SHADOW_SOUL_1) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val SHADOW_SOUL_1_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_soul_1_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_SOUL_1.get() }, 7169110, 10852742, Item.Properties())
        })

    private val CFG_SHADOW_SOUL_2 = MobConfig("shadow_soul_2", health = 200.0, speed = 0.150, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 0.70, xp = 5, fireImmune = true, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem"))

    @JvmField
    val SHADOW_SOUL_2: Supplier<EntityType<BedrockMob>> =
        registerEntity("shadow_soul_2", { type, level -> BedrockMob(type, level, CFG_SHADOW_SOUL_2) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val SHADOW_SOUL_2_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_soul_2_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_SOUL_2.get() }, 7169360, 10918526, Item.Properties())
        })

    @JvmField
    val SHADOW_ZOMBIE: Supplier<EntityType<ShadowZombie>> =
        registerEntity("shadow_zombie", { type, level -> ShadowZombie(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SHADOW_ZOMBIE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_zombie_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_ZOMBIE.get() }, 3451314, 5830399, Item.Properties())
        })

    @JvmField
    val SHADOW_ZOMBIE_PUPPET: Supplier<EntityType<ShadowZombiePuppet>> =
        registerEntity("shadow_zombie_puppet", { type, level -> ShadowZombiePuppet(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SHADOW_ZOMBIE_PUPPET_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("shadow_zombie_puppet_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SHADOW_ZOMBIE_PUPPET.get() }, 4791894, 7682950, Item.Properties())
        })

    @JvmField
    val SIMPLE_GLIDER: Supplier<EntityType<SimpleGlider>> =
        registerEntity("simple_glider", { type, level -> SimpleGlider(type, level) }, MobCategory.CREATURE) {
            sized(0.98f, 0.70f)
        }

    @JvmField
    val SIMPLE_GLIDER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("simple_glider_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SIMPLE_GLIDER.get() }, 7830655, 11713213, Item.Properties())
        })

    @JvmField
    val SKELETON_ASSASSIN: Supplier<EntityType<SkeletonAssassin>> =
        registerEntity("skeleton_assassin", { type, level -> SkeletonAssassin(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SKELETON_ASSASSIN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("skeleton_assassin_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SKELETON_ASSASSIN.get() }, 11052968, 16119029, Item.Properties())
        })

    @JvmField
    val SKELETON_KNIGHT: Supplier<EntityType<SkeletonKnight>> =
        registerEntity("skeleton_knight", { type, level -> SkeletonKnight(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SKELETON_KNIGHT_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("skeleton_knight_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SKELETON_KNIGHT.get() }, 10855845, 15856113, Item.Properties())
        })

    @JvmField
    val SKELETON_KNIGHT_COMMANDER: Supplier<EntityType<SkeletonKnightCommander>> =
        registerEntity("skeleton_knight_commander", { type, level -> SkeletonKnightCommander(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SKELETON_KNIGHT_COMMANDER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("skeleton_knight_commander_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SKELETON_KNIGHT_COMMANDER.get() }, 7681202, 11557119, Item.Properties())
        })

    @JvmField
    val SKELETON_WARRIOR: Supplier<EntityType<SkeletonWarrior>> =
        registerEntity("skeleton_warrior", { type, level -> SkeletonWarrior(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f).fireImmune()
        }

    @JvmField
    val SKELETON_WARRIOR_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("skeleton_warrior_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SKELETON_WARRIOR.get() }, 10790052, 15790320, Item.Properties())
        })

    @JvmField
    val SKELETON_WIZARD: Supplier<EntityType<SkeletonWizard>> =
        registerEntity("skeleton_wizard", { type, level -> SkeletonWizard(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SKELETON_WIZARD_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("skeleton_wizard_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SKELETON_WIZARD.get() }, 10987431, 16053236, Item.Properties())
        })

    @JvmField
    val SOLDIER: Supplier<EntityType<Soldier>> =
        registerEntity("soldier", { type, level -> Soldier(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SOLDIER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("soldier_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SOLDIER.get() }, 3435698, 5809407, Item.Properties())
        })

    @JvmField
    val SON_OF_CHAOS: Supplier<EntityType<SonOfChaos>> =
        registerEntity("son_of_chaos", { type, level -> SonOfChaos(type, level) }, MobCategory.CREATURE) {
            sized(0.70f, 1.45f)
        }

    @JvmField
    val SON_OF_CHAOS_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("son_of_chaos_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SON_OF_CHAOS.get() }, 8106548, 12123992, Item.Properties())
        })

    @JvmField
    val SON_OF_NATURE: Supplier<EntityType<SonOfNature>> =
        registerEntity("son_of_nature", { type, level -> SonOfNature(type, level) }, MobCategory.CREATURE) {
            sized(0.70f, 1.45f)
        }

    @JvmField
    val SON_OF_NATURE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("son_of_nature_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SON_OF_NATURE.get() }, 3110403, 5354006, Item.Properties())
        })

    @JvmField
    val SOUL: Supplier<EntityType<Soul>> =
        registerEntity("soul", { type, level -> Soul(type, level) }, MobCategory.MONSTER) {
            sized(0.35f, 0.60f).fireImmune()
        }

    @JvmField
    val SOUL_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("soul_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SOUL.get() }, 15921906, 16777215, Item.Properties())
        })

    @JvmField
    val SOUL_BLAZE: Supplier<EntityType<SoulBlaze>> =
        registerEntity("soul_blaze", { type, level -> SoulBlaze(type, level) }, MobCategory.MONSTER) {
            sized(0.50f, 1.80f).fireImmune()
        }

    @JvmField
    val SOUL_BLAZE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("soul_blaze_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SOUL_BLAZE.get() }, 3057078, 5304831, Item.Properties())
        })

    @JvmField
    val SOUL_INSECT: Supplier<EntityType<SoulInsect>> =
        registerEntity("soul_insect", { type, level -> SoulInsect(type, level) }, MobCategory.WATER_CREATURE) {
            sized(0.80f, 0.60f).fireImmune()
        }

    @JvmField
    val SOUL_INSECT_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("soul_insect_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SOUL_INSECT.get() }, 12567747, 16777215, Item.Properties())
        })

    @JvmField
    val SOUL_SKELETON: Supplier<EntityType<SoulSkeleton>> =
        registerEntity("soul_skeleton", { type, level -> SoulSkeleton(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SOUL_SKELETON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("soul_skeleton_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SOUL_SKELETON.get() }, 3453551, 5832616, Item.Properties())
        })

    private val CFG_SOUL_SOLDIER = MobConfig("soul_soldier", health = 50.0, speed = 0.250, attackDamage = 3.0, followRange = 32.0, knockbackResistance = 1.00, xp = 5, fireImmune = false, undead = false, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val SOUL_SOLDIER: Supplier<EntityType<BedrockMob>> =
        registerEntity("soul_soldier", { type, level -> BedrockMob(type, level, CFG_SOUL_SOLDIER) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SOUL_SOLDIER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("soul_soldier_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SOUL_SOLDIER.get() }, 7712939, 11599865, Item.Properties())
        })

    @JvmField
    val SOUL_ZOMBIE: Supplier<EntityType<SoulZombie>> =
        registerEntity("soul_zombie", { type, level -> SoulZombie(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f)
        }

    @JvmField
    val SOUL_ZOMBIE_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("soul_zombie_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SOUL_ZOMBIE.get() }, 3450802, 5829631, Item.Properties())
        })

    @JvmField
    val STAR: Supplier<EntityType<Star>> =
        registerEntity("star", { type, level -> Star(type, level) }, MobCategory.MONSTER) {
            sized(1.00f, 1.00f).fireImmune()
        }

    @JvmField
    val STAR_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("star_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { STAR.get() }, 4370996, 7077720, Item.Properties())
        })

    @JvmField
    val STONE_GOLEM: Supplier<EntityType<StoneGolem>> =
        registerEntity("stone_golem", { type, level -> StoneGolem(type, level) }, MobCategory.CREATURE) {
            sized(1.40f, 2.90f)
        }

    @JvmField
    val STONE_GOLEM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("stone_golem_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { STONE_GOLEM.get() }, 3453519, 5832573, Item.Properties())
        })

    @JvmField
    val SWAMP_DROWNED: Supplier<EntityType<SwampDrowned>> =
        registerEntity("swamp_drowned", { type, level -> SwampDrowned(type, level) }, MobCategory.WATER_CREATURE) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val SWAMP_DROWNED_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("swamp_drowned_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SWAMP_DROWNED.get() }, 7372126, 11123601, Item.Properties())
        })

    @JvmField
    val SWAMP_GOLEM: Supplier<EntityType<SwampGolem>> =
        registerEntity("swamp_golem", { type, level -> SwampGolem(type, level) }, MobCategory.WATER_CREATURE) {
            sized(0.60f, 2.00f).fireImmune()
        }

    @JvmField
    val SWAMP_GOLEM_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("swamp_golem_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { SWAMP_GOLEM.get() }, 5531198, 8690534, Item.Properties())
        })

    @JvmField
    val TNT_CREEPER: Supplier<EntityType<TntCreeper>> =
        registerEntity("tnt_creeper", { type, level -> TntCreeper(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.80f).fireImmune()
        }

    @JvmField
    val TNT_CREEPER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("tnt_creeper_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { TNT_CREEPER.get() }, 11760225, 16755860, Item.Properties())
        })

    @JvmField
    val TNT_SNOW_MAN: Supplier<EntityType<TntSnowMan>> =
        registerEntity("tnt_snow_man", { type, level -> TntSnowMan(type, level) }, MobCategory.CREATURE) {
            sized(0.40f, 1.80f)
        }

    @JvmField
    val TNT_SNOW_MAN_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("tnt_snow_man_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { TNT_SNOW_MAN.get() }, 14735315, 16777215, Item.Properties())
        })

    @JvmField
    val VAMPIRE_BAT: Supplier<EntityType<VampireBat>> =
        registerEntity("vampire_bat", { type, level -> VampireBat(type, level) }, MobCategory.MONSTER) {
            sized(0.40f, 0.72f)
        }

    @JvmField
    val VAMPIRE_BAT_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("vampire_bat_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { VAMPIRE_BAT.get() }, 11705140, 16769880, Item.Properties())
        })

    @JvmField
    val VAMPIRE_BAT_BY_BOSS: Supplier<EntityType<VampireBatByBoss>> =
        registerEntity("vampire_bat_by_boss", { type, level -> VampireBatByBoss(type, level) }, MobCategory.MONSTER) {
            sized(0.40f, 0.72f)
        }

    @JvmField
    val VAMPIRE_BAT_BY_BOSS_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("vampire_bat_by_boss_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { VAMPIRE_BAT_BY_BOSS.get() }, 7419058, 11229439, Item.Properties())
        })

    @JvmField
    val VENGEFUL_GHOST: Supplier<EntityType<VengefulGhost>> =
        registerEntity("vengeful_ghost", { type, level -> VengefulGhost(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val VENGEFUL_GHOST_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("vengeful_ghost_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { VENGEFUL_GHOST.get() }, 8091769, 12104885, Item.Properties())
        })

    @JvmField
    val VINDICATOR_BY_BOSS: Supplier<EntityType<VindicatorByBoss>> =
        registerEntity("vindicator_by_boss", { type, level -> VindicatorByBoss(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val VINDICATOR_BY_BOSS_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("vindicator_by_boss_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { VINDICATOR_BY_BOSS.get() }, 6447970, 9934743, Item.Properties())
        })

    @JvmField
    val WARPED_SKELETON: Supplier<EntityType<WarpedSkeleton>> =
        registerEntity("warped_skeleton", { type, level -> WarpedSkeleton(type, level) }, MobCategory.MONSTER) {
            sized(0.63f, 1.98f).fireImmune()
        }

    @JvmField
    val WARPED_SKELETON_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("warped_skeleton_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { WARPED_SKELETON.get() }, 938858, 2458017, Item.Properties())
        })

    @JvmField
    val WATCHER: Supplier<EntityType<Watcher>> =
        registerEntity("watcher", { type, level -> Watcher(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val WATCHER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("watcher_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { WATCHER.get() }, 11678858, 16734412, Item.Properties())
        })

    private val CFG_WEREWOLF = MobConfig("werewolf", health = 60.0, speed = 0.250, attackDamage = 5.0, followRange = 32.0, knockbackResistance = 0.75, xp = 5, fireImmune = false, undead = true, water = false, friendly = false, melee = true, rangedInterval = 40, targets = listOf("player", "iron_golem", "snow_golem", "villager"))

    @JvmField
    val WEREWOLF: Supplier<EntityType<BedrockMob>> =
        registerEntity("werewolf", { type, level -> BedrockMob(type, level, CFG_WEREWOLF) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val WEREWOLF_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("werewolf_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { WEREWOLF.get() }, 4220275, 6855342, Item.Properties())
        })

    @JvmField
    val ZOMBIE_FISH: Supplier<EntityType<ZombieFish>> =
        registerEntity("zombie_fish", { type, level -> ZombieFish(type, level) }, MobCategory.WATER_CREATURE) {
            sized(0.50f, 0.50f)
        }

    @JvmField
    val ZOMBIE_FISH_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("zombie_fish_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ZOMBIE_FISH.get() }, 11678835, 16734381, Item.Properties())
        })

    @JvmField
    val ZOMBIE_SUMMONER: Supplier<EntityType<ZombieSummoner>> =
        registerEntity("zombie_summoner", { type, level -> ZombieSummoner(type, level) }, MobCategory.MONSTER) {
            sized(0.60f, 1.90f)
        }

    @JvmField
    val ZOMBIE_SUMMONER_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("zombie_summoner_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ZOMBIE_SUMMONER.get() }, 3239011, 5547160, Item.Properties())
        })

    // ==== 属性 ====
    data class SpawnEntry(val type: Supplier<out EntityType<*>>, val rule: String)

    @JvmStatic
    val ATTRIBUTE_ENTRIES: List<Pair<Supplier<out EntityType<*>>, Supplier<AttributeSupplier.Builder>>> = listOf(
        ABYSSAL_CONTROLLER to Supplier { BedrockMob.createAttributes(CFG_ABYSSAL_CONTROLLER) },
        ABYSSAL_SHADOW to Supplier { BedrockMob.createAttributes(CFG_ABYSSAL_SHADOW) },
        ANGRY_CHICKEN to Supplier { net.minecraft.world.entity.animal.Chicken.createAttributes() },
        ARCHER to Supplier { net.minecraft.world.entity.monster.Pillager.createAttributes() },
        ASH_BLAZE to Supplier { net.minecraft.world.entity.monster.Blaze.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        ASH_BOMB to Supplier { BedrockMob.createAttributes(CFG_ASH_BOMB) },
        ASH_HORSE to Supplier { BedrockMob.createAttributes(CFG_ASH_HORSE) },
        ASH_HORSE_HEAD to Supplier { BedrockMob.createAttributes(CFG_ASH_HORSE_HEAD) },
        ASH_KNIGHT to Supplier { BedrockMob.createAttributes(CFG_ASH_KNIGHT) },
        ASH_KNIGHT_HEAD to Supplier { BedrockMob.createAttributes(CFG_ASH_KNIGHT_HEAD) },
        ASH_PUFFERFISH to Supplier { net.minecraft.world.entity.animal.AbstractFish.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 6.0) },
        ASH_SWORD to Supplier { BedrockMob.createAttributes(CFG_ASH_SWORD) },
        ASH_SWORD_PHANTOM to Supplier { BedrockMob.createAttributes(CFG_ASH_SWORD_PHANTOM) },
        BABY_ENDER_DRAGON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 300.0) },
        BAT_CHARGER to Supplier { net.minecraft.world.entity.ambient.Bat.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0) },
        BLACKSTONE_THORN to Supplier { BedrockMob.createAttributes(CFG_BLACKSTONE_THORN) },
        BLOOD_ZOMBIE to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 50.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        BOMBER to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0) },
        BROWN_BEAR to Supplier { net.minecraft.world.entity.animal.PolarBear.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0) },
        CARNAGER to Supplier { net.minecraft.world.entity.monster.Vindicator.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 35.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        CHEST_MONSTER to Supplier { BedrockMob.createAttributes(CFG_CHEST_MONSTER) },
        CHESTER to Supplier { BedrockMob.createAttributes(CFG_CHESTER) },
        CLAM to Supplier { BedrockMob.createAttributes(CFG_CLAM) },
        CRAB to Supplier { BedrockMob.createAttributes(CFG_CRAB) },
        CRIMSON_SLIME to Supplier { net.minecraft.world.entity.monster.Monster.createMonsterAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0) },
        DARK_SNOW_MAN to Supplier { net.minecraft.world.entity.animal.SnowGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 15.0) },
        DARK_WEREWOLF to Supplier { BedrockMob.createAttributes(CFG_DARK_WEREWOLF) },
        DOCTOR to Supplier { net.minecraft.world.entity.npc.Villager.createAttributes() },
        ELF_OF_ASH to Supplier { net.minecraft.world.entity.monster.Vex.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        ELF_OF_CHAOS to Supplier { net.minecraft.world.entity.animal.IronGolem.createAttributes() },
        ELF_OF_DEEP to Supplier { net.minecraft.world.entity.monster.Vex.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 10.0) },
        ELF_OF_DUST to Supplier { net.minecraft.world.entity.monster.Vex.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        ENCHANT_ARMOR to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 50.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 6.0) },
        ENCHANT_ARMOR_BY_BOSS to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 50.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 6.0) },
        ENCHANT_DIAMOND_ARMOR to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 60.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 8.0) },
        ENCHANT_ILLAGER to Supplier { net.minecraft.world.entity.monster.Evoker.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 700.0) },
        ENCHANT_ILLAGER_1 to Supplier { net.minecraft.world.entity.monster.Evoker.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 700.0) },
        ENCHANT_ILLAGER_2 to Supplier { net.minecraft.world.entity.monster.Vindicator.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 400.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 1.0) },
        END_STONE_GOLEM to Supplier { net.minecraft.world.entity.animal.IronGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 60.0) },
        ENDER_SNAIL to Supplier { BedrockMob.createAttributes(CFG_ENDER_SNAIL) },
        ENDER_SNAKE to Supplier { BedrockMob.createAttributes(CFG_ENDER_SNAKE) },
        ENDER_WITCH to Supplier { net.minecraft.world.entity.monster.Witch.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 10.0) },
        ENDER_WITCH_PUPPET to Supplier { net.minecraft.world.entity.monster.Witch.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 2.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 4.0) },
        ENTITY_SOUL to Supplier { BedrockMob.createAttributes(CFG_ENTITY_SOUL) },
        ENTITY_SOUL_1 to Supplier { BedrockMob.createAttributes(CFG_ENTITY_SOUL_1) },
        ESCAPED_SOUL_ENTITY to Supplier { BedrockMob.createAttributes(CFG_ESCAPED_SOUL_ENTITY) },
        EVERLASTING_WINTER_GHAST to Supplier { BedrockMob.createAttributes(CFG_EVERLASTING_WINTER_GHAST) },
        EVERLASTING_WINTER_GHAST_1 to Supplier { net.minecraft.world.entity.monster.Ghast.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 350.0) },
        EVERLASTING_WINTER_SHADOW to Supplier { BedrockMob.createAttributes(CFG_EVERLASTING_WINTER_SHADOW) },
        EVIL_SNOW_MAN to Supplier { net.minecraft.world.entity.animal.SnowGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 10.0) },
        FIRE_STORM to Supplier { net.minecraft.world.entity.monster.Blaze.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        FRIENDLY_LITTLE_SOUL to Supplier { BedrockMob.createAttributes(CFG_FRIENDLY_LITTLE_SOUL) },
        FRIENDLY_SPIDER to Supplier { net.minecraft.world.entity.monster.Spider.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 15.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        FROZEN_GHAST to Supplier { net.minecraft.world.entity.monster.Ghast.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 15.0) },
        FROZEN_HEART to Supplier { BedrockMob.createAttributes(CFG_FROZEN_HEART) },
        FROZEN_HEART_SLEEPING to Supplier { BedrockMob.createAttributes(CFG_FROZEN_HEART_SLEEPING) },
        FROZEN_MAN to Supplier { net.minecraft.world.entity.animal.SnowGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0) },
        FROZEN_SHADOW to Supplier { BedrockMob.createAttributes(CFG_FROZEN_SHADOW) },
        FROZEN_SPIDER to Supplier { net.minecraft.world.entity.monster.Spider.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        GARGOYLE to Supplier { net.minecraft.world.entity.ambient.Bat.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 23.0) },
        GHOST_DIAMOND_AXE to Supplier { BedrockMob.createAttributes(CFG_GHOST_DIAMOND_AXE) },
        GHOST_DIAMOND_STAFF to Supplier { BedrockMob.createAttributes(CFG_GHOST_DIAMOND_STAFF) },
        GHOST_DIAMOND_SWORD to Supplier { BedrockMob.createAttributes(CFG_GHOST_DIAMOND_SWORD) },
        GHOST_IRON_AXE to Supplier { BedrockMob.createAttributes(CFG_GHOST_IRON_AXE) },
        GHOST_IRON_SWORD to Supplier { BedrockMob.createAttributes(CFG_GHOST_IRON_SWORD) },
        GHOST_WOODEN_STAFF to Supplier { BedrockMob.createAttributes(CFG_GHOST_WOODEN_STAFF) },
        GHOST_WOODEN_SWORD to Supplier { BedrockMob.createAttributes(CFG_GHOST_WOODEN_SWORD) },
        GINGERBREAD_MAN_BY_TOTEM to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 3.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 1.0) },
        GOBLIN to Supplier { BedrockMob.createAttributes(CFG_GOBLIN) },
        GOBLIN_SNIPER to Supplier { BedrockMob.createAttributes(CFG_GOBLIN_SNIPER) },
        GOBLIN_WIZARD to Supplier { BedrockMob.createAttributes(CFG_GOBLIN_WIZARD) },
        HOST_OF_DEEP to Supplier { BedrockMob.createAttributes(CFG_HOST_OF_DEEP) },
        ICE_BAT to Supplier { net.minecraft.world.entity.ambient.Bat.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 10.0) },
        ICE_BLAZE to Supplier { net.minecraft.world.entity.monster.Blaze.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 35.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        ICE_CREEPER to Supplier { net.minecraft.world.entity.monster.Creeper.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 7.0) },
        ICE_MONSTER to Supplier { BedrockMob.createAttributes(CFG_ICE_MONSTER) },
        ICE_SPIRIT to Supplier { BedrockMob.createAttributes(CFG_ICE_SPIRIT) },
        ICE_THORN to Supplier { BedrockMob.createAttributes(CFG_ICE_THORN) },
        ICE_WIZARD to Supplier { BedrockMob.createAttributes(CFG_ICE_WIZARD) },
        ICE_ZOMBIE to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 38.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        ILLUSION_ZOMBIE to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        ILLUSIONER to Supplier { net.minecraft.world.entity.monster.Illusioner.createAttributes() },
        ILLUSIONER_PUPPET to Supplier { net.minecraft.world.entity.monster.Illusioner.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        JUNGLE_SPIDER to Supplier { net.minecraft.world.entity.monster.Spider.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 15.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        KING_OF_PILLAGER to Supplier { net.minecraft.world.entity.monster.Pillager.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 200.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        LAVA_LIZARD to Supplier { BedrockMob.createAttributes(CFG_LAVA_LIZARD) },
        LIFE_INSECT to Supplier { net.minecraft.world.entity.monster.Silverfish.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 7.0) },
        LITTLE_BAT to Supplier { net.minecraft.world.entity.ambient.Bat.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 5.0) },
        LITTLE_SOUL to Supplier { BedrockMob.createAttributes(CFG_LITTLE_SOUL) },
        LURK_SKELETON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 60.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        LURK_ZOMBIE to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 70.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 5.0) },
        MUMMY to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 4.0) },
        MUMMY_SKELETON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        MURLOC to Supplier { net.minecraft.world.entity.monster.Guardian.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 11.0) },
        MURLOC_WIZARD to Supplier { BedrockMob.createAttributes(CFG_MURLOC_WIZARD) },
        MUSHROOM_MONSTER to Supplier { BedrockMob.createAttributes(CFG_MUSHROOM_MONSTER) },
        MUSHROOM_ZOMBIE to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        MYSTICAL_SKELETON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        NAUTILUS to Supplier { BedrockMob.createAttributes(CFG_NAUTILUS) },
        NETHER_CREEPER to Supplier { net.minecraft.world.entity.monster.Creeper.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 7.0) },
        NETHER_GOLEM to Supplier { net.minecraft.world.entity.animal.IronGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 50.0) },
        NETHER_PHANTOM to Supplier { net.minecraft.world.entity.FlyingMob.createMobAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 6.0).add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 25.0) },
        NETHER_SKELETON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 50.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        NETHER_SKELETON_WIZARD to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0) },
        OBSIDIAN_GOLEM to Supplier { net.minecraft.world.entity.animal.IronGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 250.0) },
        PILLAGER_BY_BOSS to Supplier { net.minecraft.world.entity.monster.Pillager.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        PIRATE to Supplier { net.minecraft.world.entity.monster.Pillager.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 28.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        PLAYER_GHOST to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 4.0) },
        POLAR_WOLF to Supplier { net.minecraft.world.entity.animal.Wolf.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0) },
        PREDATORS to Supplier { net.minecraft.world.entity.ambient.Bat.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 300.0) },
        PUMPKIN_SLIME to Supplier { net.minecraft.world.entity.monster.Monster.createMonsterAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 6.0) },
        RADIATE_CREEPER to Supplier { net.minecraft.world.entity.monster.Creeper.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        RADIATE_ENDERMAN to Supplier { net.minecraft.world.entity.monster.EnderMan.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 50.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 7.0) },
        RADIATE_SKELETON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 35.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        RADIATE_SPIDER to Supplier { net.minecraft.world.entity.monster.Spider.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        REAL_SOUL to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 150.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 17.0) },
        RUMORER to Supplier { net.minecraft.world.entity.animal.Cow.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0) },
        SARDINE to Supplier { net.minecraft.world.entity.animal.AbstractFish.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 6.0) },
        SAUCER to Supplier { BedrockMob.createAttributes(CFG_SAUCER) },
        SEA_URCHIN to Supplier { BedrockMob.createAttributes(CFG_SEA_URCHIN) },
        SHADOW_ARCHER to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 23.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        SHADOW_CREEPER to Supplier { net.minecraft.world.entity.monster.Creeper.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 12.0) },
        SHADOW_OF_DEEP to Supplier { BedrockMob.createAttributes(CFG_SHADOW_OF_DEEP) },
        SHADOW_OF_SEA to Supplier { net.minecraft.world.entity.FlyingMob.createMobAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 6.0).add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 15.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 5.0) },
        SHADOW_SKELETON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0) },
        SHADOW_SKELETON_PUPPET to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 10.0) },
        SHADOW_SOUL_1 to Supplier { BedrockMob.createAttributes(CFG_SHADOW_SOUL_1) },
        SHADOW_SOUL_2 to Supplier { BedrockMob.createAttributes(CFG_SHADOW_SOUL_2) },
        SHADOW_ZOMBIE to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 55.0) },
        SHADOW_ZOMBIE_PUPPET to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 5.0) },
        SIMPLE_GLIDER to Supplier { net.minecraft.world.entity.animal.Chicken.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0) },
        SKELETON_ASSASSIN to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 45.0) },
        SKELETON_KNIGHT to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        SKELETON_KNIGHT_COMMANDER to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 150.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        SKELETON_WARRIOR to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        SKELETON_WIZARD to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0) },
        SOLDIER to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        SON_OF_CHAOS to Supplier { net.minecraft.world.entity.animal.IronGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 40.0) },
        SON_OF_NATURE to Supplier { net.minecraft.world.entity.animal.IronGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0) },
        SOUL to Supplier { net.minecraft.world.entity.monster.Vex.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 15.0) },
        SOUL_BLAZE to Supplier { net.minecraft.world.entity.monster.Blaze.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 8.0) },
        SOUL_INSECT to Supplier { net.minecraft.world.entity.monster.Silverfish.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 45.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 9.0) },
        SOUL_SKELETON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0) },
        SOUL_SOLDIER to Supplier { BedrockMob.createAttributes(CFG_SOUL_SOLDIER) },
        SOUL_ZOMBIE to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 5.0) },
        STAR to Supplier { net.minecraft.world.entity.monster.Blaze.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 55.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        STONE_GOLEM to Supplier { net.minecraft.world.entity.animal.IronGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 70.0) },
        SWAMP_DROWNED to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        SWAMP_GOLEM to Supplier { net.minecraft.world.entity.animal.IronGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 110.0) },
        TNT_CREEPER to Supplier { net.minecraft.world.entity.monster.Creeper.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 35.0) },
        TNT_SNOW_MAN to Supplier { net.minecraft.world.entity.animal.SnowGolem.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 15.0) },
        VAMPIRE_BAT to Supplier { net.minecraft.world.entity.ambient.Bat.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 1.0) },
        VAMPIRE_BAT_BY_BOSS to Supplier { net.minecraft.world.entity.ambient.Bat.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 3.0) },
        VENGEFUL_GHOST to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 6.0) },
        VINDICATOR_BY_BOSS to Supplier { net.minecraft.world.entity.monster.Vindicator.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 4.0) },
        WARPED_SKELETON to Supplier { net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 2.0) },
        WATCHER to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) },
        WEREWOLF to Supplier { BedrockMob.createAttributes(CFG_WEREWOLF) },
        ZOMBIE_FISH to Supplier { net.minecraft.world.entity.animal.AbstractFish.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 20.0) },
        ZOMBIE_SUMMONER to Supplier { net.minecraft.world.entity.monster.Zombie.createAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 30.0).add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 3.0) }
    )

    @JvmStatic
    val SPAWN_ENTRIES: List<SpawnEntry> = listOf(
        SpawnEntry(ASH_BLAZE, "MONSTER"),
        SpawnEntry(ASH_PUFFERFISH, "WATER"),
        SpawnEntry(BOMBER, "MONSTER"),
        SpawnEntry(BROWN_BEAR, "MOB"),
        SpawnEntry(CARNAGER, "MONSTER"),
        SpawnEntry(CHEST_MONSTER, "MONSTER"),
        SpawnEntry(CLAM, "WATER"),
        SpawnEntry(CRAB, "WATER"),
        SpawnEntry(CRIMSON_SLIME, "MONSTER"),
        SpawnEntry(DARK_SNOW_MAN, "MOB"),
        SpawnEntry(ENDER_SNAIL, "MONSTER"),
        SpawnEntry(ENDER_SNAKE, "MONSTER"),
        SpawnEntry(FROZEN_SPIDER, "MONSTER"),
        SpawnEntry(GOBLIN, "MONSTER"),
        SpawnEntry(ICE_ZOMBIE, "MONSTER"),
        SpawnEntry(ILLUSIONER, "MONSTER"),
        SpawnEntry(JUNGLE_SPIDER, "MONSTER"),
        SpawnEntry(LAVA_LIZARD, "MONSTER"),
        SpawnEntry(LIFE_INSECT, "MONSTER"),
        SpawnEntry(MUMMY, "MONSTER"),
        SpawnEntry(MUMMY_SKELETON, "MONSTER"),
        SpawnEntry(MURLOC, "MONSTER"),
        SpawnEntry(MUSHROOM_ZOMBIE, "MONSTER"),
        SpawnEntry(MYSTICAL_SKELETON, "MONSTER"),
        SpawnEntry(NAUTILUS, "WATER"),
        SpawnEntry(NETHER_CREEPER, "MONSTER"),
        SpawnEntry(NETHER_GOLEM, "MOB"),
        SpawnEntry(NETHER_PHANTOM, "MONSTER"),
        SpawnEntry(NETHER_SKELETON, "MONSTER"),
        SpawnEntry(OBSIDIAN_GOLEM, "MOB"),
        SpawnEntry(POLAR_WOLF, "MOB"),
        SpawnEntry(PUMPKIN_SLIME, "MONSTER"),
        SpawnEntry(RUMORER, "MONSTER"),
        SpawnEntry(SARDINE, "WATER"),
        SpawnEntry(SAUCER, "MONSTER"),
        SpawnEntry(SEA_URCHIN, "WATER"),
        SpawnEntry(SHADOW_OF_SEA, "WATER"),
        SpawnEntry(SOUL_BLAZE, "MONSTER"),
        SpawnEntry(SOUL_INSECT, "WATER"),
        SpawnEntry(SOUL_SKELETON, "MONSTER"),
        SpawnEntry(SOUL_ZOMBIE, "MONSTER"),
        SpawnEntry(STAR, "MONSTER"),
        SpawnEntry(STONE_GOLEM, "MOB"),
        SpawnEntry(SWAMP_DROWNED, "WATER"),
        SpawnEntry(SWAMP_GOLEM, "WATER"),
        SpawnEntry(VAMPIRE_BAT, "MONSTER"),
        SpawnEntry(WARPED_SKELETON, "MONSTER"),
        SpawnEntry(WEREWOLF, "MONSTER"),
        SpawnEntry(ZOMBIE_FISH, "WATER")
    )

    @JvmStatic
    fun register(modEventBus: IEventBus) {
        ENTITY_TYPES.register(modEventBus)
        SPAWN_EGGS.register(modEventBus)
    }

    @JvmStatic
    fun registerAttributes(event: EntityAttributeCreationEvent) {
        for ((type, builder) in ATTRIBUTE_ENTRIES) {
            // Builder 必须在事件回调期构建：<clinit> 时 NeoForge 扩展属性（swim_speed 等）尚未绑定会 NPE
            event.put(@Suppress("UNCHECKED_CAST") (type.get() as EntityType<out LivingEntity>), builder.get().build())
        }
    }

    @JvmStatic
    fun registerSpawnPlacements(event: RegisterSpawnPlacementsEvent) {
        for ((type, rule) in SPAWN_ENTRIES) {
            if (rule == "MONSTER") {
                val t = @Suppress("UNCHECKED_CAST") (type.get() as EntityType<Monster>)
                event.register(t, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            } else {
                val t = @Suppress("UNCHECKED_CAST") (type.get() as EntityType<Mob>)
                val placement = if (rule == "WATER") SpawnPlacementTypes.IN_WATER else SpawnPlacementTypes.ON_GROUND
                event.register(t, placement, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            }
        }
    }
}
