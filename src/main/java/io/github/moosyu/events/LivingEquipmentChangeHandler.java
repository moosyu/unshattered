package io.github.moosyu.events;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.abilities.PassiveAbilityItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingEquipmentChangeHandler {
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                PlayerAbilityEffectsAttachment abilityEffects = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);
                AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, serverPlayer);

                if (event.getFrom().getItem() instanceof PassiveAbilityItem oldAbilityItem) {
                    abilityEffects.removePassiveItem(oldAbilityItem, context);
                }

                if (event.getTo().getItem() instanceof PassiveAbilityItem newAbilityItem) {
                    abilityEffects.addPassiveItem(newAbilityItem, context);
                }
            }
        }
    }
}
