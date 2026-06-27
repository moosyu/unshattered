package io.github.moosyu.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.Level;

public enum TimeConditions {
    ANY,
    DAY,
    NIGHT;

    public boolean isTime(Level level) {
        return switch (this) {
            case ANY -> true;
            case DAY -> level.isBrightOutside();
            case NIGHT -> level.isDarkOutside();
        };
    }
}
