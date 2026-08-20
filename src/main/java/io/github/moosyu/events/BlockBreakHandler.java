package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.BrokenBlocksItemResult;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.datagen.UnshatteredBlockTagsProvider;
import io.github.moosyu.util.*;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.*;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_SKILLS;

// ran just before a player is to break a block
@EventBusSubscriber(modid = MODID)
public class BlockBreakHandler {
    private static final int TIME_BROKEN = 120;
    private static final Map<BlockPos, RegenBlock> BLOCKS_AWAITING_REGEN = new HashMap<>();
    public record RegenBlock(Level level, BlockState currentBlock, long regenTick) {}

    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        Player player = event.getPlayer();
        // so you can still break stuff normally in creative
        if (player.isCreative() || player.level().isClientSide()) return;
        event.setCanceled(true);

        BlockState blockState = event.getState();
        Holder<Block> blockHolder = blockState.typeHolder();
        float experienceReward = Objects.requireNonNullElse(blockHolder.getData(UnshatteredDataMaps.HARVESTABLE_BLOCKS_EXP_DATA), 0.0f);
        Block block = blockState.getBlock();

        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());

        if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_MINING_BLOCKS)) {
            ItemStack blockDrops = getBlockDrop(BrokenBlocksItemResult.getItemDropped(block), player, UnshatteredAttributeValues.MINING_FORTUNE);
            CollectionUtil.givePlayerHarvestedItemStack(player, blockDrops);
            if (experienceReward > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.MINING, experienceReward, player);
                player.syncData(PLAYER_SKILLS);
            }
        } else if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_FARMING_BLOCKS)) {
            ItemStack blockDrops = getBlockDrop(BrokenBlocksItemResult.getItemDropped(block), player, UnshatteredAttributeValues.FARMING_FORTUNE);
            CollectionUtil.givePlayerHarvestedItemStack(player, blockDrops);
            // todo: make braking cactus' both add their drops to inventory but count broken cactus parts for exp
            // could just do the same thing as done with sweeping but less costly as it's just the block above
            if (experienceReward > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.FARMING, experienceReward, player);
                player.syncData(PLAYER_SKILLS);
            }
        } else if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_FORAGING_BLOCKS)) {
            if (blockState.is(BlockTags.FLOWERS)) {
                skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, experienceReward, player);
                player.syncData(PLAYER_SKILLS);
            } else {
                TreeSweepHandler.trySweep(player.level(), event.getPos(), player);
            }
        }
    }

    // to stop players from attempting to break blocks
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        Level level = player.level();
        if ((event.getPosition().isPresent()
                && !BlockBreakingUtil.canBreakBlock(player, level.getBlockState(event.getPosition().get()).typeHolder()))
                || BlockBreakingUtil.isBreakableBlock(event.getState(), event.getEntity()) == null
                || !CheckItemRequirement.passesSkillCheck(player, player.getMainHandItem())
        ) {
            event.setNewSpeed(0.0F);
        }
    }

    private static ItemStack getBlockDrop(Item item, Player player, UnshatteredAttributeValues fortuneType) {
        return new ItemStack(item, FortuneCalculation.getItemsCount(player.getAttributeValue(fortuneType.holder), 1));
    }
}