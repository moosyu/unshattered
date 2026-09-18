package io.github.moosyu.events;

import io.github.moosyu.data.regen.RegenClientCache;
import io.github.moosyu.gui.screens.DialogueScreen;
import io.github.moosyu.packets.*;
import io.github.moosyu.sounds.UnshatteredSounds;
import io.github.moosyu.util.UnshatteredUtils;
import io.github.moosyu.util.damage.DamageNumber;
import io.github.moosyu.util.damage.DamageNumberManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

import java.util.Random;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RegisterClientPayloadHandler {
    @SubscribeEvent
    public static void registerClientPayloads(RegisterClientPayloadHandlersEvent event) {
        event.register(ZombieSwordEffectsPacket.TYPE,
                (_, context) -> context.enqueueWork(() -> {
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
                        UnshatteredUtils.playClientsideSound(player, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 0.1f, 1.0f);
                    }
                })
        );

        event.register(ExpSoundEffectPacket.TYPE,
                (_, context) -> context.enqueueWork(() -> {
                    Player player = context.player();
                    UnshatteredUtils.playClientsideSound(player, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5f, 2.0f - player.getRandom().nextFloat() * 0.3f);
                })
        );

        event.register(DeathSoundEffectPacket.TYPE,
                (_, context) -> context.enqueueWork(() -> {
                    Player player = context.player();
                    UnshatteredUtils.playClientsideSound(player, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0f, 2.0f);
                })
        );

        event.register(DamageNumberPacket.TYPE,
                (data, context) -> context.enqueueWork(() -> {
                    Player player = context.player();

                    final double TARGET_Y = data.getY() + 1.2d;
                    final double OFFSET_DISTANCE = 0.8;
                    double dirX = player.getX() - data.getX();
                    double dirY = player.getEyeY() - TARGET_Y;
                    double dirZ = player.getZ() - data.getZ();
                    final double DISTANCE = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);

                    if (DISTANCE > 0.001) {
                        dirX /= DISTANCE;
                        dirY /= DISTANCE;
                        dirZ /= DISTANCE;
                    }

                    DamageNumberManager.add(new DamageNumber(new Vec3(data.getX() + (dirX * OFFSET_DISTANCE), TARGET_Y + (dirY * OFFSET_DISTANCE), data.getZ() + (dirZ * OFFSET_DISTANCE)), Component.literal(String.valueOf(data.number()))));
                })
        );

        event.register(OpenDialoguePacket.TYPE,
                (data, context) -> context.enqueueWork(() ->
                        Minecraft.getInstance().setScreen(new DialogueScreen(data.talkableName(), data.selectedDialogueNode(), context.player()))
                )
        );

        event.register(FerocityEffectPacket.TYPE,
                (data, context) -> context.enqueueWork(() -> {
                    Level level = context.player().level();

                    if (data.playSound()) {
                        UnshatteredUtils.playClientsideSound(context.player(), UnshatteredSounds.FEROCITY_TRIGGER_SOUND.value(), SoundSource.PLAYERS, 0.6f);
                    }

                    Minecraft minecraft = Minecraft.getInstance();
                    Entity entity = level.getEntity(data.entityIdentifier());
                    if (entity == null) return;

                    minecraft.particleEngine.createTrackingEmitter(entity, ParticleTypes.RAID_OMEN);
                })
        );

        event.register(BlockBreakSyncPacket.TYPE,
                (data, context) -> context.enqueueWork(() -> {
                    if (data.regenPathIndex() == 0) {
                        RegenClientCache.remove(data.pos());
                    } else {
                        RegenClientCache.put(data.pos(), data.regenPathId(), data.regenPathIndex());
                    }
                })
        );

        event.register(WeakHitSoundEffectPacket.TYPE,
                (_, context) -> context.enqueueWork(() ->
                        UnshatteredUtils.playClientsideSound(context.player(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0f)
                )
        );

        event.register(ClientsidePlayerSoundEffectPacket.TYPE, (data, context) -> context.enqueueWork(() ->
                UnshatteredUtils.playClientsideSound(context.player(), data.soundEvent().value(), SoundSource.PLAYERS, data.volume())));
    }
}
