package io.github.moosyu.entities.projectile.terrain;

import io.github.moosyu.damage.DamageUtils;
import io.github.moosyu.damage.UnshatteredDamageSource;
import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.mob.Yeti;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static io.github.moosyu.entities.UnshatteredEntityDataSerializers.BLOCK_LIST;

public class TossedTerrain extends Projectile {
    public static final ResourceKey<DamageType> TERRAIN_CRUSH = ResourceKey.create(Registries.DAMAGE_TYPE, UnshatteredUtils.getUnshatteredIdentifier("terrain_crush"));
    public static final double GRAVITY = 0.05;
    private static final EntityDataAccessor<List<BlockState>> BLOCKS = SynchedEntityData.defineId(TossedTerrain.class, BLOCK_LIST.get());

    public TossedTerrain(EntityType<? extends TossedTerrain> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCKS, List.of());
    }

    public void setBlockStates(List<BlockState> states) {
        entityData.set(BLOCKS, List.copyOf(states));
    }

    public List<BlockState> getBlockStates() {
        return entityData.get(BLOCKS);
    }


    @Override
    public void tick() {
        super.tick();
        Vec3 velocity = getDeltaMovement();

        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hit.getType() != HitResult.Type.MISS) {
            onHit(hit);
            if (isRemoved()) {
                return;
            }
        }

        setPos(position().add(velocity));
        setDeltaMovement(velocity.add(0, -GRAVITY, 0));
    }

    @Override
    protected void onHitBlock(@NonNull BlockHitResult result) {
        super.onHitBlock(result);
        if (level() instanceof ServerLevel serverLevel && !level().isClientSide()) {
            if (owner != null) {
                Entity tosser = level().getEntity(owner.getUUID());

                if (tosser instanceof Player player) {
                    getEntitiesInCollision(serverLevel, LivingEntity.class).forEach(livingEntity -> {
                        double damage = 2000;
                        // shouldnt be too expensive considering its only being run once
                        double distanceFromImpact = position().distanceTo(livingEntity.position());

                        if (distanceFromImpact > 1) {
                            damage /= distanceFromImpact;
                        }

                        DamageUtils.playerDealDamage(player, livingEntity, damage, false, player.getMainHandItem(), true, true);
                    });
                } else if (tosser instanceof Yeti yeti) {
                    getEntitiesInCollision(serverLevel, Player.class).forEach(player -> {
                        DamageSource source = new UnshatteredDamageSource(player.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .getOrThrow(TERRAIN_CRUSH),
                                this,
                                yeti,
                                40,
                                true
                        );
                        player.hurtServer(serverLevel, source, 1.0f);
                    });
                }
            }

            Vec3 impact = result.getLocation();
            double range = getType().clientTrackingRange() * 16.0;
            double rangeSq = range * range;
            List<ServerPlayer> viewers = serverLevel.players()
                    .stream()
                    .filter(player -> player.distanceToSqr(impact) <= rangeSq)
                    .toList();
            RandomSource random = serverLevel.getRandom();
            List<BlockState> states = getBlockStates();

            serverLevel.playSound(null, impact.x, impact.y, impact.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 5.0f, 0.7f);
            sendParticlesToAll(viewers, serverLevel, ParticleTypes.EXPLOSION, impact, 0, 0, 0);

            if (!states.isEmpty()) {
                for (int i = 0; i < 80; i++) {
                    BlockState state = states.get(random.nextInt(states.size()));
                    double angle = random.nextDouble() * Math.TAU;
                    double outward = 0.2 + random.nextDouble() * 0.6;

                    sendParticlesToAll(viewers,
                            serverLevel,
                            new BlockParticleOption(ParticleTypes.BLOCK, state),
                            impact.add(0, 0.3, 0),
                            Math.cos(angle) * outward, (0.4 + random.nextDouble()) * 0.9, Math.sin(angle) * outward
                    );
                }
            }
        }

        discard();
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("Blocks", BlockState.CODEC.listOf(), getBlockStates());
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("Blocks", BlockState.CODEC.listOf()).ifPresent(this::setBlockStates);
    }

    private static <T extends ParticleOptions> void sendParticlesToAll(List<ServerPlayer> viewers, ServerLevel level, T options, Vec3 pos, double vx, double vy, double vz) {
        for (ServerPlayer player : viewers) {
            level.sendParticles(player, options, true, true, pos.x, pos.y, pos.z, 0, vx, vy, vz, 1.0);
        }
    }

    private <T extends LivingEntity> List<T> getEntitiesInCollision(ServerLevel serverLevel, Class<T> entityClass) {
        return serverLevel.getEntitiesOfClass(entityClass, UnshatteredEntities.YETI_TOSSED_TERRAIN.get()
                .getDimensions()
                .makeBoundingBox(position()).inflate(4)
        );
    }
}