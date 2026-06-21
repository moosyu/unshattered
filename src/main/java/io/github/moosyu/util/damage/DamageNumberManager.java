package io.github.moosyu.util.damage;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
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