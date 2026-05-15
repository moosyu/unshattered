package io.github.moosyu.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.NNO.MODID;

public class AttributesRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, MODID);

    public static final DeferredHolder<Attribute, Attribute> SWEEP = ATTRIBUTES.register("sweep", () -> new RangedAttribute("attribute.name.nno.sweep", 0.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> FORAGING_FORTUNE = ATTRIBUTES.register("foraging_fortune", () -> new RangedAttribute("attribute.name.nno.foraging_fortune", 0.0D, 0.0D, 1024.0D).setSyncable(true));
}