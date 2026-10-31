package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * an item ability, used really only for the corresponding data component which is then used for displaying a correct tooltip
 *
 * @param manaCost mana cost to use the ability (usually 0 if it's passive)
 * @param cooldown ability cooldown
 * @param duration duration of ability
 * @param passive  whether the ability is passive
 */
public record ItemAbility(int manaCost, int cooldown, int duration, boolean passive) {
    /**
     * create passive item ability
     */
    public ItemAbility() {
        this(0, 0, 0, true);
    }

    /**
     * create a non-passive item ability
     */
    public ItemAbility(int manaCost, int cooldown, int duration) {
        this(manaCost, cooldown, duration, false);
    }

    public static final Codec<ItemAbility> CODEC = RecordCodecBuilder.create((RecordCodecBuilder.Instance<ItemAbility> instance) -> instance.group(
            Codec.INT.optionalFieldOf("mana_cost", 0).forGetter(ItemAbility::manaCost),
            Codec.INT.optionalFieldOf("cooldown", 0).forGetter(ItemAbility::cooldown),
            Codec.INT.optionalFieldOf("duration", 0).forGetter(ItemAbility::duration),
            Codec.BOOL.fieldOf("passive").forGetter(ItemAbility::passive)
    ).apply(instance, ItemAbility::new)).validate(ability -> {
        if (ability.passive() && ability.manaCost() > 0) {
            return DataResult.error(() -> "Passive abilities cannot have a mana cost");
        }
        if (ability.passive() && ability.cooldown() > 0) {
            return DataResult.error(() -> "Passive abilities cannot have a cooldown");
        }
        if (ability.passive() && ability.duration() > 0) {
            return DataResult.error(() -> "Passive abilities cannot have a duration");
        }
        return DataResult.success(ability);
    });
}