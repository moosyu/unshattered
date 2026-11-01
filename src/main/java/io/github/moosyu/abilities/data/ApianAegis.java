package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.projectiles.HomingBee;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class ApianAegis extends Ability {
    public ApianAegis() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_TAKE_DAMAGE);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        Optional<ServerPlayer> optionalPlayer = context.getServerPlayer();
        Optional<DamageSource> optionalDamageSource = context.get(AbilityContextKey.DAMAGE_SOURCE);
        if (optionalPlayer.isEmpty() || optionalDamageSource.isEmpty()) {
            return;
        }

        ServerPlayer player = optionalPlayer.get();
        if (!(optionalDamageSource.get().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        Level level = player.level();
        Vec3 target = attacker.getBoundingBox().getCenter();
        HomingBee bee = new HomingBee(UnshatteredEntities.HOMING_BEE.get(), level, player, attacker, 20);

        float yawRad = player.getYRot() * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));
        double side = level.getRandom().nextBoolean() ? 1.0 : -1.0;
        Vec3 start = player.getEyePosition()
                .subtract(0, bee.getBbHeight() / 2, 0)
                .subtract(forward.scale(0.8))
                .add(new Vec3(-forward.z, 0, forward.x).scale(side * 0.9));

        bee.setPos(start);
        bee.setDeltaMovement(target.subtract(start).normalize().scale(HomingBee.SPEED));
        level.addFreshEntity(bee);
    }
}
