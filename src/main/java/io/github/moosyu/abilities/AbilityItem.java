package io.github.moosyu.abilities;

import java.util.List;
import java.util.function.Supplier;

public interface AbilityItem {
    List<? extends Supplier<? extends Ability>> abilities();
}