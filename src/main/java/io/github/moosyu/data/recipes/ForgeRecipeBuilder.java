package io.github.moosyu.data.recipes;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ForgeRecipeBuilder implements RecipeBuilder {
    private final ItemStackTemplate result;
    private final RecipeBookCategory category;
    private final List<SizedIngredient> ingredients = new ArrayList<>();
    private final long durationSeconds;

    public ForgeRecipeBuilder(ItemStackTemplate result, Supplier<RecipeBookCategory> category, long durationSeconds) {
        this.result = result;
        this.category = category.get();
        this.durationSeconds = durationSeconds;
    }

    public static ForgeRecipeBuilder create(ItemLike result, Supplier<RecipeBookCategory> category, long durationSeconds) {
        return new ForgeRecipeBuilder(new ItemStackTemplate(result.asItem()), category, durationSeconds);
    }

    public ForgeRecipeBuilder define(ItemLike ingredient, int count) {
        ingredients.add(SizedIngredient.of(ingredient, count));
        return this;
    }

    /**
     * define ingredient with count of 1
     */
    public ForgeRecipeBuilder define(ItemLike ingredient) {
        ingredients.add(SizedIngredient.of(ingredient, 1));
        return this;
    }

    @Override
    public @NonNull RecipeBuilder unlockedBy(@NonNull String name, @NonNull Criterion<?> criterion) {
        return this;
    }

    @Override
    public @NonNull RecipeBuilder group(@Nullable String s) {
        return this;
    }

    @Override
    public @NonNull ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(result);
    }

    @Override
    public void save(RecipeOutput recipeOutput, @NonNull ResourceKey<Recipe<?>> key) {
        recipeOutput.accept(key, new ForgeRecipe(result, ingredients, category, durationSeconds), null);
    }
}
