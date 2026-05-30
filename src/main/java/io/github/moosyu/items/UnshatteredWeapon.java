package io.github.moosyu.items;

public class UnshatteredWeapon extends UnshatteredItem {
    public UnshatteredWeapon(Properties properties) {
        this(properties, ItemTypes.ITEM);
    }

    protected UnshatteredWeapon(Properties properties, ItemTypes type) {
        super(properties.stacksTo(1), type);
    }
}