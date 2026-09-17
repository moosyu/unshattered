package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public final class PlayerFlagsAttachment {
    private final Set<Identifier> flags = new HashSet<>();
    private final Set<Identifier> flagQueue = new HashSet<>();

    public boolean hasFlag(Identifier flag) {
        return flags.contains(flag);
    }

    public boolean hasAllFlags(Collection<Identifier> required) {
        return flags.containsAll(required);
    }

    public Set<Identifier> getFlags() {
        return flags;
    }

    public Set<Identifier> getQueuedFlags() {
        return flagQueue;
    }

    /**
     * adds a single flag instantly (no queue), generally should be used for non-dialogue tracking
     * @param flag flag being added
     */
    public void addFlag(Identifier flag) {
        flags.add(flag);
    }

    public void addFlags(Collection<Identifier> allFlags) {
        flags.addAll(allFlags);
    }

    /**
     * add set of flags to queue set
     * @param allFlags all flags to be added to the queue
     */
    public void addFlagsToQueue(Collection<Identifier> allFlags) {
        flagQueue.addAll(allFlags);
    }

    /**
     * add queued flags to the main flags set and clear them
     */
    public void addQueuedFlags() {
        flags.addAll(flagQueue);
        clearFlagQueue();
    }

    /**
     * clear the flag queue without adding them to the main flags set
     */
    public void clearFlagQueue() {
        flagQueue.clear();
    }

    public void removeFlag(Identifier flag) {
        flags.remove(flag);
    }

    public void resetFlags(Player player) {
        player.setData(UnshatteredAttachments.PLAYER_FLAGS, new PlayerFlagsAttachment());
    }

    public static final Codec<PlayerFlagsAttachment> CODEC = Identifier.CODEC.listOf().xmap(list -> {
        PlayerFlagsAttachment data = new PlayerFlagsAttachment();
        data.addFlags(list);
        return data;
    },
    data -> new ArrayList<>(data.flags));

    public static final StreamCodec<ByteBuf, PlayerFlagsAttachment> STREAM_CODEC = Identifier.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new))
            .map(set -> {
                PlayerFlagsAttachment data = new PlayerFlagsAttachment();
                data.addFlags(set);
                return data;
                },
                    attachment -> new HashSet<>(attachment.flags)
            );
}
