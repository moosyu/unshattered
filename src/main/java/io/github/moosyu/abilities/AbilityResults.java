package io.github.moosyu.abilities;

import java.util.HashMap;
import java.util.Map;

public class AbilityResults {
    private final Map<AbilityResult<?>, Object> data = new HashMap<>();

    public <T> AbilityResults add(AbilityResult<T> key, T value) {
        data.merge(key, value, (a, b) -> key.combiner().apply(key.type().cast(a), key.type().cast(b)));
        return this;
    }

    public void merge(AbilityResults other) {
        other.data.forEach(this::addRaw);
    }

    public <T> T get(AbilityResult<T> key) {
        Object value = data.get(key);
        return value == null ? key.defaultValue() : key.type().cast(value);
    }

    @SuppressWarnings("unchecked")
    private <T> void addRaw(AbilityResult<T> key, Object value) {
        add(key, (T) value);
    }
}
