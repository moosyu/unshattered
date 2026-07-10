package io.github.moosyu.events;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ModifyDefaultComponentsHandler {
    @SubscribeEvent
    public static void onModifyDefaultComponentsHandler(ModifyDefaultComponentsEvent event) {
        addCollectableDataComponent(event, Items.OAK_LOG, 6.0f);
        addCollectableDataComponent(event, Items.SPRUCE_LOG, 6.0f);
        addCollectableDataComponent(event, Items.BIRCH_LOG, 6.0f);
        addCollectableDataComponent(event, Items.JUNGLE_LOG, 6.0f);
        addCollectableDataComponent(event, Items.ACACIA_LOG, 6.0f);
        addCollectableDataComponent(event, Items.DARK_OAK_LOG, 6.0f);
        addCollectableDataComponent(event, Items.COD);
        addCollectableDataComponent(event, Items.SALMON);
        addCollectableDataComponent(event, Items.PUFFERFISH);
        addCollectableDataComponent(event, Items.TROPICAL_FISH);
        addCollectableDataComponent(event, Items.PRISMARINE_SHARD);
        addCollectableDataComponent(event, Items.PRISMARINE_CRYSTALS);
        addCollectableDataComponent(event, Items.CLAY_BALL);
        addCollectableDataComponent(event, Items.SPONGE);
        addCollectableDataComponent(event, Items.LILY_PAD);
        addCollectableDataComponent(event, Items.WHEAT, 4.0f);
        addCollectableDataComponent(event, Items.POTATO, 4.0f);
        addCollectableDataComponent(event, Items.CARROT, 4.0f);
        addCollectableDataComponent(event, Items.PUMPKIN, 4.5f);
        addCollectableDataComponent(event, Items.MELON, 4.0f);
        addCollectableDataComponent(event, Items.SUGAR_CANE, 2.0f);
        addCollectableDataComponent(event, Items.MUSHROOM_STEM, 2.0f);
        addCollectableDataComponent(event, Items.BROWN_MUSHROOM_BLOCK, 2.0f);
        addCollectableDataComponent(event, Items.RED_MUSHROOM_BLOCK, 2.0f);
        addCollectableDataComponent(event, Items.BROWN_MUSHROOM, 6.0f);
        addCollectableDataComponent(event, Items.RED_MUSHROOM, 6.0f);
        addCollectableDataComponent(event, Items.CACTUS, 2.0f);
        addCollectableDataComponent(event, Items.COCOA_BEANS, 4.0f);
        addCollectableDataComponent(event, Items.NETHER_WART, 4.0f);
        addCollectableDataComponent(event, Items.BLAZE_ROD);
        addCollectableDataComponent(event, Items.BONE);
        addCollectableDataComponent(event, Items.ENDER_PEARL);
        addCollectableDataComponent(event, Items.GHAST_TEAR);
        addCollectableDataComponent(event, Items.GUNPOWDER);
        addCollectableDataComponent(event, Items.MAGMA_CREAM);
        addCollectableDataComponent(event, Items.ROTTEN_FLESH);
        addCollectableDataComponent(event, Items.SLIME_BALL);
        addCollectableDataComponent(event, Items.SPIDER_EYE);
        addCollectableDataComponent(event, Items.STRING);
    }

    private static void addCollectableDataComponent(ModifyDefaultComponentsEvent event, Item item, float exp) {
        event.modify(item, (components, _, _) -> {
            components.set(UnshatteredDataComponents.ITEM_COLLECTABLE, true);
            components.set(UnshatteredDataComponents.ITEM_EXP_REWARD, exp);
            components.build();
        });
    }

    private static void addCollectableDataComponent(ModifyDefaultComponentsEvent event, Item item) {
        event.modify(item, (components, _, _) -> {
            components.set(UnshatteredDataComponents.ITEM_COLLECTABLE, true);
            components.build();
        });
    }
}
