package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record NpcData(Identifier skinLocation, int npcColour) {
    public static Codec<NpcData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("skin_location").forGetter(NpcData::skinLocation),
                    Codec.INT.fieldOf("npc_colour").forGetter(NpcData::npcColour)
            ).apply(instance, NpcData::new)
    );
}
