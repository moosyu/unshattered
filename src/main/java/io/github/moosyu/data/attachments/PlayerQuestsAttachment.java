package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import io.github.moosyu.data.quests.Quest;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;

public class PlayerQuestsAttachment {
    private final HashMap<Quest, Boolean> quests = new HashMap<>();

    /**
     * add a new quest to a players active quests, run updateQuestCompletion if it needs to be set as completed off rip (for some reason)
     * @param quest quest to be added
     */
    public void addQuest(Quest quest) {
        quests.put(quest, false);
    }

    /**
     * inverts the quest's completion state
     * @param quest the quest to be changed
     */
    public void updateQuestCompletion(Quest quest) {
        quests.replace(quest, !quests.get(quest));
    }

    /**
     * get whether a quest is completed
     * @param quest the quest to be checked
     * @return quest completion state or null if the player doesn't have the quest
     */
    public boolean getQuestState(Quest quest) {
        return quests.get(quest);
    }

    public static final Codec<PlayerQuestsAttachment> CODEC =
            Codec.unboundedMap(Quest.CODEC, Codec.BOOL).xmap(map -> {
                PlayerQuestsAttachment attachment = new PlayerQuestsAttachment();
                attachment.quests.putAll(map);
                return attachment;
                }, attachment -> attachment.quests
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerQuestsAttachment> STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new,
                    Quest.STREAM_CODEC,
                    ByteBufCodecs.BOOL
            ).map(map -> {
                PlayerQuestsAttachment attachment = new PlayerQuestsAttachment();
                attachment.quests.putAll(map);
                return attachment;
                }, attachment -> attachment.quests
            );
}
