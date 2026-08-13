package io.github.moosyu.attachments;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public final class PlayerDialogueFlagsAttachment {
    private final Set<Identifier> flags = new HashSet<>();

    public boolean hasFlag(Identifier flag) {
        return flags.contains(flag);
    }

    public boolean hasAllFlags(Collection<Identifier> required) {
        return flags.containsAll(required);
    }

    public Set<Identifier> getFlags() {
        return flags;
    }

    public void addFlag(Identifier flag) {
        flags.add(flag);
    }

    public void addFlags(Collection<Identifier> allFlags) {
        flags.addAll(allFlags);
    }

    public void removeFlag(Identifier flag) {
        flags.remove(flag);
    }

    public void resetFlags(Player player) {
        player.setData(UnshatteredAttachments.PLAYER_DIALOGUE_FLAGS, new PlayerDialogueFlagsAttachment());
    }

    public static final Codec<PlayerDialogueFlagsAttachment> CODEC = Identifier.CODEC.listOf().xmap(list -> {
        PlayerDialogueFlagsAttachment data = new PlayerDialogueFlagsAttachment();
        data.addFlags(list);
        return data;
    },
    data -> new ArrayList<>(data.flags));

    public static final StreamCodec<ByteBuf, PlayerDialogueFlagsAttachment> STREAM_CODEC = Identifier.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new))
            .map(set -> {
                PlayerDialogueFlagsAttachment data = new PlayerDialogueFlagsAttachment();
                data.addFlags(set);
                return data;
                },
                    attachment -> new HashSet<>(attachment.flags)
            );
}
