package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStatsAttachment;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
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
            if (event.getEntity() instanceof Player player) {
                event.setNewDamage(0.0f);
                Entity attacker = event.getSource().getEntity();
                if (attacker instanceof LivingEntity entity) {
                    PlayerStatsAttachment stats = player.getData(PLAYER_STATS.get());
                    double mobDamage = entity.getAttribute(AttributesRegistry.DAMAGE).getBaseValue();
                    double playerHealth = stats.getCurrentStat(PlayerStatsAttachment.Stat.HEALTH);

                    if (playerHealth - mobDamage > 0.0d) {
                        // playerHealth.setBaseValue(playerHealth.getValue() - mobDamage);
                        stats.removeCurrentStat(PlayerStatsAttachment.Stat.HEALTH, mobDamage);
                    } else {
                        if (!player.level().isClientSide()) {
                            ServerLevel level = (ServerLevel) player.level();
                            BlockPos spawnPos = level.getSharedSpawnPos();
                            player.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                            player.sendSystemMessage(Component.literal("You were slain!").withStyle(ChatFormatting.RED));
                            player.playNotifySound(SoundEvents.ANVIL_FALL,SoundSource.PLAYERS,0.5f,2.0f);
                        }
                    }
                }
            } else if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                // seems to be required to allow players to damage enemies on fire
                // todo: make it so this doesnt go batshit when the entity is in lava
                event.getEntity().invulnerableTime = 0;
            }
        }
    }
}
