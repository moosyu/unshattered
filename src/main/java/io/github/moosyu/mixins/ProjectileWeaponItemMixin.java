package io.github.moosyu.mixins;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin {
    @Inject(method = "useAmmo", at = @At("HEAD"), cancellable = true)
    private static void useAmmo(ItemStack weapon, ItemStack projectile, LivingEntity holder, boolean forceInfinite, CallbackInfoReturnable<ItemStack> cir) {
        final ServerPlayer SERVER_PLAYER = holder instanceof ServerPlayer serverPlayer ? serverPlayer : null;
        final AbilityContext CONTEXT = SERVER_PLAYER != null ? new AbilityContext().add(AbilityContextKey.PLAYER, SERVER_PLAYER) : null;
        final List<PassiveAbilityItem> TRIGGERED_ITEMS = new ArrayList<>();

        if (SERVER_PLAYER != null) {
            for (ItemStack item : SERVER_PLAYER.getData(UnshatteredAttachments.PLAYER_ABILITIES).getStoredNonOngoingItems()) {
                if (!(item.getItem() instanceof final PassiveAbilityItem PASSIVE_ABILITY_ITEM)
                        || !PASSIVE_ABILITY_ITEM.triggerTypes().contains(AbilityTriggerType.PLAYER_USE_PROJECTILE_WEAPON_AMMO)
                        || !PASSIVE_ABILITY_ITEM.abilityConditionsMet(CONTEXT)
                ) {
                    continue;
                }

                PASSIVE_ABILITY_ITEM.onAbilityTriggered(CONTEXT);
                TRIGGERED_ITEMS.add(PASSIVE_ABILITY_ITEM);

                final Optional<?> RESULT = PASSIVE_ABILITY_ITEM.triggerResult();
                if (RESULT.isPresent() && RESULT.get() == AbilityTriggerResult.CANCEL_EVENT) {
                    finishTriggeredItems(TRIGGERED_ITEMS, CONTEXT);
                    cir.setReturnValue(projectile.copyWithCount(1));
                    return;
                }
            }
        }

        final int AMMO_TO_USE = !forceInfinite && holder.level() instanceof ServerLevel && !(holder.hasInfiniteMaterials() || (projectile.getItem() instanceof ArrowItem ai && ai.isInfinite(projectile, weapon, holder))) ? 1 : 0;
        if (AMMO_TO_USE > projectile.getCount()) {
            finishTriggeredItems(TRIGGERED_ITEMS, CONTEXT);
            cir.setReturnValue(ItemStack.EMPTY);
        } else if (AMMO_TO_USE == 0) {
            finishTriggeredItems(TRIGGERED_ITEMS, CONTEXT);
            cir.setReturnValue(projectile.copyWithCount(1));
        } else {
            final ItemStack USED_AMMO = projectile.split(AMMO_TO_USE);
            if (projectile.isEmpty() && SERVER_PLAYER != null) {
                SERVER_PLAYER.getInventory().removeItem(projectile);
                finishTriggeredItems(TRIGGERED_ITEMS, CONTEXT);
            }
            cir.setReturnValue(USED_AMMO);
        }
    }

    private static void finishTriggeredItems(List<PassiveAbilityItem> triggeredItems, AbilityContext context) {
        if (context == null) {
            return;
        }

        triggeredItems.forEach(item -> item.onAbilityFinished(context));
    }
}
