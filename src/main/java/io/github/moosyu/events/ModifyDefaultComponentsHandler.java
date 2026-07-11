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
        addCollectableDataComponent(event, Items.OAK_LOG);
        addCollectableDataComponent(event, Items.SPRUCE_LOG);
        addCollectableDataComponent(event, Items.BIRCH_LOG);
        addCollectableDataComponent(event, Items.JUNGLE_LOG);
        addCollectableDataComponent(event, Items.ACACIA_LOG);
        addCollectableDataComponent(event, Items.DARK_OAK_LOG);
        addCollectableDataComponent(event, Items.COD);
        addCollectableDataComponent(event, Items.SALMON);
        addCollectableDataComponent(event, Items.PUFFERFISH);
        addCollectableDataComponent(event, Items.TROPICAL_FISH);
        addCollectableDataComponent(event, Items.PRISMARINE_SHARD);
        addCollectableDataComponent(event, Items.PRISMARINE_CRYSTALS);
        addCollectableDataComponent(event, Items.CLAY_BALL);
        addCollectableDataComponent(event, Items.SPONGE);
        addCollectableDataComponent(event, Items.LILY_PAD);
        addCollectableDataComponent(event, Items.WHEAT);
        addCollectableDataComponent(event, Items.POTATO);
        addCollectableDataComponent(event, Items.CARROT);
        addCollectableDataComponent(event, Items.PUMPKIN);
        addCollectableDataComponent(event, Items.MELON);
        addCollectableDataComponent(event, Items.SUGAR_CANE);
        addCollectableDataComponent(event, Items.MUSHROOM_STEM);
        addCollectableDataComponent(event, Items.BROWN_MUSHROOM_BLOCK);
        addCollectableDataComponent(event, Items.RED_MUSHROOM_BLOCK);
        addCollectableDataComponent(event, Items.BROWN_MUSHROOM);
        addCollectableDataComponent(event, Items.RED_MUSHROOM);
        addCollectableDataComponent(event, Items.CACTUS);
        addCollectableDataComponent(event, Items.COCOA_BEANS);
        addCollectableDataComponent(event, Items.NETHER_WART);
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

    private static void addCollectableDataComponent(ModifyDefaultComponentsEvent event, Item item) {
        event.modify(item, (components, _, _) -> {
            components.set(UnshatteredDataComponents.ITEM_COLLECTABLE, true);
            components.build();
        });
    }
}
