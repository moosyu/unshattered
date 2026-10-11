package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.items.ItemRange;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

/**
 * record for normal items dropped by mobs
 * @param dropData stores item, it's range (min and max amounts to be randomly picked) and drop chance
 * @param combatFortuneBoosted whether the drop chance is boosted by combat fortune
 */
public record MobItemDropData(DropData dropData, boolean combatFortuneBoosted) {
    public MobItemDropData(ItemLike item, boolean combatFortuneBoosted) {
        this(new DropData(new ItemRange(item.asItem())), combatFortuneBoosted);
    }

    public MobItemDropData(ItemLike item, float dropChance, boolean combatFortuneBoosted) {
        this(new DropData(new ItemRange(item.asItem()), dropChance), combatFortuneBoosted);
    }

    public MobItemDropData(ItemLike item, int minAmount, int maxAmount, float dropChance, boolean combatFortuneBoosted) {
        this(new DropData(new ItemRange(item.asItem(), minAmount, maxAmount), dropChance), combatFortuneBoosted);
    }

    public MobItemDropData(ItemLike item, int amount, float dropChance, boolean combatFortuneBoosted) {
        this(new DropData(new ItemRange(item.asItem(), amount), dropChance), combatFortuneBoosted);
    }

    public static final Codec<MobItemDropData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DropData.CODEC.fieldOf("drop_data").forGetter(MobItemDropData::dropData),
            Codec.BOOL.fieldOf("combat_fortune_boosted").forGetter(MobItemDropData::combatFortuneBoosted)
    ).apply(instance, MobItemDropData::new));
}
