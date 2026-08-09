package io.github.moosyu.data.regions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

/**
 * a world region's data. doesn't contain world position bounds.
 * @param colour the colour for the region's name to be displayed as
 * @param temperatureType whether the region is hot, cold and whether negative effects actually trigger or the temperature just decreases slightly
 * @param harvestable whether the player can harvest materials in this area
 */
public record Region(int colour, RegionTemperatureTypes temperatureType, boolean harvestable) {
    // region to fall back on if something goes terribly wrong like if the player runs out of bounds
    public static final Codec<Region> RECORD_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("colour").forGetter(Region::colour),
                    RegionTemperatureTypes.CODEC.fieldOf("temperature_type").forGetter(Region::temperatureType),
                    Codec.BOOL.fieldOf("harvestable").forGetter(Region::harvestable)
            ).apply(instance, Region::new)
    );

    // starting to feel like im getting the hang of this codec business
    public static final StreamCodec<FriendlyByteBuf, Region> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Region::colour,
            RegionTemperatureTypes.STREAM_CODEC, Region::temperatureType,
            ByteBufCodecs.BOOL, Region::harvestable,
            Region::new
    );

    public static Region getRegion(ResourceKey<Region> regionResourceKey, Player player) {
        Holder<Region> holder = player.level().registryAccess().lookupOrThrow(DataPackRegistryHandler.REGION_REGISTRY_KEY).getOrThrow(regionResourceKey);
        return holder.value();
    }

    public static String getRegionTranslationKey(ResourceKey<Region> regionResourceKey) {
        return "region.name.unshattered." + regionResourceKey.identifier().getPath();
    }
}
