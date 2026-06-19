package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.ZombieSwordEffectsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Random;

public class ZombieSwordEffectsPayloadHandler {
    public static void handleData(final ZombieSwordEffectsPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Vec3 look = player.getLookAngle().normalize();
            Random rand = new Random();
            for (int i = 0; i < 6; i++) {
                double sideOffset = (rand.nextDouble() - 0.5) * 0.5;
                Vec3 particlePos = player.getEyePosition()
                        .add(look.scale(0.75f))
                        .add(look.cross(new Vec3(0, 1, 0)).normalize().scale(sideOffset))
                        .add(0, -0.6, 0);
                Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.HEART, particlePos.x, particlePos.y, particlePos.z, 0.0d, 0.02d, 0.0d);
                player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 0.1f, 1.0f, false);
            }
        });
    }
}
