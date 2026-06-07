package io.github.moosyu.events;

import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.AttributesRegistry;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.helpers.PlayerDamageHelper;
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
            event.setNewDamage(0.0f);
            Entity attacker = event.getSource().getEntity();
            double playerDefenseValue = player.getAttributeValue(UnshatteredAttributes.DEFENSE.holder);
            if (attacker instanceof LivingEntity entity) {
                double attackerDamageValue = entity.getAttributeValue(UnshatteredAttributes.DAMAGE.holder);
                double damageDealt = attackerDamageValue * (1 - (playerDefenseValue / (playerDefenseValue + 100)));
                PlayerDamageHelper.damagePlayer(player, damageDealt, level, player.getName().getString() + " was slain by a " + entity.getName().getString() + "!");
            } else if (event.getSource().is(DamageTypeTags.IS_FALL)) {
                int blocksFallen = (int) (event.getOriginalDamage() + 3);
                // https://old.reddit.com/r/HypixelSkyblock/comments/fvozn7/fall_damage_calculator/
                double damageDealt = (((blocksFallen - 6.5) * 200 / 33) / ((playerDefenseValue / 100) + 1));
                PlayerDamageHelper.damagePlayer(player, damageDealt, level, player.getName().getString() + " fell to their death!");
            }
        } else if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            event.getEntity().invulnerableTime = 0;
        }
    }
}
