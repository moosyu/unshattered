package io.github.moosyu.util.damage;

import java.util.ArrayList;
import java.util.List;

public class DamageNumberManager {
    private static final List<DamageNumber> NUMBERS = new ArrayList<>();

    public static void add(DamageNumber number) {
        NUMBERS.add(number);
    }

    public static List<DamageNumber> getNumbers() {
        return NUMBERS;
    }

    public static void tick() {
        NUMBERS.removeIf(DamageNumber::tick);
    }
}