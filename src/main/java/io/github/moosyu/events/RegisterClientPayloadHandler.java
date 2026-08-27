package io.github.moosyu.events;

import io.github.moosyu.packets.*;
import io.github.moosyu.packets.handlers.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RegisterClientPayloadHandler {
    @SubscribeEvent
    public static void registerClientPayloads(RegisterClientPayloadHandlersEvent event) {
        event.register(ZombieSwordEffectsPacket.TYPE, ZombieSwordEffectsPayloadHandler::handleData);
        event.register(ExpSoundEffectPacket.TYPE, ExpSoundEffectPayloadHandler::handleData);
        event.register(DeathSoundEffectPacket.TYPE, DeathSoundEffectPayloadHandler::handleData);
        event.register(DamageNumberPacket.TYPE, DamageNumberHandler::handleData);
        event.register(OpenDialoguePacket.TYPE, OpenDialogueHandler::handleData);
        event.register(FerocityEffectPacket.TYPE, FerocityEffectHandler::handleData);
    }
}
