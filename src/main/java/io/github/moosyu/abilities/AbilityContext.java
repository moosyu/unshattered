package io.github.moosyu.abilities;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AbilityContext {
    private final Map<AbilityContextKey<?>, Object> data = new HashMap<>();

    public <T> AbilityContext add(AbilityContextKey<T> key, T value) {
        data.put(key, value);
        return this;
    }

    public <T> Optional<T> get(AbilityContextKey<T> key) {
        return Optional.ofNullable(key.type().cast(data.get(key)));
    }

    public Optional<ServerPlayer> getServerPlayer() {
        return get(AbilityContextKey.PLAYER)
                .filter(ServerPlayer.class::isInstance)
                .map(ServerPlayer.class::cast);
    }
}