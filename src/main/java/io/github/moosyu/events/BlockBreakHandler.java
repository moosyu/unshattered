package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.experience.BlocksFarmingExperience;
import io.github.moosyu.experience.BlocksMiningExperience;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.sounds.UnshatteredSounds;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_SKILLS;

// ran just before a player is to break a block
@EventBusSubscriber(modid = MODID)
public class BlockBreakHandler {
    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        Player player = event.getPlayer();
        if (player.level().isClientSide()) return;

        BlockState blockState = event.getState();
        Block block = blockState.getBlock();
        PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

        float miningExp = BlocksMiningExperience.getExp(block);
        if (miningExp > 0.0f) {
            skills.addExp(PlayerSkillsAttachment.Skill.MINING, miningExp);
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
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
            // cancel the vanilla block break for logs (to add
            event.setCanceled(true);
            System.out.println("SWEEP");
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
}