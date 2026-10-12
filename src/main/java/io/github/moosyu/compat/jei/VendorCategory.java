package io.github.moosyu.compat.jei;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.moosyu.data.NpcData;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.util.UnshatteredUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class VendorCategory implements IRecipeCategory<UnshatteredJEIPlugin.VendorJEIItem> {
    public static final IRecipeType<UnshatteredJEIPlugin.VendorJEIItem> TYPE = IRecipeType.create(UnshatteredUtils.getUnshatteredIdentifier("vendor"), UnshatteredJEIPlugin.VendorJEIItem.class );
    private final int WIDTH = 140;
    private final int HEIGHT = 60;
    private final IDrawable icon;

    public VendorCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(Items.PLAYER_HEAD));
    }

    @Override
    public @NonNull IRecipeType<UnshatteredJEIPlugin.VendorJEIItem> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("screen.unshattered.vendor");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NonNull IRecipeLayoutBuilder builder, UnshatteredJEIPlugin.VendorJEIItem vendorJEIItem, @NonNull IFocusGroup focuses) {
        int slotSize = 18;
        ItemStack headItemStack = new ItemStack(Items.PLAYER_HEAD);
        NpcData skin = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(vendorJEIItem.entityType()).getData(UnshatteredDataMaps.NPC_DATA);
        if (skin != null) {
            JsonObject json = new JsonObject();
            json.addProperty("texture", skin.skinLocation().toString());
            headItemStack.set(DataComponents.PROFILE, ResolvableProfile.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow());
        }

        // add ingredients (could probably just go with gold nugget and tooltip for coins)
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, (WIDTH / 2) - (slotSize / 2), 0).setStandardSlotBackground().add(headItemStack);
        builder.addOutputSlot((WIDTH / 2) - (slotSize / 2), (HEIGHT / 2) - (slotSize / 2)).setStandardSlotBackground().add(new ItemStack(vendorJEIItem.vendorItem().item()));
    }

    @Override
    public void draw(UnshatteredJEIPlugin.@NonNull VendorJEIItem vendorJEIItem, @NonNull IRecipeSlotsView recipeSlotsView, @NonNull GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        // add arrow
    }
}
