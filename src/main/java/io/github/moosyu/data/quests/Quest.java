package io.github.moosyu.data.quests;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.dialogue.DialogueEventTypes;
import io.github.moosyu.data.dialogue.DialogueTriggeredEvent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Optional;

public record Quest(QuestTypes questType, Optional<DialogueTriggeredEvent> questCompleteEvent) {
    public Quest(QuestTypes questType) {
        this(questType, Optional.empty());
    }

    public static final Codec<Quest> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    QuestTypes.CODEC.fieldOf("quest_type").forGetter(Quest::questType),
                    Codec.lazyInitialized(() -> DialogueEventTypes.CODEC).optionalFieldOf("quest_complete_event").forGetter(Quest::questCompleteEvent)
            ).apply(instance, Quest::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Quest> STREAM_CODEC = StreamCodec.composite(
            QuestTypes.STREAM_CODEC, Quest::questType,
            ByteBufCodecs.optional(NeoForgeStreamCodecs.lazy(() -> DialogueEventTypes.STREAM_CODEC)), Quest::questCompleteEvent,
            Quest::new
    );
}
