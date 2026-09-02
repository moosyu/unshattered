package io.github.moosyu.recipes;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SizedItemRecipeBuilder implements RecipeBuilder {
    private final ItemStackTemplate result;
    private final List<SizedIngredient> ingredients = new ArrayList<>();

    public SizedItemRecipeBuilder(ItemStackTemplate result) {
        this.result = result;
    }

    public void addIngredient(SizedIngredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void addIngredient(ItemLike item) {
        this.ingredients.add(SizedIngredient.of(item, 1));
    }

    @Override
    public @NonNull RecipeBuilder unlockedBy(@NonNull String name, @NonNull Criterion<?> criterion) {
        return this;
    }

    @Override
    public @NonNull RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public @NonNull ResourceKey<Recipe<?>> defaultId() {
        return RecipeBuilder.getDefaultRecipeId(this.result);
    }

    @Override
    public void save(RecipeOutput output, @NonNull ResourceKey<Recipe<?>> key) {
        SizedItemRecipe recipe = new SizedItemRecipe(this.result, List.copyOf(this.ingredients));
        output.accept(key, recipe, null);
    }
}