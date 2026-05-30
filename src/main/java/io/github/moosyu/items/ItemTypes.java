package io.github.moosyu.items;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public enum ItemTypes {
    ITEM("item_type.unshattered.item", false),
    FISH("item_type.unshattered.fish", false),
    LOG("item_type.unshattered.log", false),
    SWORD("item_type.unshattered.sword", true),
    AXE("item_type.unshattered.axe", true),
    BATTLE_AXE("item_type.unshattered.battle_axe", true),
    BOW("item_type.unshattered.bow", true),
    HELMET("item_type.unshattered.helmet", true),
    CHESTPLATE("item_type.unshattered.chestplate", true),
    LEGGINGS("item_type.unshattered.leggings", true),
    BOOTS("item_type.unshattered.boots", true),
    WAND("item_type.unshattered.wand", true),
    ACCESSORY("item_type.unshattered.accessory", true),
    STAFF("item_type.unshattered.staff", true);

    private final String key;
    private final boolean reforgeable;

    ItemTypes(String key, boolean reforgeable) {
        this.key = key;
        this.reforgeable = reforgeable;
    }

    public String getKey() {return key;}
    public boolean reforgeable() {return reforgeable;}

    public static final Codec<ItemTypes> CODEC = Codec.STRING.xmap(ItemTypes::valueOf, ItemTypes::name);
}
