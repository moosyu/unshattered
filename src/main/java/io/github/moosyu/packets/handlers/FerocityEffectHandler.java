package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.FerocityEffectPacket;
import io.github.moosyu.sounds.UnshatteredSounds;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class FerocityEffectHandler {
    public static void handleData(final FerocityEffectPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();

            if (data.playSound()) {
                UnshatteredUtils.playClientsideSound(context.player(), UnshatteredSounds.FEROCITY_TRIGGER_SOUND.value(), SoundSource.PLAYERS, 0.6f);
            }

            Minecraft minecraft = Minecraft.getInstance();
            Entity entity = level.getEntity(data.entityIdentifier());
            if (entity == null) return;

            minecraft.particleEngine.createTrackingEmitter(entity, ParticleTypes.RAID_OMEN);
        });
    }
}
