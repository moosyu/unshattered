package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.BrokenBlocksItemResult;
import io.github.moosyu.data.RegenBlocksSavedData;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.datagen.UnshatteredBlockTagsProvider;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.util.CollectionUtil;
import io.github.moosyu.util.CheckBreakableBlock;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.util.CheckItemRequirement;
import io.github.moosyu.util.FortuneCalculation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_SKILLS;

// ran just before a player is to break a block
@EventBusSubscriber(modid = MODID)
public class BlockBreakHandler {
    private static final int TIME_BROKEN = 120;
    private static final Map<BlockPos, RegenBlock> BLOCKS_AWAITING_REGEN = new HashMap<>();
    public record RegenBlock(Level level, BlockState currentBlock, long regenTick) {}
    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        Player player = event.getPlayer();
        Level level = player.level();
        // so you can still break stuff normally in creative
        if (player.isCreative() || player.level().isClientSide()) return;
        event.setCanceled(true);

        BlockState blockState = event.getState();
        Block block = blockState.getBlock();
        DataComponentMap blockDataComponents = block.asItem().components();
        BlockState replacementBlock = CheckBreakableBlock.canBreakBlock(blockState, player);

        if (replacementBlock == null) return;
        else {
            BlockPos blockPos = event.getPos();
            level.setBlock(blockPos, replacementBlock, 3);
            // server tick count and world tick count are different (i know now)
            RegenBlocksSavedData.get((ServerLevel) level).addBlock(blockPos, level.getGameTime() + TIME_BROKEN, replacementBlock);
        }

        float experienceReward = blockDataComponents.getOrDefault(UnshatteredDataComponents.ITEM_EXP_REWARD, 0.0f);
        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());

        if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_MINING_BLOCKS)) {
            ItemStack blockDrops = getBlockDrop(BrokenBlocksItemResult.getItemDropped(block), player, UnshatteredAttributeValues.MINING_FORTUNE);
            player.getInventory().add(blockDrops);
            CollectionUtil.addItemToCollection(player, blockDrops);
            if (experienceReward > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.MINING, experienceReward, player);
                player.syncData(PLAYER_SKILLS);
                PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
            }
        } else if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_FARMING_BLOCKS)) {
            ItemStack blockDrops = getBlockDrop(BrokenBlocksItemResult.getItemDropped(block), player, UnshatteredAttributeValues.FARMING_FORTUNE);
            player.getInventory().add(blockDrops);
            CollectionUtil.addItemToCollection(player, blockDrops);
            // todo: make braking cactus' both add their drops to inventory but count broken cactus parts for exp
            // could just do the same thing as done with sweeping but less costly as it's just the block above
            if (experienceReward > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.FARMING, experienceReward, player);
                player.syncData(PLAYER_SKILLS);
                PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
            }
        } else if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_FORAGING_BLOCKS)) {
            // flowers are kind of an outlier as they dont really have their own collections either so im just leaving the logic here for now
            if (blockState.is(BlockTags.FLOWERS)) {
                skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, 1.0f, player);
                player.syncData(PLAYER_SKILLS);
                PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
            } else {
                TreeSweepHandler.trySweep(player.level(), event.getPos(), player);
            }
        }
    }

    // to stop players from attempting to break blocks
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!CheckItemRequirement.passesSkillCheck(event.getEntity(), event.getEntity().getMainHandItem())
                || CheckBreakableBlock.canBreakBlock(event.getState(), event.getEntity()) == null) {
            event.setNewSpeed(0.0F);
        }
    }

    private static ItemStack getBlockDrop(Item item, Player player, UnshatteredAttributeValues fortuneType) {
        return new ItemStack(item, FortuneCalculation.getItemsCount(player.getAttributeValue(fortuneType.holder), 1));
    }
}