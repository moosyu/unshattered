package io.github.moosyu.abilities;

import java.util.Optional;
import java.util.Set;

public interface PassiveAbilityItem extends AbilityItem {
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
}
