package io.github.moosyu.helpers;

import io.github.moosyu.attachments.PlayerStateAttachment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

// warning (for me): DO NOT FUCK UP THE ORDERING OF THE DATA IT WILL NOT FIGURE IT OUT ITSELF!!!
public class StatsSyncHandler implements AttachmentSyncHandler<PlayerStateAttachment> {
    @Override
    public void write(RegistryFriendlyByteBuf buf, PlayerStateAttachment attachment, boolean initialSync) {
        int statIndex = attachment.getLastUpdatedStat();

        boolean fullSync = initialSync || statIndex < 0;

        buf.writeBoolean(fullSync);

        if (fullSync) {
            double[] stats = attachment.getStats();
            buf.writeInt(stats.length);
            for (double stat : stats) {
                buf.writeDouble(stat);
            }
        } else {
            double value = attachment.getCurrentStatByIndex(statIndex);
            buf.writeInt(statIndex);
            buf.writeDouble(value);
        }
    }

    @Override
    public @Nullable PlayerStateAttachment read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable PlayerStateAttachment previousValue) {
        boolean fullSync = buf.readBoolean();

        if (fullSync) {
            PlayerStateAttachment attachment = previousValue != null ? previousValue : new PlayerStateAttachment();

            int length = buf.readInt();
            double[] stats = new double[length];

            for (int i = 0; i < length; i++) {
                stats[i] = buf.readDouble();
            }

            attachment.setStats(stats);
            return attachment;
        } else {
            int statIndex = buf.readInt();
            double value = buf.readDouble();

            if (previousValue == null) {
                previousValue = new PlayerStateAttachment(); // safety
            }

            previousValue.setCurrentStatByIndex(statIndex, value);
            return previousValue;
        }
    }
    @Override
    public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer player) {
        return holder == player;
    }
}
