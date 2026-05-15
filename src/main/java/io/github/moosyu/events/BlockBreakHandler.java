package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.experience.BlocksFarmingExperience;
import io.github.moosyu.experience.BlocksMiningExperience;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.sounds.ModSounds;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import static io.github.moosyu.NNO.MODID;

// ran just before a player is to break a block
public class BlockBreakHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onBlockBreak(BlockEvent.BreakEvent event) {
            Player player = event.getPlayer();
            BlockState blockState = event.getState();
            Block block = blockState.getBlock();
            PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

            if (player.level().isClientSide()) {
                return;
            }

            float miningExp = BlocksMiningExperience.getExp(block);
            if (miningExp > 0.0f) {
                skills.addMiningExp(miningExp);
                ModSounds.playerExperienceSound(player);
                return;
            }

            float farmingExp = BlocksFarmingExperience.getExp(block);
            if (farmingExp > 0.0f) {
                skills.addFarmingExp(BlocksFarmingExperience.getExp(block));
                ModSounds.playerExperienceSound(player);
                return;
            }

            if (blockState.is(BlockTags.LOGS)) {
                // cancel the vanilla block break for logs (to add
                event.setCanceled(true);
                skills.addForagingExp(TreeSweepHandler.trySweep(player.level(), event.getPos(), player) + 1 * 6.0f);
                ModSounds.playerExperienceSound(player);
                return;
            }

            if (blockState.is(BlockTags.FLOWERS)) {
                skills.addForagingExp(1.0f);
                ModSounds.playerExperienceSound(player);
                return;
            }
        }
    }
}


