package io.github.moosyu.abilities;

import java.util.Optional;
import java.util.Set;

public interface AbilityItem {
    /**
     * @return set containing types for where this ability can be checked to run assuming it meets ability conditions. see {@link  AbilityTriggerType} for more specifics.
     */
    Set<AbilityTriggerType> triggerTypes();

    /**
     * @return for abilities that need results to be handled, even if a result is added make sure it's linked up to the corresponding event because something i forget lol. set to ? even though generally {@link AbilityTriggerResult} should be used as sometimes you might need a number or something who knows.
     */
    default Optional<?> triggerResult() {
        return Optional.empty();
    }
}
