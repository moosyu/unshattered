package io.github.moosyu.mixins;

import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// removes the little crafting table in the inventory
// this breaks the creative menu but i genuinely dont know how to fix that so i just wont for now. if someone knows please tell me.
@Mixin(AbstractCraftingMenu.class)
public abstract class AbstractCraftingMenuMixin {
    @Inject(method = "addCraftingGridSlots", at = @At("HEAD"), cancellable = true)
    private void skipAddingCraftingGridSlots(CallbackInfo ci) {
        if ((Object) this instanceof InventoryMenu) ci.cancel();
    }

    @Inject(method = "addResultSlot", at = @At("HEAD"), cancellable = true)
    private void skipAddingResultSlot(CallbackInfoReturnable<Slot> cir) {
        if ((Object) this instanceof InventoryMenu) cir.setReturnValue(null);
    }
}