package io.github.moosyu.abilities.data;

import io.github.moosyu.Unshattered;
import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.packets.ZombieSwordEffectsPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public final class InstantHeal extends ActiveAbility {
    private final int healAmount;

    public InstantHeal(ItemAbility data, int healAmount) {
        super(data);
        this.healAmount = healAmount;
    }

    @Override
    protected boolean canActivate(AbilityContext context) {
        Optional<ServerPlayer> optionalPlayer = context.get(AbilityContextKey.PLAYER);
        Optional<ItemStack> optionalItemStack = context.get(AbilityContextKey.ITEM_STACK);
        if (optionalPlayer.isEmpty() || optionalItemStack.isEmpty()) return false;

        ServerPlayer player = optionalPlayer.get();
        ItemCharges charges = optionalItemStack.get().get(UnshatteredDataComponents.CHARGES.get());
        if (player.getAttribute(UnshatteredAttributeValues.MAX_HEALTH.holder) == null || charges == null) {
            Unshattered.LOGGER.error("max health attribute or charges missing (from {})", identifier());
            return false;
        }
        return player.isCreative() || UnshatteredUtils.passesChargesCheck(player, charges, identifier());
    }

    @Override
    protected void onActivate(AbilityContext context, boolean refreshing) {
        ServerPlayer player = context.get(AbilityContextKey.PLAYER).orElseThrow();
        ItemStack itemStack = context.get(AbilityContextKey.ITEM_STACK).orElseThrow();

        if (!player.isCreative()) {
            ItemCharges charges = itemStack.get(UnshatteredDataComponents.CHARGES.get());

            if (charges != null) {
                itemStack.set(UnshatteredDataComponents.CHARGES.get(), charges.decrementCharges());
            }
        }

        double maxHealth = player.getAttributeValue(UnshatteredAttributeValues.MAX_HEALTH.holder);
        player.getData(UnshatteredAttachments.PLAYER_STATE.get())
                .increaseStatValue(PlayerStateAttachment.Stat.HEALTH, healAmount + maxHealth * 0.05, player);

        PacketDistributor.sendToPlayer(player, new ZombieSwordEffectsPacket());
    }

    @Override
    protected long effectDuration(AbilityContext context) {
        ServerPlayer player = context.get(AbilityContextKey.PLAYER).orElseThrow();
        if (player.isCreative()) {
            return 0;
        }

        return context.get(AbilityContextKey.ITEM_STACK)
                .map(itemStack -> itemStack.get(UnshatteredDataComponents.CHARGES.get()))
                .map(ItemCharges::rechargeTime)
                .orElse(0);
    }

    @Override
    protected boolean canStartEffect(boolean refreshing) {
        return !refreshing;
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            ItemStack itemStack = null;

            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack currentItemStack = inventory.getItem(i);
                if (currentItemStack.getItem() instanceof AbilityItem item) {
                    for (Supplier<? extends Ability> ability : item.abilities()) {
                        if (ability.get() == this) {
                            itemStack = currentItemStack;
                        }
                    }
                }
            }

            if (itemStack == null) {
                return;
            }

            ItemCharges charges = itemStack.get(UnshatteredDataComponents.CHARGES.get());
            if (charges != null) {
                itemStack.set(UnshatteredDataComponents.CHARGES.get(), charges.incrementCharges());
            }
        });
    }
}