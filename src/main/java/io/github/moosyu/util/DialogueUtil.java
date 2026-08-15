package io.github.moosyu.util;

import io.github.moosyu.data.dialogue.DialogueTree;
import io.github.moosyu.data.dialogue.DialogueTreeOrigin;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

import static io.github.moosyu.Unshattered.MODID;

public final class DialogueUtil {
    public static Identifier createDialogueTreeIdentifier(String dialogueInitiatorName, String dialogueTreeName) {
        return Identifier.fromNamespaceAndPath(MODID, dialogueInitiatorName + "/" + dialogueTreeName);
    }

    public static Identifier createDialogueNodeIdentifier(Identifier dialogueTreeIdentifier, String dialogueInitiatorName, @NonNull String dialogueNodeName) {
        return Identifier.fromNamespaceAndPath(MODID, dialogueTreeIdentifier.getPath() + "/" + dialogueInitiatorName.toLowerCase() + "_" + dialogueNodeName);
    }

    public static DialogueTree getDialogueTreeObject(RegistryAccess registryAccess, Identifier dialogueTreeIdentifier) {
        return Objects.requireNonNull(registryAccess.lookupOrThrow(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY).getValue(dialogueTreeIdentifier));
    }
}
