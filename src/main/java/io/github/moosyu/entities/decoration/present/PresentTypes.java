package io.github.moosyu.entities.decoration.present;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.Unshattered;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PresentTypes {
    public record PresentType(Identifier texture) {
        public static final Codec<PresentType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("texture").forGetter(PresentType::texture)
        ).apply(instance, PresentType::new));
    }

    public static final ResourceKey<Registry<PresentType>> PRESENT_KEY = ResourceKey.createRegistryKey(UnshatteredUtils.getUnshatteredIdentifier("present_type"));
    public static final DeferredRegister<PresentType> PRESENT_TYPES = DeferredRegister.create(PRESENT_KEY, Unshattered.MODID);
    public static final Registry<PresentType> REGISTRY = PRESENT_TYPES.makeRegistry(builder -> builder.sync(true));

    public static final DeferredHolder<PresentType, PresentType> WHITE = PRESENT_TYPES.register("white",
            () -> new PresentType(UnshatteredUtils.getUnshatteredIdentifier("textures/entity/decoration/white_present.png")));
    public static final DeferredHolder<PresentType, PresentType> GREEN = PRESENT_TYPES.register("green",
            () -> new PresentType(UnshatteredUtils.getUnshatteredIdentifier("textures/entity/decoration/green_present.png")));
    public static final DeferredHolder<PresentType, PresentType> RED = PRESENT_TYPES.register("red",
            () -> new PresentType(UnshatteredUtils.getUnshatteredIdentifier("textures/entity/decoration/red_present.png")));
}