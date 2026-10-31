package io.github.moosyu.abilities;

import io.github.moosyu.Unshattered;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class AbilityScope implements AutoCloseable {
    private record Triggered(Ability ability, @Nullable ItemStack source) {}

    private static final AbilityScope EMPTY = new AbilityScope(List.of(), null, new AbilityResults());

    private final List<Triggered> triggeredAbilities;
    private final @Nullable AbilityContext context;
    public final AbilityResults results;
    private boolean closed;

    private AbilityScope(List<Triggered> triggeredAbilities, @Nullable AbilityContext context, AbilityResults results) {
        this.triggeredAbilities = triggeredAbilities;
        this.context = context;
        this.results = results;
    }

    public static AbilityScope open(ServerPlayer player, AbilityTriggerType type, AbilityContext context) {
        PlayerAbilityEffectsAttachment attachment = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());

        List<Ability> abilities = attachment.triggerTypeAbilities.getOrDefault(type, List.of());
        if (abilities.isEmpty()) {
            return EMPTY;
        }

        List<Triggered> triggered = null;
        AbilityResults results = new AbilityResults();

        for (Ability ability : abilities) {
            ItemStack source = attachment.abilityItemStacks.get(ability);
            if (source != null) {
                context.add(AbilityContextKey.ITEM_STACK, source);
            }

            if (!ability.conditionsMet(context)) {
                continue;
            }

            if (triggered == null) {
                triggered = new ArrayList<>(abilities.size());
                results = new AbilityResults();
            }

            triggered.add(new Triggered(ability, source));
            ability.onTrigger(context);
            results.merge(ability.abilityResults);
        }

        if (triggered == null) {
            return EMPTY;
        }
        return new AbilityScope(triggered, context, results);
    }

    public static AbilityResults fire(ServerPlayer player, AbilityTriggerType type, AbilityContext context) {
        try (AbilityScope scope = open(player, type, context)) {
            return scope.results;
        }
    }

    @Override
    public void close() {
        if (closed || context == null) {
            return;
        }

        closed = true;

        for (Triggered triggered : triggeredAbilities) {
            if (triggered.source() != null) {
                context.add(AbilityContextKey.ITEM_STACK, triggered.source());
            }

            try {
                triggered.ability().onFinish(context);
            } catch (RuntimeException exception) {
                Unshattered.LOGGER.error("onFinish failed for {}", triggered.ability().identifier(), exception);
            }
        }
    }
}