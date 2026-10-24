package io.github.moosyu.mixins;

import net.minecraft.world.item.component.BlocksAttacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlocksAttacks.class)
public class BlocksAttacksMixin {
    @Inject(method = "blockDelayTicks", at = @At("HEAD"), cancellable = true)
    private void noDelay(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }
}
