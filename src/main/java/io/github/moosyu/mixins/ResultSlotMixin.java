package io.github.moosyu.mixins;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.recipes.SizedItemRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;

@Mixin(ResultSlot.class)
public abstract class ResultSlotMixin {
    private static final float CARPENTRY_MULTIPLIER = 0.1f;
    @Shadow
    protected abstract void checkTakeAchievements(ItemStack carried);

    @Shadow
    CraftingContainer craftSlots;

    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    private void onTake(Player player, ItemStack carried, CallbackInfo ci) {
        ci.cancel();

        checkTakeAchievements(carried);
        CommonHooks.setCraftingPlayer(player);

        if (player.level() instanceof ServerLevel serverLevel) {
            CraftingInput.Positioned positionedRecipe = craftSlots.asPositionedCraftInput();
            CraftingInput input = positionedRecipe.input();

            serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, serverLevel).ifPresent(recipe -> {
                SizedItemRecipe sizedRecipe = recipe.value() instanceof SizedItemRecipe sized ? sized : null;
                double carpentryExp = 0.0f;

                for (int y = 0; y < input.height(); ++y) {
                    for (int x = 0; x < input.width(); ++x) {
                        int slot = x + positionedRecipe.left() + (y + positionedRecipe.top()) * craftSlots.getWidth();
                        int itemRemovedCount;
                        Optional<SizedIngredient> ingredient = sizedRecipe == null ? Optional.empty() : sizedRecipe.pattern().ingredients().get(slot);
                        int sellValue = Objects.requireNonNullElse(craftSlots.getItems().get(slot).getItem().components().get(UnshatteredDataComponents.SELL_VALUE), 0);

                        if (ingredient.isEmpty()) {
                            itemRemovedCount = 1;
                            carpentryExp += sellValue * CARPENTRY_MULTIPLIER;
                        } else {
                            itemRemovedCount = ingredient.get().count();
                            carpentryExp += (sellValue * itemRemovedCount) * CARPENTRY_MULTIPLIER;
                        }

                        craftSlots.removeItem(slot, itemRemovedCount);
                    }
                }

                player.getData(UnshatteredAttachments.PLAYER_SKILLS).addExp(PlayerSkillsAttachment.Skill.CARPENTRY, (float) carpentryExp, player);
                player.syncData(UnshatteredAttachments.PLAYER_SKILLS);
            });
        }
    }
}
