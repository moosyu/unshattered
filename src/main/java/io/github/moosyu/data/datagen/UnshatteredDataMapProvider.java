package io.github.moosyu.data.datagen;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.drops.BlockBreakData;
import io.github.moosyu.data.drops.DropData;
import io.github.moosyu.data.drops.MobItemDropData;
import io.github.moosyu.data.drops.MobRewardData;
import io.github.moosyu.data.fishing.FishingConditions;
import io.github.moosyu.data.fishing.FishingWeightEntry;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.items.UnshatteredItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
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
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_SOFT_MITHRIL_BLOCK, new ItemRange(UnshatteredItems.MITHRIL.get(), 2, 4), PlayerSkillsAttachment.Skill.MINING, 35);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_OAK_LOG_BLOCK, new ItemRange(Items.OAK_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_BIRCH_LOG_BLOCK, new ItemRange(Items.BIRCH_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_SPRUCE_LOG_BLOCK, new ItemRange(Items.SPRUCE_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_JUNGLE_LOG_BLOCK, new ItemRange(Items.JUNGLE_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_ACACIA_LOG_BLOCK, new ItemRange(Items.ACACIA_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_DARK_OAK_LOG_BLOCK, new ItemRange(Items.DARK_OAK_LOG), PlayerSkillsAttachment.Skill.FORAGING, 6);
        createSimpleBlockDropData(blockBreakBuilder, UnshatteredBlocks.BREAKABLE_ICE_BLOCK, new ItemRange(Items.ICE), PlayerSkillsAttachment.Skill.MINING, 0.5f);

        builder(BLOCK_BREAK_DATA)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COBBLED_MITHRIL_BLOCK.get()),
                        new BlockBreakData(PlayerSkillsAttachment.Skill.MINING, 25, List.of(new DropData(new ItemRange(UnshatteredItems.MITHRIL.get())),
                                new DropData(new ItemRange(Items.COBBLESTONE, 1, 3))
                        )),
                        false
                )
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK.get()),
                        new BlockBreakData(PlayerSkillsAttachment.Skill.MINING, 45, List.of(new DropData(new ItemRange(UnshatteredItems.MITHRIL.get(), 2, 5)),
                                new DropData(new ItemRange(UnshatteredItems.ENCHANTED_MITHRIL.get()), 0.01f)
                        )),
                        false
                );

        this.builder(FISHABLE_ITEMS_EXP_DATA)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.COD), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SALMON), 0.7f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PUFFERFISH), 1.0f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.TROPICAL_FISH), 2.0f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_SHARD), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_CRYSTALS), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.CLAY_BALL), 0.1f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SPONGE), 4.0f, false);

        this.builder(FISHABLE_MOBS_EXP_DATA)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SQUID), 25.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.GLOW_SQUID), 90.0f, false);

        this.builder(COMBATABLE_MOBS_LOOT_DATA)
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
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get()), 2, false);

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
                        ), false);
    }

    private void createSimpleBlockDropData(DataMapProvider.Builder<BlockBreakData, Block> builder, DeferredBlock<Block> block, ItemRange itemRange, PlayerSkillsAttachment.Skill skill, float expAmount) {
        builder.add(block, new BlockBreakData(skill, expAmount, List.of(new DropData(itemRange))), false);
    }
}
