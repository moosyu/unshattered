package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

// trying out something new with this but i dont really think it's gonna work out
// barely speeds up the process
public interface UnshatteredPacketBase extends CustomPacketPayload {
    String typeIdentifierPath();

    static <T extends CustomPacketPayload> Type<T> type(String path) {
        return new Type<>(UnshatteredUtils.getUnshatteredIdentifier(path));
    }

    @Override
    default @NonNull Type<? extends CustomPacketPayload> type() {
        return UnshatteredPacketBase.type(typeIdentifierPath());
    }
}