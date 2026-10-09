package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.GeneratedMobs
import com.dec.decisland.entity.custom.BedrockMob
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.client.renderer.entity.BatRenderer
import net.minecraft.client.renderer.entity.BlazeRenderer
import net.minecraft.client.renderer.entity.CaveSpiderRenderer
import net.minecraft.client.renderer.entity.ChickenRenderer
import net.minecraft.client.renderer.entity.CodRenderer
import net.minecraft.client.renderer.entity.CowRenderer
import net.minecraft.client.renderer.entity.CreeperRenderer
import net.minecraft.client.renderer.entity.EndermanRenderer
import net.minecraft.client.renderer.entity.EvokerRenderer
import net.minecraft.client.renderer.entity.GhastRenderer
import net.minecraft.client.renderer.entity.GuardianRenderer
import net.minecraft.client.renderer.entity.IllusionerRenderer
import net.minecraft.client.renderer.entity.IronGolemRenderer
import net.minecraft.client.renderer.entity.PhantomRenderer
import net.minecraft.client.renderer.entity.PillagerRenderer
import net.minecraft.client.renderer.entity.PolarBearRenderer
import net.minecraft.client.renderer.entity.SalmonRenderer
import net.minecraft.client.renderer.entity.SilverfishRenderer
import net.minecraft.client.renderer.entity.SkeletonRenderer
import net.minecraft.client.renderer.entity.SlimeRenderer
import net.minecraft.client.renderer.entity.SnowGolemRenderer
import net.minecraft.client.renderer.entity.SpiderRenderer
import net.minecraft.client.renderer.entity.VexRenderer
import net.minecraft.client.renderer.entity.VillagerRenderer
import net.minecraft.client.renderer.entity.VindicatorRenderer
import net.minecraft.client.renderer.entity.WitchRenderer
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer
import net.minecraft.client.renderer.entity.WolfRenderer
import net.minecraft.client.renderer.entity.ZombieRenderer
import net.minecraft.world.entity.ambient.Bat
import net.minecraft.world.entity.animal.Chicken
import net.minecraft.world.entity.animal.Cod
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.animal.PolarBear
import net.minecraft.world.entity.animal.Salmon
import net.minecraft.world.entity.animal.SnowGolem
import net.minecraft.world.entity.animal.Wolf
import net.minecraft.world.entity.monster.AbstractSkeleton
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
import net.minecraft.world.entity.monster.SpellcasterIllager
import net.minecraft.world.entity.monster.Spider
import net.minecraft.world.entity.monster.Vex
import net.minecraft.world.entity.monster.Vindicator
import net.minecraft.world.entity.monster.Witch
import net.minecraft.world.entity.monster.WitherSkeleton
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.npc.Villager

/** 由 tools/entity_port/generate.mjs 生成：批量实体渲染器（原版模型换贴图 + 基岩模型通用渲染） */
class AngryChickenGeneratedRenderer(context: EntityRendererProvider.Context) : ChickenRenderer(context) {
    override fun getTextureLocation(entity: Chicken): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/angry_chicken.png")
    }
}
class ArcherGeneratedRenderer(context: EntityRendererProvider.Context) : PillagerRenderer(context) {
    override fun getTextureLocation(entity: Pillager): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/archer.png")
    }
}
class AshBlazeGeneratedRenderer(context: EntityRendererProvider.Context) : BlazeRenderer(context) {
    override fun getTextureLocation(entity: Blaze): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/ash_blaze.png")
    }
}
class AshPufferfishGeneratedRenderer(context: EntityRendererProvider.Context) : CodRenderer(context) {
    override fun getTextureLocation(entity: Cod): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/ash_pufferfish.png")
    }
}
class BabyEnderDragonGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/baby_ender_dragon.png")
    }
}
class BatChargerGeneratedRenderer(context: EntityRendererProvider.Context) : BatRenderer(context) {
    override fun getTextureLocation(entity: Bat): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/bat_charger.png")
    }
}
class BloodZombieGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/blood_zombie.png")
    }
}
class BomberGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/bomber.png")
    }
}
class BrownBearGeneratedRenderer(context: EntityRendererProvider.Context) : PolarBearRenderer(context) {
    override fun getTextureLocation(entity: PolarBear): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/brown_bear.png")
    }
}
class CarnagerGeneratedRenderer(context: EntityRendererProvider.Context) : VindicatorRenderer(context) {
    override fun getTextureLocation(entity: Vindicator): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/carnager.png")
    }
}
class CrimsonSlimeGeneratedRenderer(context: EntityRendererProvider.Context) : SlimeRenderer(context) {
    override fun getTextureLocation(entity: Slime): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/crimson_slime.png")
    }
}
class DarkSnowManGeneratedRenderer(context: EntityRendererProvider.Context) : SnowGolemRenderer(context) {
    override fun getTextureLocation(entity: SnowGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/dark_snow_man.png")
    }
}
class DoctorGeneratedRenderer(context: EntityRendererProvider.Context) : VillagerRenderer(context) {
    override fun getTextureLocation(entity: Villager): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/doctor.png")
    }
}
class ElfOfAshGeneratedRenderer(context: EntityRendererProvider.Context) : VexRenderer(context) {
    override fun getTextureLocation(entity: Vex): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/elf_of_ash.png")
    }
}
class ElfOfChaosGeneratedRenderer(context: EntityRendererProvider.Context) : IronGolemRenderer(context) {
    override fun getTextureLocation(entity: IronGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/elf_of_chaos.png")
    }
}
class ElfOfDeepGeneratedRenderer(context: EntityRendererProvider.Context) : VexRenderer(context) {
    override fun getTextureLocation(entity: Vex): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/elf_of_deep.png")
    }
}
class ElfOfDustGeneratedRenderer(context: EntityRendererProvider.Context) : VexRenderer(context) {
    override fun getTextureLocation(entity: Vex): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/elf_of_dust.png")
    }
}
class EnchantArmorGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/enchant_armor.png")
    }
}
class EnchantArmorByBossGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/enchant_armor_by_boss.png")
    }
}
class EnchantDiamondArmorGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/enchant_diamond_armor.png")
    }
}
class EnchantIllagerGeneratedRenderer(context: EntityRendererProvider.Context) : EvokerRenderer<SpellcasterIllager>(context) {
    override fun getTextureLocation(entity: SpellcasterIllager): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/enchant_illager.png")
    }
}
class EnchantIllager1GeneratedRenderer(context: EntityRendererProvider.Context) : EvokerRenderer<SpellcasterIllager>(context) {
    override fun getTextureLocation(entity: SpellcasterIllager): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/enchant_illager_1.png")
    }
}
class EnchantIllager2GeneratedRenderer(context: EntityRendererProvider.Context) : VindicatorRenderer(context) {
    override fun getTextureLocation(entity: Vindicator): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/enchant_illager_2.png")
    }
}
class EndStoneGolemGeneratedRenderer(context: EntityRendererProvider.Context) : IronGolemRenderer(context) {
    override fun getTextureLocation(entity: IronGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/end_stone_golem.png")
    }
}
class EnderWitchGeneratedRenderer(context: EntityRendererProvider.Context) : WitchRenderer(context) {
    override fun getTextureLocation(entity: Witch): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/ender_witch.png")
    }
}
class EnderWitchPuppetGeneratedRenderer(context: EntityRendererProvider.Context) : WitchRenderer(context) {
    override fun getTextureLocation(entity: Witch): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/ender_witch_puppet.png")
    }
}
class EverlastingWinterGhast1GeneratedRenderer(context: EntityRendererProvider.Context) : GhastRenderer(context) {
    override fun getTextureLocation(entity: Ghast): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/everlasting_winter_ghast_1.png")
    }
}
class EvilSnowManGeneratedRenderer(context: EntityRendererProvider.Context) : SnowGolemRenderer(context) {
    override fun getTextureLocation(entity: SnowGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/evil_snow_man.png")
    }
}
class FireStormGeneratedRenderer(context: EntityRendererProvider.Context) : BlazeRenderer(context) {
    override fun getTextureLocation(entity: Blaze): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/fire_storm.png")
    }
}
class FriendlySpiderGeneratedRenderer(context: EntityRendererProvider.Context) : SpiderRenderer<Spider>(context) {
    override fun getTextureLocation(entity: Spider): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/friendly_spider.png")
    }
}
class FrozenGhastGeneratedRenderer(context: EntityRendererProvider.Context) : GhastRenderer(context) {
    override fun getTextureLocation(entity: Ghast): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/frozen_ghast.png")
    }
}
class FrozenManGeneratedRenderer(context: EntityRendererProvider.Context) : SnowGolemRenderer(context) {
    override fun getTextureLocation(entity: SnowGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/frozen_man.png")
    }
}
class FrozenSpiderGeneratedRenderer(context: EntityRendererProvider.Context) : SpiderRenderer<Spider>(context) {
    override fun getTextureLocation(entity: Spider): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/frozen_spider.png")
    }
}
class GargoyleGeneratedRenderer(context: EntityRendererProvider.Context) : BatRenderer(context) {
    override fun getTextureLocation(entity: Bat): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/gargoyle.png")
    }
}
class GingerbreadManByTotemGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
}
class IceBatGeneratedRenderer(context: EntityRendererProvider.Context) : BatRenderer(context) {
    override fun getTextureLocation(entity: Bat): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/ice_bat.png")
    }
}
class IceBlazeGeneratedRenderer(context: EntityRendererProvider.Context) : BlazeRenderer(context) {
    override fun getTextureLocation(entity: Blaze): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/ice_blaze.png")
    }
}
class IceCreeperGeneratedRenderer(context: EntityRendererProvider.Context) : CreeperRenderer(context) {
    override fun getTextureLocation(entity: Creeper): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/ice_creeper.png")
    }
}
class IceZombieGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/ice_zombie.png")
    }
}
class IllusionZombieGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/illusion_zombie.png")
    }
}
class IllusionerGeneratedRenderer(context: EntityRendererProvider.Context) : IllusionerRenderer(context) {
    override fun getTextureLocation(entity: Illusioner): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/illusioner.png")
    }
}
class IllusionerPuppetGeneratedRenderer(context: EntityRendererProvider.Context) : IllusionerRenderer(context) {
    override fun getTextureLocation(entity: Illusioner): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/illusioner_puppet.png")
    }
}
class JungleSpiderGeneratedRenderer(context: EntityRendererProvider.Context) : CaveSpiderRenderer(context) {
    override fun getTextureLocation(entity: CaveSpider): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/jungle_spider.png")
    }
}
class KingOfPillagerGeneratedRenderer(context: EntityRendererProvider.Context) : PillagerRenderer(context) {
    override fun getTextureLocation(entity: Pillager): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/king_of_pillager.png")
    }
}
class LifeInsectGeneratedRenderer(context: EntityRendererProvider.Context) : SilverfishRenderer(context) {
    override fun getTextureLocation(entity: Silverfish): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/life_insect.png")
    }
}
class LittleBatGeneratedRenderer(context: EntityRendererProvider.Context) : BatRenderer(context) {
    override fun getTextureLocation(entity: Bat): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/little_bat.png")
    }
}
class LurkSkeletonGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/lurk_skeleton.png")
    }
}
class LurkZombieGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/lurk_zombie.png")
    }
}
class MummyGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/mummy.png")
    }
}
class MummySkeletonGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/mummy_skeleton.png")
    }
}
class MurlocGeneratedRenderer(context: EntityRendererProvider.Context) : GuardianRenderer(context) {
    override fun getTextureLocation(entity: Guardian): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/murloc.png")
    }
}
class MushroomZombieGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/mushroom_zombie.png")
    }
}
class MysticalSkeletonGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/mystical_skeleton.png")
    }
}
class NetherCreeperGeneratedRenderer(context: EntityRendererProvider.Context) : CreeperRenderer(context) {
    override fun getTextureLocation(entity: Creeper): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/nether_creeper.png")
    }
}
class NetherGolemGeneratedRenderer(context: EntityRendererProvider.Context) : IronGolemRenderer(context) {
    override fun getTextureLocation(entity: IronGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/nether_golem.png")
    }
}
class NetherPhantomGeneratedRenderer(context: EntityRendererProvider.Context) : PhantomRenderer(context) {
    override fun getTextureLocation(entity: Phantom): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/nether_phantom.png")
    }
}
class NetherSkeletonGeneratedRenderer(context: EntityRendererProvider.Context) : WitherSkeletonRenderer(context) {
    override fun getTextureLocation(entity: WitherSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/nether_skeleton.png")
    }
}
class NetherSkeletonWizardGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/nether_skeleton_wizard.png")
    }
}
class ObsidianGolemGeneratedRenderer(context: EntityRendererProvider.Context) : IronGolemRenderer(context) {
    override fun getTextureLocation(entity: IronGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/obsidian_golem.png")
    }
}
class PillagerByBossGeneratedRenderer(context: EntityRendererProvider.Context) : PillagerRenderer(context) {
    override fun getTextureLocation(entity: Pillager): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/pillager_by_boss.png")
    }
}
class PirateGeneratedRenderer(context: EntityRendererProvider.Context) : PillagerRenderer(context) {
    override fun getTextureLocation(entity: Pillager): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/pirate.png")
    }
}
class PlayerGhostGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/player_ghost.png")
    }
}
class PolarWolfGeneratedRenderer(context: EntityRendererProvider.Context) : WolfRenderer(context) {
    override fun getTextureLocation(entity: Wolf): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/polar_wolf.png")
    }
}
class PredatorsGeneratedRenderer(context: EntityRendererProvider.Context) : BatRenderer(context) {
    override fun getTextureLocation(entity: Bat): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/predators.png")
    }
}
class PumpkinSlimeGeneratedRenderer(context: EntityRendererProvider.Context) : SlimeRenderer(context) {
    override fun getTextureLocation(entity: Slime): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/pumpkin_slime.png")
    }
}
class RadiateCreeperGeneratedRenderer(context: EntityRendererProvider.Context) : CreeperRenderer(context) {
    override fun getTextureLocation(entity: Creeper): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/radiate_creeper.png")
    }
}
class RadiateEndermanGeneratedRenderer(context: EntityRendererProvider.Context) : EndermanRenderer(context) {
    override fun getTextureLocation(entity: EnderMan): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/radiate_enderman.png")
    }
}
class RadiateSkeletonGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/radiate_skeleton.png")
    }
}
class RadiateSpiderGeneratedRenderer(context: EntityRendererProvider.Context) : CaveSpiderRenderer(context) {
    override fun getTextureLocation(entity: CaveSpider): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/radiate_spider.png")
    }
}
class RealSoulGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/real_soul.png")
    }
}
class RumorerGeneratedRenderer(context: EntityRendererProvider.Context) : CowRenderer(context) {
    override fun getTextureLocation(entity: Cow): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/rumorer.png")
    }
}
class SardineGeneratedRenderer(context: EntityRendererProvider.Context) : CodRenderer(context) {
    override fun getTextureLocation(entity: Cod): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/sardine.png")
    }
}
class ShadowArcherGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/shadow_archer.png")
    }
}
class ShadowCreeperGeneratedRenderer(context: EntityRendererProvider.Context) : CreeperRenderer(context) {
    override fun getTextureLocation(entity: Creeper): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/shadow_creeper.png")
    }
}
class ShadowOfSeaGeneratedRenderer(context: EntityRendererProvider.Context) : PhantomRenderer(context) {
    override fun getTextureLocation(entity: Phantom): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/shadow_of_sea.png")
    }
}
class ShadowSkeletonGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/shadow_skeleton.png")
    }
}
class ShadowSkeletonPuppetGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/shadow_skeleton_puppet.png")
    }
}
class ShadowZombieGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/shadow_zombie.png")
    }
}
class ShadowZombiePuppetGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/shadow_zombie_puppet.png")
    }
}
class SimpleGliderGeneratedRenderer(context: EntityRendererProvider.Context) : ChickenRenderer(context) {
    override fun getTextureLocation(entity: Chicken): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/simple_glider.png")
    }
}
class SkeletonAssassinGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/skeleton_assassin.png")
    }
}
class SkeletonKnightGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/skeleton_knight.png")
    }
}
class SkeletonKnightCommanderGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/skeleton_knight_commander.png")
    }
}
class SkeletonWarriorGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/skeleton_warrior.png")
    }
}
class SkeletonWizardGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/skeleton_wizard.png")
    }
}
class SoldierGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
}
class SonOfChaosGeneratedRenderer(context: EntityRendererProvider.Context) : IronGolemRenderer(context) {
    override fun getTextureLocation(entity: IronGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/son_of_chaos.png")
    }
}
class SonOfNatureGeneratedRenderer(context: EntityRendererProvider.Context) : IronGolemRenderer(context) {
    override fun getTextureLocation(entity: IronGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/son_of_nature.png")
    }
}
class SoulGeneratedRenderer(context: EntityRendererProvider.Context) : VexRenderer(context) {
    override fun getTextureLocation(entity: Vex): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/soul.png")
    }
}
class SoulBlazeGeneratedRenderer(context: EntityRendererProvider.Context) : BlazeRenderer(context) {
    override fun getTextureLocation(entity: Blaze): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/soul_blaze.png")
    }
}
class SoulInsectGeneratedRenderer(context: EntityRendererProvider.Context) : SilverfishRenderer(context) {
    override fun getTextureLocation(entity: Silverfish): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/soul_insect.png")
    }
}
class SoulSkeletonGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/soul_skeleton.png")
    }
}
class SoulZombieGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/soul_zombie.png")
    }
}
class StarGeneratedRenderer(context: EntityRendererProvider.Context) : BlazeRenderer(context) {
    override fun getTextureLocation(entity: Blaze): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/star.png")
    }
}
class StoneGolemGeneratedRenderer(context: EntityRendererProvider.Context) : IronGolemRenderer(context) {
    override fun getTextureLocation(entity: IronGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/stone_golem.png")
    }
}
class SwampDrownedGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/swamp_drowned.png")
    }
}
class SwampGolemGeneratedRenderer(context: EntityRendererProvider.Context) : IronGolemRenderer(context) {
    override fun getTextureLocation(entity: IronGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/swamp_golem.png")
    }
}
class TntCreeperGeneratedRenderer(context: EntityRendererProvider.Context) : CreeperRenderer(context) {
    override fun getTextureLocation(entity: Creeper): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/tnt_creeper.png")
    }
}
class TntSnowManGeneratedRenderer(context: EntityRendererProvider.Context) : SnowGolemRenderer(context) {
    override fun getTextureLocation(entity: SnowGolem): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/tnt_snow_man.png")
    }
}
class VampireBatGeneratedRenderer(context: EntityRendererProvider.Context) : BatRenderer(context) {
    override fun getTextureLocation(entity: Bat): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/vampire_bat.png")
    }
}
class VampireBatByBossGeneratedRenderer(context: EntityRendererProvider.Context) : BatRenderer(context) {
    override fun getTextureLocation(entity: Bat): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/vampire_bat_by_boss.png")
    }
}
class VengefulGhostGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/vengeful_ghost.png")
    }
}
class VindicatorByBossGeneratedRenderer(context: EntityRendererProvider.Context) : VindicatorRenderer(context) {
    override fun getTextureLocation(entity: Vindicator): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/vindicator_by_boss.png")
    }
}
class WarpedSkeletonGeneratedRenderer(context: EntityRendererProvider.Context) : SkeletonRenderer<AbstractSkeleton>(context) {
    override fun getTextureLocation(entity: AbstractSkeleton): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/warped_skeleton.png")
    }
}
class WatcherGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/watcher.png")
    }
}
class ZombieFishGeneratedRenderer(context: EntityRendererProvider.Context) : SalmonRenderer(context) {
    override fun getTextureLocation(entity: Salmon): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/zombie_fish.png")
    }
}
class ZombieSummonerGeneratedRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE
    companion object {
        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/zombie_summoner.png")
    }
}
object GeneratedMobClient {
    @JvmStatic
    fun registerAll() {
        EntityRenderers.register<Chicken>(GeneratedMobs.ANGRY_CHICKEN.get(), ::AngryChickenGeneratedRenderer)
        EntityRenderers.register<Pillager>(GeneratedMobs.ARCHER.get(), ::ArcherGeneratedRenderer)
        EntityRenderers.register<Blaze>(GeneratedMobs.ASH_BLAZE.get(), ::AshBlazeGeneratedRenderer)
        EntityRenderers.register<Cod>(GeneratedMobs.ASH_PUFFERFISH.get(), ::AshPufferfishGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.BABY_ENDER_DRAGON.get(), ::BabyEnderDragonGeneratedRenderer)
        EntityRenderers.register<Bat>(GeneratedMobs.BAT_CHARGER.get(), ::BatChargerGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.BLOOD_ZOMBIE.get(), ::BloodZombieGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.BOMBER.get(), ::BomberGeneratedRenderer)
        EntityRenderers.register<PolarBear>(GeneratedMobs.BROWN_BEAR.get(), ::BrownBearGeneratedRenderer)
        EntityRenderers.register<Vindicator>(GeneratedMobs.CARNAGER.get(), ::CarnagerGeneratedRenderer)
        EntityRenderers.register<Slime>(GeneratedMobs.CRIMSON_SLIME.get(), ::CrimsonSlimeGeneratedRenderer)
        EntityRenderers.register<SnowGolem>(GeneratedMobs.DARK_SNOW_MAN.get(), ::DarkSnowManGeneratedRenderer)
        EntityRenderers.register<Villager>(GeneratedMobs.DOCTOR.get(), ::DoctorGeneratedRenderer)
        EntityRenderers.register<Vex>(GeneratedMobs.ELF_OF_ASH.get(), ::ElfOfAshGeneratedRenderer)
        EntityRenderers.register<IronGolem>(GeneratedMobs.ELF_OF_CHAOS.get(), ::ElfOfChaosGeneratedRenderer)
        EntityRenderers.register<Vex>(GeneratedMobs.ELF_OF_DEEP.get(), ::ElfOfDeepGeneratedRenderer)
        EntityRenderers.register<Vex>(GeneratedMobs.ELF_OF_DUST.get(), ::ElfOfDustGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.ENCHANT_ARMOR.get(), ::EnchantArmorGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.ENCHANT_ARMOR_BY_BOSS.get(), ::EnchantArmorByBossGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.ENCHANT_DIAMOND_ARMOR.get(), ::EnchantDiamondArmorGeneratedRenderer)
        EntityRenderers.register<SpellcasterIllager>(GeneratedMobs.ENCHANT_ILLAGER.get(), ::EnchantIllagerGeneratedRenderer)
        EntityRenderers.register<SpellcasterIllager>(GeneratedMobs.ENCHANT_ILLAGER_1.get(), ::EnchantIllager1GeneratedRenderer)
        EntityRenderers.register<Vindicator>(GeneratedMobs.ENCHANT_ILLAGER_2.get(), ::EnchantIllager2GeneratedRenderer)
        EntityRenderers.register<IronGolem>(GeneratedMobs.END_STONE_GOLEM.get(), ::EndStoneGolemGeneratedRenderer)
        EntityRenderers.register<Witch>(GeneratedMobs.ENDER_WITCH.get(), ::EnderWitchGeneratedRenderer)
        EntityRenderers.register<Witch>(GeneratedMobs.ENDER_WITCH_PUPPET.get(), ::EnderWitchPuppetGeneratedRenderer)
        EntityRenderers.register<Ghast>(GeneratedMobs.EVERLASTING_WINTER_GHAST_1.get(), ::EverlastingWinterGhast1GeneratedRenderer)
        EntityRenderers.register<SnowGolem>(GeneratedMobs.EVIL_SNOW_MAN.get(), ::EvilSnowManGeneratedRenderer)
        EntityRenderers.register<Blaze>(GeneratedMobs.FIRE_STORM.get(), ::FireStormGeneratedRenderer)
        EntityRenderers.register<Spider>(GeneratedMobs.FRIENDLY_SPIDER.get(), ::FriendlySpiderGeneratedRenderer)
        EntityRenderers.register<Ghast>(GeneratedMobs.FROZEN_GHAST.get(), ::FrozenGhastGeneratedRenderer)
        EntityRenderers.register<SnowGolem>(GeneratedMobs.FROZEN_MAN.get(), ::FrozenManGeneratedRenderer)
        EntityRenderers.register<Spider>(GeneratedMobs.FROZEN_SPIDER.get(), ::FrozenSpiderGeneratedRenderer)
        EntityRenderers.register<Bat>(GeneratedMobs.GARGOYLE.get(), ::GargoyleGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.GINGERBREAD_MAN_BY_TOTEM.get(), ::GingerbreadManByTotemGeneratedRenderer)
        EntityRenderers.register<Bat>(GeneratedMobs.ICE_BAT.get(), ::IceBatGeneratedRenderer)
        EntityRenderers.register<Blaze>(GeneratedMobs.ICE_BLAZE.get(), ::IceBlazeGeneratedRenderer)
        EntityRenderers.register<Creeper>(GeneratedMobs.ICE_CREEPER.get(), ::IceCreeperGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.ICE_ZOMBIE.get(), ::IceZombieGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.ILLUSION_ZOMBIE.get(), ::IllusionZombieGeneratedRenderer)
        EntityRenderers.register<Illusioner>(GeneratedMobs.ILLUSIONER.get(), ::IllusionerGeneratedRenderer)
        EntityRenderers.register<Illusioner>(GeneratedMobs.ILLUSIONER_PUPPET.get(), ::IllusionerPuppetGeneratedRenderer)
        EntityRenderers.register<CaveSpider>(GeneratedMobs.JUNGLE_SPIDER.get(), ::JungleSpiderGeneratedRenderer)
        EntityRenderers.register<Pillager>(GeneratedMobs.KING_OF_PILLAGER.get(), ::KingOfPillagerGeneratedRenderer)
        EntityRenderers.register<Silverfish>(GeneratedMobs.LIFE_INSECT.get(), ::LifeInsectGeneratedRenderer)
        EntityRenderers.register<Bat>(GeneratedMobs.LITTLE_BAT.get(), ::LittleBatGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.LURK_SKELETON.get(), ::LurkSkeletonGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.LURK_ZOMBIE.get(), ::LurkZombieGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.MUMMY.get(), ::MummyGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.MUMMY_SKELETON.get(), ::MummySkeletonGeneratedRenderer)
        EntityRenderers.register<Guardian>(GeneratedMobs.MURLOC.get(), ::MurlocGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.MUSHROOM_ZOMBIE.get(), ::MushroomZombieGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.MYSTICAL_SKELETON.get(), ::MysticalSkeletonGeneratedRenderer)
        EntityRenderers.register<Creeper>(GeneratedMobs.NETHER_CREEPER.get(), ::NetherCreeperGeneratedRenderer)
        EntityRenderers.register<IronGolem>(GeneratedMobs.NETHER_GOLEM.get(), ::NetherGolemGeneratedRenderer)
        EntityRenderers.register<Phantom>(GeneratedMobs.NETHER_PHANTOM.get(), ::NetherPhantomGeneratedRenderer)
        EntityRenderers.register<WitherSkeleton>(GeneratedMobs.NETHER_SKELETON.get(), ::NetherSkeletonGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.NETHER_SKELETON_WIZARD.get(), ::NetherSkeletonWizardGeneratedRenderer)
        EntityRenderers.register<IronGolem>(GeneratedMobs.OBSIDIAN_GOLEM.get(), ::ObsidianGolemGeneratedRenderer)
        EntityRenderers.register<Pillager>(GeneratedMobs.PILLAGER_BY_BOSS.get(), ::PillagerByBossGeneratedRenderer)
        EntityRenderers.register<Pillager>(GeneratedMobs.PIRATE.get(), ::PirateGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.PLAYER_GHOST.get(), ::PlayerGhostGeneratedRenderer)
        EntityRenderers.register<Wolf>(GeneratedMobs.POLAR_WOLF.get(), ::PolarWolfGeneratedRenderer)
        EntityRenderers.register<Bat>(GeneratedMobs.PREDATORS.get(), ::PredatorsGeneratedRenderer)
        EntityRenderers.register<Slime>(GeneratedMobs.PUMPKIN_SLIME.get(), ::PumpkinSlimeGeneratedRenderer)
        EntityRenderers.register<Creeper>(GeneratedMobs.RADIATE_CREEPER.get(), ::RadiateCreeperGeneratedRenderer)
        EntityRenderers.register<EnderMan>(GeneratedMobs.RADIATE_ENDERMAN.get(), ::RadiateEndermanGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.RADIATE_SKELETON.get(), ::RadiateSkeletonGeneratedRenderer)
        EntityRenderers.register<CaveSpider>(GeneratedMobs.RADIATE_SPIDER.get(), ::RadiateSpiderGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.REAL_SOUL.get(), ::RealSoulGeneratedRenderer)
        EntityRenderers.register<Cow>(GeneratedMobs.RUMORER.get(), ::RumorerGeneratedRenderer)
        EntityRenderers.register<Cod>(GeneratedMobs.SARDINE.get(), ::SardineGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SHADOW_ARCHER.get(), ::ShadowArcherGeneratedRenderer)
        EntityRenderers.register<Creeper>(GeneratedMobs.SHADOW_CREEPER.get(), ::ShadowCreeperGeneratedRenderer)
        EntityRenderers.register<Phantom>(GeneratedMobs.SHADOW_OF_SEA.get(), ::ShadowOfSeaGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SHADOW_SKELETON.get(), ::ShadowSkeletonGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SHADOW_SKELETON_PUPPET.get(), ::ShadowSkeletonPuppetGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.SHADOW_ZOMBIE.get(), ::ShadowZombieGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.SHADOW_ZOMBIE_PUPPET.get(), ::ShadowZombiePuppetGeneratedRenderer)
        EntityRenderers.register<Chicken>(GeneratedMobs.SIMPLE_GLIDER.get(), ::SimpleGliderGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SKELETON_ASSASSIN.get(), ::SkeletonAssassinGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SKELETON_KNIGHT.get(), ::SkeletonKnightGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SKELETON_KNIGHT_COMMANDER.get(), ::SkeletonKnightCommanderGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SKELETON_WARRIOR.get(), ::SkeletonWarriorGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SKELETON_WIZARD.get(), ::SkeletonWizardGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.SOLDIER.get(), ::SoldierGeneratedRenderer)
        EntityRenderers.register<IronGolem>(GeneratedMobs.SON_OF_CHAOS.get(), ::SonOfChaosGeneratedRenderer)
        EntityRenderers.register<IronGolem>(GeneratedMobs.SON_OF_NATURE.get(), ::SonOfNatureGeneratedRenderer)
        EntityRenderers.register<Vex>(GeneratedMobs.SOUL.get(), ::SoulGeneratedRenderer)
        EntityRenderers.register<Blaze>(GeneratedMobs.SOUL_BLAZE.get(), ::SoulBlazeGeneratedRenderer)
        EntityRenderers.register<Silverfish>(GeneratedMobs.SOUL_INSECT.get(), ::SoulInsectGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.SOUL_SKELETON.get(), ::SoulSkeletonGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.SOUL_ZOMBIE.get(), ::SoulZombieGeneratedRenderer)
        EntityRenderers.register<Blaze>(GeneratedMobs.STAR.get(), ::StarGeneratedRenderer)
        EntityRenderers.register<IronGolem>(GeneratedMobs.STONE_GOLEM.get(), ::StoneGolemGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.SWAMP_DROWNED.get(), ::SwampDrownedGeneratedRenderer)
        EntityRenderers.register<IronGolem>(GeneratedMobs.SWAMP_GOLEM.get(), ::SwampGolemGeneratedRenderer)
        EntityRenderers.register<Creeper>(GeneratedMobs.TNT_CREEPER.get(), ::TntCreeperGeneratedRenderer)
        EntityRenderers.register<SnowGolem>(GeneratedMobs.TNT_SNOW_MAN.get(), ::TntSnowManGeneratedRenderer)
        EntityRenderers.register<Bat>(GeneratedMobs.VAMPIRE_BAT.get(), ::VampireBatGeneratedRenderer)
        EntityRenderers.register<Bat>(GeneratedMobs.VAMPIRE_BAT_BY_BOSS.get(), ::VampireBatByBossGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.VENGEFUL_GHOST.get(), ::VengefulGhostGeneratedRenderer)
        EntityRenderers.register<Vindicator>(GeneratedMobs.VINDICATOR_BY_BOSS.get(), ::VindicatorByBossGeneratedRenderer)
        EntityRenderers.register<AbstractSkeleton>(GeneratedMobs.WARPED_SKELETON.get(), ::WarpedSkeletonGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.WATCHER.get(), ::WatcherGeneratedRenderer)
        EntityRenderers.register<Salmon>(GeneratedMobs.ZOMBIE_FISH.get(), ::ZombieFishGeneratedRenderer)
        EntityRenderers.register<Zombie>(GeneratedMobs.ZOMBIE_SUMMONER.get(), ::ZombieSummonerGeneratedRenderer)
        EntityRenderers.register(GeneratedMobs.ABYSSAL_CONTROLLER.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("abyssal_controller"), anim("abyssal_controller"), "idle", "walk", tex("abyssal_controller"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ABYSSAL_SHADOW.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("abyssal_shadow"), anim("abyssal_shadow"), "idle", "walk", tex("abyssal_shadow"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ASH_BOMB.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ash_bomb"), anim("ash_bomb"), "idle", "walk", tex("ash_bomb"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ASH_HORSE.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ash_horse"), anim("ash_horse"), "idle", "walk", tex("ash_horse"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ASH_HORSE_HEAD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ash_horse_head"), anim("ash_horse_head"), "idle", "walk", tex("ash_horse_head"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ASH_KNIGHT.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ash_knight"), anim("ash_knight"), "idle", "walk", tex("ash_knight"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ASH_KNIGHT_HEAD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ash_knight_head"), anim("ash_knight_head"), "idle", "walk", tex("ash_knight_head"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ASH_SWORD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ash_sword"), anim("ash_sword"), "idle", "walk", tex("ash_sword"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ASH_SWORD_PHANTOM.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ash_sword_phantom"), anim("ash_sword_phantom"), "idle", "walk", tex("ash_sword_phantom"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.BLACKSTONE_THORN.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("blackstone_thorn"), anim("blackstone_thorn"), "idle", "walk", tex("blackstone_thorn"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.CHEST_MONSTER.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("chest_monster"), anim("chest_monster"), "idle", "walk", tex("chest_monster"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.CHESTER.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("chester"), anim("chester"), "idle", "walk", tex("chester"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.CLAM.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("clam"), anim("clam"), "idle", "walk", tex("clam"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.CRAB.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("crab"), anim("crab"), "idle", "walk", tex("crab"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.DARK_WEREWOLF.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("dark_werewolf"), anim("dark_werewolf"), "idle", "walk", tex("dark_werewolf"), 1.4f)
        }
        EntityRenderers.register(GeneratedMobs.ENDER_SNAIL.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ender_snail"), anim("ender_snail"), "idle", "walk", tex("ender_snail"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ENDER_SNAKE.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ender_snake"), anim("ender_snake"), "idle", "walk", tex("ender_snake"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ENTITY_SOUL.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("entity_soul"), anim("entity_soul"), "idle", "walk", tex("entity_soul"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ENTITY_SOUL_1.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("entity_soul_1"), anim("entity_soul_1"), "idle", "walk", tex("entity_soul_1"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ESCAPED_SOUL_ENTITY.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("escaped_soul_entity"), anim("escaped_soul_entity"), "idle", "walk", tex("escaped_soul_entity"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.EVERLASTING_WINTER_GHAST.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("everlasting_winter_ghast"), anim("everlasting_winter_ghast"), "idle", "walk", tex("everlasting_winter_ghast"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.EVERLASTING_WINTER_SHADOW.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("everlasting_winter_shadow"), anim("everlasting_winter_shadow"), "idle", "walk", tex("everlasting_winter_shadow"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.FRIENDLY_LITTLE_SOUL.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("friendly_little_soul"), anim("friendly_little_soul"), "idle", "walk", tex("friendly_little_soul"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.FROZEN_HEART.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("frozen_heart"), anim("frozen_heart"), "idle", "walk", tex("frozen_heart"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.FROZEN_HEART_SLEEPING.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("frozen_heart_sleeping"), anim("frozen_heart_sleeping"), "idle", "walk", tex("frozen_heart_sleeping"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.FROZEN_SHADOW.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("frozen_shadow"), anim("frozen_shadow"), "idle", "walk", tex("frozen_shadow"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GHOST_DIAMOND_AXE.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ghost_diamond_axe"), anim("ghost_diamond_axe"), "idle", "walk", tex("ghost_diamond_axe"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GHOST_DIAMOND_STAFF.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ghost_diamond_staff"), anim("ghost_diamond_staff"), "idle", "walk", tex("ghost_diamond_staff"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GHOST_DIAMOND_SWORD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ghost_diamond_sword"), anim("ghost_diamond_sword"), "idle", "walk", tex("ghost_diamond_sword"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GHOST_IRON_AXE.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ghost_iron_axe"), anim("ghost_iron_axe"), "idle", "walk", tex("ghost_iron_axe"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GHOST_IRON_SWORD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ghost_iron_sword"), anim("ghost_iron_sword"), "idle", "walk", tex("ghost_iron_sword"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GHOST_WOODEN_STAFF.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ghost_wooden_staff"), anim("ghost_wooden_staff"), "idle", "walk", tex("ghost_wooden_staff"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GHOST_WOODEN_SWORD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ghost_wooden_sword"), anim("ghost_wooden_sword"), "idle", "walk", tex("ghost_wooden_sword"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GOBLIN.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("goblin"), anim("goblin"), "idle", "walk", tex("goblin"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GOBLIN_SNIPER.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("goblin_sniper"), anim("goblin_sniper"), "idle", "walk", tex("goblin_sniper"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.GOBLIN_WIZARD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("goblin_wizard"), anim("goblin_wizard"), "idle", "walk", tex("goblin_wizard"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.HOST_OF_DEEP.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("host_of_deep"), anim("host_of_deep"), "idle", "walk", tex("host_of_deep"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ICE_MONSTER.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ice_monster"), anim("ice_monster"), "idle", "walk", tex("ice_monster"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ICE_SPIRIT.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ice_spirit"), anim("ice_spirit"), "idle", "walk", tex("ice_spirit"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ICE_THORN.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ice_thorn"), anim("ice_thorn"), "idle", "walk", tex("ice_thorn"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.ICE_WIZARD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("ice_wizard"), anim("ice_wizard"), "idle", "walk", tex("ice_wizard"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.LAVA_LIZARD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("lava_lizard"), anim("lava_lizard"), "idle", "walk", tex("lava_lizard"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.LITTLE_SOUL.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("little_soul"), anim("little_soul"), "idle", "walk", tex("little_soul"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.MURLOC_WIZARD.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("murloc_wizard"), anim("murloc_wizard"), "idle", "walk", tex("murloc_wizard"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.MUSHROOM_MONSTER.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("mushroom_monster"), anim("mushroom_monster"), "idle", "walk", tex("mushroom_monster"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.NAUTILUS.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("nautilus"), anim("nautilus"), "idle", "walk", tex("nautilus"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.SAUCER.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("saucer"), anim("saucer"), "idle", "walk", tex("saucer"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.SEA_URCHIN.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("sea_urchin"), anim("sea_urchin"), "idle", "walk", tex("sea_urchin"), 1.2f)
        }
        EntityRenderers.register(GeneratedMobs.SHADOW_OF_DEEP.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("shadow_of_deep"), anim("shadow_of_deep"), "idle", "walk", tex("shadow_of_deep"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.SHADOW_SOUL_1.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("shadow_soul_1"), anim("shadow_soul_1"), "idle", "walk", tex("shadow_soul_1"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.SHADOW_SOUL_2.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("shadow_soul_2"), anim("shadow_soul_2"), "idle", "walk", tex("shadow_soul_2"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.SOUL_SOLDIER.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("soul_soldier"), anim("soul_soldier"), "idle", "walk", tex("soul_soldier"), 1f)
        }
        EntityRenderers.register(GeneratedMobs.WEREWOLF.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("werewolf"), anim("werewolf"), "idle", "walk", tex("werewolf"), 1.2f)
        }
    }

    private fun geo(name: String) = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/entity/$name.geometry.json")
    private fun anim(name: String) = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/animations/entity/$name.animation.json")
    private fun tex(name: String) = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/$name.png")
}
