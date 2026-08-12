package io.github.moosyu.attachments;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public final class PlayerDialogueFlagsAttachment {
    private final Set<String> flags = new HashSet<>();

    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }

    public boolean hasAllFlags(Collection<String> required) {
        return flags.containsAll(required);
    }

    public void addFlag(String flag) {
        flags.add(flag);
    }

    public void addFlags(Collection<String> allFlags) {
        flags.addAll(allFlags);
    }


    public void removeFlag(String flag) {
        flags.remove(flag);
    }

    public static final Codec<PlayerDialogueFlagsAttachment> CODEC = Codec.STRING.listOf().xmap(list -> {
        PlayerDialogueFlagsAttachment data = new PlayerDialogueFlagsAttachment();
        data.addFlags(list);
        return data;
    },
    data -> new ArrayList<>(data.flags));

    public static final StreamCodec<ByteBuf, PlayerDialogueFlagsAttachment> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new))
            .map(set -> {
                PlayerDialogueFlagsAttachment data = new PlayerDialogueFlagsAttachment();
                data.addFlags(set);
                return data;
                },
                    attachment -> new HashSet<>(attachment.flags)
            );
}
