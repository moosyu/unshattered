package io.github.moosyu.attributes;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

import static io.github.moosyu.Unshattered.LOGGER;

public enum UnshatteredAttributeValues {
    // defaults are the player's base values for the attributes
    MAX_HEALTH("health", "❤", 20.0, 0.0, 2147483647.0, UnshatteredUtils.RED, true, false, false),
    HEALTH_REGEN("health_regen", "❣", 100.0, 0.0, 2048.0, UnshatteredUtils.RED, true, false, true),
    DEFENCE("defence", "❈", 0.0, 0.0, 131072.0, UnshatteredUtils.GREEN, true, false, false),
    TRUE_DEFENCE("true_defence", "❂", 0.0, 0.0, 1024.0, UnshatteredUtils.WHITE, true, false, false),
    STRENGTH("strength", "❁", 0.0, 0.0, 4096.0, UnshatteredUtils.RED, true, true, false),
    CRITICAL_CHANCE("critical_chance", "☣", 0.0, 0.0, 400.0, UnshatteredUtils.MAGENTA, true, false, true),
    CRITICAL_DAMAGE("critical_damage", "☠", 0.0, 0.0, 4096.0, UnshatteredUtils.MAGENTA, true, true, true),
    FEROCITY("ferocity", "⫽", 0.0, 0.0, 200.0, UnshatteredUtils.RED, true, true, true),
    MANA("mana", "✎", 20.0, 0.0, 131072.0, UnshatteredUtils.CYAN, true, false, false),
    MANA_REGEN("mana_regen", "✎", 100.0, 0.0, 2048.0, UnshatteredUtils.CYAN, true, false, true),
    MINING_SPEED("mining_speed", "⸕", 0.0, 0.0, 400.0, UnshatteredUtils.GOLD, true, false, false),
    MINING_FORTUNE("mining_fortune", "\uD83D\uDC8E", 0.0, 0.0, 2048.0, UnshatteredUtils.GOLD, true, false, false),
    MINING_SPREAD("mining_spread", "▚", 0.0, 0.0, 1024.0, UnshatteredUtils.YELLOW, true, false, false),
    PRISTINE("pristine", "✧", 0.0, 0.0, 32.0, UnshatteredUtils.PURPLE, true, false, false),
    FARMING_FORTUNE("farming_fortune", "☘", 0.0, 0.0, 2048.0, UnshatteredUtils.GOLD, true, false, false),
    FORAGING_FORTUNE("foraging_fortune", "\uD83E\uDE93", 0.0, 0.0, 2048.0, UnshatteredUtils.GOLD, true, false, false),
    SWEEP("sweep", "∮", 0.0, 0.0, 1024.0, UnshatteredUtils.DARK_GREEN, true, false, false),
    COMBAT_FORTUNE("combat_fortune", "✯", 0.0, 0.0, 900.0, UnshatteredUtils.CYAN, true, false, false),
    FISHING_SPEED("fishing_speed", "☂", 0.0, 0.0, 448.0, UnshatteredUtils.CYAN, true, false, false),
    FISHING_FORTUNE("fishing_fortune", "⛃", 0.0, 0.0, 2048.0, UnshatteredUtils.DARK_AQUA, true, false, false),
    DAMAGE("damage", 0.0, 0.0, 2147483647.0),
    FINAL_DAMAGE_MODIFIER("final_damage_modifier", 1, 0, 10.0),
    BREAKING_POWER("breaking_power", "Ⓟ", 0, 0, 20, UnshatteredUtils.DARK_GREEN, true, false, false),
    COMBAT_WISDOM("combat_wisdom", "☯", 100, 0, 500, UnshatteredUtils.DARK_AQUA, true, false, true),
    FARMING_WISDOM("farming_wisdom", "☯", 100, 0, 500, UnshatteredUtils.DARK_AQUA, true, false, true),
    FISHING_WISDOM("fishing_wisdom", "☯", 100, 0, 500, UnshatteredUtils.DARK_AQUA, true, false, true),
    MINING_WISDOM("mining_wisdom", "☯", 100, 0, 500, UnshatteredUtils.DARK_AQUA, true, false, true),
    FORAGING_WISDOM("foraging_wisdom", "☯", 100, 0, 500, UnshatteredUtils.DARK_AQUA, true, false, true),
    MAGECRAFT_WISDOM("magecraft_wisdom", "☯", 100, 0, 500, UnshatteredUtils.DARK_AQUA, true, false, true),
    CARPENTRY_WISDOM("carpentry_wisdom", "☯", 100, 0, 500, UnshatteredUtils.DARK_AQUA, true, false, true),
    CURRENT_HEALTH("max_health", 20.0, 0.0, 2147483647.0);

    private static final Map<Attribute, UnshatteredAttributeValues> ATTRIBUTE_MAP = new HashMap<>();
    public final String id;
    public final String symbol;
    public final double def, min, max;
    public final int color;
    public DeferredHolder<Attribute, Attribute> holder;
    public final boolean visible, offensive, percentage;
    private static Map<Attribute, UnshatteredAttributeValues> attributeMap;

    UnshatteredAttributeValues(String id, String symbol, double def, double min, double max, int color, boolean visible, boolean offensive, boolean percentage) {
        this.id = id;
        this.symbol = symbol;
        this.def = def;
        this.min = min;
        this.max = max;
        this.color = color;
        this.visible = visible;
        this.offensive = offensive;
        this.percentage = percentage;
    }

    UnshatteredAttributeValues(String id, double def, double min, double max) {
        this(id, "", def, min, max, 0x00000000, false, true, false);
    }

    public String getTranslationKey() {
        return "attribute.name.unshattered." + id;
    }

    public static UnshatteredAttributeValues fromAttribute(Attribute attribute) {
        Map<Attribute, UnshatteredAttributeValues> map = attributeMap;
        // so the server and client build their own (hopefully identical) copies of the attributes map when needed
        if (map == null) {
            map = new HashMap<>();
            for (UnshatteredAttributeValues value : values()) {
                if (value.holder != null) {
                    map.put(value.holder.value(), value);
                }
            }
            attributeMap = map;
        }
        return map.get(attribute);
    }

    public static void modifyAttributeBaseValue(Player player, UnshatteredAttributeValues attribute, double amount) {
        if (player.level().isClientSide()) {
            return;
        }

        AttributeInstance playerAttribute = player.getAttribute(attribute.holder);
        if (playerAttribute == null) {
            LOGGER.error("player didn't have attribute: {}", attribute.id);
            return;
        }
        playerAttribute.setBaseValue(playerAttribute.getBaseValue() + amount);
    }
}