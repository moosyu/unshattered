package io.github.moosyu.data;

import io.github.moosyu.Unshattered;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class UnshatteredStatistics {
    public static final DeferredRegister<Identifier> CUSTOM_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, Unshattered.MODID);

    public static final DeferredHolder<Identifier, Identifier> GIFTS_OPENED = CUSTOM_STATS.register("gifts_opened",
            () -> UnshatteredUtils.getUnshatteredIdentifier("gifts_opened")
    );
}
