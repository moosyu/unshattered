package io.github.moosyu.registers;

import io.github.moosyu.attributes.ModAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;

public class AttributesRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, MODID);

    public static void registerAll() {
        for (ModAttributes attribute : ModAttributes.values()) {
            attribute.holder = ATTRIBUTES.register(attribute.id, () ->
                    new RangedAttribute(attribute.getTranslationKey(), attribute.def, attribute.min, attribute.max).setSyncable(true));
        }
    }
}