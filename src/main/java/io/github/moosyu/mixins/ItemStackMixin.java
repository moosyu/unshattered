package io.github.moosyu.mixins;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// shoutout https://github.com/CursedFlames/MCTweaks/blob/stonecutter/NoDurability/src/main/java/cursedflames/nodurability/mixin/MixinItemStack.java
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "isDamageableItem", at = @At("HEAD"), cancellable = true)
    private void isDamageableItem(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}