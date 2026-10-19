package io.github.moosyu.entities.mobs;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class SlimeChild extends UnshatteredSlime {
    private static final float DAMAGE = 8.0f;

    public SlimeChild(EntityType<? extends Mob> type, Level level) {
        super(type, level, DAMAGE);
    }

    public static AttributeSupplier.@NonNull Builder createAttributes() {
        return UnshatteredUtils.createHostileMobAttributes(20.0f, DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0f);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SlimeFloatGoal(this));
        goalSelector.addGoal(2, new SlimeAttackGoal(this));
        goalSelector.addGoal(3, new SlimeRandomDirectionGoal(this));
        goalSelector.addGoal(5, new SlimeKeepOnJumpingGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (target, _) -> Math.abs(target.getY() - this.getY()) <= 4.0));
    }
}
