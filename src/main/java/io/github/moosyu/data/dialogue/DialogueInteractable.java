package io.github.moosyu.data.dialogue;

import io.github.moosyu.Unshattered;
import io.github.moosyu.data.attachments.PlayerFlagsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.packets.OpenDialoguePacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface DialogueInteractable {
    /**
     * @return identifier used for dialogue tree
     */
    Identifier getInteractableIdentifier();

    /**
     * @return the name of whatever is triggering the dialgoue
     */
    Component getInteractableName();

    /**
     * @return the origin trees for possible dialogue
     */
    default @Nullable DialogueTree getDialogueTree(RegistryAccess registryAccess) {
        Optional<Registry<DialogueTree>> dialogueTreeRegistry = registryAccess.lookup(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY);
        return dialogueTreeRegistry.map(dialogueTree -> dialogueTree.getValue(getInteractableIdentifier())).orElse(null);
    }

    /**
     * code to run when dialogue is triggered
     * @param player the player triggering the dialogue
     */
    default void onDialogueTriggered(Player player) {
        if (!player.level().isClientSide()) {
            PlayerFlagsAttachment playerFlagsAttachment = player.getData(UnshatteredAttachments.PLAYER_FLAGS);
            DialogueTree dialogueTree = getDialogueTree(player.registryAccess());
            if (dialogueTree == null) {
                Unshattered.LOGGER.error("dialogue tree of {} is null (did you datagen?)", getInteractableName().getString());
                return;
            }

            DialogueTreeOrigin chosenOrigin = null;
            for (DialogueTreeOrigin dialogueTreeOrigin : dialogueTree.dialogueTreeOrigins()) {
                if ((dialogueTreeOrigin.dialogueFlagRequirements().isEmpty()
                        || dialogueTreeOrigin.dialogueFlagRequirements().get().isSatisfied(playerFlagsAttachment))
                        && (chosenOrigin == null || dialogueTreeOrigin.priority() > chosenOrigin.priority())
                ) {
                    chosenOrigin = dialogueTreeOrigin;
                }
            }

            if (chosenOrigin == null) {
                Unshattered.LOGGER.error("failed to find a dialogue to trigger for {}", getInteractableName().getString());
                return;
            }

            playerFlagsAttachment.addFlagsToQueue(chosenOrigin.setFlags());
            PacketDistributor.sendToPlayer((ServerPlayer) player, new OpenDialoguePacket(getInteractableName(), chosenOrigin.dialogueNode()));
        } else {
            player.swing(InteractionHand.MAIN_HAND);
        }
    }
}
