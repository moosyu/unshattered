package io.github.moosyu.events;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.RarityTypes;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ModifyDefaultComponentsHandler {
    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.WOODEN_PICKAXE, ((components, _, _) ->
                components.set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                        .set(UnshatteredDataComponents.SELL_VALUE, 1)
                )
        );

        event.modify(Items.GOLDEN_PICKAXE, ((components, _, _) ->
                components.set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                        .set(UnshatteredDataComponents.SELL_VALUE, 6)
                )
        );

        event.modify(Items.STONE_PICKAXE, ((components, _, _) ->
                components.set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                        .set(UnshatteredDataComponents.SELL_VALUE, 2)
                )
        );


        event.modify(Items.IRON_PICKAXE, ((components, _, _) ->
                components.set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                        .set(UnshatteredDataComponents.SELL_VALUE, 4)
                )
        );

        event.modify(Items.DIAMOND_PICKAXE, ((components, _, _) ->
                components.set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                        .set(UnshatteredDataComponents.RARITY, RarityTypes.UNCOMMON)
                        .set(UnshatteredDataComponents.SELL_VALUE, 12)
                )
        );
    }
}
