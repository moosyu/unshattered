package io.github.moosyu.items.talismans;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class TieredTalisman extends Item {
    public final Identifier tierIdentifier;

    public TieredTalisman(Properties properties, Identifier tierIdentifier) {
        super(properties.stacksTo(1));

        this.tierIdentifier = tierIdentifier;
    }
}
