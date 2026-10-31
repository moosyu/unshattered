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
        if (holder instanceof ServerPlayer serverPlayer) {
            AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, serverPlayer);

            try (AbilityScope scope = AbilityScope.open(serverPlayer, AbilityTriggerType.PLAYER_USE_PROJECTILE_WEAPON_AMMO, context)) {
                if (scope.results.get(AbilityResult.CANCEL_EVENT)) {
                    scope.close();
                    cir.setReturnValue(projectile.copyWithCount(1));
                    return;
                }

                int ammoToUse = !forceInfinite && !(holder.hasInfiniteMaterials() || projectile.getItem() instanceof ArrowItem ai && ai.isInfinite(projectile, weapon, holder)) ? 1 : 0;
                if (ammoToUse > projectile.getCount()) {
                    scope.close();
                    cir.setReturnValue(ItemStack.EMPTY);
                } else if (ammoToUse == 0) {
                    scope.close();
                    cir.setReturnValue(projectile.copyWithCount(1));
                } else {
                    ItemStack usedAmmo = projectile.split(ammoToUse);
                    if (projectile.isEmpty()) {
                        serverPlayer.getInventory().removeItem(projectile);
                        scope.close();
                    }
                    cir.setReturnValue(usedAmmo);
                }
            }
        }
    }
}
