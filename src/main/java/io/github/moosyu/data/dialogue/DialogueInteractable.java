package io.github.moosyu.data.dialogue;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attachments.PlayerDialogueFlagsAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.packets.OpenDialoguePacket;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public interface DialogueInteractable {
    /**
     * @return the name of whatever is triggering the dialgoue
     */
    Component getInteractableName();

    /**
     * @return the origin trees for possible dialogue
     */
    List<DialogueTreeOrigin> getDialogueTreeOrigins(RegistryAccess registryAccess);

    /**
     * code to run when dialogue is triggered
     * @param player the player triggering the dialogue
     */
    default void onDialogueTriggered(Player player) {
        if (!player.level().isClientSide()) {
            PlayerDialogueFlagsAttachment playerDialogueFlagsAttachment = player.getData(UnshatteredAttachments.PLAYER_DIALOGUE_FLAGS);
            DialogueTreeOrigin chosenOrigin = null;
            for (DialogueTreeOrigin dialogueTreeOrigin : getDialogueTreeOrigins(player.registryAccess())) {
                if (playerDialogueFlagsAttachment.hasAllFlags(dialogueTreeOrigin.requiredFlags())
                        && (chosenOrigin == null || dialogueTreeOrigin.priority() > chosenOrigin.priority())) {
                    chosenOrigin = dialogueTreeOrigin;
                }
            }

            if (chosenOrigin == null) {
                Unshattered.LOGGER.error("failed to find a dialogue to trigger for {}", getInteractableName());
                return;
            }

            playerDialogueFlagsAttachment.addFlags(chosenOrigin.setFlags());
            PacketDistributor.sendToPlayer((ServerPlayer) player, new OpenDialoguePacket(getInteractableName(), chosenOrigin.dialogueNode()));
        }
    }
}
