package io.github.moosyu.compat.jei;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.moosyu.data.NpcData;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.VendorItem;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class VendorCategory implements IRecipeCategory<UnshatteredJEIPlugin.VendorJEIItem> {
    public static final IRecipeType<UnshatteredJEIPlugin.VendorJEIItem> TYPE = IRecipeType.create(UnshatteredUtils.getUnshatteredIdentifier("vendor"), UnshatteredJEIPlugin.VendorJEIItem.class );
    private final int WIDTH = 140;
    private final int HEIGHT = 60;
    private final int SLOT_SIZE = 18;
    private final int Y_OFFSET = 5;
    private static final int COLUMNS = 3;

    private final IDrawable icon;
    private final IDrawable arrow;

    public VendorCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(Items.PLAYER_HEAD));
        arrow = guiHelper.getRecipeArrow();
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
        EntityType<?> entityType = vendorJEIItem.entityType();
        NpcData npcData = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType).getData(UnshatteredDataMaps.ENTITY_NPC_DATA);

        if (npcData != null) {
            ItemStack headItemStack = new ItemStack(Items.PLAYER_HEAD);
            JsonObject json = new JsonObject();
            json.addProperty("texture", npcData.skinLocation().toString());
            headItemStack.set(DataComponents.PROFILE, ResolvableProfile.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow());

            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, (WIDTH / 2) - (SLOT_SIZE / 2), 0)
                    .setStandardSlotBackground()
                    .add(headItemStack)
                    .addRichTooltipCallback((_, tooltip) -> {
                        tooltip.clear();
                        tooltip.add(Component.translatable(entityType.getDescriptionId()).withColor(npcData.npcColour()));
                    });
        }

        VendorItem vendorItem = vendorJEIItem.vendorItem();
        Optional<Integer> priceOptional = vendorItem.price();
        Optional<List<ItemStack>> requirementsOptional = vendorItem.itemTradeRequirements();

        if (priceOptional.isPresent() && requirementsOptional.isEmpty()) {
            createCoinsSlot(builder, SLOT_SIZE + 7, (HEIGHT / 2) - (SLOT_SIZE / 2) + Y_OFFSET, priceOptional.get());
        } else if (priceOptional.isEmpty() && requirementsOptional.isPresent()) {
            List<ItemStack> itemTradeRequirements = requirementsOptional.get();
            for (int i = 0, size = itemTradeRequirements.size(); i < size; i++) {
                builder.addInputSlot(1 + (i % COLUMNS) * SLOT_SIZE, 1 + (i / COLUMNS) * SLOT_SIZE)
                        .setStandardSlotBackground()
                        .add(itemTradeRequirements.get(i));
            }
        } else if (requirementsOptional.isPresent()) {
            List<ItemStack> itemTradeRequirements = requirementsOptional.get();

            createCoinsSlot(builder, SLOT_SIZE, SLOT_SIZE, priceOptional.get());
            for (int i = 1, size = itemTradeRequirements.size(); i <= size; i++) {
                builder.addInputSlot(1 + (i % COLUMNS) * SLOT_SIZE, 1 + (i / COLUMNS) * SLOT_SIZE)
                        .setStandardSlotBackground()
                        .add(itemTradeRequirements.get(i - 1));
            }
        }

        builder.addOutputSlot(WIDTH - (SLOT_SIZE * 2) - 5, (HEIGHT / 2) - (SLOT_SIZE / 2) + Y_OFFSET)
                .setStandardSlotBackground()
                .add(new ItemStack(vendorItem.item()));
    }

    @Override
    public void draw(UnshatteredJEIPlugin.@NonNull VendorJEIItem vendorJEIItem, @NonNull IRecipeSlotsView recipeSlotsView, @NonNull GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, (WIDTH / 2) - 11, (HEIGHT / 2) - (SLOT_SIZE / 2) + Y_OFFSET);
    }

    private static void createCoinsSlot(IRecipeLayoutBuilder builder, int x, int y, int price) {
        builder.addInputSlot(x, y)
                .add(Items.GOLD_NUGGET)
                .setStandardSlotBackground().addRichTooltipCallback((_, tooltip) -> {
                    tooltip.clear();
                    tooltip.add(Component.literal(String.format("%,d", price) + " ").append(Component.translatable("misc.unshattered.coins")).withColor(UnshatteredUtils.GOLD));
                });
    }
}