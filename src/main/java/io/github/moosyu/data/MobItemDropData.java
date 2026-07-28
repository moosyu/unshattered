package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * record for normal items dropped by mobs
 * @param item the item reward
 * @param baseDropChance drop chance from 0.0 -> 1.0
 * @param combatFortuneBoosted whether the drop chance is boosted by combat fortune
 * @param minItemAmount the minimum amount of the item able to be dropped on the condition that it drops
 * @param maxItemAmount the maximum amount of the item able to be dropped on the condition that it drops
 */
public record MobItemDropData(Item item, float baseDropChance, boolean combatFortuneBoosted, int minItemAmount, int maxItemAmount) {
    public MobItemDropData(Item item, float baseDropChance, boolean combatFortuneBoosted, int itemAmount) {
        this(item, baseDropChance, combatFortuneBoosted, itemAmount, itemAmount);
    }

    public static final Codec<MobItemDropData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(MobItemDropData::item),
            Codec.FLOAT.fieldOf("base_drop_chance").forGetter(MobItemDropData::baseDropChance),
            Codec.BOOL.fieldOf("combat_fortune_boosted").forGetter(MobItemDropData::combatFortuneBoosted),
            Codec.INT.fieldOf("min").forGetter(MobItemDropData::minItemAmount),
            Codec.INT.fieldOf("max").forGetter(MobItemDropData::maxItemAmount)
    ).apply(instance, MobItemDropData::new));
}
