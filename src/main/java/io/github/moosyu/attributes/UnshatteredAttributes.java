package io.github.moosyu.attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

public enum UnshatteredAttributes {
    HEALTH("health", "❤", 100.0, 0.0, 2147483647.0, 0xFFFC3A3A, AttributeTypes.IMPORTANT, false, false),
    DEFENSE("defense", "❈", 0.0, 0.0, 131072.0, 0xFF55FF55, AttributeTypes.IMPORTANT, false, false),
    STRENGTH("strength", "❁", 0.0, 0.0, 4096.0, 0xFFFC3A3A, AttributeTypes.IMPORTANT, true, false),
    CRITICAL_CHANCE("critical_chance", "☣", 30.0, 0.0, 2048.0, 0xFF3535CC, AttributeTypes.IMPORTANT, false, true),
    CRITICAL_DAMAGE("critical_damage", "☠", 50.0, 0.0, 4096.0, 0xFF3535CC, AttributeTypes.IMPORTANT, true, true),
    MANA("mana", "✎", 100.0, 0.0, 131072.0, 0xFF55D5FF, AttributeTypes.IMPORTANT, false, false),
    HEALTH_REGEN("health_regen", "❣", 100.0, 0.0, 2048.0, 0xFFFC3A3A, AttributeTypes.VISIBLE, false, false),
    TRUE_DEFENSE("true_defense", "❂", 0.0, 0.0, 1024.0, 0xFFFFFFFF, AttributeTypes.VISIBLE, false, false),
    FEROCITY("ferocity", "⫽", 0.0, 0.0, 131072.0, 0xFFFF5555, AttributeTypes.VISIBLE, true, false),
    DAMAGE("damage", 0.0, 0.0, 2147483647.0),
    MINING_SPEED("mining_speed", "⸕", 0.0, 0.0, 131072.0, 0xFFFFAA00, AttributeTypes.VISIBLE, false, false),
    MINING_FORTUNE("mining_fortune", "☘", 0.0, 0.0, 2048.0, 0xFFFFAA00, AttributeTypes.IMPORTANT, false, false),
    MINING_SPREAD("mining_spread", "☘", 0.0, 0.0, 1024.0, 0xFFFFAA00, AttributeTypes.VISIBLE, false, false),
    PRISTINE("pristine", "✧", 0.0, 0.0, 32.0, 0xFFAA00AA, AttributeTypes.VISIBLE, false, false),
    FARMING_FORTUNE("farming_fortune", "☘", 0.0, 0.0, 2048.0, 0xFFFFAA00, AttributeTypes.IMPORTANT, false, false),
    FORAGING_FORTUNE("foraging_fortune", "☘", 0.0, 0.0, 2048.0, 0xFFFFAA00, AttributeTypes.IMPORTANT, false, false),
    SWEEP("sweep", "∮", 0.0, 0.0, 1024.0, 0xFF00AA00, AttributeTypes.VISIBLE, false, false),
    MAGIC_FIND("magic_find", "✯", 0.0, 0.0, 900.0, 0xFF55FFFF, AttributeTypes.VISIBLE, false, false),
    FINAL_DAMAGE_MODIFIER("final_damage_modifier", 1, 0, 10.0);

    private static final Map<Attribute, UnshatteredAttributes> ATTRIBUTE_MAP = new HashMap<>();
    public final String id;
    public final String symbol;
    public final double def, min, max;
    public final int color;
    public final AttributeTypes type;
    public DeferredHolder<Attribute, Attribute> holder;
    public final boolean offensive, percentage;

    UnshatteredAttributes(String id, String symbol, double def, double min, double max, int color, AttributeTypes type, boolean offensive, boolean percentage) {
        this.id = id;
        this.symbol = symbol;
        this.def = def;
        this.min = min;
        this.max = max;
        this.color = color;
        this.type = type;
        this.offensive = offensive;
        this.percentage = percentage;
    }

    UnshatteredAttributes(String id, double def, double min, double max) {
        this(id, "", def, min, max, 0x00000000, AttributeTypes.INVISIBLE, true, false);
    }

    public String getTranslationKey() {
        return "attribute.name.unshattered." + id;
    }

    // for accessing attributes using holder values
    public static void buildLookup() {
        ATTRIBUTE_MAP.clear();
        for (UnshatteredAttributes modAttribute : values()) {
            if (modAttribute.holder != null) {
                ATTRIBUTE_MAP.put(modAttribute.holder.value(), modAttribute);
            }
        }
    }

    public static UnshatteredAttributes fromAttribute(Attribute attribute) {
        return ATTRIBUTE_MAP.get(attribute);
    }
}