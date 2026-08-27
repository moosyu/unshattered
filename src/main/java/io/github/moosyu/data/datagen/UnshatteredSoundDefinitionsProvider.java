package io.github.moosyu.data.datagen;

import io.github.moosyu.sounds.UnshatteredSounds;
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
                sound(Identifier.fromNamespaceAndPath(MODID, "ferocity_trigger"))
        ).subtitle("sound." + MODID + ".ferocity_trigger"));
    }
}
