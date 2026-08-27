package io.github.moosyu.sounds;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MODID);

    public static final Holder<SoundEvent> FEROCITY_TRIGGER_SOUND = SOUND_EVENTS.register("ferocity_trigger_sound", SoundEvent::createVariableRangeEvent);
}
