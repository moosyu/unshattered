package io.github.moosyu.damage;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class UnshatteredDamageSource extends DamageSource {
    private final double damage;
    private final boolean overrideInvulnerability;

    public UnshatteredDamageSource(Holder<DamageType> type, @Nullable Entity directEntity, @Nullable Entity causingEntity, double damage, boolean overrideInvulnerability) {
        super(type, directEntity, causingEntity);
        this.damage = damage;
        this.overrideInvulnerability = overrideInvulnerability;
    }

    public double getDamage() {
        return damage;
    }

    public boolean shouldOverrideInvulnerability() {
        return overrideInvulnerability;
    }
}

