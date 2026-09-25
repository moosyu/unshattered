package io.github.moosyu;

import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.recipes.UnshatteredRecipes;
import net.neoforged.bus.api.SubscribeEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

import static io.github.moosyu.data.attachments.UnshatteredAttachments.ATTACHMENT_TYPES;
import static io.github.moosyu.attributes.UnshatteredAttributes.ATTRIBUTES;
import static io.github.moosyu.blocks.UnshatteredBlocks.BLOCKS;
import static io.github.moosyu.creative.UnshatteredCreativeTabs.CREATIVE_MODE_TABS;
import static io.github.moosyu.data.components.UnshatteredDataComponents.DATA_COMPONENTS;
import static io.github.moosyu.entities.UnshatteredEntities.ENTITY_TYPES;
import static io.github.moosyu.gui.menus.UnshatteredMenus.MENUS;
import static io.github.moosyu.items.UnshatteredItems.*;
import static io.github.moosyu.sounds.UnshatteredSounds.SOUND_EVENTS;

@Mod(Unshattered.MODID)
public class Unshattered {
    public static final String MODID = "unshattered";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Unshattered(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        UnshatteredAttributes.registerAll();
        ATTRIBUTES.register(modEventBus);
        DATA_COMPONENTS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        UnshatteredRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        UnshatteredRecipes.RECIPE_TYPES.register(modEventBus);
        MENUS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
