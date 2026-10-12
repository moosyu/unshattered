package io.github.moosyu.compat.jei;

import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.VendorItem;
import io.github.moosyu.events.RecipeReceivedHandler;
import io.github.moosyu.util.UnshatteredUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class UnshatteredJEIPlugin implements IModPlugin {
    @Override
    public @NonNull Identifier getPluginUid() {
        return UnshatteredUtils.getUnshatteredIdentifier("unshattered_jei_plugin");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        runtime.getRecipeManager().hideRecipeCategory(RecipeTypes.SMITHING);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ForgeRecipeCategory.TYPE, RecipeReceivedHandler.FORGE_RECIPES);
        registration.addRecipes(VendorCategory.TYPE, buildVendorItems());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new ForgeRecipeCategory(guiHelper), new VendorCategory(guiHelper));
    }

    public List<VendorJEIItem> buildVendorItems() {
        List<VendorJEIItem> vendorItems = new ArrayList<>();
        ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();

        if (clientPacketListener != null) {
            for (Map.Entry<ResourceKey<EntityType<?>>, List<VendorItem>> vendorShopEntry : clientPacketListener.registryAccess()
                    .lookupOrThrow(Registries.ENTITY_TYPE)
                    .getDataMap(UnshatteredDataMaps.VENDOR_SHOP_DATA)
                    .entrySet()
            ) {
                EntityType<?> entityType = clientPacketListener.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getValue(vendorShopEntry.getKey());

                if (entityType == null) {
                    continue;
                }

                vendorShopEntry.getValue().forEach(currentItem -> {
                    vendorItems.add(new VendorJEIItem(entityType, currentItem));
                });
            }
        }

        System.out.println("DONE");
        System.out.println(vendorItems);

        return vendorItems;
    }

    public record VendorJEIItem(EntityType<?> entityType, VendorItem vendorItem) {}
}
