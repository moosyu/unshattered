package io.github.moosyu;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.attributes.UnshatteredAttributes;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static io.github.moosyu.attachments.UnshatteredAttachments.ATTACHMENT_TYPES;
import static io.github.moosyu.attributes.UnshatteredAttributes.ATTRIBUTES;
import static io.github.moosyu.blocks.UnshatteredBlocks.BLOCKS;
import static io.github.moosyu.creative.UnshatteredCreativeTabs.CREATIVE_MODE_TABS;
import static io.github.moosyu.data.components.DataComponentRegistry.DATA_COMPONENTS;
import static io.github.moosyu.entities.UnshatteredEntities.ENTITY_TYPES;
import static io.github.moosyu.items.UnshatteredItems.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Unshattered.MODID)
public class Unshattered {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "unshattered";
    public static final String MOD_NAME = "Skyblock: Unshattered";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Unshattered(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        UnshatteredAttributes.registerAll();
        ATTRIBUTES.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (unshattered) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
        event.enqueueWork(UnshatteredAttributeValues::buildLookup);
        Minecraft.getInstance().options.attackIndicator().set(AttackIndicatorStatus.OFF);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        UnshatteredAttributeValues.buildLookup();
    }
}
