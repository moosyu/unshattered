package io.github.moosyu.events;

import io.github.moosyu.packets.DeathSoundEffectPacket;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.packets.ZombieSwordEffectsPacket;
import io.github.moosyu.packets.handlers.DeathSoundEffectPayloadHandler;
import io.github.moosyu.packets.handlers.ExpSoundEffectPayloadHandler;
import io.github.moosyu.packets.handlers.ZombieSwordEffectsPayloadHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RegisterPayloadHandler {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ZombieSwordEffectsPacket.TYPE, ZombieSwordEffectsPacket.STREAM_CODEC, ZombieSwordEffectsPayloadHandler::handleData);
        registrar.playToClient(ExpSoundEffectPacket.TYPE, ExpSoundEffectPacket.STREAM_CODEC, ExpSoundEffectPayloadHandler::handleData);
        registrar.playToClient(DeathSoundEffectPacket.TYPE, DeathSoundEffectPacket.STREAM_CODEC, DeathSoundEffectPayloadHandler::handleData);
    }
}
