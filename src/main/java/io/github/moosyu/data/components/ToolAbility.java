package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ToolAbility(String abilityId, int manaCost, int cooldown, int duration, boolean passive) {
    public static final Codec<ToolAbility> CODEC = RecordCodecBuilder.create((RecordCodecBuilder.Instance<ToolAbility> instance) -> instance.group(
            Codec.STRING.fieldOf("ability_id").forGetter(ToolAbility::abilityId),
            Codec.INT.optionalFieldOf("mana_cost", 0).forGetter(ToolAbility::manaCost),
            Codec.INT.optionalFieldOf("cooldown", 0).forGetter(ToolAbility::cooldown),
            Codec.INT.optionalFieldOf("duration", 0).forGetter(ToolAbility::duration),
            Codec.BOOL.fieldOf("passive").forGetter(ToolAbility::passive)
    ).apply(instance, ToolAbility::new)).validate(ability -> {
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