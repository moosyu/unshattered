package io.github.moosyu.data.datagen;

import io.github.moosyu.sounds.UnshatteredSounds;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredSoundDefinitionsProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     */
    public UnshatteredSoundDefinitionsProvider(PackOutput output) {
        super(output, MODID);
    }

    @Override
    public void registerSounds() {
        add(UnshatteredSounds.FEROCITY_TRIGGER_SOUND, SoundDefinition.definition().with(
                sound(UnshatteredUtils.getUnshatteredIdentifier("ferocity_trigger"))
        ).subtitle("sound." + MODID + ".ferocity_trigger"));

        add(UnshatteredSounds.ICICLE_PLACE, SoundDefinition.definition().with(
                sound(UnshatteredUtils.getUnshatteredIdentifier("icicle_place_1")),
                sound(UnshatteredUtils.getUnshatteredIdentifier("icicle_place_2")),
                sound(UnshatteredUtils.getUnshatteredIdentifier("icicle_place_3")),
                sound(UnshatteredUtils.getUnshatteredIdentifier("icicle_place_4")),
                sound(UnshatteredUtils.getUnshatteredIdentifier("icicle_place_5")),
                sound(UnshatteredUtils.getUnshatteredIdentifier("icicle_place_6"))
        ).subtitle("sound." + MODID + ".icicle_place"));
    }
}
