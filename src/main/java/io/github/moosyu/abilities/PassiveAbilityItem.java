package io.github.moosyu.abilities;

import java.util.Optional;
import java.util.Set;

// only has code to make this do stuff if its for killing an enemy or during tree sweep, more will be added when required
// not really passive as it only changes something the instant that the ability is to be triggered not always
public interface PassiveAbilityItem {
    /**
     * runs when a passive ability should be fired off
     * @param context ability context
     */
    void onAbilityTriggered(AbilityContext context);

    /**
     * runs when a passive ability should be ended, should be used to reset things like attribute modifiers. do note for cleanup i fire this WITHOUT checking whether
     * onAbilityTriggered was successful or abilityConditionsMet are currently true so if that's going to be a problem it should be guarded against.
     * @param context ability context
     */
    void onAbilityFinished(AbilityContext context);

    /**
     * @param context ability context
     * @return whether the conditions were met to run the passive effect. for ongoing abilities especially this needs to be really quick or stuff will get wild, if it's a talisman it shouldn't check whether it's in a talisman bag as the ability would only be active if it was.
     */
    boolean abilityConditionsMet(AbilityContext context);

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
