package io.github.moosyu.data.recipes;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SizedItemRecipeBuilder implements RecipeBuilder {
    private final ItemStackTemplate result;
    private final Map<Character, SizedIngredient> key = new LinkedHashMap<>();
    private final List<String> pattern = new ArrayList<>();
    private final RecipeUnlockAdvancementBuilder advancementBuilder;
    private final RecipeCategory category;

    public SizedItemRecipeBuilder(ItemStackTemplate result, RecipeCategory category) {
        this.result = result;
        this.advancementBuilder = new RecipeUnlockAdvancementBuilder();
        this.category = category;
    }

    public SizedItemRecipeBuilder pattern(String... rows) {
        this.pattern.addAll(List.of(rows));
        return this;
    }

    public SizedItemRecipeBuilder define(char symbol, SizedIngredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("symbol " + symbol + " is already defined");
        }

        if (symbol == ' ') {
            throw new IllegalArgumentException("space can't be used as a symbol for the recipe builder");
        }

        this.key.put(symbol, ingredient);
        return this;
    }

    @Override
    public @NonNull RecipeBuilder unlockedBy(@NonNull String name, @NonNull Criterion<?> criterion) {
        this.advancementBuilder.unlockedBy(name, criterion);
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
        output.accept(key, new SizedItemRecipe(this.result, SizedShapedRecipePattern.of(this.key, this.pattern)), advancementBuilder.build(output, key, category));
    }
}