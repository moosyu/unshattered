package io.github.moosyu.data.fishing;

import java.util.Optional;

public interface FishingEntry {
    Optional<FishingConditions> condition();
    double weight();
    Optional<Integer> fishingLevelRequirement();
}
