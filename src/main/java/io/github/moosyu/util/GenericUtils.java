package io.github.moosyu.util;

import net.minecraft.resources.Identifier;

import static io.github.moosyu.Unshattered.MODID;

public class GenericUtils {
    /**
     * @param path identifier path
     * @return an identifier with unshattered's modid as the namespace (like withDefaultNamespace)
     */
    public static Identifier getUnshatteredIdentifier(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}