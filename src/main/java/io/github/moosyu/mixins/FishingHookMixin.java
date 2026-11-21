package io.github.moosyu.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.datagen.UnshatteredItemTagsProvider;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.damage.DamageUtils;
import io.github.moosyu.items.tools.rods.UnshatteredFishingRod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Projectile {
    protected FishingHookMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Shadow
    private int timeUntilLured;

    @Shadow
    private int timeUntilHooked;

    @Shadow
    private int nibble;

    @Shadow
    private float fishAngle;

    @Shadow
    private static EntityDataAccessor<Boolean> DATA_BITING;

    /**
     * vanilla tick() calls FluidState.is(FluidTags.WATER) to decide the fluid height, when to switch from FLYING
     * to BOBBING, and whether gravity applies. this enables that functionality for lava.
     */
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean lavaCountsAsWater(FluidState state, TagKey<Fluid> tag, Operation<Boolean> original) {
        if (tag == FluidTags.WATER && this.fireImmune() && state.is(FluidTags.LAVA)) {
            return true;
        }
        return original.call(state, tag);
    }

    @Unique
    private boolean isFishable(BlockState state) {
        return state.is(Blocks.WATER) || (this.fireImmune() && state.is(Blocks.LAVA));
    }

    @Unique
    private boolean lavaHook() {
        return this.fireImmune() && this.level().getFluidState(this.blockPosition()).is(FluidTags.LAVA);
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    protected void onHitEntity(EntityHitResult hitResult, CallbackInfo ci) {
        if (this.getOwner() instanceof Player player) {
            Entity entity = hitResult.getEntity();
            if (entity.is(EntityType.ARMOR_STAND) && !player.isCreative()) {
                ci.cancel();
            } else {
                InteractionHand hand;
                if (player.getItemInHand(InteractionHand.MAIN_HAND).typeHolder().getData(UnshatteredDataMaps.ITEM_TYPE_DATA) == ItemType.FISHING_ROD) {
                    hand = InteractionHand.MAIN_HAND;
                } else if (player.getItemInHand(InteractionHand.OFF_HAND).typeHolder().getData(UnshatteredDataMaps.ITEM_TYPE_DATA) == ItemType.FISHING_ROD) {
                    hand = InteractionHand.OFF_HAND;
                } else {
                    ci.cancel();
                    return;
                }

                if (entity instanceof LivingEntity livingEntity && !player.level().isClientSide()) {
                    DamageUtils.playerDealDamage(player,
                            livingEntity,
                            player.getAttributeValue(UnshatteredAttributeValue.DAMAGE.holder) + 1,
                            false,
                            player.getItemInHand(hand),
                            true,
                            false
                    );
                }
            }
        }
    }

    @Inject(method = "catchingFish", at = @At("HEAD"), cancellable = true)
    private void catchingFish(BlockPos pos, CallbackInfo ci) {
        ci.cancel();
        FishingHook hook = (FishingHook) (Object) this;
        if (!(hook.getOwner() instanceof Player player)) {
            return;
        }
        ServerLevel level = (ServerLevel) hook.level();
        RandomSource random = hook.getRandom();
        double fishingSpeed = player.getAttributeValue(UnshatteredAttributeValue.FISHING_SPEED.holder);
        double fishingSpeedPercentage = (UnshatteredAttributeValue.FISHING_SPEED.max + 20 - fishingSpeed) / UnshatteredAttributeValue.FISHING_SPEED.max;

        boolean lava = lavaHook();

        if (nibble > 0) {
            --nibble;
            if (nibble <= 0) {
                timeUntilLured = 0;
                timeUntilHooked = 0;
                hook.getEntityData().set(DATA_BITING, false);
            }
        } else if (timeUntilHooked > 0) {
            timeUntilHooked -= 1;
            if (timeUntilHooked > 0) {
                fishAngle += (float) random.triangle(0.0, 9.188);
                float angle = fishAngle * (float) (Math.PI / 180.0);
                float angleSin = Mth.sin(angle);
                float angleCos = Mth.cos(angle);
                double fishX = hook.getX() + angleSin * timeUntilHooked * 0.1f;
                double fishY = Mth.floor(hook.getY()) + 1.0f;
                double fishZ = hook.getZ() + angleCos * timeUntilHooked * 0.1f;
                BlockState splashBlockState = level.getBlockState(BlockPos.containing(fishX, fishY - 1.0, fishZ));
                if (isFishable(splashBlockState)) {
                    if (random.nextFloat() < 0.15f) {
                        level.sendParticles(lava ? ParticleTypes.LAVA : ParticleTypes.BUBBLE,
                                fishX, fishY - 0.1f, fishZ, 1, angleSin, 0.1, angleCos, 0.0);
                    }

                    if (lava) {
                        // (-sin, -cos) points from the fish back to the hook.
                        double speed = 0.05;
                        double velX = -angleSin * speed;
                        double velZ = -angleCos * speed;
                        level.sendParticles(ParticleTypes.FLAME, fishX, fishY, fishZ, 0, velX, 0.01, velZ, 1.0);
                        level.sendParticles(ParticleTypes.SMALL_FLAME, fishX, fishY, fishZ, 0, velX, 0.01, velZ, 1.0);
                    } else {
                        float particleXMovement = angleSin * 0.04f;
                        float particleZMovement = angleCos * 0.04f;
                        level.sendParticles(ParticleTypes.FISHING, fishX, fishY, fishZ, 0, particleZMovement, 0.01, -particleXMovement, 1.0);
                        level.sendParticles(ParticleTypes.FISHING, fishX, fishY, fishZ, 0, -particleZMovement, 0.01, particleXMovement, 1.0);
                    }
                }
            } else {
                level.playSound(null, hook.getX(), hook.getY(), hook.getZ(), SoundEvents.FISHING_BOBBER_SPLASH, hook.getSoundSource(), 0.25f, 1.0f + (random.nextFloat() - random.nextFloat()) * 0.4f);
                double y = hook.getY() + 0.5;
                ParticleOptions bubbles = lava ? ParticleTypes.LAVA : ParticleTypes.BUBBLE;
                ParticleOptions burst = lava ? ParticleTypes.FLAME : ParticleTypes.FISHING;
                int count = (int) (1.0f + hook.getBbWidth() * 20.0f);
                level.sendParticles(bubbles, hook.getX(), y, hook.getZ(), count, hook.getBbWidth(), 0.0, hook.getBbWidth(), 0.2f);
                level.sendParticles(burst, hook.getX(), y, hook.getZ(), count, hook.getBbWidth(), 0.0, hook.getBbWidth(), 0.2f);
                nibble = Mth.nextInt(random, 20, 40);
                hook.getEntityData().set(DATA_BITING, true);
            }
        } else if (this.timeUntilLured > 0) {
            timeUntilLured -= 1;
            float teaseChance = 0.15f;
            if (timeUntilLured < 20) {
                teaseChance += (20 - timeUntilLured) * 0.05f;
            } else if (timeUntilLured < 40) {
                teaseChance += (40 - timeUntilLured) * 0.02f;
            } else if (timeUntilLured < 60) {
                teaseChance += (60 - timeUntilLured) * 0.01f;
            }

            if (random.nextFloat() < teaseChance) {
                float angle = Mth.nextFloat(random, 0.0f, 360.0f) * (float) (Math.PI / 180.0);
                float dist = Mth.nextFloat(random, 25.0f, 60.0f);
                double fishX = hook.getX() + Mth.sin(angle) * dist * 0.1;
                double fishY = Mth.floor(hook.getY()) + 1.0f;
                double fishZ = hook.getZ() + Mth.cos(angle) * dist * 0.1;
                BlockState splashBlockState = level.getBlockState(BlockPos.containing(fishX, fishY - 1.0, fishZ));
                if (isFishable(splashBlockState)) {
                    level.sendParticles(
                            lava ? ParticleTypes.LAVA : ParticleTypes.SPLASH,
                            fishX,
                            fishY,
                            fishZ,
                            2 + random.nextInt(2),
                            0.1f,
                            0.0,
                            0.1f,
                            0.0
                    );
                }
            }

            if (timeUntilLured <= 0) {
                fishAngle = Mth.nextFloat(random, 0.0f, 360.0f);
                timeUntilHooked = random.nextInt(10, Math.max(20, (int) (100 * fishingSpeedPercentage)));
            }
        } else {
            timeUntilLured = random.nextInt(20, Math.max(40, (int) (160 * fishingSpeedPercentage)));
        }
    }

    @Override
    public boolean fireImmune() {
        if (getOwner() instanceof Player player) {
            ItemStack rodItemStack = UnshatteredFishingRod.getCastingItemStack(player);
            if (rodItemStack != null) {
                return rodItemStack.is(UnshatteredItemTagsProvider.CAN_FISH_IN_LAVA);
            }
        }

        return false;
    }
}
