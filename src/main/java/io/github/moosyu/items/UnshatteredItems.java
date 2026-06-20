package io.github.moosyu.items;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.items.weapons.cleavers.*;
import io.github.moosyu.items.weapons.swords.*;
import io.github.moosyu.rarities.RarityTypes;
import io.github.moosyu.data.components.DataComponentRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.materials.ArmorMaterials.LEAFLET_ARMOR_MATERIAL;
import static io.github.moosyu.blocks.UnshatteredBlocks.*;

public class UnshatteredItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<UnshatteredAxeWeapon> MERCENARY_AXE = ITEMS.registerItem("mercenary_axe",props -> new UnshatteredAxeWeapon(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE)
            .component(DataComponentRegistry.SKILL_REQUIREMENT.get(), new SkillRequirement(PlayerSkillsAttachment.Skill.COMBAT, 4))
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "mercenary_axe_damage"), 70, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "mercenary_axe_strength"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredSword> WOODEN_SWORD = ITEMS.registerItem("wooden_sword", props -> new UnshatteredSword(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON).component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 1)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_sword_damage"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredSword> STONE_SWORD = ITEMS.registerItem("stone_sword", props -> new UnshatteredSword(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 1)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_sword_damage"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredSword> IRON_SWORD = ITEMS.registerItem("iron_sword", props -> new UnshatteredSword(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON).component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 3)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_sword_damage"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredSword> GOLDEN_SWORD = ITEMS.registerItem("golden_sword", props -> new UnshatteredSword(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON).component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 4)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_sword_damage"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredSword> DIAMOND_SWORD = ITEMS.registerItem("diamond_sword", props -> new UnshatteredSword(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 8)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_sword_damage"), 35, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredAxeWeapon> WOODEN_AXE = ITEMS.registerItem("wooden_axe", props -> new UnshatteredAxeWeapon(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 1)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_axe_damage"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredAxeWeapon> STONE_AXE = ITEMS.registerItem("stone_axe", props -> new UnshatteredAxeWeapon(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON).component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 2)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_axe_damage"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredAxeWeapon> GOLDEN_AXE = ITEMS.registerItem("golden_axe", props -> new UnshatteredAxeWeapon(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 1)
            .attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_axe_damage"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredAxeWeapon> IRON_AXE = ITEMS.registerItem("iron_axe", props -> new UnshatteredAxeWeapon(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 6)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_axe_damage"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredAxeWeapon> DIAMOND_AXE = ITEMS.registerItem("diamond_axe", props -> new UnshatteredAxeWeapon(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON)
            .component(DataComponentRegistry.DESCRIPTION.get(), true).component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 12)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_axe_damage"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    // todo: make it so the foraging fortune only applies in certain areas (the park, not the galatea)
    public static final DeferredItem<UnshatteredAxeTool> TREECAPITATOR = ITEMS.registerItem("treecapitator", props -> new UnshatteredAxeTool(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.EPIC)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 10000)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "treecapitator_sweep"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "treecapitator_foraging_fortune"), 100, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            12.0f
    ));
    // todo: make it so the foraging fortune only applies in certain areas (the park, not the galatea)
    public static final DeferredItem<UnshatteredAxeTool> SPRUCE_AXE = ITEMS.registerItem("spruce_axe", props -> new UnshatteredAxeTool(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "spruce_axe_sweep"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "spruce_axe_foraging_fortune"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            6.0f
    ));
    public static final DeferredItem<UnshatteredAxeTool> SERIOUSLY_DAMAGED_AXE = ITEMS.registerItem("seriously_damaged_axe", props -> new UnshatteredAxeTool(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "seriously_damaged_axe_sweep"), 7, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "seriously_damaged_axe_foraging_fortune"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            6.0f
    ));
    public static final DeferredItem<UnshatteredAxeTool> DECENT_AXE = ITEMS.registerItem("decent_axe", props -> new UnshatteredAxeTool(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "decent_axe_sweep"), 24, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "decent_axe_foraging_fortune"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            8.0f
    ));
    public static final DeferredItem<UnshatteredAxeTool> FIG_HEW = ITEMS.registerItem("fig_hew", props -> new UnshatteredAxeTool(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "fig_hew_sweep"), 7, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "fig_hew_foraging_fortune"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            10.0f
    ));
    public static final DeferredItem<UnshatteredAxeTool> FIGSTONE_SPLITTER = ITEMS.registerItem("figstone_splitter", props -> new UnshatteredAxeTool(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.EPIC)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "figstone_splitter_sweep"), 24, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "figstone_splitter_foraging_fortune"), 35, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            12.0f
    ));
    public static final DeferredItem<Item> LEAFLET_HELMET = ITEMS.registerItem("leaflet_helmet", props -> new Item(props
            .humanoidArmor(LEAFLET_ARMOR_MATERIAL, ArmorType.HELMET)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.HELMET)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 2)
            .attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_helmet_health"), 70, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_helmet_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                    .build()
            )
    ));
    public static final DeferredItem<Item> LEAFLET_CHESTPLATE = ITEMS.registerItem("leaflet_chestplate", props -> new Item(props
            .humanoidArmor(LEAFLET_ARMOR_MATERIAL, ArmorType.CHESTPLATE)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.CHESTPLATE)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 4)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_chestplate_health"), 80, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_chestplate_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                    .build()
            )
    ));
    public static final DeferredItem<Item> LEAFLET_LEGGINGS = ITEMS.registerItem("leaflet_leggings", props -> new Item(props
            .humanoidArmor(LEAFLET_ARMOR_MATERIAL, ArmorType.LEGGINGS)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.LEGGINGS)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 3)
            .attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_leggings_health"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS).add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_leggings_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS).build())));
    public static final DeferredItem<Item> LEAFLET_BOOTS = ITEMS.registerItem("leaflet_boots", props -> new Item(props
            .humanoidArmor(LEAFLET_ARMOR_MATERIAL, ArmorType.BOOTS)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.BOOTS)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 2)
            .component(DataComponentRegistry.SKILL_REQUIREMENT.get(), new SkillRequirement(PlayerSkillsAttachment.Skill.COMBAT, 4))
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_boots_health"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "leaflet_boots_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .build()
            )
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> BAT_THE_FISH = ITEMS.registerItem("bat_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> CENTURY_THE_FISH = ITEMS.registerItem("century_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> CHILL_THE_FISH = ITEMS.registerItem("chill_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1).component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> CLUNK_THE_FISH = ITEMS.registerItem("clunk_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1).component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> DIAMOND_THE_FISH = ITEMS.registerItem("diamond_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> DUST_THE_FISH = ITEMS.registerItem("dust_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> EGG_THE_FISH = ITEMS.registerItem("egg_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1).component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> EON_THE_FISH = ITEMS.registerItem("eon_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> FLAKE_THE_FISH = ITEMS.registerItem("flake_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH).component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> EXPERIMENT_THE_FISH = ITEMS.registerItem("experiment_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH).component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> FOSSIL_THE_FISH = ITEMS.registerItem("fossil_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH).component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> GABAGOOL_THE_FISH = ITEMS.registerItem("gabagool_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> GIFT_THE_FISH = ITEMS.registerItem("gift_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> HERRING_THE_FISH = ITEMS.registerItem("herring_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> NOPE_THE_FISH = ITEMS.registerItem("nope_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> OOPS_THE_FISH = ITEMS.registerItem("oops_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> PARTY_THE_FISH = ITEMS.registerItem("party_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> ROCK_THE_FISH = ITEMS.registerItem("rock_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> SHRIMP_THE_FISH = ITEMS.registerItem("shrimp_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> SKELETON_THE_FISH = ITEMS.registerItem("skeleton_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> SPOOK_THE_FISH = ITEMS.registerItem("spook_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> STEW_THE_FISH = ITEMS.registerItem("stew_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1).component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> SWAMP_THE_FISH = ITEMS.registerItem("swamp_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1).component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> ZOOP_THE_FISH = ITEMS.registerItem("zoop_the_fish", props -> new UnshatteredEnchantedItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.SPECIAL)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.FISH)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<UnshatteredEnchantedItem> ENCHANTED_FIG_LOG = ITEMS.registerItem("enchanted_fig_log", props -> new UnshatteredEnchantedItem(props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.LOG)
    ));
    public static final DeferredItem<Item> BEDROCK = ITEMS.registerItem("bedrock", props -> new Item(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.LEGENDARY)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.ITEM)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<Item> CAKE_SOUL = ITEMS.registerItem("cake_soul", props -> new Item(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.MYTHIC)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.ITEM)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<FishingRodItem> CHALLENGING_ROD = ITEMS.registerItem("challenging_rod", props -> new FishingRodItem(props
            .stacksTo(1)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.ITEM)
            .component(DataComponentRegistry.SKILL_REQUIREMENT.get(), new SkillRequirement(PlayerSkillsAttachment.Skill.FISHING, 5))
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "challenging_rod_damage"), 75, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "challenging_rod_strength"), 75, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<BlockItem> FIG_LOG = ITEMS.registerItem("fig_log", props -> new BlockItem(FIG_LOG_BLOCK.get(), props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.LOG)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
    ));
    public static final DeferredItem<BlockItem> BREAKABLE_FIG_LOG = ITEMS.registerItem("breakable_fig_log", props -> new BlockItem(BREAKABLE_FIG_LOG_BLOCK.get(), props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.LOG)
    ));
    public static final DeferredItem<BlockItem> BREAKABLE_STONE = ITEMS.registerItem("breakable_stone", props -> new BlockItem(BREAKABLE_STONE_BLOCK.get(), props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.LOG)
    ));
    public static final DeferredItem<BlockItem> BREAKABLE_COBBLESTONE = ITEMS.registerItem("breakable_cobblestone", props -> new BlockItem(BREAKABLE_COBBLESTONE_BLOCK.get(), props
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.COMMON)
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.LOG)
    ));
    public static final DeferredItem<Item> ROGUE_SWORD = ITEMS.registerItem("rogue_sword", RogueSword::new);
    public static final DeferredItem<Item> SQUIRE_SWORD = ITEMS.registerItem("squire_sword", props -> new Item(props
            .component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.SWORD)
            .component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON)
            .component(DataComponentRegistry.ITEM_SELL_VALUE.get(), 2500)
            .component(DataComponentRegistry.DESCRIPTION.get(), true)
            .component(DataComponentRegistry.SKILL_REQUIREMENT.get(), new SkillRequirement(PlayerSkillsAttachment.Skill.COMBAT, 4))
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "squire_sword_damage"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "squire_sword_strength"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));
    public static final DeferredItem<Item> UNDEAD_SWORD = ITEMS.registerItem("undead_sword", UndeadSword::new);
    public static final DeferredItem<Item> ZOMBIE_SWORD = ITEMS.registerItem("zombie_sword", ZombieSword::new);
    public static final DeferredItem<Item> ORNATE_ZOMBIE_SWORD = ITEMS.registerItem("ornate_zombie_sword", OrnateZombieSword::new);
    public static final DeferredItem<Item> FLORID_ZOMBIE_SWORD = ITEMS.registerItem("florid_zombie_sword", FloridZombieSword::new);
    public static final DeferredItem<Item> RUSTY_CLEAVER = ITEMS.registerItem("rusty_cleaver", RustyCleaver::new);
    public static final DeferredItem<Item> GOLDEN_CLEAVER = ITEMS.registerItem("golden_cleaver", GoldenCleaver::new);
    public static final DeferredItem<Item> SUPER_CLEAVER = ITEMS.registerItem("super_cleaver", SuperCleaver::new);
    public static final DeferredItem<Item> HYPER_CLEAVER = ITEMS.registerItem("hyper_cleaver", HyperCleaver::new);
    public static final DeferredItem<Item> GIANT_CLEAVER = ITEMS.registerItem("giant_cleaver", GiantCleaver::new);
}
