package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.quests.Quest;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PlayerQuestsAttachment {
    // tracks whether a quest has been completed by a player
    private final HashMap<Quest, Boolean> quests = new HashMap<>();

    /**
     * add a new quest to a players active quests, run updateQuestCompletion if it needs to be set as completed off rip (for some reason)
     * @param quest quest to be added
     */
    public void addQuest(Quest quest, ServerPlayer player, Identifier questIdentifier) {
        quests.put(quest, false);

        player.connection.send(new ClientboundSetTitlesAnimationPacket(20, 80, 20));
        player.connection.send(new ClientboundSetSubtitleTextPacket(Component.translatable("quest.name.unshattered." + questIdentifier.getPath())));
        player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("quest.title.unshattered.quest_started")));
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

    public HashMap<Quest, Boolean> getQuests() {
        return quests;
    }

    private static final Codec<Map.Entry<Quest, Boolean>> ENTRY_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Quest.CODEC.fieldOf("quest").forGetter(Map.Entry::getKey),
                    Codec.BOOL.fieldOf("completed").forGetter(Map.Entry::getValue)
            ).apply(instance, Map::entry)
    );

    public static final Codec<PlayerQuestsAttachment> CODEC = ENTRY_CODEC.listOf().xmap(list -> {
        PlayerQuestsAttachment attachment = new PlayerQuestsAttachment();
        list.forEach(entry -> attachment.quests.put(entry.getKey(), entry.getValue()));
        return attachment;
        }, attachment -> new ArrayList<>(attachment.quests.entrySet())
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
