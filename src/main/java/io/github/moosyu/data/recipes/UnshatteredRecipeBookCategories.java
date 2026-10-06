package io.github.moosyu.data.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredRecipeBookCategories {
    public static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, MODID);

    public static final Supplier<RecipeBookCategory> REFINING = RECIPE_BOOK_CATEGORIES.register("refining", RecipeBookCategory::new);
    public static final Supplier<RecipeBookCategory> GEAR = RECIPE_BOOK_CATEGORIES.register("gear", RecipeBookCategory::new);
    public static final Supplier<RecipeBookCategory> PERFECT_GEMSTONES = RECIPE_BOOK_CATEGORIES.register("perfect_gemstones", RecipeBookCategory::new);
    public static final Supplier<RecipeBookCategory> FORGING = RECIPE_BOOK_CATEGORIES.register("forging", RecipeBookCategory::new);
    public static final Supplier<RecipeBookCategory> REFORGE_STONES = RECIPE_BOOK_CATEGORIES.register("reforge_stones", RecipeBookCategory::new);
    public static final Supplier<RecipeBookCategory> PETS = RECIPE_BOOK_CATEGORIES.register("pets", RecipeBookCategory::new);
    public static final Supplier<RecipeBookCategory> TOOLS = RECIPE_BOOK_CATEGORIES.register("tools", RecipeBookCategory::new);
    public static final Supplier<RecipeBookCategory> DRILL_PARTS = RECIPE_BOOK_CATEGORIES.register("drill_parts", RecipeBookCategory::new);
    public static final Supplier<RecipeBookCategory> OTHER = RECIPE_BOOK_CATEGORIES.register("other", RecipeBookCategory::new);
}