package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.projectiles.HomingBee;
import io.github.moosyu.items.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class HoneycombRing extends TalismanItem implements PassiveAbilityItem {
    public HoneycombRing(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("apian_aegis"), 0, 0, 0, true))
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 150000)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        if (context.get(AbilityContextKey.DAMAGE_SOURCE).isPresent() && context.get(AbilityContextKey.PLAYER).isPresent()) {
            ServerPlayer player = context.get(AbilityContextKey.PLAYER).get();
            DamageSource damageSource = context.get(AbilityContextKey.DAMAGE_SOURCE).get();
            Level level = player.level();
            Entity attacker = damageSource.getEntity();

            if (attacker instanceof LivingEntity livingEntity) {
                Vec3 target = attacker.getBoundingBox().getCenter();
                HomingBee bee = new HomingBee(UnshatteredEntities.HOMING_BEE.get(), level, player, livingEntity, 20);
                float yawRad = player.getYRot() * Mth.DEG_TO_RAD;
                Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad));
                Vec3 start = player.getEyePosition()
                        .subtract(0, bee.getBbHeight() / 2, 0)
                        .subtract(forward.scale(0.8))
                        .add(new Vec3(-forward.z, 0, forward.x).scale(level.getRandom().nextBoolean() ? 1.0 : -1.0 * 0.9));

                bee.setPos(start);
                bee.setDeltaMovement(target.subtract(start).normalize().scale(HomingBee.SPEED));
                level.addFreshEntity(bee);
            }
        }
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {}

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return true;
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_TAKE_DAMAGE);
    }
}
