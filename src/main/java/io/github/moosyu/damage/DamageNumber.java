package io.github.moosyu.damage;

import net.minecraft.world.phys.Vec3;

public class DamageNumber {
    public static final int MAX_LIFETIME = 30;

    public final int number;
    public final Vec3 position;
    public final DamageType damageType;
    // both in ticks
    public int age = 0;

    public DamageNumber(Vec3 position, int number, DamageType damageType) {
        this.position = position;
        this.number = number;
        this.damageType = damageType;
    }

    public boolean tick() {
        age++;
        return age >= MAX_LIFETIME;
    }
}
