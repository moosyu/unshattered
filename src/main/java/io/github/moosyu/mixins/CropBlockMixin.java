package io.github.moosyu.mixins;

import net.minecraft.world.level.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// so things like breakable wheat dont update and break from vanilla random ticking n such
@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    protected void mayPlaceOn(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    protected void isRandomlyTicking(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
