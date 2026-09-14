package io.github.moosyu.data.datagen;

import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.data.recipes.SizedItemRecipeBuilder;
import io.github.moosyu.data.recipes.SizedShapedRecipePattern;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;
import org.lwjgl.system.ffm.mapping.Mapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredRecipeProvider extends RecipeProvider {
    final HolderLookup.Provider provider;
    protected UnshatteredRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);

        this.provider = provider;
    }

    @Override
    protected void buildRecipes() {
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

        createEnchantedItemWithBlocksRecipe(output, Items.GOLD_INGOT, Items.GOLD_BLOCK, UnshatteredItems.ENCHANTED_GOLD_INGOT.get(), getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT));
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_GOLD_INGOT, UnshatteredItems.ENCHANTED_GOLD_BLOCK.get(), getHasName(UnshatteredItems.ENCHANTED_GOLD_INGOT), has(UnshatteredItems.ENCHANTED_GOLD_INGOT));
        createEnchantedItemWithBlocksRecipe(output, Items.DIAMOND, Items.DIAMOND_BLOCK, UnshatteredItems.ENCHANTED_DIAMOND.get(), getHasName(Items.DIAMOND), has(Items.DIAMOND));
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_DIAMOND, UnshatteredItems.ENCHANTED_DIAMOND_BLOCK.get(), getHasName(UnshatteredItems.ENCHANTED_DIAMOND), has(UnshatteredItems.ENCHANTED_DIAMOND));
        createEnchantedItemWithBlocksRecipe(output, Items.EMERALD, Items.EMERALD_BLOCK, UnshatteredItems.ENCHANTED_EMERALD.get(), getHasName(Items.EMERALD), has(Items.EMERALD));
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_EMERALD, UnshatteredItems.ENCHANTED_EMERALD_BLOCK.get(), getHasName(UnshatteredItems.ENCHANTED_EMERALD), has(UnshatteredItems.ENCHANTED_EMERALD));
        createEnchantedItemWithBlocksRecipe(output, Items.IRON_INGOT, Items.IRON_BLOCK, UnshatteredItems.ENCHANTED_IRON.get(), getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT));
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_IRON, UnshatteredItems.ENCHANTED_IRON_BLOCK.get(), getHasName(UnshatteredItems.ENCHANTED_IRON), has(UnshatteredItems.ENCHANTED_IRON));
        createEnchantedItemWithBlocksRecipe(output, Items.COAL, Items.COAL_BLOCK, UnshatteredItems.ENCHANTED_COAL.get(), getHasName(Items.COAL), has(Items.COAL));
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_COAL, UnshatteredItems.ENCHANTED_COAL_BLOCK.get(), getHasName(UnshatteredItems.ENCHANTED_COAL), has(UnshatteredItems.ENCHANTED_COAL));
        createEnchantedItemWithBlocksRecipe(output, Items.LAPIS_ORE, Items.LAPIS_BLOCK, UnshatteredItems.ENCHANTED_LAPIS.get(), getHasName(Items.LAPIS_ORE), has(Items.LAPIS_ORE));
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_LAPIS, UnshatteredItems.ENCHANTED_LAPIS_BLOCK.get(), getHasName(UnshatteredItems.ENCHANTED_LAPIS), has(UnshatteredItems.ENCHANTED_LAPIS));
        createEnchantedItemWithBlocksRecipe(output, Items.REDSTONE, Items.REDSTONE_BLOCK, UnshatteredItems.ENCHANTED_REDSTONE.get(), getHasName(Items.REDSTONE), has(Items.REDSTONE));
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_REDSTONE, UnshatteredItems.ENCHANTED_REDSTONE_BLOCK.get(), getHasName(UnshatteredItems.ENCHANTED_REDSTONE), has(UnshatteredItems.ENCHANTED_REDSTONE));
        createEnchantedItemRecipe(output, Items.OAK_LOG, UnshatteredItems.ENCHANTED_OAK_LOG.get(), getHasName(Items.OAK_LOG), has(Items.OAK_LOG));
        createEnchantedItemRecipe(output, Items.BIRCH_LOG, UnshatteredItems.ENCHANTED_BIRCH_LOG.get(), getHasName(Items.BIRCH_LOG), has(Items.BIRCH_LOG));
        createEnchantedItemRecipe(output, Items.SPRUCE_LOG, UnshatteredItems.ENCHANTED_SPRUCE_LOG.get(), getHasName(Items.SPRUCE_LOG), has(Items.SPRUCE_LOG));
        createEnchantedItemRecipe(output, Items.JUNGLE_LOG, UnshatteredItems.ENCHANTED_JUNGLE_LOG.get(), getHasName(Items.JUNGLE_LOG), has(Items.JUNGLE_LOG));
        createEnchantedItemRecipe(output, Items.ACACIA_LOG, UnshatteredItems.ENCHANTED_ACACIA_LOG.get(), getHasName(Items.ACACIA_LOG), has(Items.ACACIA_LOG));
        createEnchantedItemRecipe(output, Items.DARK_OAK_LOG, UnshatteredItems.ENCHANTED_DARK_OAK_LOG.get(), getHasName(Items.DARK_OAK_LOG), has(Items.DARK_OAK_LOG));
        createEnchantedItemRecipe(output, UnshatteredItems.MITHRIL, UnshatteredItems.ENCHANTED_MITHRIL.get(), getHasName(UnshatteredItems.MITHRIL), has(UnshatteredItems.MITHRIL));
        createEnchantedItemRecipe(output, Items.POISONOUS_POTATO, UnshatteredItems.ENCHANTED_POISONOUS_POTATO.get(), getHasName(Items.POISONOUS_POTATO), has(Items.POISONOUS_POTATO));
        createEnchantedItemRecipe(output, Items.BONE, UnshatteredItems.ENCHANTED_BONE.get(), getHasName(Items.BONE), has(Items.BONE));
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_BONE, UnshatteredItems.ENCHANTED_BONE_BLOCK.get(), getHasName(UnshatteredItems.ENCHANTED_BONE), has(UnshatteredItems.ENCHANTED_BONE));
        createEnchantedItemRecipe(output, Items.STRING, UnshatteredItems.ENCHANTED_STRING.get(), getHasName(Items.STRING), has(Items.STRING));
        createEnchantedItemRecipe(output, Items.FLINT, UnshatteredItems.ENCHANTED_FLINT.get(), getHasName(Items.FLINT), has(Items.FLINT));
        createEnchantedItemRecipe(output, Items.COBBLESTONE, UnshatteredItems.ENCHANTED_COBBLESTONE.get(), getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE));

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.ZOMBIE_HEART.get()), RecipeCategory.COMBAT)
                .pattern("AAA", "A A", "AAA")
                .define('A', SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32))
                .unlockedBy(getHasName(UnshatteredItems.ENCHANTED_ROTTEN_FLESH), has(UnshatteredItems.ENCHANTED_ROTTEN_FLESH))
                .save(output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, UnshatteredItems.ZOMBIE_SWORD)
                .pattern("A")
                .pattern("A")
                .pattern("B")
                .define('A', UnshatteredItems.ZOMBIE_HEART)
                .define('B', Items.STICK)
                .unlockedBy(getHasName(UnshatteredItems.ZOMBIE_HEART), has(UnshatteredItems.ZOMBIE_HEART))
                .save(output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, UnshatteredItems.ORNATE_ZOMBIE_SWORD)
                .pattern("A")
                .pattern("B")
                .pattern("C")
                .define('A', UnshatteredItems.ENCHANTED_GOLD_BLOCK)
                .define('B', UnshatteredItems.GOLDEN_POWDER)
                .define('C', Items.STICK)
                .unlockedBy(getHasName(UnshatteredItems.GOLDEN_POWDER), has(UnshatteredItems.GOLDEN_POWDER))
                .save(output);

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.FLORID_ZOMBIE_SWORD.get()), RecipeCategory.COMBAT)
                .pattern("A", "A", "C")
                .define('A', SizedIngredient.of(UnshatteredItems.HEALING_TISSUE, 24))
                .define('C', singleSizedIngredient(Items.STICK))
                .unlockedBy(getHasName(UnshatteredItems.HEALING_TISSUE), has(UnshatteredItems.HEALING_TISSUE))
                .save(output);

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.COINS_TALISMAN.get()), RecipeCategory.TOOLS)
                .pattern(" A ", "ABA", " A ")
                .define('A', SizedIngredient.of(Items.EMERALD, 5))
                .define('B', SizedIngredient.of(Items.GOLD_INGOT, 5))
                .unlockedBy(getHasName(Items.EMERALD), has(Items.EMERALD))
                .save(output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, UnshatteredItems.SKELETON_HAT)
                .pattern("BBB")
                .pattern("B B")
                .pattern("BBB")
                .define('B', Items.BONE)
                .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                .save(output);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, UnshatteredItems.EMERALD_DAGGER)
                .pattern("E")
                .pattern("S")
                .define('E', UnshatteredItems.ENCHANTED_EMERALD_BLOCK)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(UnshatteredItems.ENCHANTED_EMERALD), has(UnshatteredItems.ENCHANTED_EMERALD))
                .save(output);

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.SPRUCE_AXE.get()), RecipeCategory.TOOLS)
                .pattern(" WW", " SW", " S ")
                .define('W', SizedIngredient.of(Items.SPRUCE_LOG, 32))
                .define('S', singleSizedIngredient(Items.STICK))
                .unlockedBy(getHasName(Items.SPRUCE_LOG), has(Items.SPRUCE_LOG))
                .save(output);

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.TREECAPITATOR.get()), RecipeCategory.TOOLS)
                .pattern("OOO", "OSO", "OOO")
                .define('O', SizedIngredient.of(UnshatteredItems.ENCHANTED_OBSIDIAN, 8))
                .define('S', singleSizedIngredient(UnshatteredItems.SPRUCE_AXE))
                .unlockedBy(getHasName(UnshatteredItems.ENCHANTED_OBSIDIAN), has(UnshatteredItems.ENCHANTED_OBSIDIAN))
                .save(output);

        createSimpleEnchantedBook(Enchantments.EFFICIENCY, 1, getHasName(UnshatteredItems.ENCHANTED_COBBLESTONE), has(UnshatteredItems.ENCHANTED_COBBLESTONE), UnshatteredItems.ENCHANTED_COBBLESTONE, 64);
        createSimpleEnchantedBook(Enchantments.SMITE, 1, getHasName(UnshatteredItems.ENCHANTED_ROTTEN_FLESH), has(UnshatteredItems.ENCHANTED_ROTTEN_FLESH), UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32);
        createSimpleEnchantedBook(Enchantments.SHARPNESS, 1, getHasName(UnshatteredItems.ENCHANTED_FLINT), has(UnshatteredItems.ENCHANTED_FLINT), UnshatteredItems.ENCHANTED_FLINT, 64);
        createSimpleEnchantedBook(Enchantments.BANE_OF_ARTHROPODS, 1, getHasName(UnshatteredItems.ENCHANTED_STRING), has(UnshatteredItems.ENCHANTED_STRING), UnshatteredItems.ENCHANTED_STRING, 32);
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

    /**
     * creates a simple recipe with a shape across and then down (sorry im bad at describing things)
     * @param result the item  to be crafted
     * @param ingredients sized ingredients required to create the result
     */
    private void createRecipe(Item result, RecipeCategory category, String unlockedById, Criterion<?> criterion, SizedIngredient... ingredients) {
        createRecipe(new ItemStackTemplate(result), 1, createRecipeResourceKey(result), category, unlockedById, criterion, ingredients);
    }

    /**
     * creates a simple recipe with a shape across and then down (sorry im bad at describing things)
     * @param amount amount of item crafted
     * @param key recipe identifier key
     * @param ingredients sized ingredients required to create the item
     */
    private void createRecipe(ItemStackTemplate itemStackTemplate, int amount, ResourceKey<Recipe<?>> key, RecipeCategory category, String unlockedById, Criterion<?> criterion, SizedIngredient... ingredients) {
        if (ingredients.length == 0 || ingredients.length > 9) {
            throw new IllegalArgumentException("count must be between 1 and 9");
        }

        SizedItemRecipeBuilder builder = new SizedItemRecipeBuilder(itemStackTemplate, category);
        int width = Math.min(ingredients.length, 3);
        int height = (int) Math.ceil(ingredients.length / (double) width);
        char[] symbols = "ABCDEFGHI".toCharArray();
        StringBuilder[] rows = new StringBuilder[height];
        HashMap<SizedIngredient, Character> recipeEntries = new HashMap<>();

        for (int row = 0; row < height; row++) {
            rows[row] = new StringBuilder();
        }

        for (int i = 0; i < ingredients.length; i++) {
            char symbol = symbols[i];
            if (recipeEntries.containsKey(ingredients[i])) {
                rows[i / width].append(recipeEntries.get(ingredients[i]));
            } else {
                builder.define(symbol, ingredients[i]);
                rows[i / width].append(symbol);
                recipeEntries.put(ingredients[i], symbol);
            }
        }

        for (StringBuilder row : rows) {
            while (row.length() < width) {
                row.append(SizedShapedRecipePattern.EMPTY_SLOT);
            }
        }

        builder.pattern(Arrays.stream(rows).map(StringBuilder::toString).toArray(String[]::new));
        builder.unlockedBy(unlockedById, criterion);
        builder.save(output, key);
    }

    public static ResourceKey<Recipe<?>> createRecipeResourceKey(Item result) {
        return ResourceKey.create(Registries.RECIPE, UnshatteredUtils.getUnshatteredIdentifier(BuiltInRegistries.ITEM.getKey(result).getPath() + "_recipe"));
    }

    public static ResourceKey<Recipe<?>> createRecipeResourceKey(Item result, String suffix) {
        return ResourceKey.create(Registries.RECIPE, UnshatteredUtils.getUnshatteredIdentifier(BuiltInRegistries.ITEM.getKey(result).getPath() + "_recipe" + suffix));
    }

    /**
     * creates a basic enchanted item recipe (5 sets of 32)
     * @param output output
     * @param ingredient the ingredient that makes up the enchanted item
     * @param result the item that's crafted
     */
    private void createEnchantedItemRecipe(RecipeOutput output, ItemLike ingredient, Item result, String unlockedById, Criterion<?> criterion) {
        SizedIngredient[] ingredients = new SizedIngredient[5];
        Arrays.fill(ingredients, SizedIngredient.of(ingredient, 32));
        createRecipe(result, RecipeCategory.MISC, unlockedById, criterion, ingredients);

        SizedItemRecipeBuilder builder = new SizedItemRecipeBuilder(new ItemStackTemplate(result), RecipeCategory.MISC);
        builder.pattern(" A ", "AAA", " A ")
                .define('A', SizedIngredient.of(ingredient, 32))
                .unlockedBy(unlockedById, criterion)
                .save(output, createRecipeResourceKey(result, "_2"));
    }

    /**
     * creates a basic enchanted item recipe including block variants which create 9
     * @param output output
     * @param itemIngredient the ingredient that makes up the enchanted item's single outputs
     * @param blockIngredient the block used to create 9 of the enchanted item
     * @param result the item that's crafted
     */
    private void createEnchantedItemWithBlocksRecipe(RecipeOutput output, ItemLike itemIngredient, ItemLike blockIngredient, Item result, String unlockedById, Criterion<?> criterion) {
        SizedIngredient[] ingredients = new SizedIngredient[5];
        Arrays.fill(ingredients, SizedIngredient.of(itemIngredient, 32));
        createRecipe(result, RecipeCategory.MISC, unlockedById, criterion, ingredients);

        SizedItemRecipeBuilder builderItem = new SizedItemRecipeBuilder(new ItemStackTemplate(result), RecipeCategory.MISC);
        builderItem.pattern(" A ", "AAA", " A ")
                .define('A', SizedIngredient.of(itemIngredient, 32))
                .unlockedBy(unlockedById, criterion)
                .save(output, createRecipeResourceKey(result, "_2"));

        Arrays.fill(ingredients, SizedIngredient.of(blockIngredient, 32));
        createRecipe(new ItemStackTemplate(result),
                9,
                createRecipeResourceKey(result, "_3"),
                RecipeCategory.MISC,
                unlockedById,
                criterion,
                ingredients
        );

        SizedItemRecipeBuilder builderBlock = new SizedItemRecipeBuilder(new ItemStackTemplate(result, 9), RecipeCategory.MISC);
        builderBlock.pattern(" A ", "AAA", " A ")
                .define('A', SizedIngredient.of(blockIngredient, 32))
                .unlockedBy(unlockedById, criterion)
                .save(output, createRecipeResourceKey(result, "_4"));
    }

    private void createSimpleEnchantedBook(ResourceKey<Enchantment> enchantment, int level, String unlockedById, Criterion<?> criterion, ItemLike ingredient, int amount) {
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        mutable.set(provider.holderOrThrow(enchantment), level);
        ItemEnchantments enchantments = mutable.toImmutable();

        createRecipe(new ItemStackTemplate(BuiltInRegistries.ITEM.wrapAsHolder(Items.ENCHANTED_BOOK), 1, DataComponentPatch.builder().set(DataComponents.STORED_ENCHANTMENTS, enchantments).build()),
                1,
                ResourceKey.create(Registries.RECIPE, UnshatteredUtils.getUnshatteredIdentifier(enchantments.keySet().iterator().next().value().description().getString() + "_recipe")),
                RecipeCategory.MISC,
                unlockedById,
                criterion,
                singleSizedIngredient(Items.BOOK),
                SizedIngredient.of(ingredient, amount)
        );
    }

    private SizedIngredient singleSizedIngredient(ItemLike item) {
        return SizedIngredient.of(item, 1);
    }
}