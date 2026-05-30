package io.github.moosyu.networking.payloads;

import io.github.moosyu.events.AttackEntityHandler;
import io.github.moosyu.particles.DamageNumberParticle;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleDamageNumberEmission(final AttackEntityHandler.DamageNumberData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().particleEngine.add(new DamageNumberParticle(Minecraft.getInstance().level, data.targetPosX(), data.targetPosY(), data.targetPosZ(), String.valueOf(data.damage())));
            }
        });
    }
}
