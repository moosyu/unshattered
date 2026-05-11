package io.github.moosyu.registers;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

public class AttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);
    public static final Supplier<AttachmentType<PlayerSkillsAttachment>> PLAYER_SKILLS = ATTACHMENTS.register("player_skills", () ->
            AttachmentType.builder(() -> new PlayerSkillsAttachment(0, 0, 0, 0, 0))
                    .serialize(PlayerSkillsAttachment.RECORD_CODEC)
                    .build()
    );
}
