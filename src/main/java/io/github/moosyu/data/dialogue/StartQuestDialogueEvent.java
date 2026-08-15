package io.github.moosyu.data.dialogue;

import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.data.quests.Quest;
import net.minecraft.server.level.ServerPlayer;

public record StartQuestDialogueEvent(Quest quest) implements DialogueTriggeredEvent {
    @Override
    public void trigger(ServerPlayer player) {
        player.getData(UnshatteredAttachments.PLAYER_QUESTS).addQuest(quest);
        player.syncData(UnshatteredAttachments.PLAYER_QUESTS);
    }
}
