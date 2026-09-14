package io.github.moosyu.data.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public record SizedItemRecipe(ItemStackTemplate result, SizedShapedRecipePattern pattern) implements CraftingRecipe {
    public static final MapCodec<SizedItemRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(SizedItemRecipe::result),
                    SizedShapedRecipePattern.MAP_CODEC.forGetter(SizedItemRecipe::pattern)
            ).apply(instance, SizedItemRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SizedItemRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, SizedItemRecipe::result,
            SizedShapedRecipePattern.STREAM_CODEC, SizedItemRecipe::pattern,
            SizedItemRecipe::new
    );

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public boolean matches(@NonNull CraftingInput input, @NonNull Level level) {
        return pattern.matches(input);
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull CraftingInput craftingInput) {
        return result.create();
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull CraftingBookCategory category() {
        return CraftingBookCategory.EQUIPMENT;
    }

    @Override
    public @NonNull RecipeType<CraftingRecipe> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public @NonNull RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return UnshatteredRecipes.SIZED_RECIPE.get();
    }

    @Override
    public @NonNull List<RecipeDisplay> display() {
        List<SlotDisplay> ingredientDisplays = pattern.ingredients().stream()
                .map(optSized -> optSized.map(SizedIngredient::ingredient))
                .map(Ingredient::optionalIngredientToDisplay)
                .toList();

        return List.of(new ShapedCraftingRecipeDisplay(
                pattern.width,
                pattern.height,
                ingredientDisplays,
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
        ));
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        List<Optional<Ingredient>> ingredients = pattern.ingredients().stream().map(optSized -> optSized.map(SizedIngredient::ingredient)).toList();

        return PlacementInfo.createFromOptionals(ingredients);
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}