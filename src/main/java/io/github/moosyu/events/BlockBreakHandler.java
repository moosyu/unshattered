package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.experience.BlocksFarmingExperience;
import io.github.moosyu.experience.BlocksMiningExperience;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockBreakHandler {
    @EventBusSubscriber(modid = "nno")
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
                return;
            }

            float farmingExp = BlocksFarmingExperience.getExp(block);
            if (farmingExp > 0.0f) {
                skills.addFarmingExp(BlocksFarmingExperience.getExp(block));
                return;
            }

            if (blockState.is(BlockTags.LOGS)) {
                skills.addForagingExp(6.0f);
                return;
            }

            if (blockState.is(BlockTags.FLOWERS)) {
                skills.addForagingExp(1.0f);
                return;
            }
        }
    }
}


