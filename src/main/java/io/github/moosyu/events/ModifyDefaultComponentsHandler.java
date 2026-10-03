package io.github.moosyu.events;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ModifyDefaultComponentsHandler {
    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.WOODEN_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 1))
        );

        event.modify(Items.GOLDEN_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 6))
        );

        event.modify(Items.STONE_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 2))
        );

        event.modify(Items.IRON_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.PICKAXE)
                .set(UnshatteredDataComponents.SELL_VALUE, 4))
        );

        event.modify(Items.DIAMOND_PICKAXE, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.UNCOMMON)
                .set(UnshatteredDataComponents.SELL_VALUE, 12))
        );

        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.PICKAXE)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true).set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.WOODEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.STONE_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 1))
        );

        event.modify(Items.IRON_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 3))
        );

        event.modify(Items.GOLDEN_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.COMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 4))
        );

        event.modify(Items.DIAMOND_SWORD, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.SWORD)
                .set(UnshatteredDataComponents.RARITY, UnshatteredRarity.UNCOMMON)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 8))
        );

        event.modify(Items.BOW, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.BOW)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 3))
        );

        event.modify(Items.CROSSBOW, ((components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, ItemType.CROSSBOW)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), 7))
        );

        event.modify(Items.WOODEN_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarity.COMMON, 1));
        event.modify(Items.STONE_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarity.COMMON, 2));
        event.modify(Items.IRON_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarity.COMMON, 4));
        event.modify(Items.GOLDEN_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarity.COMMON, 6));
        event.modify(Items.DIAMOND_AXE, (components, _, _) -> modifyVanillaAxeComponents(components, UnshatteredRarity.UNCOMMON, 12));
        modifyVanillaItem(event, Items.END_STONE, ItemType.MATERIAL, 2, true);
        modifyVanillaItem(event, Items.DIAMOND, ItemType.MATERIAL, 8, false);
        modifyVanillaItem(event, Items.COBBLESTONE, ItemType.MATERIAL, 1, false);
        modifyVanillaItem(event, Items.IRON_HELMET, ItemType.HELMET, 2, false);
        modifyVanillaItem(event, Items.IRON_CHESTPLATE, ItemType.CHESTPLATE, 3, false);
        modifyVanillaItem(event, Items.IRON_LEGGINGS, ItemType.LEGGINGS, 3, false);
        modifyVanillaItem(event, Items.IRON_BOOTS, ItemType.BOOTS, 2, false);
        modifyVanillaItem(event, Items.DIAMOND_HELMET, ItemType.HELMET, UnshatteredRarity.UNCOMMON, 3, false);
        modifyVanillaItem(event, Items.DIAMOND_CHESTPLATE, ItemType.CHESTPLATE, UnshatteredRarity.UNCOMMON, 5, false);
        modifyVanillaItem(event, Items.DIAMOND_LEGGINGS, ItemType.LEGGINGS, UnshatteredRarity.UNCOMMON, 4, false);
        modifyVanillaItem(event, Items.DIAMOND_BOOTS, ItemType.BOOTS, UnshatteredRarity.UNCOMMON, 3, false);
        modifyVanillaItem(event, Items.GOLD_INGOT, ItemType.MATERIAL, 4);
        modifyVanillaItem(event, Items.GOLD_BLOCK, ItemType.MATERIAL, 27);
        modifyVanillaItem(event, Items.DIAMOND, ItemType.MATERIAL, 8);
        modifyVanillaItem(event, Items.DIAMOND_BLOCK, ItemType.MATERIAL, 72);
        modifyVanillaItem(event, Items.EMERALD, ItemType.MATERIAL, 4);
        modifyVanillaItem(event, Items.EMERALD_BLOCK, ItemType.MATERIAL, 36);
        modifyVanillaItem(event, Items.IRON_INGOT, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.IRON_BLOCK, ItemType.MATERIAL, 18);
        modifyVanillaItem(event, Items.COAL, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.COAL_BLOCK, ItemType.MATERIAL, 18);
        modifyVanillaItem(event, Items.LAPIS_LAZULI, ItemType.MATERIAL, 1);
        modifyVanillaItem(event, Items.LAPIS_BLOCK, ItemType.MATERIAL, 9);
        modifyVanillaItem(event, Items.REDSTONE, ItemType.MATERIAL, 1);
        modifyVanillaItem(event, Items.REDSTONE_BLOCK, ItemType.MATERIAL, 9);
        modifyVanillaItem(event, Items.OAK_LOG, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.BIRCH_LOG, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.SPRUCE_LOG, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.JUNGLE_LOG, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.ACACIA_LOG, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.DARK_OAK_LOG, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.ENCHANTED_BOOK, ItemType.ENCHANTED_BOOK, UnshatteredRarity.RARE, 0, false);
        modifyVanillaItem(event, Items.POISONOUS_POTATO, ItemType.MATERIAL, 10);
        modifyVanillaItem(event, Items.BONE, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.FLINT, ItemType.MATERIAL, 4);
        modifyVanillaItem(event, Items.STRING, ItemType.MATERIAL, 3);
        modifyVanillaItem(event, Items.CLAY_BALL, ItemType.MATERIAL, 3);
        modifyVanillaItem(event, Items.INK_SAC, ItemType.MATERIAL, 2);
        modifyVanillaItem(event, Items.LILY_PAD, ItemType.MATERIAL, 10);
        modifyVanillaItem(event, Items.PRISMARINE_SHARD, ItemType.MATERIAL, 5);
        modifyVanillaItem(event, Items.PRISMARINE_CRYSTALS, ItemType.MATERIAL, 5);
        modifyVanillaItem(event, Items.PUFFERFISH, ItemType.MATERIAL, 15);
        modifyVanillaItem(event, Items.COD, ItemType.MATERIAL, 6);
        modifyVanillaItem(event, Items.SALMON, ItemType.MATERIAL, 10);
        modifyVanillaItem(event, Items.SPONGE, ItemType.MATERIAL, 50);
        modifyVanillaItem(event, Items.TROPICAL_FISH, ItemType.MATERIAL, 20);
        modifyVanillaItem(event, Items.HONEYCOMB, ItemType.MATERIAL, 100);
        modifyVanillaItem(event, Items.ICE, ItemType.MATERIAL, 1);
        modifyVanillaItem(event, Items.BLAZE_ROD, ItemType.MATERIAL, 9);

        for (Item wool : UnshatteredUtils.WOOL_TYPES) {
            modifyVanillaItem(event, wool, ItemType.MATERIAL, 2);
        }
    }

    /**
     * removes axe's data components to convert them into battleaxes
     * @param components data component map builder
     * @param rarity axe rarity
     * @param sellValue sell value
     */
    private static void modifyVanillaAxeComponents(DataComponentMap.Builder components, UnshatteredRarity rarity, int sellValue) {
        components.set(UnshatteredDataComponents.ITEM_TYPE, ItemType.BATTLE_AXE)
                .set(UnshatteredDataComponents.RARITY, rarity)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
                .set(DataComponents.TOOL, new Tool(List.of(), 1.0f, 0, false));
    }

    /**
     * modify a vanilla item by adding unshattered components
     * @param event ModifyDefaultComponentsEvent
     * @param item item having its components modified
     * @param itemType the item type
     * @param rarity the rarity
     * @param sellValue the sell value
     * @param description whether the item has a description (item.description.unshattered.item_id) set in lang.
     */
    private static void modifyVanillaItem(ModifyDefaultComponentsEvent event, Item item, ItemType itemType, UnshatteredRarity rarity, int sellValue, boolean description) {
        event.modify(item, (components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, itemType)
                .set(UnshatteredDataComponents.RARITY, rarity)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), description)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
        );
    }

    /**
     * modify a vanilla item by adding unshattered components (assuming it's common)
     * @param event ModifyDefaultComponentsEvent
     * @param item item having its components modified
     * @param itemType the item type
     * @param sellValue the sell value
     * @param description whether the item has a description (item.description.unshattered.item_id) set in lang.
     */
    private static void modifyVanillaItem(ModifyDefaultComponentsEvent event, Item item, ItemType itemType, int sellValue, boolean description) {
        event.modify(item, (components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, itemType)
                .set(UnshatteredDataComponents.DESCRIPTION.get(), description)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
        );
    }

    /**
     * modify a vanilla item by adding unshattered components (assuming it's common)
     * @param event ModifyDefaultComponentsEvent
     * @param item item having its components modified
     * @param itemType the item type
     * @param sellValue the sell value
     */
    private static void modifyVanillaItem(ModifyDefaultComponentsEvent event, Item item, ItemType itemType, int sellValue) {
        event.modify(item, (components, _, _) -> components
                .set(UnshatteredDataComponents.ITEM_TYPE, itemType)
                .set(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
        );
    }
}
