package io.github.moosyu.data.regions;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jspecify.annotations.NonNull;

public enum RegionTemperatureTypes implements StringRepresentable {
    FREEZING("freezing", false, -0.1f),
    COLD("cold", false, -0.01f),
    COMFORTABLE("comfortable", true, 0.01f),
    WARM("warm", false, 0.01f),
    HOT("hot", false, 0.05f);

    private final String serialisedName;
    private final boolean safe;
    private final float temperatureChange;

    /**
     * set definitions for how hot regions can be
     * @param serialisedName required for StringRepresentable
     * @param safe whether the temperature with increase/decrease to a safe temperature or keep going to dangerous level
     * @param temperatureChange the base amount the temperature increases/decreases per increment. -ve for decrease
     */
    RegionTemperatureTypes(String serialisedName, boolean safe, float temperatureChange) {
        this.serialisedName = serialisedName;
        this.safe = safe;
        this.temperatureChange = temperatureChange;
    }

    public boolean isRegionSafe() {
        return safe;
    }
    public float getRegionTemperatureChange() {
        return temperatureChange;
    }

    public static final Codec<RegionTemperatureTypes> CODEC = StringRepresentable.fromEnum(RegionTemperatureTypes::values);
    public static final StreamCodec<FriendlyByteBuf, RegionTemperatureTypes> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(RegionTemperatureTypes.class);

    @Override
    public @NonNull String getSerializedName() {
        return serialisedName;
    }
}