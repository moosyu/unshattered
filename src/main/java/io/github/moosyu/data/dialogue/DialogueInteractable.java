package io.github.moosyu.data.dialogue;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public interface DialogueInteractable {
    /**
     * get the name of the thing triggering the dialogue
     * @return the name of whatever is having dialgoue triggered
     */
    Component getDialogueHaverName();

    /**
     * code to run when dialogue is triggered
     * @param player the player triggering the dialogue
     */
    void onDialogueTriggered(Player player);

    /**
     * check if the player can trigger the dialogue
     * @param player the player attempting to trigger the dialogue
     * @return true if the conditions are met
     */
    boolean dialogueConditionsMet(Player player);
}
