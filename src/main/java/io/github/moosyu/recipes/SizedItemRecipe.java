package io.github.moosyu.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record SizedItemRecipe(ItemStackTemplate result, List<SizedIngredient> ingredients) implements Recipe<SizedItemRecipeInput> {
    @Override
    public boolean matches(@NonNull SizedItemRecipeInput inputs, @NonNull Level level) {
        if (inputs.size() != ingredients.size()) return false;
        for (int i = 0; i < inputs.size(); i++) {
            if (!ingredients.get(i).test(inputs.getItem(i))) return false;
        }
        return true;
    }

    public static final MapCodec<SizedItemRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(SizedItemRecipe::result),
                    SizedIngredient.NESTED_CODEC.listOf().fieldOf("ingredients").forGetter(SizedItemRecipe::ingredients)
            ).apply(instance, SizedItemRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SizedItemRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, SizedItemRecipe::result,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), SizedItemRecipe::ingredients,
            SizedItemRecipe::new
    );

    @Override
    public @NonNull ItemStack assemble(@NonNull SizedItemRecipeInput input) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<SizedItemRecipeInput>> getSerializer() {
        return UnshatteredRecipes.SIZED_RECIPE.get();
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<SizedItemRecipeInput>> getType() {
        return UnshatteredRecipes.SIZED_RECIPE_TYPE.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
