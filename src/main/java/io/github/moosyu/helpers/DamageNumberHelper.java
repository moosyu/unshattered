package io.github.moosyu.helpers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class DamageNumberHelper {
    public static class DamageNumber {
        public final Component text;
        public final Vec3 position;
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

    public static class DamageNumberManager {
        public static final List<DamageNumber> NUMBERS = new ArrayList<>();

        public static void add(Vec3 pos, Component text) {
            NUMBERS.add(new DamageNumber(pos, text));
        }
        
        public static void tick() {
            NUMBERS.removeIf(DamageNumber::tick);
        }
    }
}
