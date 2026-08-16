package io.github.moosyu.data.dialogue;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.quests.Quest;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

public record StartQuestDialogueEvent(Identifier questIdentifier) implements DialogueTriggeredEvent {
    @Override
    public void trigger(ServerPlayer player) {
        Quest quest = player.registryAccess().getOrThrow(ResourceKey.create(DataPackRegistryHandler.QUEST_REGISTRY_KEY, questIdentifier)).value();
        System.out.println("started quest!!");
        player.getData(UnshatteredAttachments.PLAYER_QUESTS).addQuest(quest);
        player.syncData(UnshatteredAttachments.PLAYER_QUESTS);
    }

    @Override
    public MapCodec<? extends DialogueTriggeredEvent> codec() {
        return CODEC;
    }

    public static final MapCodec<StartQuestDialogueEvent> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("quest_identifier").forGetter(StartQuestDialogueEvent::questIdentifier)
            ).apply(instance, StartQuestDialogueEvent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, StartQuestDialogueEvent> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, StartQuestDialogueEvent::questIdentifier,
            StartQuestDialogueEvent::new
    );
}
