package io.github.moosyu.data.datagen;

import io.github.moosyu.data.attachments.PlayerPowderAttachment;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.drops.BlockBreakData;
import io.github.moosyu.data.drops.DropData;
import io.github.moosyu.data.drops.MobItemDropData;
import io.github.moosyu.data.drops.MobRewardData;
import io.github.moosyu.data.fishing.FishingConditions;
import io.github.moosyu.data.fishing.FishingWeightEntry;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.items.armours.SkeletonHat;
import io.github.moosyu.items.talismans.BloodChalice;
import io.github.moosyu.items.talismans.DwarfTurtleTalisman;
import io.github.moosyu.items.talismans.HasteRing;
import io.github.moosyu.items.talismans.RainbowYarnTalisman;
import io.github.moosyu.items.tools.pickaxes.ZombiePickaxe;
import io.github.moosyu.items.tools.rods.UnshatteredRod;
import io.github.moosyu.items.weapons.daggers.EmeraldDagger;
import io.github.moosyu.items.weapons.swords.RogueSword;
import io.github.moosyu.items.weapons.swords.UndeadSword;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.data.UnshatteredDataMaps.*;

public class UnshatteredDataMapProvider extends DataMapProvider {
    public UnshatteredDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NonNull Provider provider) {
        Builder<BlockBreakData, Block> blockBreakBuilder = builder(BLOCK_BREAK_DATA);

        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK, new ItemRange(UnshatteredItems.FIG_LOG.get()), PlayerSkillsAttachment.Skill.FORAGING, 15);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_STONE_BLOCK, new ItemRange(Items.COBBLESTONE), PlayerSkillsAttachment.Skill.MINING, 1);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK, new ItemRange(Items.COBBLESTONE), PlayerSkillsAttachment.Skill.MINING, 1);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK, new ItemRange(Items.COAL), PlayerSkillsAttachment.Skill.MINING, 4);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_WHEAT_BLOCK, new ItemRange(Items.WHEAT, 1, 2), PlayerSkillsAttachment.Skill.MINING, 4);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_IRON_ORE_BLOCK, new ItemRange(Items.IRON_INGOT), PlayerSkillsAttachment.Skill.MINING, 5);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_COPPER_ORE_BLOCK, new ItemRange(Items.COPPER_INGOT, 2, 5), PlayerSkillsAttachment.Skill.MINING, 5);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_GOLD_ORE_BLOCK, new ItemRange(Items.GOLD_INGOT), PlayerSkillsAttachment.Skill.MINING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_REDSTONE_ORE_BLOCK, new ItemRange(Items.REDSTONE, 4, 5), PlayerSkillsAttachment.Skill.MINING, 7);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_EMERALD_ORE_BLOCK, new ItemRange(Items.EMERALD), PlayerSkillsAttachment.Skill.MINING, 9);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_DIAMOND_ORE_BLOCK, new ItemRange(Items.DIAMOND), PlayerSkillsAttachment.Skill.MINING, 10);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_LAPIS_ORE_BLOCK, new ItemRange(Items.LAPIS_LAZULI, 4, 9), PlayerSkillsAttachment.Skill.MINING, 7);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.PURE_DIAMOND_BLOCK, new ItemRange(Items.DIAMOND, 8, 10), PlayerSkillsAttachment.Skill.MINING, 20);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_OBSIDIAN_BLOCK, new ItemRange(Items.OBSIDIAN), PlayerSkillsAttachment.Skill.MINING, 20);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_OAK_LOG_BLOCK, new ItemRange(Items.OAK_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_BIRCH_LOG_BLOCK, new ItemRange(Items.BIRCH_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_SPRUCE_LOG_BLOCK, new ItemRange(Items.SPRUCE_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_JUNGLE_LOG_BLOCK, new ItemRange(Items.JUNGLE_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_ACACIA_LOG_BLOCK, new ItemRange(Items.ACACIA_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_DARK_OAK_LOG_BLOCK, new ItemRange(Items.DARK_OAK_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_ICE_BLOCK, new ItemRange(Items.ICE), PlayerSkillsAttachment.Skill.MINING, 0.5f);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK, new ItemRange(UnshatteredItems.TITANIUM.get(), 2), PlayerSkillsAttachment.Skill.MINING, 100);

        builder(BLOCK_BREAK_DATA)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COBBLED_MITHRIL_BLOCK.get()),
                        new BlockBreakData(PlayerSkillsAttachment.Skill.MINING, 25, List.of(new DropData(new ItemRange(UnshatteredItems.MITHRIL.get())),
                                new DropData(new ItemRange(Items.COBBLESTONE, 1, 3))),
                                Optional.of(new PlayerPowderAttachment.Powder(PlayerPowderAttachment.PowderType.MITHRIL, 1))
                        ), false
                ).add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_SOFT_MITHRIL_BLOCK.get()),
                        new BlockBreakData(PlayerSkillsAttachment.Skill.MINING,
                                35,
                                List.of(new DropData(new ItemRange(UnshatteredItems.MITHRIL.get(), 2, 4))),
                                Optional.of(new PlayerPowderAttachment.Powder(PlayerPowderAttachment.PowderType.MITHRIL, 2))
                        ), false
                ).add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK.get()),
                        new BlockBreakData(PlayerSkillsAttachment.Skill.MINING, 45, List.of(new DropData(new ItemRange(UnshatteredItems.MITHRIL.get(), 2, 5)),
                                new DropData(new ItemRange(UnshatteredItems.ENCHANTED_MITHRIL.get()), 0.01f)),
                                Optional.of(new PlayerPowderAttachment.Powder(PlayerPowderAttachment.PowderType.MITHRIL, 5))
                        ), false
                );

        builder(FISHABLE_ITEMS_EXP_DATA)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.COD), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SALMON), 0.7f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PUFFERFISH), 1.0f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.TROPICAL_FISH), 2.0f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_SHARD), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_CRYSTALS), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.CLAY_BALL), 0.1f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SPONGE), 4.0f, false);

        builder(FISHABLE_MOBS_EXP_DATA)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SQUID), 25.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.GLOW_SQUID), 90.0f, false);

        builder(COMBATABLE_MOBS_LOOT_DATA)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ZOMBIE), new MobRewardData(
                        List.of(new MobItemDropData(Items.ROTTEN_FLESH, false),
                                new MobItemDropData(Items.POISONOUS_POTATO, 0.02f, true),
                                new MobItemDropData(Items.POTATO, 0.01f, true),
                                new MobItemDropData(Items.CARROT, 0.01f, true)
                        ),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        6.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SKELETON), new MobRewardData(
                        List.of(new MobItemDropData(Items.BONE, 1, 2, 1.0f, false)),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        6.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SLIME), new MobRewardData(
                        List.of(new MobItemDropData(Items.SLIME_BALL, false)),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        6.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SPIDER), new MobRewardData(
                        List.of(new MobItemDropData(Items.STRING, false),
                                new MobItemDropData(Items.SPIDER_EYE, 0.5f, false)
                        ),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        8.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CAVE_SPIDER), new MobRewardData(
                        List.of(new MobItemDropData(Items.STRING, false),
                                new MobItemDropData(Items.SPIDER_EYE, 0.5f, false)
                        ),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        8.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.WITCH), new MobRewardData(
                        List.of(new MobItemDropData(Items.GUNPOWDER, 0.5f, false),
                                new MobItemDropData(Items.GLOWSTONE_DUST, 0.5f, false),
                                new MobItemDropData(Items.GLASS_BOTTLE, 1, 2, 0.2f, false)
                        ),
                        1,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        15.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ENDERMAN), new MobRewardData(
                        List.of(new MobItemDropData(Items.ENDER_PEARL, false)),
                        2,
                        12,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        15.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.BAT), new MobRewardData(
                        List.of(new MobItemDropData(UnshatteredItems.BAT_TALISMAN.get(), 0.01f, true)),
                        100,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        33.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CREEPER), new MobRewardData(
                        List.of(new MobItemDropData(Items.GUNPOWDER, false)),
                        2,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        8.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.BLAZE), new MobRewardData(
                        List.of(new MobItemDropData(Items.BLAZE_ROD, false)),
                        3,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        10.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SQUID), new MobRewardData(
                        List.of(new MobItemDropData(Items.INK_SAC, 1, 2, 1.0f, false),
                                new MobItemDropData(Items.LILY_PAD, false)
                        ),
                        5,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        75.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.GLOW_SQUID), new MobRewardData(
                        List.of(new MobItemDropData(Items.INK_SAC, 3, 6, 1.0f, false),
                                new MobItemDropData(Items.LILY_PAD, false),
                                new MobItemDropData(UnshatteredItems.GLOW_SQUID_BOOTS.get(),  0.08f, true)
                        ),
                        5,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        36.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SHEEP), new MobRewardData(
                        List.of(new MobItemDropData(Items.MUTTON, 1, 2, 1.0f, false),
                                new MobItemDropData(Items.WHITE_WOOL, false)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        3.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.COW), new MobRewardData(
                        List.of(new MobItemDropData(Items.BEEF, false),
                                new MobItemDropData(Items.LEATHER, false)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        3.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CHICKEN), new MobRewardData(
                        List.of(new MobItemDropData(Items.FEATHER, false),
                                new MobItemDropData(Items.CHICKEN, false)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        2.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.RABBIT), new MobRewardData(
                        List.of(new MobItemDropData(Items.RABBIT, false),
                                new MobItemDropData(Items.RABBIT_HIDE, 0.7f, false),
                                new MobItemDropData(Items.RABBIT_FOOT, 0.7f, false)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        5.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.PIG), new MobRewardData(
                        List.of(new MobItemDropData(Items.PORKCHOP, false)),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        3.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.MOOSHROOM), new MobRewardData(
                        List.of(new MobItemDropData(Items.BEEF, false),
                                new MobItemDropData(Items.RED_MUSHROOM, 1, 4, 1.0f, false),
                                new MobItemDropData(Items.LEATHER, false)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        5.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ENDERMITE), new MobRewardData(
                        List.of(new MobItemDropData(Items.END_STONE, 1, 2, 1.0f, false)),
                        10,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        20.0f
                ), false);

        builder(BLOCK_BREAKING_POWER_DATA)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get()), 1, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get()), 1, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK.get()), 1, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_IRON_ORE_BLOCK.get()), 2, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COPPER_ORE_BLOCK.get()), 1, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_GOLD_ORE_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_REDSTONE_ORE_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_EMERALD_ORE_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_DIAMOND_ORE_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.PURE_DIAMOND_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_OBSIDIAN_BLOCK.get()), 4, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COBBLED_MITHRIL_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_SOFT_MITHRIL_BLOCK.get()), 4, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK.get()), 4, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get()), 2, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK.get()), 5, false);

        builder(FISHING_ITEM_WEIGHT_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(Items.COD), new FishingWeightEntry(100000.0d), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SALMON), new FishingWeightEntry(75000.0d), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PUFFERFISH), new FishingWeightEntry(60000.0d), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.TROPICAL_FISH), new FishingWeightEntry(55000.0d), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_SHARD), new FishingWeightEntry(45000.0d), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_CRYSTALS), new FishingWeightEntry(45000.0d), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.CLAY_BALL), new FishingWeightEntry(45000.0d), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SPONGE), new FishingWeightEntry(35000.0d), false);

        builder(FISHING_MOB_WEIGHT_DATA).add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SQUID), new FishingWeightEntry(1200.0d), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.GLOW_SQUID),
                        new FishingWeightEntry(Optional.of(FishingConditions.NIGHT),
                                1100.0d,
                                Optional.of(1)
                        ),
                        false
                );

        builder(ITEM_RARITY_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MERCENARY_AXE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TREECAPITATOR.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SPRUCE_AXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SERIOUSLY_DAMAGED_AXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DECENT_AXE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIG_HEW.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIGSTONE_SPLITTER.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_HELMET.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_CHESTPLATE.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_LEGGINGS.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_BOOTS.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BAT_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CENTURY_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CHILL_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CLUNK_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DIAMOND_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DUST_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EGG_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EON_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FLAKE_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EXPERIMENT_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FOSSIL_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GABAGOOL_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GIFT_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HERRING_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.NOPE_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.OOPS_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PARTY_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ROCK_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SHRIMP_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SKELETON_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SPOOK_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.STEW_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SWAMP_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOOP_THE_FISH.get()), UnshatteredRarity.SPECIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_FIG_LOG.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BEDROCK.get()), UnshatteredRarity.LEGENDARY, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CAKE_SOUL.get()), UnshatteredRarity.MYTHIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CHALLENGING_ROD.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FISHING_ROD.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIG_LOG.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ROTTEN_FLESH.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_HEART.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_POWDER.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_GOLD_INGOT.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_GOLD_BLOCK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DIAMOND.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DIAMOND_BLOCK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_EMERALD.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_EMERALD_BLOCK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_IRON.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_IRON_BLOCK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COAL.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COAL_BLOCK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LAPIS.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LAPIS_BLOCK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_REDSTONE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_REDSTONE_BLOCK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HEALING_TISSUE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ROGUE_SWORD.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SQUIRE_SWORD.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.UNDEAD_SWORD.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_SWORD.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BROKEN_MITHRIL_PICKAXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BANDAGED_MITHRIL_PICKAXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PICKAXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RUSTED_TITANIUM_PICKAXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_PICKAXE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LAPIS_PICKAXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FLORID_ZOMBIE_SWORD.get()), UnshatteredRarity.LEGENDARY, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RUSTY_CLEAVER.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_CLEAVER.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SUPER_CLEAVER.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HYPER_CLEAVER.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GIANT_CLEAVER.get()), UnshatteredRarity.LEGENDARY, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BAT_TALISMAN.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.IRON_DAGGER.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EMERALD_DAGGER.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.COINS_TALISMAN.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GLOW_SQUID_BOOTS.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_MITHRIL.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_OAK_LOG.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BIRCH_LOG.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SPRUCE_LOG.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_JUNGLE_LOG.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ACACIA_LOG.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DARK_OAK_LOG.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_POISONOUS_POTATO.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BONE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BONE_BLOCK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SKELETON_HAT.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COBBLESTONE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_FLINT.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_STRING.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_OBSIDIAN.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_CLAY_BALL.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_CLAY_BLOCK.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_INK_SAC.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LILY_PAD.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PRISMARINE_SHARD.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PRISMARINE_CRYSTALS.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PUFFERFISH.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COD.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COOKED_COD.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SALMON.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COOKED_SALMON.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SPONGE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_WET_SPONGE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_TROPICAL_FISH.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_RAW_PORK_CHOP.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_HONEYCOMB.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ICE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PACKED_ICE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_TITANIUM.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_DRILL_SX_R226.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_DRILL_SX_R326.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X355.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X455.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X555.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X655.get()), UnshatteredRarity.LEGENDARY, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BLAZE_POWDER.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BLAZE_ROD.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_WOOL.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PLATED_DRILL_ENGINE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FRIED_GOBLIN_EGG.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_MITHRIL.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TREASURITE.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_DIAMOND.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GLACITE_JEWEL.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FUEL_CANISTER.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_PLATE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_TIATNIUM.get()), UnshatteredRarity.LEGENDARY, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PLATE.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DRILL_MOTOR.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOBLIN_EGG.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BLOOD_CHALICE.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_INFUSED_FUEL_TANK.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.OIL_BARREL.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.WOODEN_AXE), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.STONE_AXE), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_AXE), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.GOLDEN_AXE), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND_AXE), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DWARF_TURTLE_SHELL.get()), UnshatteredRarity.EPIC, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIRE_TALISMAN.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HASTE_RING.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HONEYCOMB_RING.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.INFINITE_QUIVER.get()), UnshatteredRarity.RARE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PIGGY_BANK_TALISMAN.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RAINBOW_YARN_TALISMAN.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PROMISING_AXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ARTISANAL_SHORTBOW.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_PICKAXE.get()), UnshatteredRarity.COMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PROMISING_PICKAXE.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_POPPY.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DANDELION.get()), UnshatteredRarity.UNCOMMON, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BIOFUEL.get()), UnshatteredRarity.RARE, false);

        builder(ITEM_SELL_VALUE_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TREECAPITATOR.get()), 10000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SPRUCE_AXE.get()), 480, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_HELMET.get()), 2, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_CHESTPLATE.get()), 4, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_LEGGINGS.get()), 3, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_BOOTS.get()), 2, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CHALLENGING_ROD.get()), 5000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FISHING_ROD.get()), 2, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIG_LOG.get()), 8, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ROTTEN_FLESH.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_HEART.get()), 123000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_POWDER.get()), 10000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_GOLD_INGOT.get()), 480, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_GOLD_BLOCK.get()), 76800, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DIAMOND.get()), 1280, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DIAMOND_BLOCK.get()), 204800, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_EMERALD.get()), 640, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_EMERALD_BLOCK.get()), 102400, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_IRON_BLOCK.get()), 51200, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COAL.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COAL_BLOCK.get()), 25600, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LAPIS.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LAPIS_BLOCK.get()), 25600, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_REDSTONE.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_REDSTONE_BLOCK.get()), 25600, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HEALING_TISSUE.get()), 5000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SQUIRE_SWORD.get()), 2500, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_SWORD.get()), 300000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LAPIS_PICKAXE.get()), 256, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get()), 600000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FLORID_ZOMBIE_SWORD.get()), 2000000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GLOW_SQUID_BOOTS.get()), 15, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL.get()), 9, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_MITHRIL.get()), 1440, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_OAK_LOG.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BIRCH_LOG.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SPRUCE_LOG.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_JUNGLE_LOG.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ACACIA_LOG.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DARK_OAK_LOG.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_POISONOUS_POTATO.get()), 1600, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BONE.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BONE_BLOCK.get()), 51200, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COBBLESTONE.get()), 160, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_FLINT.get()), 640, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_STRING.get()), 576, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_OBSIDIAN.get()), 1440, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_CLAY_BALL.get()), 480, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_CLAY_BLOCK.get()), 76800, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_INK_SAC.get()), 160, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LILY_PAD.get()), 1600, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PRISMARINE_SHARD.get()), 400, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PRISMARINE_CRYSTALS.get()), 400, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PUFFERFISH.get()), 2400, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COD.get()), 960, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COOKED_COD.get()), 150000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SALMON.get()), 1600, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COOKED_SALMON.get()), 256000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SPONGE.get()), 2000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_WET_SPONGE.get()), 80000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_TROPICAL_FISH.get()), 3200, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_RAW_PORK_CHOP.get()), 800, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_HONEYCOMB.get()), 16000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ICE.get()), 80, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PACKED_ICE.get()), 12800, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM.get()), 20, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_TITANIUM.get()), 3200, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DWARF_TURTLE_SHELL.get()), 250000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PIGGY_BANK_TALISMAN.get()), 80000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HONEYCOMB_RING.get()), 150000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HASTE_RING.get()), 80000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BLOOD_CHALICE.get()), 100000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.INFINITE_QUIVER.get()), 10000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ARTISANAL_SHORTBOW.get()), 100, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_PICKAXE.get()), 3, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PROMISING_PICKAXE.get()), 10, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PROMISING_AXE.get()), 10, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIRE_TALISMAN.get()), 6480, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_DRILL_SX_R226.get()), 500000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_DRILL_SX_R326.get()), 1000000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X355.get()), 1000000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X455.get()), 4000000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X555.get()), 8000000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X655.get()), 16000000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.OIL_BARREL.get()), 1000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOBLIN_EGG.get()), 421, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DRILL_MOTOR.get()), 5884, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PLATE.get()), 10108, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_TIATNIUM.get()), 51200, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_PLATE.get()), 3068, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FUEL_CANISTER.get()), 510, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GLACITE_JEWEL.get()), 2000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_DIAMOND.get()), 408000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TREASURITE.get()), 5000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_MITHRIL.get()), 204800, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FRIED_GOBLIN_EGG.get()), 20840, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PLATED_DRILL_ENGINE.get()), 45500, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_WOOL.get()), 320, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_INFUSED_FUEL_TANK.get()), 35300, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BLAZE_ROD.get()), 230400, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BLAZE_POWDER.get()), 1440, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ROGUE_SWORD.get()), 3, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.UNDEAD_SWORD.get()), 5, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BAT_TALISMAN.get()), 10000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SKELETON_HAT.get()), 8, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.WOODEN_AXE), 1, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.STONE_AXE), 2, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_AXE), 4, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.GOLDEN_AXE), 6, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND_AXE), 12, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.COINS_TALISMAN.get()), 70, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SUPER_CLEAVER.get()), 20000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HYPER_CLEAVER.get()), 100000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RUSTY_CLEAVER.get()), 8, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_CLEAVER.get()), 80, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GIANT_CLEAVER.get()), 200000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RUSTED_TITANIUM_PICKAXE.get()), 2500, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_PICKAXE.get()), 100000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BROKEN_MITHRIL_PICKAXE.get()), 500, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BANDAGED_MITHRIL_PICKAXE.get()), 5000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PICKAXE.get()), 50000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DANDELION.get()), 160, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_POPPY.get()), 576, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BIOFUEL.get()), 10000, false);

        builder(ITEM_TYPE_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MERCENARY_AXE.get()), ItemType.BATTLE_AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TREECAPITATOR.get()), ItemType.AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SPRUCE_AXE.get()), ItemType.AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SERIOUSLY_DAMAGED_AXE.get()), ItemType.AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DECENT_AXE.get()), ItemType.AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIG_HEW.get()), ItemType.AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIGSTONE_SPLITTER.get()), ItemType.AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_HELMET.get()), ItemType.HELMET, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_CHESTPLATE.get()), ItemType.CHESTPLATE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_LEGGINGS.get()), ItemType.LEGGINGS, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LEAFLET_BOOTS.get()), ItemType.BOOTS, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BAT_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CENTURY_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CHILL_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CLUNK_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DIAMOND_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DUST_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EGG_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EON_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FLAKE_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EXPERIMENT_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FOSSIL_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GABAGOOL_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GIFT_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HERRING_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.NOPE_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.OOPS_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PARTY_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ROCK_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SHRIMP_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SKELETON_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SPOOK_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.STEW_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SWAMP_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOOP_THE_FISH.get()), ItemType.FISH, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_FIG_LOG.get()), ItemType.LOG, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BEDROCK.get()), ItemType.ITEM, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CAKE_SOUL.get()), ItemType.ITEM, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CHALLENGING_ROD.get()), ItemType.FISHING_ROD, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FISHING_ROD.get()), ItemType.FISHING_ROD, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIG_LOG.get()), ItemType.LOG, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ROTTEN_FLESH.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_HEART.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_POWDER.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_GOLD_INGOT.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_GOLD_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DIAMOND.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DIAMOND_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_EMERALD.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_EMERALD_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_IRON.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_IRON_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COAL.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COAL_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LAPIS.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LAPIS_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_REDSTONE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_REDSTONE_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HEALING_TISSUE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ROGUE_SWORD.get()), ItemType.SWORD, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SQUIRE_SWORD.get()), ItemType.SWORD, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.UNDEAD_SWORD.get()), ItemType.SWORD, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_SWORD.get()), ItemType.SWORD, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BROKEN_MITHRIL_PICKAXE.get()), ItemType.PICKAXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BANDAGED_MITHRIL_PICKAXE.get()), ItemType.PICKAXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PICKAXE.get()), ItemType.PICKAXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RUSTED_TITANIUM_PICKAXE.get()), ItemType.PICKAXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_PICKAXE.get()), ItemType.PICKAXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.LAPIS_PICKAXE.get()), ItemType.PICKAXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get()), ItemType.SWORD, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FLORID_ZOMBIE_SWORD.get()), ItemType.SWORD, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RUSTY_CLEAVER.get()), ItemType.CLEAVER, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_CLEAVER.get()), ItemType.CLEAVER, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SUPER_CLEAVER.get()), ItemType.CLEAVER, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HYPER_CLEAVER.get()), ItemType.CLEAVER, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GIANT_CLEAVER.get()), ItemType.CLEAVER, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BAT_TALISMAN.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.IRON_DAGGER.get()), ItemType.DAGGER, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EMERALD_DAGGER.get()), ItemType.DAGGER, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GLOW_SQUID_BOOTS.get()), ItemType.BOOTS, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_MITHRIL.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_OAK_LOG.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BIRCH_LOG.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SPRUCE_LOG.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_JUNGLE_LOG.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ACACIA_LOG.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DARK_OAK_LOG.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_POISONOUS_POTATO.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BONE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BONE_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SKELETON_HAT.get()), ItemType.HELMET, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COBBLESTONE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_FLINT.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_STRING.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_OBSIDIAN.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_CLAY_BALL.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_CLAY_BLOCK.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_INK_SAC.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_LILY_PAD.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PRISMARINE_SHARD.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PRISMARINE_CRYSTALS.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PUFFERFISH.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COD.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COOKED_COD.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SALMON.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_COOKED_SALMON.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_SPONGE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_WET_SPONGE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_TROPICAL_FISH.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_RAW_PORK_CHOP.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_HONEYCOMB.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_ICE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_PACKED_ICE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_TITANIUM.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RAINBOW_YARN_TALISMAN.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DWARF_TURTLE_SHELL.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PIGGY_BANK_TALISMAN.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HONEYCOMB_RING.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HASTE_RING.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BLOOD_CHALICE.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.INFINITE_QUIVER.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ARTISANAL_SHORTBOW.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_PICKAXE.get()), ItemType.PICKAXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PROMISING_PICKAXE.get()), ItemType.PICKAXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PROMISING_AXE.get()), ItemType.AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIRE_TALISMAN.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_DRILL_SX_R226.get()), ItemType.DRILL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_DRILL_SX_R326.get()), ItemType.DRILL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X355.get()), ItemType.DRILL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X455.get()), ItemType.DRILL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X555.get()), ItemType.DRILL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_DRILL_DR_X655.get()), ItemType.DRILL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BLAZE_POWDER.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_BLAZE_ROD.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_INFUSED_FUEL_TANK.get()), ItemType.FUEL_TANK, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_WOOL.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PLATED_DRILL_ENGINE.get()), ItemType.DRILL_ENGINE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FRIED_GOBLIN_EGG.get()), ItemType.UPGRADE_MODULE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_MITHRIL.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TREASURITE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_DIAMOND.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GLACITE_JEWEL.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FUEL_CANISTER.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_PLATE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.REFINED_TIATNIUM.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PLATE.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DRILL_MOTOR.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOBLIN_EGG.get()), ItemType.MATERIAL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.OIL_BARREL.get()), ItemType.FUEL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.WOODEN_AXE), ItemType.BATTLE_AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.STONE_AXE), ItemType.BATTLE_AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_AXE), ItemType.BATTLE_AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.GOLDEN_AXE), ItemType.BATTLE_AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND_AXE), ItemType.BATTLE_AXE, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.COINS_TALISMAN.get()), ItemType.TALISMAN, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_POPPY.get()), ItemType.FUEL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DANDELION.get()), ItemType.FUEL, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BIOFUEL.get()), ItemType.FUEL, false);

        builder(ITEM_ABILITY_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TREECAPITATOR.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("treecapitator_park_enthusiast"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SPRUCE_AXE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("spruce_axe_park_enthusiast"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FISHING_ROD.get()), new ItemAbility(UnshatteredRod.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_SWORD.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("zombie_sword_instant_heal"), 24, 10, 0, false), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("ornate_zombie_sword_instant_heal"), 30, 10, 0, false), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FLORID_ZOMBIE_SWORD.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("florid_zombie_sword_instant_heal"), 36, 10, 0, false), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SKELETON_HAT.get()), new ItemAbility(SkeletonHat.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PLATED_DRILL_ENGINE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("mithril_plated_drill_engine"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FRIED_GOBLIN_EGG.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("fried_goblin_egg"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_INFUSED_FUEL_TANK.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("mithril_infused_fuel_tank"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BAT_TALISMAN.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("bat_talisman_leech"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BLOOD_CHALICE.get()), new ItemAbility(BloodChalice.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.COINS_TALISMAN.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("coin_talisman_accrual"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.DWARF_TURTLE_SHELL.get()), new ItemAbility(DwarfTurtleTalisman.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.CHALLENGING_ROD.get()), new ItemAbility(UnshatteredRod.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.FIRE_TALISMAN.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("blazeborn"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HASTE_RING.get()), new ItemAbility(HasteRing.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HONEYCOMB_RING.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("apian_aegis"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.INFINITE_QUIVER.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("bottomless"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PIGGY_BANK_TALISMAN.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("piggy_bank_saving_grace"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RAINBOW_YARN_TALISMAN.get()), new ItemAbility(RainbowYarnTalisman.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PROMISING_AXE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("axe_stored_potential"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.UNDEAD_SWORD.get()), new ItemAbility(UndeadSword.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ROGUE_SWORD.get()), RogueSword.SPEED_BOOST_ABILITY, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.EMERALD_DAGGER.get()), new ItemAbility(EmeraldDagger.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.SUPER_CLEAVER.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("super_cleaver_cleave"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RUSTY_CLEAVER.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("rusty_cleaver_cleave"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.HYPER_CLEAVER.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("hyper_cleaver_cleave"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOLDEN_CLEAVER.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("golden_cleaver_cleave"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GIANT_CLEAVER.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("giant_cleaver_cleave"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ZOMBIE_PICKAXE.get()), new ItemAbility(ZombiePickaxe.ABILITY_IDENTIFIER, 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.RUSTED_TITANIUM_PICKAXE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("rusted_titanium_pickaxe_titanium_fanatic"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.TITANIUM_PICKAXE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("titanium_pickaxe_titanium_fanatic"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.PROMISING_PICKAXE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("stored_potential"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BROKEN_MITHRIL_PICKAXE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("broken_mithril_pickaxe_mithril_speed"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BANDAGED_MITHRIL_PICKAXE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("bandaged_mithril_pickaxe_mithril_speed"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_PICKAXE.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("mithril_pickaxe_mithril_speed"), 0, 0, 0, true), false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.MITHRIL_DRILL_SX_R226.get()), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("r226_junkie"), 0, 0, 0, true), false);

        setBaseData(Items.END_STONE, ItemType.MATERIAL, 2);
        setBaseData(Items.DIAMOND, ItemType.MATERIAL, 8);
        setBaseData(Items.COBBLESTONE, ItemType.MATERIAL, 1);
        setBaseData(Items.IRON_HELMET, ItemType.HELMET, 2);
        setBaseData(Items.IRON_CHESTPLATE, ItemType.CHESTPLATE, 3);
        setBaseData(Items.IRON_LEGGINGS, ItemType.LEGGINGS, 3);
        setBaseData(Items.IRON_BOOTS, ItemType.BOOTS, 2);
        setBaseData(Items.DIAMOND_HELMET, ItemType.HELMET, UnshatteredRarity.UNCOMMON, 3);
        setBaseData(Items.DIAMOND_CHESTPLATE, ItemType.CHESTPLATE, UnshatteredRarity.UNCOMMON, 5);
        setBaseData(Items.DIAMOND_LEGGINGS, ItemType.LEGGINGS, UnshatteredRarity.UNCOMMON, 4);
        setBaseData(Items.DIAMOND_BOOTS, ItemType.BOOTS, UnshatteredRarity.UNCOMMON, 3);
        setBaseData(Items.GOLD_INGOT, ItemType.MATERIAL, 4);
        setBaseData(Items.GOLD_BLOCK, ItemType.MATERIAL, 27);
        setBaseData(Items.DIAMOND, ItemType.MATERIAL, 8);
        setBaseData(Items.DIAMOND_BLOCK, ItemType.MATERIAL, 72);
        setBaseData(Items.EMERALD, ItemType.MATERIAL, 4);
        setBaseData(Items.EMERALD_BLOCK, ItemType.MATERIAL, 36);
        setBaseData(Items.IRON_INGOT, ItemType.MATERIAL, 2);
        setBaseData(Items.IRON_BLOCK, ItemType.MATERIAL, 18);
        setBaseData(Items.COAL, ItemType.MATERIAL, 2);
        setBaseData(Items.COAL_BLOCK, ItemType.MATERIAL, 18);
        setBaseData(Items.LAPIS_LAZULI, ItemType.MATERIAL, 1);
        setBaseData(Items.LAPIS_BLOCK, ItemType.MATERIAL, 9);
        setBaseData(Items.REDSTONE, ItemType.MATERIAL, 1);
        setBaseData(Items.REDSTONE_BLOCK, ItemType.MATERIAL, 9);
        setBaseData(Items.OAK_LOG, ItemType.MATERIAL, 2);
        setBaseData(Items.BIRCH_LOG, ItemType.MATERIAL, 2);
        setBaseData(Items.SPRUCE_LOG, ItemType.MATERIAL, 2);
        setBaseData(Items.JUNGLE_LOG, ItemType.MATERIAL, 2);
        setBaseData(Items.ACACIA_LOG, ItemType.MATERIAL, 2);
        setBaseData(Items.DARK_OAK_LOG, ItemType.MATERIAL, 2);
        setBaseData(Items.ENCHANTED_BOOK, ItemType.ENCHANTED_BOOK, UnshatteredRarity.RARE, 0);
        setBaseData(Items.POISONOUS_POTATO, ItemType.MATERIAL, 10);
        setBaseData(Items.BONE, ItemType.MATERIAL, 2);
        setBaseData(Items.FLINT, ItemType.MATERIAL, 4);
        setBaseData(Items.STRING, ItemType.MATERIAL, 3);
        setBaseData(Items.CLAY_BALL, ItemType.MATERIAL, 3);
        setBaseData(Items.INK_SAC, ItemType.MATERIAL, 2);
        setBaseData(Items.LILY_PAD, ItemType.MATERIAL, 10);
        setBaseData(Items.PRISMARINE_SHARD, ItemType.MATERIAL, 5);
        setBaseData(Items.PRISMARINE_CRYSTALS, ItemType.MATERIAL, 5);
        setBaseData(Items.PUFFERFISH, ItemType.MATERIAL, 15);
        setBaseData(Items.COD, ItemType.MATERIAL, 6);
        setBaseData(Items.SALMON, ItemType.MATERIAL, 10);
        setBaseData(Items.SPONGE, ItemType.MATERIAL, 50);
        setBaseData(Items.TROPICAL_FISH, ItemType.MATERIAL, 20);
        setBaseData(Items.HONEYCOMB, ItemType.MATERIAL, 100);
        setBaseData(Items.ICE, ItemType.MATERIAL, 1);
        setBaseData(Items.WOODEN_PICKAXE, ItemType.PICKAXE, 1);
        setBaseData(Items.GOLDEN_PICKAXE, ItemType.PICKAXE, 6);
        setBaseData(Items.STONE_PICKAXE, ItemType.PICKAXE, 2);
        setBaseData(Items.IRON_PICKAXE, ItemType.PICKAXE, 4);
        setBaseData(Items.DIAMOND_PICKAXE, ItemType.PICKAXE, UnshatteredRarity.UNCOMMON, 12);
        setBaseData(Items.WOODEN_SWORD, ItemType.SWORD, 1);
        setBaseData(Items.STONE_SWORD, ItemType.SWORD, 1);
        setBaseData(Items.IRON_SWORD, ItemType.SWORD, 3);
        setBaseData(Items.GOLDEN_SWORD, ItemType.SWORD, 4);
        setBaseData(Items.DIAMOND_SWORD, ItemType.SWORD, UnshatteredRarity.UNCOMMON, 8);
        setBaseData(Items.BOW, ItemType.BOW, 3);
        setBaseData(Items.CROSSBOW, ItemType.CROSSBOW, UnshatteredRarity.UNCOMMON, 100);
        setBaseData(Items.POPPY, ItemType.FUEL, UnshatteredRarity.COMMON, 2);
        setBaseData(Items.DANDELION, ItemType.FUEL, UnshatteredRarity.COMMON, 2);

        for (Item wool : UnshatteredUtils.WOOL_TYPES) {
            setBaseData(wool, ItemType.MATERIAL, 2);
        }

        builder(ITEM_FUEL_VALUE_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(Items.POPPY), 2, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.DANDELION), 2, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_DANDELION.get()), 360, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.ENCHANTED_POPPY.get()), 1024, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.GOBLIN_EGG.get()), 2000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.OIL_BARREL.get()), 10000, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(UnshatteredItems.BIOFUEL.get()), 12000, false);
    }

    private void createSimpleBlockDropData(DataMapProvider.Builder<BlockBreakData, Block> builder, DeferredBlock<Block> block, ItemRange itemRange, PlayerSkillsAttachment.Skill skill, float expAmount) {
        builder.add(block, new BlockBreakData(skill, expAmount, List.of(new DropData(itemRange))), false);
    }

    private void setBaseData(Item item, ItemType itemType, UnshatteredRarity rarity, int sellValue) {
        builder(ITEM_TYPE_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(item), itemType, false);
        builder(ITEM_RARITY_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(item), rarity, false);
        builder(ITEM_SELL_VALUE_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(item), sellValue, false);
    }

    private void setBaseData(Item item, ItemType itemType, int sellValue) {
        builder(ITEM_TYPE_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(item), itemType, false);
        builder(ITEM_RARITY_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(item), UnshatteredRarity.COMMON, false);
        builder(ITEM_SELL_VALUE_DATA).add(BuiltInRegistries.ITEM.wrapAsHolder(item), sellValue, false);
    }
}
