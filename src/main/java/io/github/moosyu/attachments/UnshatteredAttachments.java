package io.github.moosyu.attachments;

import com.mojang.serialization.Codec;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.RegionTemperatureTypes;
import io.github.moosyu.data.regions.UnshatteredRegions;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector2i;

import java.util.Set;
import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

// copy on death added to all even though the player should never really "die"
public final class UnshatteredAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final Supplier<AttachmentType<PlayerSkillsAttachment>> PLAYER_SKILLS = ATTACHMENT_TYPES.register("player_skills", () ->
            AttachmentType.builder(() -> new PlayerSkillsAttachment(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
                    .serialize(PlayerSkillsAttachment.RECORD_CODEC.fieldOf("skills"))
                    .sync(PlayerSkillsAttachment.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );

    // no serializer as the state should reset on rejoin
    public static final Supplier<AttachmentType<PlayerStateAttachment>> PLAYER_STATE = ATTACHMENT_TYPES.register("player_state", () ->
            AttachmentType.builder(PlayerStateAttachment::new)
                    .sync(new StateSyncHandler())
                    .build()
    );

    public static final Supplier<AttachmentType<PlayerAbilityEffectsAttachment>> PLAYER_ABILITIES = ATTACHMENT_TYPES.register("player_abilities", () ->
            AttachmentType.builder(PlayerAbilityEffectsAttachment::new)
                    .build()
    );

    public static final Supplier<AttachmentType<PlayerCurrencyAttachment>> PLAYER_CURRENCY = ATTACHMENT_TYPES.register("player_currency", () ->
            AttachmentType.builder(() -> new PlayerCurrencyAttachment(0, 0))
                    .serialize(PlayerCurrencyAttachment.RECORD_CODEC.fieldOf("currency"))
                    .sync(PlayerCurrencyAttachment.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<PlayerCollectionsAttachment>> PLAYER_COLLECTIONS = ATTACHMENT_TYPES.register("player_collections", () ->
            AttachmentType.builder(() -> new PlayerCollectionsAttachment())
                    .serialize(PlayerCollectionsAttachment.RECORD_CODEC.fieldOf("collections"))
                    .sync(PlayerCollectionsAttachment.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<PlayerRegionAttachment>> PLAYER_REGION = ATTACHMENT_TYPES.register("player_region", () ->
            AttachmentType.builder(() -> new PlayerRegionAttachment(UnshatteredRegions.DEFAULT_REGION, new Vector2i(0, 0), new BlockPos(0, 0, 0)))
                    .serialize(PlayerRegionAttachment.RECORD_CODEC.fieldOf("region"))
                    .sync(PlayerRegionAttachment.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<Float>> PLAYER_TEMPERATURE = ATTACHMENT_TYPES.register("player_temperature", () ->
            AttachmentType.builder(() -> 37.0f)
                    .serialize(Codec.FLOAT.fieldOf("temperature"))
                    .sync(ByteBufCodecs.FLOAT)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<PlayerDialogueFlagsAttachment>> PLAYER_DIALOGUE_FLAGS = ATTACHMENT_TYPES.register("player_dialogue_flags", () ->
            AttachmentType.builder(PlayerDialogueFlagsAttachment::new)
                    .serialize(PlayerDialogueFlagsAttachment.CODEC.fieldOf("flags"))
                    .sync(PlayerDialogueFlagsAttachment.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );
}
