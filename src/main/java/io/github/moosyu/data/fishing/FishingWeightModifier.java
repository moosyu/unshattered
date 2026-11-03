package io.github.moosyu.data.fishing;

import net.minecraft.resources.ResourceKey;

@FunctionalInterface
public interface FishingWeightModifier {
    FishingWeightModifier NONE = (_, weight, _) -> weight;

    /**
     * @param key the entry being weighted (ResourceKey of an Item or EntityType)
     * @param weight the current weight (with usual modifiers like fortune already applied)
     * @param position position in the table by raw weight as a fraction in [0, 1): 0.0 is the heaviest entry, approaching 1.0 for the lightest
     * @return the new weight
     */
    double apply(ResourceKey<?> key, double weight, double position);

    default FishingWeightModifier combine(FishingWeightModifier next) {
        return (key, weight, rank) -> next.apply(key, this.apply(key, weight, rank), rank);
    }

    static FishingWeightModifier boostKey(ResourceKey<?> target, double multiplier) {
        return (key, weight, _) -> key.equals(target) ? weight * multiplier : weight;
    }

    static FishingWeightModifier boostTopFraction(double fraction, double percent) {
        return (_, weight, rank) -> rank < fraction ? weight * (1.0 + percent) : weight;
    }
}