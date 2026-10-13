package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * two paths for textures are used and theyre often resolved in two different ways
 * @param skinLocation path in entity/npcs/name format
 * @param texturePath path in textures/entity/npcs/name.png format
 */
public record NpcData(Identifier skinLocation, int npcColour, Identifier texturePath) {
    public NpcData(Identifier skinLocation, int npcColour) {
        this(skinLocation, npcColour, skinLocation.withPrefix("textures/").withSuffix(".png"));
    }

    public static Codec<NpcData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("skin_location").forGetter(NpcData::skinLocation),
                    Codec.INT.fieldOf("npc_colour").forGetter(NpcData::npcColour),
                    Identifier.CODEC.fieldOf("texture_path").forGetter(NpcData::texturePath)
            ).apply(instance, NpcData::new)
    );
}
