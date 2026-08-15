package io.github.moosyu.data.quests;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record Quest(QuestTypes questType) {
    public static final Codec<Quest> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    QuestTypes.CODEC.fieldOf("quest_type").forGetter(Quest::questType)
            ).apply(instance, Quest::new)
    );

    public static final StreamCodec<FriendlyByteBuf, Quest> STREAM_CODEC = StreamCodec.composite(
            QuestTypes.STREAM_CODEC, Quest::questType,
            Quest::new
    );
}
