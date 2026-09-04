package io.github.moosyu.mixins;

import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// removes the little crafting table in the inventory
@Mixin(AbstractCraftingMenu.class)
public abstract class AbstractCraftingMenuMixin {
    @Inject(method = "addCraftingGridSlots", at = @At("HEAD"), cancellable = true)
    private void skipAddingCraftingGridSlots(CallbackInfo ci) {
        // i know intellij says Condition '(Object) this instanceof InventoryMenu' is always 'false' but its wrong...
        if ((Object) this instanceof InventoryMenu) {
            ci.cancel();
        }
    }

    @Inject(method = "addResultSlot", at = @At("HEAD"), cancellable = true)
    private void skipAddingResultSlot(CallbackInfoReturnable<Slot> cir) {
        if ((Object) this instanceof InventoryMenu) {
            cir.setReturnValue(null);
        }
    }
}