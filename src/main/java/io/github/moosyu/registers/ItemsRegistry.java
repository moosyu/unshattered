package io.github.moosyu.registers;

import io.github.moosyu.items.TreecapitatorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.NNO.MODID;
import static io.github.moosyu.registers.BlocksRegistry.EXAMPLE_BLOCK;

public class ItemsRegistry {
    // Create a Deferred Register to hold Items which will all be registered under the "nno" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Creates a new BlockItem with the id "nno:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "nno:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties()
            .food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    public static final DeferredItem<Item> TREECAPITATOR = ITEMS.register("treecapitator", () -> new TreecapitatorItem(Tiers.GOLD,
            new Item.Properties().attributes(ItemAttributeModifiers.builder().add(AttributesRegistry.SWEEP,
                    new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "sweep"),
                            25,
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND).build())));

    public static final DeferredItem<Item> SPRUCE_AXE = ITEMS.register("spruce_axe", () -> new TreecapitatorItem(Tiers.WOOD,
            new Item.Properties().attributes(ItemAttributeModifiers.builder().add(AttributesRegistry.SWEEP,
                    new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "sweep"),
                            4,
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND).build())));

    public static final DeferredItem<Item> SERIOUSLY_DAMAGED_AXE = ITEMS.register("seriously_damaged_axe", () -> new TreecapitatorItem(Tiers.WOOD,
            new Item.Properties().attributes(ItemAttributeModifiers.builder().add(AttributesRegistry.SWEEP,
                    new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "sweep"),
                            7,
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND).build())));

    public static final DeferredItem<Item> DECENT_AXE = ITEMS.register("decent_axe", () -> new TreecapitatorItem(Tiers.WOOD,
            new Item.Properties().attributes(ItemAttributeModifiers.builder().add(AttributesRegistry.SWEEP,
                    new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "sweep"),
                            24,
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND).build())));

    public static final DeferredItem<Item> FIG_HEW = ITEMS.register("fig_hew", () -> new TreecapitatorItem(Tiers.WOOD,
            new Item.Properties().attributes(ItemAttributeModifiers.builder().add(AttributesRegistry.SWEEP,
                    new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "sweep"),
                            7,
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND).build())));

    public static final DeferredItem<Item> FIGSTONE_SPLITTER = ITEMS.register("figstone_splitter", () -> new TreecapitatorItem(Tiers.WOOD,
            new Item.Properties().attributes(ItemAttributeModifiers.builder().add(AttributesRegistry.SWEEP,
                    new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "sweep"),
                            24,
                            AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND).build())));
}
