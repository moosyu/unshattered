package io.github.moosyu.entities.mob;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.LoopType;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import io.github.moosyu.abilities.data.TerrainToss;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

public class Yeti extends PathfinderMob implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation SLAM = RawAnimation.begin().then("slam", LoopType.PLAY_ONCE);
    private static final RawAnimation THROW = RawAnimation.begin().then("throw", LoopType.PLAY_ONCE);
    private static final RawAnimation ROAR = RawAnimation.begin().then("roar", LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Yeti(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new ThrowGoal(this));
        goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.@NonNull ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("base", 15, test ->
                test.setAndContinue(test.isMoving() ? WALK : IDLE))
        );

        controllers.add(new AnimationController<>("action", 0, _ -> PlayState.STOP)
                .triggerableAnim("slam", SLAM)
                .triggerableAnim("roar", THROW)
                .triggerableAnim("roar", ROAR)
        );
    }

    @Override
    public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        // quite sure damage won't matter as ill have to pick on a per-move basis but whatever
        return UnshatteredUtils.createHostileMobAttributes(10000.0f, 15.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    private static class ThrowGoal extends Goal {
        private final Yeti yeti;

        public ThrowGoal(Yeti yeti) {
            this.yeti = yeti;
        }

        public boolean canUse() {
            System.out.println("tried");
            LivingEntity target = yeti.getTarget();
            System.out.println(target);
            return target != null && yeti.distanceToSqr(target) > 150;
        }

        @Override
        public void start() {
            LivingEntity target = yeti.getTarget();
            if (target != null) {
                // fallback is snow mainly because in some niche cases this guy can throw straight nothing at you if
                // it's air and considering its spawn there's probably snow nearby
                TerrainToss.throwTerrain(yeti.blockPosition().mutable(), yeti.level(), yeti, target.position(), Blocks.SNOW_BLOCK, 2.0f);
            }
        }
    }
}
