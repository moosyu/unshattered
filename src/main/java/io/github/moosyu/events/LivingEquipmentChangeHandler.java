package io.github.moosyu.events;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.function.Consumer;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingEquipmentChangeHandler {
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        PlayerAbilityEffectsAttachment abilityEffects = serverPlayer.getData(UnshatteredAttachments.PLAYER_ABILITIES);
        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, serverPlayer);
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        EquipmentSlot slot = event.getSlot();

        // remove the old stack first so swapping between two items doesn't double up
        forEachAttachment(from, attachment -> abilityEffects.removePassiveItem(attachment, context), slot);

        if (from.getItem() instanceof AbilityItem) {
            abilityEffects.removePassiveItem(from, context);
        }

        forEachAttachment(to, attachment -> abilityEffects.addPassiveItem(attachment, context), slot);

        if (to.getItem() instanceof AbilityItem) {
            abilityEffects.addPassiveItem(to, context);
        }
    }

    /**
     * doesnt run inside the instanceof AbilityItem bits as the tools might not be but the attachments always will be
     */
    private static void forEachAttachment(ItemStack itemStack, Consumer<ItemStack> action, EquipmentSlot slot) {
        if (itemStack.isEmpty() || slot != EquipmentSlot.MAINHAND) return;

        ItemAttachments attachments = itemStack.get(UnshatteredDataComponents.ITEM_ATTACHMENTS);
        if (attachments == null) return;

        attachments.attachments().forEach((_, attachment) -> {
            if (!attachment.isEmpty() && attachment.getItem() instanceof AbilityItem) {
                action.accept(attachment);
            }
        });
    }
}