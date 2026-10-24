package io.github.moosyu.data.fishing;

import java.util.Optional;

public interface FishingEntry {
    Optional<FishingCondition> condition();
    double weight();
    Optional<Integer> fishingLevelRequirement();
}
