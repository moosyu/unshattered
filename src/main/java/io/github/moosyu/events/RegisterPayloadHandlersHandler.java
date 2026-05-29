package io.github.moosyu.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static io.github.moosyu.NNO.MODID;
import static io.github.moosyu.networking.payloads.ClientPayloadHandler.handleDamageNumberEmission;

// great name by the way
public class RegisterPayloadHandlersHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(
                    AttackEntityHandler.DamageNumberData.TYPE,
                    AttackEntityHandler.DamageNumberData.STREAM_CODEC,
                    (payload, context) -> context.enqueueWork(() -> handleDamageNumberEmission(payload, context))
            );
        }
    }
}
