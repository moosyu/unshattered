package io.github.moosyu.helpers;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static io.github.moosyu.attachments.AttachmentRegistry.PLAYER_STATE;
import static io.github.moosyu.sounds.UnshatteredSounds.playerDeathSound;

public final class PlayerDamageHelper {
    public static void damagePlayer(Player player, double damageDealt, Level level, String deathMessage) {
        PlayerStateAttachment states = player.getData(PLAYER_STATE.get());
        double playerHealth = states.getCurrentStat(PlayerStateAttachment.Stat.HEALTH);
        if (states.isInvulnerable()) return;
        if (playerHealth - damageDealt > 0.0d) {
            states.removeCurrentStat(PlayerStateAttachment.Stat.HEALTH, damageDealt);
            states.setInvulnerableTime(20);
            player.syncData(PLAYER_STATE.get());
        } else {
            BlockPos spawnPos = level.getRespawnData().pos();
            player.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
            player.sendSystemMessage(Component.literal(deathMessage).withStyle(ChatFormatting.RED));
            states.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, player.getAttributeValue(UnshatteredAttributes.HEALTH.holder));
            player.syncData(PLAYER_STATE.get());
            // todo: fix the death sound not going off
            playerDeathSound(player);
            states.setCancelledKnockback(true);
        }
        player.invulnerableTime = 0;
    }
}
