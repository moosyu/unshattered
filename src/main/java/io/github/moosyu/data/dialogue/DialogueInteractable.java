package io.github.moosyu.data.dialogue;

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
            // todo: change this once flags are set up
            DialogueTreeOrigin chosenOrigin = getDialogueTreeOrigins(player.registryAccess()).getFirst();
//            for (DialogueTreeOrigin dialogueTreeOrigin : getDialogueTreeOrigins()) {
//
//            }
            PacketDistributor.sendToPlayer((ServerPlayer) player, new OpenDialoguePacket(getInteractableName(), chosenOrigin.dialogueNode()));
        }
    }

    /**
     * check if the player can trigger the dialogue
     * @param player the player attempting to trigger the dialogue
     * @return true if the conditions are met
     */
    boolean dialogueConditionsMet(Player player);
}
