package io.github.moosyu.events;

import io.github.moosyu.packets.*;
import io.github.moosyu.packets.handlers.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RegisterPayloadHandler {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ZombieSwordEffectsPacket.TYPE, ZombieSwordEffectsPacket.STREAM_CODEC, ZombieSwordEffectsPayloadHandler::handleData);
        registrar.playToClient(ExpSoundEffectPacket.TYPE, ExpSoundEffectPacket.STREAM_CODEC, ExpSoundEffectPayloadHandler::handleData);
        registrar.playToClient(DeathSoundEffectPacket.TYPE, DeathSoundEffectPacket.STREAM_CODEC, DeathSoundEffectPayloadHandler::handleData);
        registrar.playToClient(DamageNumberPacket.TYPE, DamageNumberPacket.STREAM_CODEC, DamageNumberHandler::handleData);
        registrar.playToServer(OpenProfilePayload.TYPE, OpenProfilePayload.STREAM_CODEC, OpenProfileHandler::handleData);
        registrar.playToClient(OpenDialoguePacket.TYPE, OpenDialoguePacket.STREAM_CODEC, OpenDialogueHandler::handleData);
        registrar.playToServer(ResetFlagQueuePacket.TYPE, ResetFlagQueuePacket.STREAM_CODEC, ResetFlagQueueHandler::handleData);
        registrar.playToServer(QueueNewFlagsPacket.TYPE, QueueNewFlagsPacket.STREAM_CODEC, QueueNewFlagsHandler::handleData);
        registrar.playToServer(TriggerEventPacket.TYPE, TriggerEventPacket.STREAM_CODEC, TriggerEventHandler::handleData);
    }
}
