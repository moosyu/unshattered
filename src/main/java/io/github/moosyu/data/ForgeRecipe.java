package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

public record ForgeRecipe(List<SizedIngredient> ingredients, ItemStack result) {
    public static Codec<ForgeRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    SizedIngredient.NESTED_CODEC.listOf().fieldOf("ingredients").forGetter(ForgeRecipe::ingredients),
                    ItemStack.CODEC.fieldOf("result").forGetter(ForgeRecipe::result)
            ).apply(instance, ForgeRecipe::new)
    );
}
