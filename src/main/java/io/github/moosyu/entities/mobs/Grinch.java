package io.github.moosyu.entities.mobs;

import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class Grinch extends PathfinderMob {
    public Grinch(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35f)
                .add(UnshatteredAttributeValue.CURRENT_HEALTH.holder, 10.0f)
                .add(UnshatteredAttributeValue.MAX_HEALTH.holder, 10.0f);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 16.0f, 1.0, 1.6));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));

    }

    @Override
    public boolean hurtServer(@NonNull ServerLevel level, @NonNull DamageSource source, float amount) {
        boolean wasHurt = super.hurtServer(level, source, amount);

        if (wasHurt) {
            int selectedNumber = level.getRandom().nextInt(0, 10);
            Item presentType;

            if (selectedNumber == 0) {
                presentType = UnshatteredItems.RED_PRESENT.get();
            } else if (selectedNumber < 8) {
                presentType = UnshatteredItems.WHITE_PRESENT.get();
            } else {
                presentType = UnshatteredItems.GREEN_PRESENT.get();
            }

            spawnAtLocation(level, presentType);
        }

        return wasHurt;
    }
}
