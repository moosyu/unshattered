package io.github.moosyu.helpers;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.packets.DeathSoundEffectPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_STATE;

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
            states.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, player.getAttributeValue(UnshatteredAttributeValues.HEALTH.holder));
            player.syncData(PLAYER_STATE.get());
            states.setCurrentStat(PlayerStateAttachment.Stat.MANA, player.getAttributeValue(UnshatteredAttributeValues.MANA.holder));
            player.syncData(PLAYER_STATE.get());
            PacketDistributor.sendToPlayer((ServerPlayer) player, new DeathSoundEffectPacket());
            states.setCancelledKnockback(true);
        }
        player.invulnerableTime = 0;
    }
}
