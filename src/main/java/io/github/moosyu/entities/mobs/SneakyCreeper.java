package io.github.moosyu.entities.mobs;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class SneakyCreeper extends Creeper {
    public SneakyCreeper(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
        
        if (level.isClientSide()) {
            setInvisible(true);
        }
    }

    public static AttributeSupplier.@NonNull Builder createAttributes() {
        return Creeper.createAttributes()
                .add(UnshatteredAttributeValues.HEALTH.holder, 25.0f)
                .add(UnshatteredAttributeValues.DAMAGE.holder, 60.0f);
    }

    @Override
    public void tick() {
        super.tick();
        setInvisible(level().isClientSide() && getSwellDir() <= 0);
    }
}
