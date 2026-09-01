package io.github.moosyu.events;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.UnshatteredRarities;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.List;
import java.util.Objects;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ModifyDefaultComponentsHandler {
    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.WOODEN_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 1))
        );
        event.modify(Items.GOLDEN_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 6))
        );
        event.modify(Items.STONE_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 2))
        );
        event.modify(Items.IRON_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 4))
        );
        event.modify(Items.DIAMOND_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.SELL_VALUE, 12))
        );
        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );
        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );
        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true).set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );
        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );
        event.modify(Items.STONE_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );
        event.modify(Items.IRON_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 3))
        );
        event.modify(Items.GOLDEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 4))
        );
        event.modify(Items.DIAMOND_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarities.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 8))
        );

        event.modify(Items.WOODEN_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.COMMON, 1));
        event.modify(Items.STONE_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.COMMON, 2));
        event.modify(Items.IRON_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.COMMON, 4));
        event.modify(Items.GOLDEN_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.COMMON, 6));
        event.modify(Items.DIAMOND_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarities.UNCOMMON, 12));
    }

    private static void modifyVanillaAxeComponents(DataComponentMap.Builder components, UnshatteredRarities rarity, int sellValue) {
        components.set(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.BATTLE_AXE)
                .set(UnshatteredDataComponents.RARITY, rarity)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
                .set(DataComponents.TOOL, new Tool(List.of(), 1.0f, 0, false));
    }
}
