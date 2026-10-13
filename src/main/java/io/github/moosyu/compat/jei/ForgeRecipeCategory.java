package io.github.moosyu.compat.jei;

import io.github.moosyu.data.recipes.ForgeRecipe;
import io.github.moosyu.data.recipes.UnshatteredRecipes;
import io.github.moosyu.util.UnshatteredUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ForgeRecipeCategory implements IRecipeCategory<RecipeHolder<ForgeRecipe>> {
    public static final IRecipeType<RecipeHolder<ForgeRecipe>> TYPE = IRecipeType.create(UnshatteredRecipes.FORGE_TYPE.get());
    private static final int COLUMNS = 3;
    private static final int SLOT = 18;
    private static final int WIDTH = 140;

    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public ForgeRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(Items.BLAST_FURNACE));
        arrow = guiHelper.createAnimatedRecipeArrow(40);
    }

    @Override
    public @NonNull IRecipeType<RecipeHolder<ForgeRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("screen.unshattered.forge");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return 54;
    }

    @Override
    public void setRecipe(@NonNull IRecipeLayoutBuilder builder, RecipeHolder<ForgeRecipe> recipeHolder, @NonNull IFocusGroup focuses) {
        ForgeRecipe recipe = recipeHolder.value();

        List<SizedIngredient> ingredients = recipe.ingredients();
        for (int i = 0; i < ingredients.size(); i++) {
            SizedIngredient sized = ingredients.get(i);

            builder.addInputSlot(1 + (i % COLUMNS) * SLOT, 1 + (i / COLUMNS) * SLOT).setStandardSlotBackground().addItemStacks(sized.ingredient().getValues().stream()
                    .map(holder -> new ItemStack(holder, sized.count()))
                    .toList());
        }
        builder.addOutputSlot(WIDTH - 1 - SLOT, 12).setStandardSlotBackground().add(recipe.result().create());
    }

    @Override
    public void draw(RecipeHolder<ForgeRecipe> holder, @NonNull IRecipeSlotsView recipeSlotsView, @NonNull GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        Component time = Component.literal(UnshatteredUtils.getTimeDisplay(holder.value().durationSeconds()));
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(time);

        arrow.draw(graphics, WIDTH / 2, 13);
        graphics.text(font, time, (WIDTH - textWidth) / 2 + 10, 45, 0xFF808080, false);
    }
}