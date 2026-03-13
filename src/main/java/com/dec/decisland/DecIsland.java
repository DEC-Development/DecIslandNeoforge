package com.dec.decisland;

import com.dec.decisland.api.ModItemEventTrigger;
import com.dec.decisland.attachment.ModAttachments;
import com.dec.decisland.block.ModBlocks;
import com.dec.decisland.client.renderer.EmptyRenderer;
import com.dec.decisland.entity.ModEntities;
import com.dec.decisland.item.ModCreativeModeTabs;
import com.dec.decisland.item.ModItems;
import com.dec.decisland.item.category.Mask;
import com.dec.decisland.item.category.Material;
import com.dec.decisland.item.category.Weapon;
import com.dec.decisland.item.custom.AbsoluteZero;
import com.dec.decisland.network.AttackPayload;
import com.dec.decisland.particles.ModParticles;
import com.dec.decisland.particles.custom.AbsoluteZeroSmokeSeedParticle;
import com.dec.decisland.particles.custom.AbsoluteZeroSmokeSingleParticle;
import com.dec.decisland.particles.custom.BlizzardWakeParticle;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.Optional;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DecIsland.MOD_ID)
public class DecIsland {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "decisland";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();



//    // Create a Deferred Register to hold Blocks which will all be registered under the "decisland" namespace
//    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
//    // Create a Deferred Register to hold Items which will all be registered under the "decisland" namespace
//    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
//    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "decisland" namespace
//    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
//
//    // Creates a new Block with the id "decisland:example_block", combining the namespace and path
//    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", p -> p.mapColor(MapColor.STONE));
//    // Creates a new BlockItem with the id "decisland:example_block", combining the namespace and path
//    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
//
//    // Creates a new food item with the id "decisland:example_id", nutrition 1 and saturation 2
//    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", p -> p.food(new FoodProperties.Builder()
//            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
//
//    // Creates a creative tab with the id "decisland:example_tab" for the example item, that is placed after the combat tab
//    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
//            .title(Component.translatable("itemGroup.decisland")) //The language key for the title of your CreativeModeTab
//            .withTabsBefore(CreativeModeTabs.COMBAT)
//            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
//            .displayItems((parameters, output) -> {
//                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
//            }).build());



    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public DecIsland(IEventBus modEventBus, ModContainer modContainer) {

        try {
            // Register the commonSetup method for modloading
            modEventBus.addListener(this::commonSetup);

            Material.load();
            Weapon.load();
            Mask.load();
            ModItems.register(modEventBus);

            ModBlocks.register(modEventBus);
            ModEntities.registry(modEventBus);
            ModCreativeModeTabs.register(modEventBus);
            ModParticles.register(modEventBus);
            ModAttachments.register(modEventBus);

            // Register the Deferred Register to the mod event bus so blocks get registered
//        BLOCKS.register(modEventBus);
//        // Register the Deferred Register to the mod event bus so items get registered
//        ITEMS.register(modEventBus);
//        // Register the Deferred Register to the mod event bus so tabs get registered
//        CREATIVE_MODE_TABS.register(modEventBus);

            // Register ourselves for server and other game events we are interested in.
            // Note that this is necessary if and only if we want *this* class (DecIsland) to respond directly to events.
            // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
            NeoForge.EVENT_BUS.register(this);

            // Register the item to a creative tab
            modEventBus.addListener(this::addCreative);

            // Register our mod's ModConfigSpec so that FML can create and load the config file for us
            modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        } catch (Throwable t) {
            System.err.println("========== ERROR DURING MOD CONSTRUCTION ==========");
            t.printStackTrace(); // 强制输出到控制台
            throw t; // 重新抛出，让 mod 加载失败，但堆栈会显示
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.AMETHYST_AXE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }


    @EventBusSubscriber(modid = MOD_ID)
    public static class ServerModEvents {

//        @SubscribeEvent
//        public static void onEntityHurt(LivingDamageEvent.Pre event) {
//            LivingEntity target = event.getEntity();
//            Entity source = event.getSource().getEntity();
//
//            if (source instanceof LivingEntity livingSource && event.getSource().isDirect()) {
//                ItemStack weapon = livingSource.getWeaponItem();
//
//                if (weapon.getItem() instanceof ModItemEventTrigger trigger) {
//                    // 1. 服务器端执行逻辑
//                    InteractionHand hand = (livingSource instanceof Player player) ? player.getUsedItemHand() : InteractionHand.MAIN_HAND;
//                    trigger.onAttack(livingSource, target, weapon, hand);
//
//                    // 2. 发送数据包到客户端
//                    if (!livingSource.level().isClientSide()) {
//                        // 使用 ItemStack.CODEC 将物品编码为 NBT（需要注册表上下文）
//                        var registryAccess = livingSource.registryAccess();
//                        var ops = registryAccess.createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE); // 确保导入 NbtOps
//
//                        // 编码物品，得到 DataResult<Tag>
//                        var dataResult = ItemStack.CODEC.encodeStart(ops, weapon);
//                        // 获取结果（Optional<Tag>），并尝试转为 CompoundTag
//                        CompoundTag weaponTag = dataResult
//                                .result() // 返回 Optional<Tag>
//                                .filter(tag -> tag instanceof CompoundTag) // 确保是 CompoundTag
//                                .map(tag -> (CompoundTag) tag)
//                                .orElse(new CompoundTag()); // 失败时返回空 CompoundTag
//
//                        AttackPayload payload = new AttackPayload(livingSource.getId(), target.getId(), weaponTag, hand);
//
//                        if (livingSource instanceof ServerPlayer serverPlayer) {
//                            PacketDistributor.sendToPlayer(serverPlayer, payload);
//                        } else {
//                            PacketDistributor.sendToPlayersTrackingEntity(livingSource, payload);
//                        }
//                    }
//                }
//            }
//        }
    }
}
