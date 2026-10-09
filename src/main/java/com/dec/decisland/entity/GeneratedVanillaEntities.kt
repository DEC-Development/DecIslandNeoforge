package com.dec.decisland.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ambient.Bat
import net.minecraft.world.entity.animal.Chicken
import net.minecraft.world.entity.animal.Cod
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.animal.PolarBear
import net.minecraft.world.entity.animal.Salmon
import net.minecraft.world.entity.animal.SnowGolem
import net.minecraft.world.entity.animal.Wolf
import net.minecraft.world.entity.monster.Blaze
import net.minecraft.world.entity.monster.CaveSpider
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.entity.monster.EnderMan
import net.minecraft.world.entity.monster.Evoker
import net.minecraft.world.entity.monster.Ghast
import net.minecraft.world.entity.monster.Guardian
import net.minecraft.world.entity.monster.Illusioner
import net.minecraft.world.entity.monster.Phantom
import net.minecraft.world.entity.monster.Pillager
import net.minecraft.world.entity.monster.Silverfish
import net.minecraft.world.entity.monster.Skeleton
import net.minecraft.world.entity.monster.Slime
import net.minecraft.world.entity.monster.Spider
import net.minecraft.world.entity.monster.Vex
import net.minecraft.world.entity.monster.Vindicator
import net.minecraft.world.entity.monster.Witch
import net.minecraft.world.entity.monster.WitherSkeleton
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.level.Level

// 由 tools/entity_port/generate.mjs 生成：基岩版原版基类生物（原版 AI + 自定义贴图）

class AngryChicken(entityType: EntityType<out Chicken>, level: Level) : Chicken(entityType, level) {
}

class Archer(entityType: EntityType<out Pillager>, level: Level) : Pillager(entityType, level) {
}

class AshBlaze(entityType: EntityType<out Blaze>, level: Level) : Blaze(entityType, level) {
    override fun fireImmune() = true
}

class AshPufferfish(entityType: EntityType<out Cod>, level: Level) : Cod(entityType, level) {
}

class BabyEnderDragon(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
    override fun fireImmune() = true
}

class BatCharger(entityType: EntityType<out Bat>, level: Level) : Bat(entityType, level) {
}

class BloodZombie(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class Bomber(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
    override fun fireImmune() = true
}

class BrownBear(entityType: EntityType<out PolarBear>, level: Level) : PolarBear(entityType, level) {
}

class Carnager(entityType: EntityType<out Vindicator>, level: Level) : Vindicator(entityType, level) {
}

class CrimsonSlime(entityType: EntityType<out Slime>, level: Level) : Slime(entityType, level) {
}

class DarkSnowMan(entityType: EntityType<out SnowGolem>, level: Level) : SnowGolem(entityType, level) {
}

class Doctor(entityType: EntityType<out Villager>, level: Level) : Villager(entityType, level) {
}

class ElfOfAsh(entityType: EntityType<out Vex>, level: Level) : Vex(entityType, level) {
    override fun fireImmune() = true
}

class ElfOfChaos(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {
    override fun fireImmune() = true
}

class ElfOfDeep(entityType: EntityType<out Vex>, level: Level) : Vex(entityType, level) {
    override fun fireImmune() = true
}

class ElfOfDust(entityType: EntityType<out Vex>, level: Level) : Vex(entityType, level) {
    override fun fireImmune() = true
}

class EnchantArmor(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class EnchantArmorByBoss(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class EnchantDiamondArmor(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class EnchantIllager(entityType: EntityType<out Evoker>, level: Level) : Evoker(entityType, level) {
}

class EnchantIllager1(entityType: EntityType<out Evoker>, level: Level) : Evoker(entityType, level) {
}

class EnchantIllager2(entityType: EntityType<out Vindicator>, level: Level) : Vindicator(entityType, level) {
}

class EndStoneGolem(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {
    override fun fireImmune() = true
}

class EnderWitch(entityType: EntityType<out Witch>, level: Level) : Witch(entityType, level) {
}

class EnderWitchPuppet(entityType: EntityType<out Witch>, level: Level) : Witch(entityType, level) {
}

class EverlastingWinterGhast1(entityType: EntityType<out Ghast>, level: Level) : Ghast(entityType, level) {
    override fun fireImmune() = true
}

class EvilSnowMan(entityType: EntityType<out SnowGolem>, level: Level) : SnowGolem(entityType, level) {
}

class FireStorm(entityType: EntityType<out Blaze>, level: Level) : Blaze(entityType, level) {
    override fun fireImmune() = true
}

class FriendlySpider(entityType: EntityType<out Spider>, level: Level) : Spider(entityType, level) {
}

class FrozenGhast(entityType: EntityType<out Ghast>, level: Level) : Ghast(entityType, level) {
    override fun fireImmune() = true
}

class FrozenMan(entityType: EntityType<out SnowGolem>, level: Level) : SnowGolem(entityType, level) {
}

class FrozenSpider(entityType: EntityType<out Spider>, level: Level) : Spider(entityType, level) {
}

class Gargoyle(entityType: EntityType<out Bat>, level: Level) : Bat(entityType, level) {
}

class GingerbreadManByTotem(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class IceBat(entityType: EntityType<out Bat>, level: Level) : Bat(entityType, level) {
}

class IceBlaze(entityType: EntityType<out Blaze>, level: Level) : Blaze(entityType, level) {
    override fun fireImmune() = true
}

class IceCreeper(entityType: EntityType<out Creeper>, level: Level) : Creeper(entityType, level) {
    override fun fireImmune() = true
}

class IceZombie(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class IllusionZombie(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class Illusioner(entityType: EntityType<out net.minecraft.world.entity.monster.Illusioner>, level: Level) : net.minecraft.world.entity.monster.Illusioner(entityType, level) {
}

class IllusionerPuppet(entityType: EntityType<out Illusioner>, level: Level) : Illusioner(entityType, level) {
}

class JungleSpider(entityType: EntityType<out CaveSpider>, level: Level) : CaveSpider(entityType, level) {
}

class KingOfPillager(entityType: EntityType<out Pillager>, level: Level) : Pillager(entityType, level) {
    override fun fireImmune() = true
}

class LifeInsect(entityType: EntityType<out Silverfish>, level: Level) : Silverfish(entityType, level) {
    override fun fireImmune() = true
}

class LittleBat(entityType: EntityType<out Bat>, level: Level) : Bat(entityType, level) {
}

class LurkSkeleton(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class LurkZombie(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class Mummy(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class MummySkeleton(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class Murloc(entityType: EntityType<out Guardian>, level: Level) : Guardian(entityType, level) {
}

class MushroomZombie(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class MysticalSkeleton(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class NetherCreeper(entityType: EntityType<out Creeper>, level: Level) : Creeper(entityType, level) {
    override fun fireImmune() = true
}

class NetherGolem(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {
    override fun fireImmune() = true
}

class NetherPhantom(entityType: EntityType<out Phantom>, level: Level) : Phantom(entityType, level) {
}

class NetherSkeleton(entityType: EntityType<out WitherSkeleton>, level: Level) : WitherSkeleton(entityType, level) {
    override fun fireImmune() = true
}

class NetherSkeletonWizard(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class ObsidianGolem(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {
    override fun fireImmune() = true
}

class PillagerByBoss(entityType: EntityType<out Pillager>, level: Level) : Pillager(entityType, level) {
}

class Pirate(entityType: EntityType<out Pillager>, level: Level) : Pillager(entityType, level) {
}

class PlayerGhost(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class PolarWolf(entityType: EntityType<out Wolf>, level: Level) : Wolf(entityType, level) {
}

class Predators(entityType: EntityType<out Bat>, level: Level) : Bat(entityType, level) {
    override fun fireImmune() = true
}

class PumpkinSlime(entityType: EntityType<out Slime>, level: Level) : Slime(entityType, level) {
}

class RadiateCreeper(entityType: EntityType<out Creeper>, level: Level) : Creeper(entityType, level) {
    override fun fireImmune() = true
}

class RadiateEnderman(entityType: EntityType<out EnderMan>, level: Level) : EnderMan(entityType, level) {
}

class RadiateSkeleton(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
    override fun fireImmune() = true
}

class RadiateSpider(entityType: EntityType<out CaveSpider>, level: Level) : CaveSpider(entityType, level) {
}

class RealSoul(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class Rumorer(entityType: EntityType<out Cow>, level: Level) : Cow(entityType, level) {
}

class Sardine(entityType: EntityType<out Cod>, level: Level) : Cod(entityType, level) {
}

class ShadowArcher(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class ShadowCreeper(entityType: EntityType<out Creeper>, level: Level) : Creeper(entityType, level) {
}

class ShadowOfSea(entityType: EntityType<out Phantom>, level: Level) : Phantom(entityType, level) {
}

class ShadowSkeleton(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class ShadowSkeletonPuppet(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class ShadowZombie(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class ShadowZombiePuppet(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class SimpleGlider(entityType: EntityType<out Chicken>, level: Level) : Chicken(entityType, level) {
}

class SkeletonAssassin(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class SkeletonKnight(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class SkeletonKnightCommander(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class SkeletonWarrior(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
    override fun fireImmune() = true
}

class SkeletonWizard(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class Soldier(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class SonOfChaos(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {
}

class SonOfNature(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {
}

class Soul(entityType: EntityType<out Vex>, level: Level) : Vex(entityType, level) {
    override fun fireImmune() = true
}

class SoulBlaze(entityType: EntityType<out Blaze>, level: Level) : Blaze(entityType, level) {
    override fun fireImmune() = true
}

class SoulInsect(entityType: EntityType<out Silverfish>, level: Level) : Silverfish(entityType, level) {
    override fun fireImmune() = true
}

class SoulSkeleton(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
}

class SoulZombie(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class Star(entityType: EntityType<out Blaze>, level: Level) : Blaze(entityType, level) {
    override fun fireImmune() = true
}

class StoneGolem(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {
}

class SwampDrowned(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class SwampGolem(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {
    override fun fireImmune() = true
}

class TntCreeper(entityType: EntityType<out Creeper>, level: Level) : Creeper(entityType, level) {
    override fun fireImmune() = true
}

class TntSnowMan(entityType: EntityType<out SnowGolem>, level: Level) : SnowGolem(entityType, level) {
}

class VampireBat(entityType: EntityType<out Bat>, level: Level) : Bat(entityType, level) {
}

class VampireBatByBoss(entityType: EntityType<out Bat>, level: Level) : Bat(entityType, level) {
}

class VengefulGhost(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class VindicatorByBoss(entityType: EntityType<out Vindicator>, level: Level) : Vindicator(entityType, level) {
}

class WarpedSkeleton(entityType: EntityType<out Skeleton>, level: Level) : Skeleton(entityType, level) {
    override fun fireImmune() = true
}

class Watcher(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}

class ZombieFish(entityType: EntityType<out Salmon>, level: Level) : Salmon(entityType, level) {
}

class ZombieSummoner(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {
}
