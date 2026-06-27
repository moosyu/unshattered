package io.github.moosyu.mixins;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Inject(method = "catchingFish", at = @At("HEAD"), cancellable = true)
    // hook.timeUntilHooked = rand.nextInt(10, Math.max(10, (int) (UnshatteredAttributeValues.FISHING_SPEED.max + 20 - fishingSpeed)));
    // hook.timeUntilLured = rand.nextInt(20, Math.max(20, (int) (UnshatteredAttributeValues.FISHING_SPEED.max + 20 - fishingSpeed)));
    private void unshattered$catchingFish(BlockPos blockPos, CallbackInfo ci) {
        ci.cancel();
        FishingHook hook = (FishingHook)(Object)this;
        ServerLevel level = (ServerLevel)hook.level();
        if (!(hook.getOwner() instanceof Player player)) return;
        double fishingSpeed = player.getAttributeValue(UnshatteredAttributeValues.FISHING_SPEED.holder);
        System.out.println(fishingSpeed);
    }
}
