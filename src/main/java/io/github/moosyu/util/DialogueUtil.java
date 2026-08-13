package io.github.moosyu.util;

import io.github.moosyu.data.dialogue.DialogueTreeOrigin;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;

import java.util.Objects;

import static io.github.moosyu.Unshattered.MODID;

public final class DialogueUtil {
    public static Identifier createDialogueNodeIdentifier(String dialogueInitiatorName, String dialogueID) {
        return Identifier.fromNamespaceAndPath(MODID, dialogueInitiatorName + "/" + dialogueID);
    }

    public static DialogueTreeOrigin getDialogueTreeOriginObject(RegistryAccess registryAccess, Identifier getDialogueTreeOriginIdentifier) {
        return Objects.requireNonNull(registryAccess.lookupOrThrow(DataPackRegistryHandler.DIALOGUE_TREE_ORIGIN_REGISTRY_KEY).getValue(getDialogueTreeOriginIdentifier));
    }
}
