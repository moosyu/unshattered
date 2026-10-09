package io.github.moosyu.data.recipes;

import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.level.ItemLike;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ForgeCategoryDisplay {
    private static final Map<RecipeBookCategory, Supplier<ItemStack>> ICONS = new LinkedHashMap<>();

    private static void init() {
        put(UnshatteredRecipeBookCategories.REFINING, UnshatteredItems.REFINED_MITHRIL);
        put(UnshatteredRecipeBookCategories.GEAR, Items.GOLDEN_CHESTPLATE);
        put(UnshatteredRecipeBookCategories.FORGING, UnshatteredItems.FUEL_CANISTER);
        put(UnshatteredRecipeBookCategories.TOOLS, UnshatteredItems.MITHRIL_DRILL_SX_R226);
        put(UnshatteredRecipeBookCategories.DRILL_PARTS, UnshatteredItems.MITHRIL_INFUSED_FUEL_TANK);
    }

    private static void put(Supplier<RecipeBookCategory> category, ItemLike item) {
        ICONS.put(category.get(), () -> new ItemStack(item));
    }

    /**
     * should only be run clientside
     * @return list copy all categories to get the icons from
     */
    public static List<RecipeBookCategory> all() {
        if (ICONS.isEmpty()) {
            init();
        }

        return List.copyOf(ICONS.keySet());
    }

    /**
     * should only be run clientside
     */
    public static ItemStack getIcon(RecipeBookCategory category) {
        if (ICONS.isEmpty()) {
            init();
        }

        Supplier<ItemStack> itemStackSupplier = ICONS.get(category);
        return itemStackSupplier == null ? ItemStack.EMPTY : itemStackSupplier.get();
    }

    public static Component getName(RecipeBookCategory category) {
        Identifier recipeBookCategoryIdentifier = BuiltInRegistries.RECIPE_BOOK_CATEGORY.getKey(category);

        if (recipeBookCategoryIdentifier == null) {
            return Component.empty();
        }

        return Component.translatable("recipe_book.category." + recipeBookCategoryIdentifier.getNamespace() + "." + recipeBookCategoryIdentifier.getPath()).withColor(UnshatteredUtils.GREEN);
    }

    public static Component getDescription(RecipeBookCategory category) {
        Identifier recipeBookCategoryIdentifier = BuiltInRegistries.RECIPE_BOOK_CATEGORY.getKey(category);

        if (recipeBookCategoryIdentifier == null) {
            return Component.empty();
        }

        return Component.translatable("recipe_book.category." + recipeBookCategoryIdentifier.getNamespace() + ".description." + recipeBookCategoryIdentifier.getPath()).withColor(UnshatteredUtils.GRAY);
    }
}
