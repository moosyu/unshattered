package io.github.moosyu.registers;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.PlayerStatsAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.NNO.MODID;
import static net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.ATTACHMENT_TYPES;

public class AttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(ATTACHMENT_TYPES, MODID);
    public static final Supplier<AttachmentType<PlayerSkillsAttachment>> PLAYER_SKILLS = ATTACHMENTS.register("player_skills", () ->
            AttachmentType.builder(() -> new PlayerSkillsAttachment(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f))
                    .serialize(PlayerSkillsAttachment.RECORD_CODEC)
                    .build()
    );

    public static final Supplier<AttachmentType<PlayerStatsAttachment>> PLAYER_STATS = ATTACHMENTS.register("player_stats", () ->
            AttachmentType.builder(PlayerStatsAttachment::new).build());
}
