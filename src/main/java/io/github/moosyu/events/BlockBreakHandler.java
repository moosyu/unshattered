package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.blocks.BrokenBlocksItemResult;
import io.github.moosyu.experience.BlocksFarmingExperience;
import io.github.moosyu.experience.BlocksMiningExperience;
import io.github.moosyu.helpers.CheckBreakableBlock;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.helpers.CheckSkillRequirementHelper;
import io.github.moosyu.sounds.UnshatteredSounds;
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

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.AttachmentRegistry.PLAYER_SKILLS;

// ran just before a player is to break a block
@EventBusSubscriber(modid = MODID)
public class BlockBreakHandler {
    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        Player player = event.getPlayer();
        Level level = player.level();
        // so you can still break stuff normally in creative
        if (player.isCreative()) {
            return;
        }
        if (player.level().isClientSide()) {
            return;
        }
        event.setCanceled(true);

        BlockState blockState = event.getState();
        Block block = blockState.getBlock();
        BlockState replacementBlock = CheckBreakableBlock.canBreakBlock(blockState, player);

        if (replacementBlock == null) {
            return;
        } else {
            level.setBlock(event.getPos(), replacementBlock, 3);
        }

        PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

        float miningExp = BlocksMiningExperience.getExp(block);
        if (miningExp > 0.0f) {
            skills.addExp(PlayerSkillsAttachment.Skill.MINING, miningExp);
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
            player.getInventory().add(new ItemStack(BrokenBlocksItemResult.getItemDropped(block)));
            return;
        }

        // todo: make braking cactus' both add their drops to inventory but count broken cactus parts for exp
        // could just do the same thing as done with sweeping but less costly as it's just the block above
        float farmingExp = BlocksFarmingExperience.getExp(block);
        if (farmingExp > 0.0f) {
            skills.addExp(PlayerSkillsAttachment.Skill.FARMING, BlocksFarmingExperience.getExp(block));
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
            return;
        }

        if (blockState.is(BlockTags.LOGS)) {
            TreeSweepHandler.trySweep(player.level(), event.getPos(), player);
            return;
        }

        if (blockState.is(BlockTags.FLOWERS)) {
            skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, 1.0f);
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
            return;
        }
    }

    // to stop players from attempting to break blocks
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!CheckSkillRequirementHelper.canUseItem(event.getEntity(), event.getEntity().getMainHandItem())
                || CheckBreakableBlock.canBreakBlock(event.getState(), event.getEntity()) == null) {
            event.setNewSpeed(0.0F);
        }
    }
}