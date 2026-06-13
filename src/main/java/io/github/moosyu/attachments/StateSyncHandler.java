package io.github.moosyu.attachments;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public final class StateSyncHandler implements AttachmentSyncHandler<PlayerStateAttachment> {
    @Override
    public void write(@NonNull RegistryFriendlyByteBuf buf, PlayerStateAttachment attachment, boolean initialSync) {
        attachment.writeSync(buf, initialSync);
    }

    @Override
    public @NonNull PlayerStateAttachment read(@NonNull IAttachmentHolder holder, @NonNull RegistryFriendlyByteBuf buf, @Nullable PlayerStateAttachment previousValue) {
        return PlayerStateAttachment.readSync(buf, previousValue);
    }

    @Override
    public boolean sendToPlayer(@NonNull IAttachmentHolder holder, @NonNull ServerPlayer player) {
        return holder == player;
    }
}
