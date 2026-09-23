package io.github.moosyu.entities.projectiles;

import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.util.UnshatteredUtils;
import io.github.moosyu.util.damage.DamageUtils;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class HomingBee extends Projectile {
    private final Player player;
    private final LivingEntity target;
    private final double damage;

    protected HomingBee(EntityType<? extends Projectile> type, Level level) {
        this(type, level, null, null, 0.0d);
    }

    protected HomingBee(EntityType<? extends Projectile> type, Level level, Player player, LivingEntity target, double damage) {
        super(type, level);
        this.target = target;
        this.damage = damage;
        this.player = player;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (target != null && target.isAlive()) {
            // go to target
        } else {
            discard();
        }

        Vec3 movement = getDeltaMovement();
        setPos(getX() + movement.x, getY() + movement.y, getZ() + movement.z);
    }

    @Override
    protected void onHit(@NonNull HitResult result) {
        super.onHit(result);
        if (!level().isClientSide()) {
            level().explode(
                    this,
                    getX(), getY(), getZ(),
                    2.0f,
                    Level.ExplosionInteraction.MOB
            );

            if (result.getType() == HitResult.Type.ENTITY && player != null && target != null) {
                // probably needs to check if it collided with target and not a random entity somehow
                DamageUtils.playerDealDamage(player, target, ItemTypes.TALISMAN);
            }

            discard();
        }
    }
}