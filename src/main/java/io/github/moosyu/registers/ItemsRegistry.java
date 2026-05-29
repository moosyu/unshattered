package io.github.moosyu.registers;

import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.items.*;
import io.github.moosyu.rarities.RarityTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.registers.ArmorMaterialRegister.LEAFLET_ARMOR_MATERIAL;
import static io.github.moosyu.registers.BlocksRegistry.EXAMPLE_BLOCK;

public class ItemsRegistry {
    // Create a Deferred Register to hold Items which will all be registered under the "unshattered" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Creates a new BlockItem with the id "unshattered:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    public static final DeferredItem<UnshatteredAxeWeapon> MERCENARY_AXE = ITEMS.register("mercenary_axe", () -> new UnshatteredAxeWeapon(new Item.Properties()
        .component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE)
        .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.BATTLE_AXE)

        .attributes(ItemAttributeModifiers.builder().add(ModAttributes.DAMAGE.holder,
        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "mercenary_axe_damage"), 70, AttributeModifier.Operation.ADD_VALUE),
        EquipmentSlotGroup.MAINHAND).add(ModAttributes.STRENGTH.holder,
        new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "mercenary_axe_strength"), 20, AttributeModifier.Operation.ADD_VALUE),
        EquipmentSlotGroup.MAINHAND
        ).build()))
    );
    // todo: make it so the foraging fortune only applies in certain areas (the park, not the galatea)
    public static final DeferredItem<UnshatteredAxeTool> TREECAPITATOR = ITEMS.register("treecapitator", () -> new UnshatteredAxeTool(Tiers.GOLD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.EPIC).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.AXE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "treecapitator_sweep"), 25, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "treecapitator_foraging_fortune"), 100, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    // todo: make it so the foraging fortune only applies in certain areas (the park, not the galatea)
    public static final DeferredItem<UnshatteredAxeTool> SPRUCE_AXE = ITEMS.register("spruce_axe", () -> new UnshatteredAxeTool(Tiers.WOOD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.AXE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "spruce_axe_sweep"), 4, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "spruce_axe_foraging_fortune"), 50, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<UnshatteredAxeTool> SERIOUSLY_DAMAGED_AXE = ITEMS.register("seriously_damaged_axe", () -> new UnshatteredAxeTool(Tiers.WOOD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.AXE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "seriously_damaged_axe_sweep"), 7, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "seriously_damaged_axe_foraging_fortune"), 5, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<UnshatteredAxeTool> DECENT_AXE = ITEMS.register("decent_axe", () -> new UnshatteredAxeTool(Tiers.WOOD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.AXE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "decent_axe_sweep"), 24, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "decent_axe_foraging_fortune"), 5, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<UnshatteredAxeTool> FIG_HEW = ITEMS.register("fig_hew", () -> new UnshatteredAxeTool(Tiers.WOOD, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.AXE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "fig_hew_sweep"), 7, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "fig_hew_foraging_fortune"), 20, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<UnshatteredAxeTool> FIGSTONE_SPLITTER = ITEMS.register("figstone_splitter", () -> new UnshatteredAxeTool(Tiers.STONE, new Item.Properties().component(DataComponentRegistry.RARITY.get(), RarityTypes.EPIC).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.AXE).attributes(
        ItemAttributeModifiers.builder().add(ModAttributes.SWEEP.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "figstone_splitter_sweep"), 24, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).add(ModAttributes.FORAGING_FORTUNE.holder,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "figstone_splitter_foraging_fortune"), 35, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build()))
    );

    public static final DeferredItem<UnshatteredArmorItem> LEAFLET_HELMET = ITEMS.registerItem("leaflet_helmet", props -> new UnshatteredArmorItem(
            LEAFLET_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.HELMET).attributes(
            ItemAttributeModifiers.builder().add(ModAttributes.HEALTH.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_helmet_health"), 70, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.HEAD).add(ModAttributes.FORAGING_FORTUNE.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_helmet_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.HEAD).build()), "Helmet")
    );

    public static final DeferredItem<UnshatteredArmorItem> LEAFLET_CHESTPLATE = ITEMS.registerItem("leaflet_chestplate", props -> new UnshatteredArmorItem(
            LEAFLET_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.CHESTPLATE).attributes(
            ItemAttributeModifiers.builder().add(ModAttributes.HEALTH.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_chestplate_health"), 80, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.CHEST).add(ModAttributes.FORAGING_FORTUNE.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_chestplate_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.CHEST).build()), "Chestplate")
    );

    public static final DeferredItem<UnshatteredArmorItem> LEAFLET_LEGGINGS = ITEMS.registerItem("leaflet_leggings", props -> new UnshatteredArmorItem(
            LEAFLET_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.LEGGINGS).attributes(
            ItemAttributeModifiers.builder().add(ModAttributes.HEALTH.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_leggings_health"), 20, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.LEGS).add(ModAttributes.FORAGING_FORTUNE.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_leggings_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.LEGS).build()), "Leggings")
    );

    public static final DeferredItem<UnshatteredArmorItem> LEAFLET_BOOTS = ITEMS.registerItem("leaflet_boots", props -> new UnshatteredArmorItem(
            LEAFLET_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.BOOTS).attributes(
            ItemAttributeModifiers.builder().add(ModAttributes.HEALTH.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_boots_health"), 25, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.FEET).add(ModAttributes.FORAGING_FORTUNE.holder,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(MODID, "leaflet_boots_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.FEET).build()), "Boots")
    );

    public static final DeferredItem<UnshatteredEnchantedItem> BAT_THE_FISH = ITEMS.register("bat_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Where is my cape!"))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> CENTURY_THE_FISH = ITEMS.register("century_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "The term 'century' was actually named after him, or so he says."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> CHILL_THE_FISH = ITEMS.register("chill_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "This fish was found frozen solid in the lake of the island during the Holiday months."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> CLUNK_THE_FISH = ITEMS.register("clunk_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Cluck cluck bkaawwk. Puk Puk Pukaaak!"))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> DIAMOND_THE_FISH = ITEMS.register("diamond_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Shines bright like a fish."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> DUST_THE_FISH = ITEMS.register("dust_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Pretty sure they forgot about me..."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> EGG_THE_FISH = ITEMS.register("egg_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "This delicate, painted fish was found in the depths of the island during the Easter season."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> EON_THE_FISH = ITEMS.register("eon_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Back in my day, we swam upstream for hours to get to school, and we didn't complain!"))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> FLAKE_THE_FISH = ITEMS.register("flake_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "I'm a special snowflake."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> EXPERIMENT_THE_FISH = ITEMS.register("experiment_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Fish me once, shame on you. Fish me twice, shame on me."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> FOSSIL_THE_FISH = ITEMS.register("fossil_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Older than time itself yet never changing, for he was born perfect."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> GABAGOOL_THE_FISH = ITEMS.register("gabagool_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "If the salad is on top, I send it back."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> GIFT_THE_FISH = ITEMS.register("gift_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "I'm empty inside."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> HERRING_THE_FISH = ITEMS.register("herring_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Sometimes patience is the key to victory. Sometimes it leads to very little and it seems like it's not worth it. And you wonder why you waited so long for something so disappointing."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> NOPE_THE_FISH = ITEMS.register("nope_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Why did we follow Shrimp..."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> OOPS_THE_FISH = ITEMS.register("oops_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Discovered in the depths of the island during the summer months."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> PARTY_THE_FISH = ITEMS.register("party_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Big fan of parties, yet it's hard to enjoy them without friends."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> ROCK_THE_FISH = ITEMS.register("rock_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Don't take me for granite."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> SHRIMP_THE_FISH = ITEMS.register("shrimp_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "I achieved nothing."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> SKELETON_THE_FISH = ITEMS.register("skeleton_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Once a beautiful bass, but his loud mouth got the better of him."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> SPOOK_THE_FISH = ITEMS.register("spook_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "This fish lurks in the darkness below the island during the Halloween season."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> STEW_THE_FISH = ITEMS.register("stew_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "The stew was expired."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> SWAMP_THE_FISH = ITEMS.register("swamp_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "I once tried a clear pond. too shiny. Never again."))
    );

    public static final DeferredItem<UnshatteredEnchantedItem> ZOOP_THE_FISH = ITEMS.register("zoop_the_fish", () -> new UnshatteredEnchantedItem(new Item.Properties()
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION_KEY.get(), "Keep me in here please."))
    );
}
