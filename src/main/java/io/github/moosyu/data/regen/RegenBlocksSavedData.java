package io.github.moosyu.data.regen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import static io.github.moosyu.Unshattered.MODID;

public class RegenBlocksSavedData extends SavedData {
    public static final SavedDataType<RegenBlocksSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(MODID, "regen_blocks_saved_data"),
            RegenBlocksSavedData::new,
            RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.fieldOf("regen_path_identifier").forGetter(sd -> sd.regenPathIdentifier),
                    Codec.INT.fieldOf("regen_position").forGetter(sd -> sd.regenIndex)
            ).apply(instance, RegenBlocksSavedData::new))
    );

    private final Identifier regenPathIdentifier;
    private final int regenIndex;

    public RegenBlocksSavedData() {
        this(Identifier.fromNamespaceAndPath(MODID, "empty"), 0);
    }

    public RegenBlocksSavedData(Identifier regenPathIdentifier, int regenIndex) {
        this.regenPathIdentifier = regenPathIdentifier;
        this.regenIndex = regenIndex;
    }
}