package io.github.moosyu.registers;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.helpers.StateSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;
import static net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.ATTACHMENT_TYPES;

public class AttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
    public static final Supplier<AttachmentType<PlayerSkillsAttachment>> PLAYER_SKILLS = ATTACHMENT_TYPES.register("player_skills", () ->
            AttachmentType.builder(() -> new PlayerSkillsAttachment(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
                    .serialize(PlayerSkillsAttachment.RECORD_CODEC.fieldOf("skills"))
                    .sync(PlayerSkillsAttachment.STREAM_CODEC)
                    .build()
    );

    // no serializer as the state should reset on rejoin
    public static final Supplier<AttachmentType<PlayerStateAttachment>> PLAYER_STATE = ATTACHMENT_TYPES.register("player_state", () ->
            AttachmentType.builder(PlayerStateAttachment::new)
                    .sync(new StateSyncHandler())
                    .build()
    );
}
