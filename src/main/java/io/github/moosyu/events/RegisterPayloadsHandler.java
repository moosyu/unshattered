package io.github.moosyu.events;

import io.github.moosyu.packets.*;
import io.github.moosyu.packets.handlers.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RegisterPayloadsHandler {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ZombieSwordEffectsPacket.TYPE, ZombieSwordEffectsPacket.STREAM_CODEC);
        registrar.playToClient(ExpSoundEffectPacket.TYPE, ExpSoundEffectPacket.STREAM_CODEC);
        registrar.playToClient(DeathSoundEffectPacket.TYPE, DeathSoundEffectPacket.STREAM_CODEC);
        registrar.playToClient(DamageNumberPacket.TYPE, DamageNumberPacket.STREAM_CODEC);
        registrar.playToClient(OpenDialoguePacket.TYPE, OpenDialoguePacket.STREAM_CODEC);
        registrar.playToClient(FerocityEffectPacket.TYPE, FerocityEffectPacket.STREAM_CODEC);
        registrar.playToClient(BlockBreakSyncPacket.TYPE, BlockBreakSyncPacket.STREAM_CODEC);
        registrar.playToClient(WeakHitSoundEffectPacket.TYPE, WeakHitSoundEffectPacket.STREAM_CODEC);
        registrar.playToServer(OpenProfilePayload.TYPE, OpenProfilePayload.STREAM_CODEC, OpenProfileHandler::handleData);
        registrar.playToServer(ResetFlagQueuePacket.TYPE, ResetFlagQueuePacket.STREAM_CODEC, ResetFlagQueueHandler::handleData);
        registrar.playToServer(QueueNewFlagsPacket.TYPE, QueueNewFlagsPacket.STREAM_CODEC, QueueNewFlagsHandler::handleData);
        registrar.playToServer(TriggerEventPacket.TYPE, TriggerEventPacket.STREAM_CODEC, TriggerEventHandler::handleData);
        registrar.playToServer(UpdateDialogueStatePacket.TYPE, UpdateDialogueStatePacket.STREAM_CODEC, UpdateDialogueStateHandler::handleData);
    }
}
