package io.github.moosyu.items;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public enum ItemTypes {
    ITEM("item_type.nno.item", false),
    FISH("item_type.nno.fish", false),
    SWORD("item_type.nno.sword", true),
    AXE("item_type.nno.axe", true),
    BATTLE_AXE("item_type.nno.battle_axe", true),
    BOW("item_type.nno.bow", true),
    HELMET("item_type.nno.helmet", true),
    CHESTPLATE("item_type.nno.chestplate", true),
    LEGGINGS("item_type.nno.leggings", true),
    BOOTS("item_type.nno.boots", true),
    WAND("item_type.nno.wand", true),
    ACCESSORY("item_type.nno.accessory", true),
    STAFF("item_type.nno.staff", true);

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
