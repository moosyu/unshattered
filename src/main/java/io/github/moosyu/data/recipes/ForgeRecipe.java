package io.github.moosyu.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * @param durationSeconds seconds (not ticks) the recipe takes to forge. used as forge time uses epoch seconds.
 */
public record ForgeRecipe(ItemStackTemplate result, List<SizedIngredient> ingredients, RecipeBookCategory category, long durationSeconds) implements Recipe<ForgeRecipeInput> {
    @Override
    public boolean matches(@NonNull ForgeRecipeInput input, @NonNull Level level) {
        for (SizedIngredient ingredient : ingredients) {
            int playerIngredientCount = 0;
            for (ItemStack itemStack : input.inputs()) {
                if (ingredient.test(itemStack)) {
                    playerIngredientCount += itemStack.count();
                }
            }

            if (playerIngredientCount < ingredient.count()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull ForgeRecipeInput input) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<ForgeRecipeInput>> getSerializer() {
        return UnshatteredRecipes.FORGE_SERIALIZER.get();
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<ForgeRecipeInput>> getType() {
        return UnshatteredRecipes.FORGE_TYPE.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        // (not recipes where you can place the blocks)
        return PlacementInfo.NOT_PLACEABLE;
    }


    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return category;
    }

    public void consume(Inventory inventory) {
        for (SizedIngredient ingredient : ingredients) {
            int remaining = ingredient.count();
            for (ItemStack itemStack : inventory.getNonEquipmentItems()) {
                if (remaining <= 0) {
                    break;
                }

                if (ingredient.ingredient().test(itemStack)) {
                    int take = Math.min(remaining, itemStack.getCount());
                    itemStack.shrink(take);
                    remaining -= take;
                }
            }
        }
    }

    public static final MapCodec<ForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(ForgeRecipe::result),
                    SizedIngredient.NESTED_CODEC.listOf().fieldOf("ingredients").forGetter(ForgeRecipe::ingredients),
                    BuiltInRegistries.RECIPE_BOOK_CATEGORY.byNameCodec().fieldOf("category").forGetter(ForgeRecipe::category),
                    Codec.LONG.fieldOf("duration_ticks").forGetter(ForgeRecipe::durationSeconds)
            ).apply(instance, ForgeRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ForgeRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, ForgeRecipe::result,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), ForgeRecipe::ingredients,
            ByteBufCodecs.registry(Registries.RECIPE_BOOK_CATEGORY), ForgeRecipe::category,
            ByteBufCodecs.LONG, ForgeRecipe::durationSeconds,
            ForgeRecipe::new
    );

}
