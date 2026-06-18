package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.DamageNumberPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DamageNumberHandler {
    public static void handleData(final DamageNumberPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            System.out.println("damage dealt: " + data.number());
        });
    }
}
