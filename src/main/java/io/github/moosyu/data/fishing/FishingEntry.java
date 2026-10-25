package io.github.moosyu.data.fishing;

import java.util.List;
import java.util.Optional;

public interface FishingEntry {
    Optional<List<FishingCondition>> conditions();
    double weight();
    Optional<Integer> fishingLevelRequirement();
}
