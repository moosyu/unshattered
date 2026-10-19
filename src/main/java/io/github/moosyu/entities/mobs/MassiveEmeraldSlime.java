package io.github.moosyu.entities.mobs;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class MassiveEmeraldSlime extends UnshatteredSlime {
    private final static float DAMAGE = 12.0f;

    public MassiveEmeraldSlime(EntityType<? extends Mob> type, Level level) {
        super(type, level, DAMAGE);
    }

    public static AttributeSupplier.@NonNull Builder createAttributes() {
        return UnshatteredUtils.createHostileMobAttributes(100.0f, DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0f);
    }
}
