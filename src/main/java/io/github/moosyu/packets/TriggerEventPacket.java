package io.github.moosyu.packets;

import io.github.moosyu.Unshattered;
import io.github.moosyu.data.dialogue.DialogueEventTypes;
import io.github.moosyu.data.dialogue.DialogueTriggeredEvent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record TriggerEventPacket(DialogueTriggeredEvent dialogueTriggeredEvent) implements CustomPacketPayload {
    public static final Type<TriggerEventPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unshattered.MODID, "trigger_event"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TriggerEventPacket> STREAM_CODEC = StreamCodec.composite(
            DialogueEventTypes.STREAM_CODEC, TriggerEventPacket::dialogueTriggeredEvent,
            TriggerEventPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
