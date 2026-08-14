package io.github.moosyu.packets;

import io.github.moosyu.Unshattered;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

/**
 * @param addToPlayerFlags whether the reset is being triggered by a dialogue being completed normally or exited early. queues arent serialised so if the player leaves they'll reset by themselves.
 */
public record ResetFlagQueuePacket(boolean addToPlayerFlags) implements CustomPacketPayload {
    public static final Type<ResetFlagQueuePacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unshattered.MODID, "reset_flag_queue"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetFlagQueuePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ResetFlagQueuePacket::addToPlayerFlags,
            ResetFlagQueuePacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
