package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.AttachmentRegistry.PLAYER_STATE;
import static io.github.moosyu.sounds.UnshatteredSounds.playerDeathSound;

@EventBusSubscriber(modid = MODID)
public class LivingDamageHandler {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        Level level = event.getEntity().level();
        if (level.isClientSide()) return;
        if (event.getEntity() instanceof Player player) {
            Entity attacker = event.getSource().getEntity();
            event.setNewDamage(0.0f);
            if (attacker instanceof LivingEntity entity) {
                AttributeInstance attackerDamageHolder = entity.getAttribute(UnshatteredAttributes.DAMAGE.holder);
                AttributeInstance playerDefenseAttribute = player.getAttribute(UnshatteredAttributes.DEFENSE.holder);
                if (attackerDamageHolder == null || playerDefenseAttribute == null) return;
                PlayerStateAttachment states = player.getData(PLAYER_STATE.get());
                double playerHealth = states.getCurrentStat(PlayerStateAttachment.Stat.HEALTH);
                double damageDealt = attackerDamageHolder.getBaseValue() * (1 - (playerDefenseAttribute.getValue() / (playerDefenseAttribute.getValue() + 100)));
                if (playerHealth - damageDealt > 0.0d) {
                    states.removeCurrentStat(PlayerStateAttachment.Stat.HEALTH, damageDealt);
                    player.syncData(PLAYER_STATE.get());
                } else {
                    BlockPos spawnPos = level.getRespawnData().pos();
                    player.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                    player.sendSystemMessage(Component.literal(player.getName().getString() + " was slain by a " + entity.getName().getString() + "!").withStyle(ChatFormatting.RED));
                    states.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, player.getAttributeValue(UnshatteredAttributes.HEALTH.holder));
                    player.syncData(PLAYER_STATE.get());
                    // todo: fix the death sound not going off
                    playerDeathSound(player);
                    states.setCancelledKnockback(true);
                }
            }
        } else if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            event.getEntity().invulnerableTime = 0;
        }
    }
}
