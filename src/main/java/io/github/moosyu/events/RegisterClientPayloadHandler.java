package io.github.moosyu.events;

import io.github.moosyu.data.regen.RegenClientCache;
import io.github.moosyu.gui.screens.DialogueScreen;
import io.github.moosyu.packets.*;
import io.github.moosyu.sounds.UnshatteredSounds;
import io.github.moosyu.util.UnshatteredUtils;
import io.github.moosyu.damage.DamageNumber;
import io.github.moosyu.damage.DamageNumberManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SpellParticleOption;
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

                    final double TARGET_Y = data.pos().y + 1.2d;
                    final double OFFSET_DISTANCE = 0.8;
                    double dirX = player.getX() - data.pos().x;
                    double dirY = player.getEyeY() - TARGET_Y;
                    double dirZ = player.getZ() - data.pos().z;
                    final double DISTANCE = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);

                    if (DISTANCE > 0.001) {
                        dirX /= DISTANCE;
                        dirY /= DISTANCE;
                        dirZ /= DISTANCE;
                    }

                    DamageNumberManager.NUMBERS.add(new DamageNumber(new Vec3(data.pos().x + (dirX * OFFSET_DISTANCE),
                            TARGET_Y + (dirY * OFFSET_DISTANCE),
                            data.pos().z + (dirZ * OFFSET_DISTANCE)),
                            data.number(),
                            data.damageType())
                    );
                })
        );

        event.register(OpenDialoguePacket.TYPE,
                (data, context) -> context.enqueueWork(() ->
                        Minecraft.getInstance().setScreen(new DialogueScreen(data.talkableName(), data.selectedDialogueNode(), context.player()))
                )
        );

        event.register(FerocityEffectPacket.TYPE,
                (data, context) -> context.enqueueWork(() -> {
                    Player player = context.player();
                    Level level = player.level();

                    if (data.playSound()) {
                        UnshatteredUtils.playClientsideSound(player, UnshatteredSounds.FEROCITY_TRIGGER_SOUND.value(), SoundSource.PLAYERS, 0.6f);
                    }

                    Entity entity = level.getEntity(data.entityIdentifier());
                    if (entity == null) {
                        return;
                    }

                    ParticleOptions slashParticle = SpellParticleOption.create(ParticleTypes.INSTANT_EFFECT, 0xFFF82423, 1.0f);


                    int particleCount = 24;
                    // Entity#position is the foot
                    Vec3 center = entity.position().add(0, entity.getBbHeight() * 0.5, 0);

                    // plane perpendicular to the player in the direction of the entity
                    Vec3 toEntity = center.subtract(player.getEyePosition());
                    Vec3 flat = new Vec3(toEntity.x, 0, toEntity.z);
                    // if flat.lengthSqr() < 1.0E-6 it's basically 0 length so normalising would cause a crash
                    // (supposedly, ive never seen this behaviour, but it seems to be best practice)
                    flat = flat.lengthSqr() < 1.0E-6 ? player.getLookAngle().multiply(1, 0, 1) : flat;
                    flat = flat.normalize();

                    Vec3 right = new Vec3(-flat.z, 0, flat.x);
                    Vec3 up = new Vec3(0, 1, 0);

                    // +45 / -45 from horizontal so diagonal line
                    double angle = Math.toRadians(level.getRandom().nextBoolean() ? 45.0 : -45.0);
                    // 2d trig at this point. would you believe me if i told you this took 20 minutes to setup.
                    // its a miracle i did well in calculus.
                    Vec3 slashDir = right.scale(Math.cos(angle)).add(up.scale(Math.sin(angle)));

                    for (int i = 0; i < particleCount; i++) {
                        double progressPosition = (i / (double) (particleCount - 1)) - 0.5;
                        Vec3 pos = center.add(slashDir.scale(progressPosition * Math.max(1.0, entity.getBbHeight() * 1.4)));
                        level.addParticle(slashParticle, pos.x, pos.y, pos.z, 0, 0, 0);
                    }
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

        event.register(ClientsidePlayerSoundEffectPacket.TYPE, (data, context) -> context.enqueueWork(() -> {
            UnshatteredUtils.playClientsideSound(context.player(), data.soundEvent().value(), SoundSource.PLAYERS, data.volume(), data.pitch());
        }));
    }
}
