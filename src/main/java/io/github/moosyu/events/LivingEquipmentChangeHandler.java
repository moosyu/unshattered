package io.github.moosyu.events;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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

                Item fromItem = event.getFrom().getItem();
                if (fromItem instanceof AbilityItem) {
                    ItemAttachments itemAttachments = event.getFrom().get(UnshatteredDataComponents.ITEM_ATTACHMENTS);

                    if (itemAttachments != null) {
                        itemAttachments.attachments().forEach((_, itemStack) -> {
                            if (itemStack.getItem() instanceof AbilityItem) {
                                abilityEffects.removePassiveItem(itemStack, context);
                            }
                        });
                    }

                    abilityEffects.removePassiveItem(event.getFrom(), context);
                }

                if (event.getTo().getItem() instanceof AbilityItem) {
                    ItemAttachments itemAttachments = event.getTo().get(UnshatteredDataComponents.ITEM_ATTACHMENTS);

                    if (itemAttachments != null) {
                        itemAttachments.attachments().forEach((_, itemStack) -> {
                            if (itemStack.getItem() instanceof AbilityItem) {
                                abilityEffects.addPassiveItem(itemStack, context);
                            }
                        });
                    }

                    abilityEffects.addPassiveItem(event.getTo(), context);
                }
            }
        }
    }
}
