package io.github.moosyu.data.datagen;

import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.recipes.SizedItemRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredRecipeProvider extends RecipeProvider {
    protected UnshatteredRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        createRecipe(output, UnshatteredItems.ZOMBIE_HEART.get(),
                SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32),
                SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32),
                SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32),
                SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32),
                SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32),
                SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32),
                SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32),
                SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32)
        );

        createRecipe(output, UnshatteredItems.ZOMBIE_SWORD.get(),
                singleSizedIngredient(UnshatteredItems.ZOMBIE_HEART),
                singleSizedIngredient(UnshatteredItems.ZOMBIE_HEART),
                singleSizedIngredient(Items.STICK)
        );

        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_GOLD_BLOCK.get(), UnshatteredItems.ENCHANTED_GOLD_INGOT);

        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_GOLD_INGOT.get(), Items.GOLD_INGOT);

        createRecipe(output, UnshatteredItems.ORNATE_ZOMBIE_SWORD.get(),
                singleSizedIngredient(UnshatteredItems.ENCHANTED_GOLD_BLOCK),
                singleSizedIngredient(UnshatteredItems.GOLDEN_POWDER),
                singleSizedIngredient(Items.STICK)
        );

        createRecipe(output, UnshatteredItems.FLORID_ZOMBIE_SWORD.get(),
                SizedIngredient.of(UnshatteredItems.HEALING_TISSUE, 24),
                SizedIngredient.of(UnshatteredItems.HEALING_TISSUE, 24),
                singleSizedIngredient(Items.STICK)
        );
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput output) {
            return new UnshatteredRecipeProvider(provider, output);
        }

        @Override
        public @NonNull String getName() {
            return "Unshattered Recipe Provider";
        }
    }

    private void createRecipe(RecipeOutput output, Item result, SizedIngredient... ingredients) {
        SizedItemRecipeBuilder builder = new SizedItemRecipeBuilder(new ItemStackTemplate(result));

        for (SizedIngredient ingredient : ingredients) {
            builder.addIngredient(ingredient);
        }

        builder.save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(MODID, result.getDescriptionId().replace("item." + MODID + ".", "") + "_recipe")));
    }

    private void createEnchantedItemRecipe(RecipeOutput output, Item result, ItemLike ingredient) {
        SizedItemRecipeBuilder builder = new SizedItemRecipeBuilder(new ItemStackTemplate(result));

        for (int i = 0; i < 5; i++) {
            builder.addIngredient(SizedIngredient.of(ingredient, 32));
        }

        builder.save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(MODID, result.getDescriptionId().replace("item." + MODID + ".", "") + "_recipe")));
    }

    private SizedIngredient singleSizedIngredient(ItemLike item) {
        return new SizedIngredient(Ingredient.of(item), 1);
    }
}