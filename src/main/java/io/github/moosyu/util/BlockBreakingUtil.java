package io.github.moosyu.util;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.RegenSavedData;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Optional;

public final class BlockBreakingUtil {
    /**
     * @param blockPos the position of the block being broken
     * @param player the player breaking the block
     * @return true if it's breakable
     */
    public static boolean isBreakableBlock(GlobalPos blockPos, Player player) {
        Level level = player.level();
        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            RegenSavedData regenSavedData = serverLevel.getDataStorage().computeIfAbsent(RegenSavedData.ID);

            return player.registryAccess()
                    .lookupOrThrow(DataPackRegistryHandler.REGION_REGISTRY_KEY)
                    .getValueOrThrow(player.getData(UnshatteredAttachments.PLAYER_REGION).regionKey())
                    .harvestable()
                    && regenSavedData.canDestroyRegeneratingBLock(blockPos, serverLevel);
        }
        return false;
    }

    /**
     * check if a player can break a given block based on their breaking power, sending a message if not
     * @param player player breaking the block
     * @param block block attempting to be broken
     * @return whether the block can be broken by the player
     */
    public static boolean hasBreakingPowerRequirement(Player player, Holder<Block> block) {
        int requiredBreakingPower = Objects.requireNonNullElse(block.getData(UnshatteredDataMaps.BLOCK_BREAKING_POWER_DATA), 0);
        int playerBreakingPower = (int) player.getAttributeValue(UnshatteredAttributeValues.BREAKING_POWER.holder);
        if (playerBreakingPower >= requiredBreakingPower) return true;
        else if (!player.level().isClientSide()) {
            PlayerStateAttachment playerStateAttachment = player.getData(UnshatteredAttachments.PLAYER_STATE);
            if (!playerStateAttachment.isFailedMessageFired()) {
                player.sendSystemMessage(
                        Component.translatable(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() ? "breaking_power.messages.unshattered.hand" : "breaking_power.messages.unshattered.tool")
                                .append(Component.translatable("breaking_power.messages.unshattered.generic_breaking_power_requirment_start"))
                                .append(Component.literal(requiredBreakingPower + UnshatteredAttributeValues.BREAKING_POWER.symbol).withColor(UnshatteredAttributeValues.BREAKING_POWER.color))
                                .append(Component.translatable("breaking_power.messages.unshattered.generic_breaking_power_requirment_end"))
                                .append(".")
                );
            }
            playerStateAttachment.setFailedMessageFired(true);
            player.syncData(UnshatteredAttachments.PLAYER_STATE);

        }
        return false;
    }
}

