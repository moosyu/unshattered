package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStatsAttachment;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static io.github.moosyu.NNO.MODID;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_STATS;

public class LivingDamageHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Pre event) {
            Level level = event.getEntity().level();
            if (level.isClientSide()) return;
            if (event.getEntity() instanceof Player player) {
                Entity attacker = event.getSource().getEntity();
                event.setNewDamage(0.0f);
                if (attacker instanceof LivingEntity entity) {
                    PlayerStatsAttachment stats = player.getData(PLAYER_STATS.get());
                    double mobDamage = entity.getAttribute(AttributesRegistry.DAMAGE).getBaseValue();
                    double playerHealth = stats.getCurrentStat(PlayerStatsAttachment.Stat.HEALTH);
                    if (playerHealth - mobDamage > 0.0d) {
                        stats.removeCurrentStat(PlayerStatsAttachment.Stat.HEALTH, mobDamage);
                        player.syncData(PLAYER_STATS.get());
                    } else {
                        BlockPos spawnPos = level.getSharedSpawnPos();
                        player.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                        player.sendSystemMessage(Component.literal(player.getName().getString() + " was slain by a " + entity.getName().getString() + "!").withStyle(ChatFormatting.RED));
                        stats.setCurrentStat(PlayerStatsAttachment.Stat.HEALTH, player.getAttributeValue(AttributesRegistry.HEALTH));
                        //todo: add level.playLocalSound() for the anvil falling sound here, but needs to be clientside somehow
                        player.syncData(PLAYER_STATS.get());
                    }
                }
            } else if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                event.getEntity().invulnerableTime = 0;
            }
        }
    }
}
