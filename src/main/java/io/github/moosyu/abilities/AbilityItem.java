package io.github.moosyu.abilities;

import java.util.Set;

public interface AbilityItem {
    /**
     * @return set containing types for where this ability can be checked to run assuming it meets ability conditions. see {@link  AbilityTriggerType} for more specifics.
     */
    Set<AbilityTriggerType> triggerTypes();
}
