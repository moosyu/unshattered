package io.github.moosyu.items;

public class UnshatteredAxeWeapon extends UnshatteredItem {
    public UnshatteredAxeWeapon(Properties properties) {
        super(applyDefaults(properties), "Axe");
    }

    private static Properties applyDefaults(Properties properties) {return properties.stacksTo(1);}
}