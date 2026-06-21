package io.github.moosyu.util.damage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class DamageNumber {
    public final Component text;
    public final Vec3 position;
    // both in ticks
    public int age = 0;
    public final int lifetime = 30;

    public DamageNumber(Vec3 position, Component text) {
        this.position = position;
        this.text = text;
    }

    public boolean tick() {
        age++;
        return age >= lifetime;
    }
}
