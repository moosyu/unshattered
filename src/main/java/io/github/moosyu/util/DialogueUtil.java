package io.github.moosyu.util;

import net.minecraft.resources.Identifier;

import static io.github.moosyu.Unshattered.MODID;

public final class DialogueUtil {
    public static Identifier createDialogueNodeIdentifier(String dialogueInitiatorName, String dialogueID) {
        return Identifier.fromNamespaceAndPath(MODID, dialogueInitiatorName + "/" + dialogueID);
    }
}
