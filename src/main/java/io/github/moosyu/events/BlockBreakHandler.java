package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.blocks.BlocksRegistry;
import io.github.moosyu.blocks.BrokenBlocksItemResult;
import io.github.moosyu.blocks.BrokenBlocksWorldResult;
import io.github.moosyu.data.RegenBlocksSavedData;
import io.github.moosyu.skills.experience.BlocksFarmingExperience;
import io.github.moosyu.skills.experience.BlocksMiningExperience;
import io.github.moosyu.helpers.CheckBreakableBlock;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.helpers.CheckItemRequirementHelper;
import io.github.moosyu.sounds.UnshatteredSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.AttachmentRegistry.PLAYER_SKILLS;

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
        BlockState replacementBlock = CheckBreakableBlock.canBreakBlock(blockState, player);

        if (replacementBlock == null) return;
        else {
            BlockPos blockPos = event.getPos();
            level.setBlock(blockPos, replacementBlock, 3);
            // server tick count and world tick count are different (i know now)
            RegenBlocksSavedData.get((ServerLevel) level).addBlock(blockPos, level.getGameTime() + TIME_BROKEN, replacementBlock);
        }

        PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

        float miningExp = BlocksMiningExperience.getExp(block);
        if (miningExp > 0.0f) {
            skills.addExp(PlayerSkillsAttachment.Skill.MINING, miningExp, player);
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
            player.getInventory().add(new ItemStack(BrokenBlocksItemResult.getItemDropped(block)));
            return;
        }

        // todo: make braking cactus' both add their drops to inventory but count broken cactus parts for exp
        // could just do the same thing as done with sweeping but less costly as it's just the block above
        float farmingExp = BlocksFarmingExperience.getExp(block);
        if (farmingExp > 0.0f) {
            skills.addExp(PlayerSkillsAttachment.Skill.FARMING, BlocksFarmingExperience.getExp(block), player);
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
            return;
        }

        if (blockState.is(BlockTags.LOGS)) {
            TreeSweepHandler.trySweep(player.level(), event.getPos(), player);
            return;
        }

        if (blockState.is(BlockTags.FLOWERS)) {
            skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, 1.0f, player);
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
            return;
        }
    }

    // to stop players from attempting to break blocks
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!CheckItemRequirementHelper.passesSkillCheck(event.getEntity(), event.getEntity().getMainHandItem())
                || CheckBreakableBlock.canBreakBlock(event.getState(), event.getEntity()) == null) {
            event.setNewSpeed(0.0F);
        }
    }

    @SubscribeEvent
    private static void onServerTickEvent(ServerTickEvent.Post event) {
        // im hoping that this wont be absurdly laggy but if this project ends up being anyhow successful i imagine this could get fucked up if like
        // 1000 blocks are waiting to regen at the same time
        if (event.getServer().getTickCount() % 10 != 0) return;
        for (ServerLevel level : event.getServer().getAllLevels()) {
            RegenBlocksSavedData data = RegenBlocksSavedData.get(level);
            if (data.getRegenTicks().isEmpty()) continue;
            long gameTime = level.getGameTime();
            List<BlockPos> toRemove = new ArrayList<>();
            Map<BlockPos, RegenBlocksSavedData.RegenEntry> toAdd = new HashMap<>();
            for (Map.Entry<BlockPos, RegenBlocksSavedData.RegenEntry> entry : data.getRegenTicks().entrySet()) {
                RegenBlocksSavedData.RegenEntry regenEntry = entry.getValue();
                if (gameTime >= regenEntry.targetTick()) {
                    BlockPos pos = entry.getKey();
                    BlockState originalState = regenEntry.targetState();
                    level.setBlock(pos, originalState, 3);
                    toRemove.add(pos);
                    if (originalState.is(BlocksRegistry.BREAKABLE_COBBLESTONE_BLOCK.get())) {
                        // How do we know if it was Iron Ore or Stone?
                        // You could check a separate "original source" map, or handle it during the initial break.
                        // Alternatively, if you want it to go Bedrock -> Cobble -> Iron Ore:
                        // You'd track the ultimate origin state.
                    }
                }
            }

            toRemove.forEach(data::removeBlock);
        }
    }
}