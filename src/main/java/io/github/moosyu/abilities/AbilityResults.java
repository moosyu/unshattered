package io.github.moosyu.abilities;

import java.util.HashMap;
import java.util.Map;

public class AbilityResults {
    private final Map<AbilityResult<?>, Object> data = new HashMap<>();

    public <T> AbilityResults add(AbilityResult<T> key, T value) {
        data.merge(key, value, (a, b) -> key.combiner().apply(key.type().cast(a), key.type().cast(b)));
        return this;
    }

    public <T> T get(AbilityResult<T> key) {
        Object value = data.get(key);
        return value == null ? key.identity() : key.type().cast(value);
    }
}
