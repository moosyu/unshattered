package io.github.moosyu.events;

import io.github.moosyu.data.recipes.ForgeRecipe;
import io.github.moosyu.data.recipes.UnshatteredRecipes;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RecipeReceivedHandler {
    public static final List<RecipeHolder<ForgeRecipe>> FORGE_RECIPES = new ArrayList<>();

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        FORGE_RECIPES.clear();
        FORGE_RECIPES.addAll(event.getRecipeMap().byType(UnshatteredRecipes.FORGE_TYPE.get()));
    }
}
