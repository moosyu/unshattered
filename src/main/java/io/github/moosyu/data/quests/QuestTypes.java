package io.github.moosyu.data.quests;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jspecify.annotations.NonNull;

public enum QuestTypes implements StringRepresentable {
    NOVICE("novice"),
    INTERMEDIATE("intermediate"),
    EXPERIENCED("experienced"),
    MASTER("master"),
    GRANDMASTER("grandmaster");

    private final String serialisedName;

    QuestTypes(String serialisedName) {
        this.serialisedName = serialisedName;
    }

    public static final Codec<QuestTypes> CODEC = StringRepresentable.fromEnum(QuestTypes::values);
    public static final StreamCodec<FriendlyByteBuf, QuestTypes> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(QuestTypes.class);

    @Override
    public @NonNull String getSerializedName() {
        return serialisedName;
    }
}
