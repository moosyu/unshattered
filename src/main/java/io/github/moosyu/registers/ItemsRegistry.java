package io.github.moosyu.registers;

import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.items.NNOArmorItem;
import io.github.moosyu.items.NNOAxeTool;
import io.github.moosyu.items.NNOAxeWeapon;
import io.github.moosyu.items.NNOItem;
import io.github.moosyu.rarities.RarityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.NNO.MODID;
import static io.github.moosyu.registers.ArmorMaterialRegister.LEAFLET_ARMOR_MATERIAL;
import static io.github.moosyu.registers.BlocksRegistry.EXAMPLE_BLOCK;

public class ItemsRegistry {
    // Create a Deferred Register to hold Items which will all be registered under the "nno" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Creates a new BlockItem with the id "nno:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    public static final DeferredItem<NNOAxeWeapon> MERCENARY_AXE = ITEMS.register("mercenary_axe", () -> new NNOAxeWeapon(new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.DAMAGE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "mercenary_axe_damage"), 70, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.STRENGTH.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "mercenary_axe_strength"), 20, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );
    // todo: make it so the foraging fortune only applies in certain areas (the park, not the galatea)
    public static final DeferredItem<NNOAxeTool> TREECAPITATOR = ITEMS.register("treecapitator", () -> new NNOAxeTool(Tiers.GOLD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.EPIC).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "treecapitator_sweep"), 25, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "treecapitator_foraging_fortune"), 100, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    // todo: make it so the foraging fortune only applies in certain areas (the park, not the galatea)
    public static final DeferredItem<NNOAxeTool> SPRUCE_AXE = ITEMS.register("spruce_axe", () -> new NNOAxeTool(Tiers.WOOD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "spruce_axe_sweep"), 4, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "spruce_axe_foraging_fortune"), 50, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<NNOAxeTool> SERIOUSLY_DAMAGED_AXE = ITEMS.register("seriously_damaged_axe", () -> new NNOAxeTool(Tiers.WOOD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "seriously_damaged_axe_sweep"), 7, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "seriously_damaged_axe_foraging_fortune"), 5, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<NNOAxeTool> DECENT_AXE = ITEMS.register("decent_axe", () -> new NNOAxeTool(Tiers.WOOD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "decent_axe_sweep"), 24, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "decent_axe_foraging_fortune"), 5, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<NNOAxeTool> FIG_HEW = ITEMS.register("fig_hew", () -> new NNOAxeTool(Tiers.WOOD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "fig_hew_sweep"), 7, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "fig_hew_foraging_fortune"), 20, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<NNOAxeTool> FIGSTONE_SPLITTER = ITEMS.register("figstone_splitter", () -> new NNOAxeTool(Tiers.STONE, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.EPIC).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "figstone_splitter_sweep"), 24, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "figstone_splitter_foraging_fortune"), 35, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<NNOArmorItem> LEAFLET_HELMET = ITEMS.registerItem("leaflet_helmet", props -> new NNOArmorItem(
            LEAFLET_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1).attributes(
            ItemAttributeModifiers.builder().add(ModAttributes.HEALTH.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_helmet_health"), 70, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.HEAD).add(ModAttributes.FORAGING_FORTUNE.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_helmet_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.HEAD).build()), "Helmet")
    );

    public static final DeferredItem<NNOArmorItem> LEAFLET_CHESTPLATE = ITEMS.registerItem("leaflet_chestplate", props -> new NNOArmorItem(
            LEAFLET_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1).attributes(
            ItemAttributeModifiers.builder().add(ModAttributes.HEALTH.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_chestplate_health"), 80, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.CHEST).add(ModAttributes.FORAGING_FORTUNE.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_chestplate_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.CHEST).build()), "Chestplate")
    );

    public static final DeferredItem<NNOArmorItem> LEAFLET_LEGGINGS = ITEMS.registerItem("leaflet_leggings", props -> new NNOArmorItem(
            LEAFLET_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1).attributes(
            ItemAttributeModifiers.builder().add(ModAttributes.HEALTH.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_leggings_health"), 20, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.LEGS).add(ModAttributes.FORAGING_FORTUNE.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_leggings_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.LEGS).build()), "Leggings")
    );

    public static final DeferredItem<NNOArmorItem> LEAFLET_BOOTS = ITEMS.registerItem("leaflet_boots", props -> new NNOArmorItem(
            LEAFLET_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1).attributes(
            ItemAttributeModifiers.builder().add(ModAttributes.HEALTH.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_boots_health"), 25, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.FEET).add(ModAttributes.FORAGING_FORTUNE.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_boots_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.FEET).build()), "Boots")
    );
}
