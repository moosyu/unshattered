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
        ServerPlayer player = holder instanceof ServerPlayer sp ? sp : null;
        AbilityContext context = player != null ? new AbilityContext().add(AbilityContextKey.PLAYER, player) : null;
        List<PassiveAbilityItem> triggeredItems = new ArrayList<>();

        if (player != null) {
            for (PassiveAbilityItem item : player.getData(UnshatteredAttachments.PLAYER_ABILITIES).getStoredPassiveNonOngoingItems()) {
                if (!item.triggerTypes().contains(AbilityTriggerType.PLAYER_USE_PROJECTILE_WEAPON_AMMO) || !item.abilityConditionsMet(context)) {
                    continue;
                }

                item.onAbilityTriggered(context);
                triggeredItems.add(item);


                Optional<?> result = item.triggerResult();
                if (result.isPresent() && result.get() == AbilityTriggerResult.CANCEL_EVENT) {
                    finishTriggeredItems(triggeredItems, context);
                    cir.setReturnValue(projectile.copyWithCount(1));
                    return;
                }
            }
        }

        int ammoToUse = !forceInfinite && holder.level() instanceof ServerLevel && !(holder.hasInfiniteMaterials() || (projectile.getItem() instanceof ArrowItem ai && ai.isInfinite(projectile, weapon, holder))) ? 1 : 0;
        if (ammoToUse > projectile.getCount()) {
            finishTriggeredItems(triggeredItems, context);
            cir.setReturnValue(ItemStack.EMPTY);
        } else if (ammoToUse == 0) {
            finishTriggeredItems(triggeredItems, context);
            cir.setReturnValue(projectile.copyWithCount(1));
        } else {
            ItemStack used = projectile.split(ammoToUse);
            if (projectile.isEmpty() && player != null) {
                player.getInventory().removeItem(projectile);
                finishTriggeredItems(triggeredItems, context);
            }
            cir.setReturnValue(used);
        }
    }

    private static void finishTriggeredItems(List<PassiveAbilityItem> triggeredItems, AbilityContext context) {
        if (context == null) return;
        triggeredItems.forEach(item -> item.onAbilityFinished(context));
    }
}
