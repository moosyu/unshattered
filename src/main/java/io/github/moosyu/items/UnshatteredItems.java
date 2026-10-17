package io.github.moosyu.items;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.regions.UnshatteredRegions;
import io.github.moosyu.items.armours.SkeletonHat;
import io.github.moosyu.items.attachments.DrillEngineAttachment;
import io.github.moosyu.items.attachments.FriedGoblinEggAttachment;
import io.github.moosyu.items.attachments.FuelAttachmentItem;
import io.github.moosyu.items.talismans.*;
import io.github.moosyu.items.tools.UnshatteredMiningToolBase;
import io.github.moosyu.items.tools.axes.PromisingAxe;
import io.github.moosyu.items.tools.axes.RegionLockedFortuneAxe;
import io.github.moosyu.items.tools.axes.UnshatteredAxeTool;
import io.github.moosyu.items.tools.drills.DrillItem;
import io.github.moosyu.items.tools.drills.MithrilDrillSXR226;
import io.github.moosyu.items.tools.drills.MithrilDrillSXR326;
import io.github.moosyu.items.tools.pickaxes.*;
import io.github.moosyu.items.tools.rods.UnshatteredRod;
import io.github.moosyu.items.weapons.axes.AxeWeapon;
import io.github.moosyu.items.weapons.cleavers.*;
import io.github.moosyu.items.weapons.daggers.DaggerItem;
import io.github.moosyu.items.weapons.daggers.EmeraldDagger;
import io.github.moosyu.items.weapons.shortbows.ArtisanalShortbow;
import io.github.moosyu.items.weapons.swords.*;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.blocks.UnshatteredBlocks.*;

public class UnshatteredItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<AxeWeapon> MERCENARY_AXE = ITEMS.registerItem("mercenary_axe", props -> new AxeWeapon(props
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("mercenary_axe_damage"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("mercenary_axe_strength"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("mercenary_axe_attack_speed"), -3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<RegionLockedFortuneAxe> TREECAPITATOR = ITEMS.registerItem("treecapitator", props -> new RegionLockedFortuneAxe(props,
            50,
            5,
            12,
            UnshatteredRegions.PLAINS_REGION,
            UnshatteredUtils.getUnshatteredIdentifier("treecapitator_park_enthusiast")
    ));

    public static final DeferredItem<RegionLockedFortuneAxe> SPRUCE_AXE = ITEMS.registerItem("spruce_axe", props -> new RegionLockedFortuneAxe(props,
            25,
            2,
            6,
            UnshatteredRegions.PLAINS_REGION,
            UnshatteredUtils.getUnshatteredIdentifier("spruce_axe_park_enthusiast")
    ));

    public static final DeferredItem<UnshatteredAxeTool> SERIOUSLY_DAMAGED_AXE = ITEMS.registerItem("seriously_damaged_axe", props -> new UnshatteredAxeTool(props
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("seriously_damaged_axe_sweep"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("seriously_damaged_axe_foraging_fortune"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("seriously_damaged_axe_breaking_power"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            6.0f
    ));

    public static final DeferredItem<UnshatteredAxeTool> DECENT_AXE = ITEMS.registerItem("decent_axe", props -> new UnshatteredAxeTool(props
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("decent_axe_sweep"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("decent_axe_foraging_fortune"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("decent_axe_breaking_power"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            8.0f
    ));

    public static final DeferredItem<UnshatteredAxeTool> FIG_HEW = ITEMS.registerItem("fig_hew", props -> new UnshatteredAxeTool(props
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("fig_hew_sweep"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("fig_hew_foraging_fortune"), 12, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("fig_hew_breaking_power"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            10.0f
    ));

    public static final DeferredItem<UnshatteredAxeTool> FIGSTONE_SPLITTER = ITEMS.registerItem("figstone_splitter", props -> new UnshatteredAxeTool(props
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.SWEEP.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("figstone_splitter_sweep"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("figstone_splitter_foraging_fortune"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("figstone_splitter_breaking_power"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()),
            12.0f
    ));

    public static final DeferredItem<Item> LEAFLET_HELMET = createSimpleArmourPiece("leaflet_helmet",
            UnshatteredArmourMaterials.LEAFLET_MATERIAL,
            ArmorType.HELMET,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("leaflet_helmet_health"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("leaflet_helmet_foraging_fortune"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                    .build()
    );

    public static final DeferredItem<Item> LEAFLET_CHESTPLATE = createSimpleArmourPiece("leaflet_chestplate",
            UnshatteredArmourMaterials.LEAFLET_MATERIAL,
            ArmorType.CHESTPLATE,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("leaflet_chestplate_health"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("leaflet_chestplate_foraging_fortune"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                    .build()
    );

    public static final DeferredItem<Item> LEAFLET_LEGGINGS = createSimpleArmourPiece("leaflet_leggings",
            UnshatteredArmourMaterials.LEAFLET_MATERIAL,
            ArmorType.LEGGINGS,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("leaflet_leggings_health"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("leaflet_leggings_foraging_fortune"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
                    .build()
    );

    public static final DeferredItem<Item> LEAFLET_BOOTS = createSimpleArmourPiece("leaflet_boots",
            UnshatteredArmourMaterials.LEAFLET_MATERIAL,
            ArmorType.BOOTS,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("leaflet_boots_health"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .add(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("leaflet_boots_foraging_fortune"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .build()
    );

    public static final DeferredItem<EnchantedItem> BAT_THE_FISH = ITEMS.registerItem("bat_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> CENTURY_THE_FISH = ITEMS.registerItem("century_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> CHILL_THE_FISH = ITEMS.registerItem("chill_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> CLUNK_THE_FISH = ITEMS.registerItem("clunk_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> DIAMOND_THE_FISH = ITEMS.registerItem("diamond_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> DUST_THE_FISH = ITEMS.registerItem("dust_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> EGG_THE_FISH = ITEMS.registerItem("egg_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> EON_THE_FISH = ITEMS.registerItem("eon_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> FLAKE_THE_FISH = ITEMS.registerItem("flake_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> EXPERIMENT_THE_FISH = ITEMS.registerItem("experiment_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> FOSSIL_THE_FISH = ITEMS.registerItem("fossil_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> GABAGOOL_THE_FISH = ITEMS.registerItem("gabagool_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> GIFT_THE_FISH = ITEMS.registerItem("gift_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> HERRING_THE_FISH = ITEMS.registerItem("herring_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> NOPE_THE_FISH = ITEMS.registerItem("nope_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> OOPS_THE_FISH = ITEMS.registerItem("oops_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> PARTY_THE_FISH = ITEMS.registerItem("party_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> ROCK_THE_FISH = ITEMS.registerItem("rock_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> SHRIMP_THE_FISH = ITEMS.registerItem("shrimp_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> SKELETON_THE_FISH = ITEMS.registerItem("skeleton_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> SPOOK_THE_FISH = ITEMS.registerItem("spook_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> STEW_THE_FISH = ITEMS.registerItem("stew_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> SWAMP_THE_FISH = ITEMS.registerItem("swamp_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> ZOOP_THE_FISH = ITEMS.registerItem("zoop_the_fish", props -> new EnchantedItem(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> ENCHANTED_FIG_LOG = ITEMS.registerItem("enchanted_fig_log", EnchantedItem::new);

    public static final DeferredItem<Item> BEDROCK = ITEMS.registerItem("bedrock", props -> new Item(props
            .stacksTo(1)
    ));

    public static final DeferredItem<Item> CAKE_SOUL = ITEMS.registerItem("cake_soul", props -> new Item(props
            .stacksTo(1)
    ));

    public static final DeferredItem<UnshatteredRod> CHALLENGING_ROD = ITEMS.registerItem("challenging_rod", props -> new UnshatteredRod(props
            .stacksTo(1)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("challenging_rod_damage"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("challenging_rod_strength"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FISHING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("challenging_rod_fishing_fortune"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FISHING_SPEED.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("challenging_rod_fishing_speed"), 9, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<UnshatteredRod> FISHING_ROD = ITEMS.registerItem("fishing_rod", props -> new UnshatteredRod(props
            .stacksTo(1)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("fishing_rod_damage"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("fishing_rod_strength"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<BlockItem> FIG_LOG = ITEMS.registerItem("fig_log", props -> new BlockItem(FIG_LOG_BLOCK.get(), props));

    public static final DeferredItem<EnchantedItem> ENCHANTED_ROTTEN_FLESH = ITEMS.registerItem("enchanted_rotten_flesh", EnchantedItem::new);

    public static final DeferredItem<Item> ZOMBIE_HEART = ITEMS.registerItem("zombie_heart", props -> new Item(props
            .stacksTo(1)
    ));

    public static final DeferredItem<EnchantedItem> GOLDEN_POWDER = ITEMS.registerItem("golden_powder", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_GOLD_INGOT = ITEMS.registerItem("enchanted_gold_ingot", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_GOLD_BLOCK = ITEMS.registerItem("enchanted_gold_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_DIAMOND = ITEMS.registerItem("enchanted_diamond", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_DIAMOND_BLOCK = ITEMS.registerItem("enchanted_diamond_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_EMERALD = ITEMS.registerItem("enchanted_emerald", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_EMERALD_BLOCK = ITEMS.registerItem("enchanted_emerald_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_IRON = ITEMS.registerItem("enchanted_iron", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_IRON_BLOCK = ITEMS.registerItem("enchanted_iron_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_COAL = ITEMS.registerItem("enchanted_coal", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_COAL_BLOCK = ITEMS.registerItem("enchanted_coal_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_LAPIS = ITEMS.registerItem("enchanted_lapis", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_LAPIS_BLOCK = ITEMS.registerItem("enchanted_lapis_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_REDSTONE = ITEMS.registerItem("enchanted_redstone", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_REDSTONE_BLOCK = ITEMS.registerItem("enchanted_redstone_block", EnchantedItem::new);

    public static final DeferredItem<Item> HEALING_TISSUE = ITEMS.registerItem("healing_tissue", Item::new);

    public static final DeferredItem<BlockItem> BREAKABLE_FIG_LOG = ITEMS.registerSimpleBlockItem(BREAKABLE_FIG_LOG_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_STONE = ITEMS.registerSimpleBlockItem(BREAKABLE_STONE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_COAL_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_COAL_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_WHEAT = ITEMS.registerSimpleBlockItem(BREAKABLE_WHEAT_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_COBBLESTONE = ITEMS.registerSimpleBlockItem(BREAKABLE_COBBLESTONE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_IRON_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_IRON_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_COPPER_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_COPPER_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_GOLD_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_GOLD_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_REDSTONE_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_REDSTONE_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_EMERALD_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_EMERALD_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_DIAMOND_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_DIAMOND_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> PURE_DIAMOND = ITEMS.registerSimpleBlockItem(PURE_DIAMOND_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_OBSIDIAN = ITEMS.registerSimpleBlockItem(BREAKABLE_OBSIDIAN_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_LAPIS_ORE = ITEMS.registerSimpleBlockItem(BREAKABLE_LAPIS_ORE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_ICE = ITEMS.registerSimpleBlockItem(BREAKABLE_ICE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_CARROT = ITEMS.registerSimpleBlockItem(BREAKABLE_CARROT_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_POTATO = ITEMS.registerSimpleBlockItem(BREAKABLE_POTATO_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_MELON = ITEMS.registerSimpleBlockItem(BREAKABLE_MELON_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_SUGAR_CANE = ITEMS.registerSimpleBlockItem(BREAKABLE_SUGARCANE_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_CACTUS = ITEMS.registerSimpleBlockItem(BREAKABLE_CACTUS_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_SAND = ITEMS.registerSimpleBlockItem(BREAKABLE_SAND_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_BROWN_MUSHROOM_BLOCK = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.BREAKABLE_BROWN_MUSHROOM_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_RED_MUSHROOM_BLOCK = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.BREAKABLE_RED_MUSHROOM_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_BROWN_MUSHROOM = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.BREAKABLE_BROWN_MUSHROOM.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_RED_MUSHROOM = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.BREAKABLE_RED_MUSHROOM.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_COCOA_BEAN = ITEMS.registerSimpleBlockItem(BREAKABLE_COCOA_BEAN_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> BREAKABLE_PUMPKIN = ITEMS.registerSimpleBlockItem(BREAKABLE_PUMPKIN_BLOCK.getDelegate());
    public static final DeferredItem<BlockItem> GIANT_POTATO = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.GIANT_POTATO.getDelegate());
    public static final DeferredItem<BlockItem> GIANT_CARROT = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.GIANT_CARROT.getDelegate());

    public static final DeferredItem<Item> ROGUE_SWORD = ITEMS.registerItem("rogue_sword", RogueSword::new);

    public static final DeferredItem<Item> SQUIRE_SWORD = ITEMS.registerItem("squire_sword", props -> new Item(props
            .stacksTo(1)
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("squire_sword_damage"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("squire_sword_strength"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<Item> UNDEAD_SWORD = ITEMS.registerItem("undead_sword", UndeadSword::new);

    public static final DeferredItem<ZombieSwordBase> ZOMBIE_SWORD = ITEMS.registerItem("zombie_sword",
            props -> new ZombieSwordBase(props.component(UnshatteredDataComponents.CHARGES.get(), new ItemCharges(4, 300)),
                    "zombie_sword",
                    28,
                    12,
                    20,
                    40,
                    24,
                    10
            )
    );

    public static final DeferredItem<Item> BROKEN_MITHRIL_PICKAXE = ITEMS.registerItem("broken_mithril_pickaxe",
            props -> new MithriPickaxeBase(props, 13, 2, "broken_mithril_pickaxe", 20)
    );

    public static final DeferredItem<Item> BANDAGED_MITHRIL_PICKAXE = ITEMS.registerItem("bandaged_mithril_pickaxe",
            props -> new MithriPickaxeBase(props, 15, 4, "bandaged_mithril_pickaxe", 25)
    );

    public static final DeferredItem<Item> MITHRIL_PICKAXE = ITEMS.registerItem("mithril_pickaxe",
            props -> new MithriPickaxeBase(props, 17, 7, "mithril_pickaxe", 30)
    );

    public static final DeferredItem<Item> RUSTED_TITANIUM_PICKAXE = ITEMS.registerItem("rusted_titanium_pickaxe",
            props -> new TitaniumPickaxeBase(props, "rusted_titanium_pickaxe", 15, 15)
    );

    public static final DeferredItem<Item> TITANIUM_PICKAXE = ITEMS.registerItem("titanium_pickaxe",
            props -> new TitaniumPickaxeBase(props, "titanium_pickaxe", 20, 20)
    );

    public static final DeferredItem<Item> LAPIS_PICKAXE = ITEMS.registerItem("lapis_pickaxe",
            props -> new UnshatteredMiningToolBase(props.stacksTo(1)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(UnshatteredAttributeValues.DAMAGE.holder,
                                    new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_pickaxe_damage"),
                                            3,
                                            AttributeModifier.Operation.ADD_VALUE
                                    ), EquipmentSlotGroup.MAINHAND
                            ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                                    new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_pickaxe_mining_speed"),
                                            10,
                                            AttributeModifier.Operation.ADD_VALUE
                                    ), EquipmentSlotGroup.MAINHAND
                            ).add(UnshatteredAttributeValues.MINING_FORTUNE.holder,
                                    new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_pickaxe_mining_fortune"),
                                            4,
                                            AttributeModifier.Operation.ADD_VALUE
                                    ), EquipmentSlotGroup.MAINHAND
                            ).add(UnshatteredAttributeValues.BREAKING_POWER.holder,
                                    new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_pickaxe_breaking_power"),
                                            3,
                                            AttributeModifier.Operation.ADD_VALUE
                                    ), EquipmentSlotGroup.MAINHAND
                            ).add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_pickaxe_attack_speed"),
                                            -2.8,
                                            AttributeModifier.Operation.ADD_VALUE
                                    ), EquipmentSlotGroup.MAINHAND
                            ).build()
                    )
           )
    );

    public static final DeferredItem<ZombieSwordBase> ORNATE_ZOMBIE_SWORD = ITEMS.registerItem("ornate_zombie_sword",
            props -> new ZombieSwordBase(props.component(UnshatteredDataComponents.CHARGES.get(), new ItemCharges(5, 300)),
                    "ornate_zombie_sword",
                    35,
                    15,
                    25,
                    50,
                    30,
                    10
            )
    );

    public static final DeferredItem<ZombieSwordBase> FLORID_ZOMBIE_SWORD = ITEMS.registerItem("florid_zombie_sword",
            props -> new ZombieSwordBase(props.component(UnshatteredDataComponents.CHARGES.get(), new ItemCharges(5, 300)),
                    "florid_zombie_sword",
                    42,
                    20,
                    30,
                    60,
                    36,
                    10
            )
    );

    public static final DeferredItem<Item> RUSTY_CLEAVER = ITEMS.registerItem("rusty_cleaver", RustyCleaver::new);
    public static final DeferredItem<Item> GOLDEN_CLEAVER = ITEMS.registerItem("golden_cleaver", GoldenCleaver::new);
    public static final DeferredItem<Item> SUPER_CLEAVER = ITEMS.registerItem("super_cleaver", SuperCleaver::new);
    public static final DeferredItem<Item> HYPER_CLEAVER = ITEMS.registerItem("hyper_cleaver", HyperCleaver::new);
    public static final DeferredItem<Item> GIANT_CLEAVER = ITEMS.registerItem("giant_cleaver", GiantCleaver::new);
    public static final DeferredItem<BatTalismanBase> BAT_TALISMAN = ITEMS.registerItem("bat_talisman", props -> new BatTalismanBase(props, 0.05f));

    public static final DeferredItem<DaggerItem> IRON_DAGGER = ITEMS.registerItem("iron_dagger", props -> new DaggerItem(props
            .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("iron_dagger_damage"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.FEROCITY.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("iron_dagger_ferocity"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("iron_dagger_attack_speed"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
    ));

    public static final DeferredItem<DaggerItem> EMERALD_DAGGER = ITEMS.registerItem("emerald_dagger", EmeraldDagger::new);

    public static final DeferredItem<Item> COINS_TALISMAN = ITEMS.registerItem("coins_talisman", CoinTalisman::new);

    public static final DeferredItem<Item> GLOW_SQUID_BOOTS = createSimpleArmourPiece("glow_squid_boots",
            UnshatteredArmourMaterials.GLOW_SQUID_BOOTS_MATERIAL,
            ArmorType.BOOTS,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.FISHING_FORTUNE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("squid_boots_fishing_fortune"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .add(UnshatteredAttributeValues.HEALTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("squid_boots_health"), 12, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                    .build()
    );

    public static final DeferredItem<BlockItem> ROCK_TALKABLE_BLOCK = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.ROCK_TALKABLE_BLOCK.getDelegate());

    public static final DeferredItem<BlockItem> BREAKABLE_SOFT_MITHRIL_BLOCK = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.BREAKABLE_SOFT_MITHRIL_BLOCK.getDelegate());

    public static final DeferredItem<BlockItem> BREAKABLE_HARD_MITHRIL_BLOCK = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK.getDelegate());

    public static final DeferredItem<BlockItem> BREAKABLE_COBBLED_MITHRIL_BLOCK = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.BREAKABLE_COBBLED_MITHRIL_BLOCK.getDelegate());

    public static final DeferredItem<BlockItem> BREAKABLE_TITANIUM = ITEMS.registerSimpleBlockItem(UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK.getDelegate());

    public static final DeferredItem<Item> MITHRIL = ITEMS.registerItem("mithril", Item::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_MITHRIL = ITEMS.registerItem("enchanted_mithril", EnchantedItem::new);

    public static final DeferredItem<BlockItem> BREAKABLE_OAK_LOG = ITEMS.registerSimpleBlockItem(BREAKABLE_OAK_LOG_BLOCK.getDelegate());

    public static final DeferredItem<EnchantedItem> ENCHANTED_OAK_LOG = ITEMS.registerItem("enchanted_oak_log", EnchantedItem::new);

    public static final DeferredItem<BlockItem> BREAKABLE_BIRCH_LOG = ITEMS.registerSimpleBlockItem(BREAKABLE_BIRCH_LOG_BLOCK.getDelegate());

    public static final DeferredItem<EnchantedItem> ENCHANTED_BIRCH_LOG = ITEMS.registerItem("enchanted_birch_log", EnchantedItem::new);

    public static final DeferredItem<BlockItem> BREAKABLE_SPRUCE_LOG = ITEMS.registerSimpleBlockItem(BREAKABLE_SPRUCE_LOG_BLOCK.getDelegate());

    public static final DeferredItem<EnchantedItem> ENCHANTED_SPRUCE_LOG = ITEMS.registerItem("enchanted_spruce_log", EnchantedItem::new);

    public static final DeferredItem<BlockItem> BREAKABLE_JUNGLE_LOG = ITEMS.registerSimpleBlockItem(BREAKABLE_JUNGLE_LOG_BLOCK.getDelegate());

    public static final DeferredItem<EnchantedItem> ENCHANTED_JUNGLE_LOG = ITEMS.registerItem("enchanted_jungle_log", EnchantedItem::new);

    public static final DeferredItem<BlockItem> BREAKABLE_ACACIA_LOG = ITEMS.registerSimpleBlockItem(BREAKABLE_ACACIA_LOG_BLOCK.getDelegate());

    public static final DeferredItem<EnchantedItem> ENCHANTED_ACACIA_LOG = ITEMS.registerItem("enchanted_acacia_log", EnchantedItem::new);

    public static final DeferredItem<BlockItem> BREAKABLE_DARK_OAK_LOG = ITEMS.registerSimpleBlockItem(BREAKABLE_DARK_OAK_LOG_BLOCK.getDelegate());

    public static final DeferredItem<EnchantedItem> ENCHANTED_DARK_OAK_LOG = ITEMS.registerItem("enchanted_dark_oak_log", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_POISONOUS_POTATO = ITEMS.registerItem("enchanted_poisonous_potato", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_BONE = ITEMS.registerItem("enchanted_bone", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_BONE_BLOCK = ITEMS.registerItem("enchanted_bone_block", EnchantedItem::new);

    public static final DeferredItem<Item> SKELETON_HAT = ITEMS.registerItem("skeleton_hat", SkeletonHat::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_COBBLESTONE = ITEMS.registerItem("enchanted_cobblestone", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_FLINT = ITEMS.registerItem("enchanted_flint", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_STRING = ITEMS.registerItem("enchanted_string", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_OBSIDIAN = ITEMS.registerItem("enchanted_obsidian", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_CLAY_BALL = ITEMS.registerItem("enchanted_clay_ball", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_CLAY_BLOCK = ITEMS.registerItem("enchanted_clay_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_INK_SAC = ITEMS.registerItem("enchanted_ink_sac", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_LILY_PAD = ITEMS.registerItem("enchanted_lily_pad", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_PRISMARINE_SHARD = ITEMS.registerItem("enchanted_prismarine_shard", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_PRISMARINE_CRYSTALS = ITEMS.registerItem("enchanted_prismarine_crystals", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_PUFFERFISH = ITEMS.registerItem("enchanted_pufferfish", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_COD = ITEMS.registerItem("enchanted_cod", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_COOKED_COD = ITEMS.registerItem("enchanted_cooked_cod", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_SALMON = ITEMS.registerItem("enchanted_salmon", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_COOKED_SALMON = ITEMS.registerItem("enchanted_cooked_salmon", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_SPONGE = ITEMS.registerItem("enchanted_sponge", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_WET_SPONGE = ITEMS.registerItem("enchanted_wet_sponge", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_TROPICAL_FISH = ITEMS.registerItem("enchanted_tropical_fish", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_RAW_PORK_CHOP = ITEMS.registerItem("enchanted_raw_pork_chop", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_HONEYCOMB = ITEMS.registerItem("enchanted_honeycomb", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_ICE = ITEMS.registerItem("enchanted_ice", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_PACKED_ICE = ITEMS.registerItem("enchanted_packed_ice", EnchantedItem::new);

    public static final DeferredItem<Item> TITANIUM = ITEMS.registerItem("titanium", Item::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_TITANIUM = ITEMS.registerItem("enchanted_titanium", EnchantedItem::new);

    public static final DeferredItem<Item> RAINBOW_YARN_TALISMAN = ITEMS.registerItem("rainbow_yarn_talisman", RainbowYarnTalisman::new);

    public static final DeferredItem<Item> DWARF_TURTLE_SHELL = ITEMS.registerItem("dwarf_turtle_shell", DwarfTurtleTalisman::new);

    public static final DeferredItem<Item> PIGGY_BANK_TALISMAN = ITEMS.registerItem("piggy_bank", PiggyBankTalisman::new);

    public static final DeferredItem<Item> HONEYCOMB_RING = ITEMS.registerItem("honeycomb_ring", HoneycombRing::new);

    public static final DeferredItem<Item> HASTE_RING = ITEMS.registerItem("haste_ring", HasteRing::new);

    public static final DeferredItem<Item> BLOOD_CHALICE = ITEMS.registerItem("blood_chalice", BloodChalice::new);

    public static final DeferredItem<Item> INFINITE_QUIVER = ITEMS.registerItem("infinite_quiver", InfiniteQuiver::new);

    public static final DeferredItem<Item> ARTISANAL_SHORTBOW = ITEMS.registerItem("artisanal_shortbow", ArtisanalShortbow::new);

    public static final DeferredItem<Item> ZOMBIE_PICKAXE = ITEMS.registerItem("zombie_pickaxe", ZombiePickaxe::new);

    public static final DeferredItem<Item> PROMISING_PICKAXE = ITEMS.registerItem("promising_pickaxe", PromisingPickaxe::new);

    public static final DeferredItem<Item> PROMISING_AXE = ITEMS.registerItem("promising_axe", PromisingAxe::new);

    public static final DeferredItem<Item> FIRE_TALISMAN = ITEMS.registerItem("fire_talisman", FireTalisman::new);

    public static final DeferredItem<Item> MITHRIL_DRILL_SX_R226 = ITEMS.registerItem("mithril_drill_sx_r226", MithrilDrillSXR226::new);

    public static final DeferredItem<Item> MITHRIL_DRILL_SX_R326 = ITEMS.registerItem("mithril_drill_sx_r326", MithrilDrillSXR326::new);

    public static final DeferredItem<Item> TITANIUM_DRILL_DR_X355 = ITEMS.registerItem("titanium_drill_dr_x355", props ->
            new DrillItem(props,
                    10,
                    90,
                    18,
                    7,
                    "titanium_drill_dr_x355"
            )
    );

    public static final DeferredItem<Item> TITANIUM_DRILL_DR_X455 = ITEMS.registerItem("titanium_drill_dr_x455", props ->
            new DrillItem(props,
                    10,
                    110,
                    23,
                    8,
                    "titanium_drill_dr_x455"
            )
    );

    public static final DeferredItem<Item> TITANIUM_DRILL_DR_X555 = ITEMS.registerItem("titanium_drill_dr_x555", props ->
            new DrillItem(props,
                    10,
                    140,
                    30,
                    9,
                    "titanium_drill_dr_x555"
            )
    );

    public static final DeferredItem<Item> TITANIUM_DRILL_DR_X655 = ITEMS.registerItem("titanium_drill_dr_x655", props ->
            new DrillItem(props,
                    10,
                    180,
                    40,
                    9,
                    "titanium_drill_dr_x655"
            )
    );

    public static final DeferredItem<EnchantedItem> ENCHANTED_BLAZE_POWDER = ITEMS.registerItem("enchanted_blaze_powder", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_BLAZE_ROD = ITEMS.registerItem("enchanted_blaze_rod", EnchantedItem::new);

    public static final DeferredItem<Item> MITHRIL_INFUSED_FUEL_TANK = ITEMS.registerItem("mithril_infused_fuel_tank", props -> new FuelAttachmentItem(props,10000, -0.2f));

    public static final DeferredItem<EnchantedItem> ENCHANTED_WOOL = ITEMS.registerItem("enchanted_wool", EnchantedItem::new);

    public static final DeferredItem<Item> MITHRIL_PLATED_DRILL_ENGINE = ITEMS.registerItem("mithril_plated_drill_engine", props -> new DrillEngineAttachment(props,8, 5, "mithril_plated_drill_engine"));

    public static final DeferredItem<Item> FRIED_GOBLIN_EGG = ITEMS.registerItem("fried_goblin_egg", FriedGoblinEggAttachment::new);

    public static final DeferredItem<Item> REFINED_MITHRIL = ITEMS.registerItem("refined_mithril", Item::new);

    public static final DeferredItem<Item> TREASURITE = ITEMS.registerItem("treasurite", Item::new);

    public static final DeferredItem<Item> REFINED_DIAMOND = ITEMS.registerItem("refined_diamond", Item::new);

    public static final DeferredItem<Item> GLACITE_JEWEL = ITEMS.registerItem("glacite_jewel", Item::new);

    public static final DeferredItem<Item> FUEL_CANISTER = ITEMS.registerItem("fuel_canister", Item::new);

    public static final DeferredItem<Item> GOLDEN_PLATE = ITEMS.registerItem("golden_plate", Item::new);

    public static final DeferredItem<Item> REFINED_TITANIUM = ITEMS.registerItem("refined_titanium", Item::new);

    public static final DeferredItem<Item> MITHRIL_PLATE = ITEMS.registerItem("mithril_plate", Item::new);

    public static final DeferredItem<Item> DRILL_MOTOR = ITEMS.registerItem("drill_motor", props -> new Item(props.stacksTo(1)));

    public static final DeferredItem<Item> GOBLIN_EGG = ITEMS.registerItem("goblin_egg", Item::new);

    public static final DeferredItem<Item> OIL_BARREL = ITEMS.registerItem("oil_barrel", Item::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_POPPY = ITEMS.registerItem("enchanted_poppy", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_DANDELION = ITEMS.registerItem("enchanted_dandelion", EnchantedItem::new);

    public static final DeferredItem<Item> BIOFUEL = ITEMS.registerItem("biofuel", Item::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_WHEAT = ITEMS.registerItem("enchanted_wheat", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_CARROT = ITEMS.registerItem("enchanted_carrot", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_POTATO = ITEMS.registerItem("enchanted_potato", EnchantedItem::new);

    public static final DeferredItem<Item> ROOKIE_HOE = ITEMS.registerItem("rookie_hoe", props -> new Item(props.stacksTo(1)
            .attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_SPEED,
                    new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("rooke_hoe_attack_speed"), -2.0f, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
            ).add(UnshatteredAttributeValues.FARMING_FORTUNE.holder,
                    new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("rooke_hoe_farming_fortune"), 5.0f, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
            ).build()))
    );

    public static final DeferredItem<EnchantedItem> ENCHANTED_MELON_SLICE = ITEMS.registerItem("enchanted_melon_slice", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_MELON = ITEMS.registerItem("enchanted_melon", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_SUGAR = ITEMS.registerItem("enchanted_sugar", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_SUGAR_CANE = ITEMS.registerItem("enchanted_sugar_cane", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_PUMPKIN = ITEMS.registerItem("enchanted_pumpkin", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_COCOA_BEANS = ITEMS.registerItem("enchanted_cocoa_beans", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_COOKIE = ITEMS.registerItem("enchanted_cookie", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_RED_MUSHROOM = ITEMS.registerItem("enchanted_red_mushroom", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_BROWN_MUSHROOM = ITEMS.registerItem("enchanted_brown_mushroom", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_RED_MUSHROOM_BLOCK = ITEMS.registerItem("enchanted_red_mushroom_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_BROWN_MUSHROOM_BLOCK = ITEMS.registerItem("enchanted_brown_mushroom_block", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_SAND = ITEMS.registerItem("enchanted_sand", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_CACTUS_GREEN = ITEMS.registerItem("enchanted_cactus_green", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_CACTUS = ITEMS.registerItem("enchanted_cactus", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_BAKED_POTATO = ITEMS.registerItem("enchanted_baked_potato", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_HAY_BALE = ITEMS.registerItem("enchanted_hay_bale", EnchantedItem::new);

    public static final DeferredItem<EnchantedItem> ENCHANTED_GOLDEN_CARROT = ITEMS.registerItem("enchanted_golden_carrot", EnchantedItem::new);

    public static final DeferredItem<Item> POLISHED_PUMPKIN = ITEMS.registerItem("polished_pumpkin", Item::new);

    public static final DeferredItem<Item> STRETCHING_STICK = ITEMS.registerItem("stretching_stick", Item::new);

    public static final DeferredItem<Item> FIGSTONE = ITEMS.registerItem("figstone", Item::new);

    public static final DeferredItem<Item> PLASMA = ITEMS.registerItem("plasma", Item::new);

    public static final DeferredItem<Item> BEJEWELED_HANDLE = ITEMS.registerItem("bejeweled_handle", Item::new);

    public static final DeferredItem<BatTalismanBase> BAT_ARTIFACT = ITEMS.registerItem("bat_artifact", props -> new BatTalismanBase(props, 0.15f));

    public static final DeferredItem<Item> GREEN_CANDY = ITEMS.registerItem("green_candy", Item::new);

    public static final DeferredItem<Item> PURPLE_CANDY = ITEMS.registerItem("purple_candy", Item::new);

    public static final DeferredItem<Item> LAPIS_HELMET = createSimpleArmourPiece("lapis_helmet",
            UnshatteredArmourMaterials.LAPIS_ARMOUR_MATERIAL,
            ArmorType.HELMET,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DEFENCE.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_helmet_defence"), 4, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.HEAD
                    ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_helmet_mining_speed"), 3, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.HEAD
                    ).add(UnshatteredAttributeValues.MINING_FORTUNE.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_helmet_mining_fortune"), 1, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.HEAD
                    ).add(UnshatteredAttributeValues.MINING_WISDOM.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_helmet_mining_wisdom"), 10, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.HEAD
                    ).build()
    );

    public static final DeferredItem<Item> LAPIS_CHESTPLATE = createSimpleArmourPiece("lapis_chestplate",
            UnshatteredArmourMaterials.LAPIS_ARMOUR_MATERIAL,
            ArmorType.CHESTPLATE,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DEFENCE.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_chestplate_defence"), 7, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.CHEST
                    ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_chestplate_mining_speed"), 3, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.CHEST
                    ).add(UnshatteredAttributeValues.MINING_FORTUNE.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_chestplate_mining_fortune"), 2, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.CHEST
                    ).add(UnshatteredAttributeValues.MINING_WISDOM.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_chestplate_mining_wisdom"), 10, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.CHEST
                    ).build()
    );

    public static final DeferredItem<Item> LAPIS_LEGGINGS = createSimpleArmourPiece("lapis_leggings",
            UnshatteredArmourMaterials.LAPIS_ARMOUR_MATERIAL,
            ArmorType.LEGGINGS,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DEFENCE.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_leggings_defence"), 6, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.LEGS
                    ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_leggings_mining_speed"), 3, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.LEGS
                    )
                    .add(UnshatteredAttributeValues.MINING_FORTUNE.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_leggings_mining_fortune"), 1, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.LEGS
                    ).add(UnshatteredAttributeValues.MINING_WISDOM.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_leggings_mining_wisdom"), 10, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.LEGS
                    ).build()
    );

    public static final DeferredItem<Item> LAPIS_BOOTS = createSimpleArmourPiece("lapis_boots",
            UnshatteredArmourMaterials.LAPIS_ARMOUR_MATERIAL,
            ArmorType.BOOTS,
            ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DEFENCE.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_boots_defence"), 3, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.FEET
                    ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_boots_mining_speed"), 3, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.FEET
                    ).add(UnshatteredAttributeValues.MINING_FORTUNE.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_boots_mining_fortune"), 1, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.FEET
                    ).add(UnshatteredAttributeValues.MINING_WISDOM.holder,
                            new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("lapis_boots_mining_wisdom"), 10, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.FEET
                    ).build()
    );

    public static final DeferredItem<EnchantedItem> ENCHANTED_GUNPOWDER = ITEMS.registerItem("enchanted_gunpowder", EnchantedItem::new);

    private static DeferredItem<Item> createSimpleArmourPiece(String name, UnshatteredArmourMaterials.UnshatteredArmourMaterial material, ArmorType type, ItemAttributeModifiers attributeModifiers) {
        return ITEMS.registerItem(name, props -> new Item(props
                .stacksTo(1)
                .component(DataComponents.EQUIPPABLE, Equippable.builder(type.getSlot())
                        .setEquipSound(material.equipSound())
                        .setAsset(material.assetId())
                        .build()
                )
                .attributes(attributeModifiers)
        ));
    }
}