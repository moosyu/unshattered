package io.github.moosyu.data.attachments;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class PlayerAbilityEffectsAttachment {
    public record ActiveEffectEntry(long expiryTime, @Nullable Consumer<AbilityContext> onExpire) {}

    public final Map<Identifier, ActiveEffectEntry> activeEffects = new HashMap<>();

    public Map<Ability, ItemStack> abilityItemStacks = Map.of();
    public Map<AbilityTriggerType, List<Ability>> triggerTypeAbilities = Map.of();
    private List<Ability> tickingAbilities = List.of();
    private final Set<Ability> activeAbilities = Collections.newSetFromMap(new IdentityHashMap<>());

    private long lookCacheTick = -1;
    private BlockState lookedAtState = Blocks.AIR.defaultBlockState();

    public void addActiveEffect(Ability ability, long abilityLength, Level level) {
        addActiveEffect(ability.id(), abilityLength, level, ability::deactivate);
    }

    /**
     * @param abilityIdentifier identifier for the ability
     * @param abilityLength length of the ability in ticks
     * @param level server level
     * @param onExpire consumer to run when effect expires (aside from being removed from active effects)
     */
    public void addActiveEffect(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<AbilityContext> onExpire) {
        activeEffects.put(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire));
    }

    /**
     * cancel an active effect early (expatriation code runs)
     */
    public void removeActiveEffect(Identifier abilityIdentifier, ServerPlayer player) {
        ActiveEffectEntry entry = activeEffects.remove(abilityIdentifier);
        if (entry == null) {
            return;
        }

        if (entry.onExpire() != null) {
            entry.onExpire().accept(new AbilityContext().add(AbilityContextKey.PLAYER, player));
        }
    }

    /**
     * @return whether an effects expiry time has passed, if it's 0 it will return false as if expiry time is 0 the effect should be removed manually
     */
    private static boolean isFinished(ActiveEffectEntry entry, Level level) {
        return entry.expiryTime() != 0 && level.getGameTime() > entry.expiryTime();
    }

    public long expiryTimeTicks(Identifier abilityIdentifier) {
        ActiveEffectEntry entry = activeEffects.get(abilityIdentifier);
        if (entry == null) {
            throw new IllegalArgumentException("no effect for " + abilityIdentifier);
        }
        return entry.expiryTime();
    }

    /**
     * rescans everything that can provide abilities (equipment, equipment attachments, talisman bag etc),
     * finishes abilities that went away and starts ongoing abilities that appeared.
     * call on login and whenever equipment, attachments or the talisman bag change.
     */
    public void refresh(ServerPlayer player) {
        // needs to be populated with itemstacks from every possible location where ability items could be stored
        Map<Ability, ItemStack> nextSources = new HashMap<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = player.getItemBySlot(slot);
            if (itemStack.isEmpty()) {
                continue;
            }

            ItemAttachments attachments = itemStack.get(UnshatteredDataComponents.ITEM_ATTACHMENTS);
            if (attachments != null) {
                attachments.attachments().forEach((_, attachment) -> attemptAddingAbilitySource(nextSources, attachment));
            }

            attemptAddingAbilitySource(nextSources, itemStack);
        }

        player.getData(UnshatteredAttachments.PLAYER_TALISMAN_STORAGE.get()).forEach(itemStack -> {
            if (UnshatteredUtils.getItemType(itemStack) == ItemType.TALISMAN) {
                attemptAddingAbilitySource(nextSources, itemStack);
            }
        });

        for (Ability oldAbility : abilityItemStacks.keySet()) {
            AbilityContext oldContext = new AbilityContext().add(AbilityContextKey.PLAYER, player).add(AbilityContextKey.ITEM_STACK, abilityItemStacks.get(oldAbility));
            if (!nextSources.containsKey(oldAbility) && activeAbilities.remove(oldAbility)) {
                oldAbility.deactivate(oldContext);
            }
        }

        EnumMap<AbilityTriggerType, List<Ability>> nextTriggerTypeAbilities = new EnumMap<>(AbilityTriggerType.class);
        List<Ability> tickList = new ArrayList<>();
        for (Ability ability : nextSources.keySet()) {
            if (ability.isTicking()) {
                tickList.add(ability);
            }

            AbilityTriggerType trigger = ability.trigger;
            if (trigger != null && trigger != AbilityTriggerType.ONGOING && trigger != AbilityTriggerType.TICKED) {
                nextTriggerTypeAbilities.computeIfAbsent(trigger, _ -> new ArrayList<>()).add(ability);
            }
        }

        nextTriggerTypeAbilities.replaceAll((_, list) -> List.copyOf(list));

        abilityItemStacks = nextSources;
        triggerTypeAbilities = nextTriggerTypeAbilities;
        // supposedly i should always be using immutable lists when i can even if im not really modifying it
        // though that is kind of the vibe we're on with this class right now
        tickingAbilities = List.copyOf(tickList);

        for (Ability ability : tickList) {
            AbilityContext tickedContext = new AbilityContext().add(AbilityContextKey.PLAYER, player).add(AbilityContextKey.ITEM_STACK, abilityItemStacks.get(ability));
            if (!(ability.trigger == AbilityTriggerType.TICKED) && !activeAbilities.contains(ability) && ability.conditionsMet(tickedContext)) {
                ability.activate(tickedContext);
                activeAbilities.add(ability);
            }
        }
    }

    /**
     * adds an item to sources if it is in fact an ability item, adding all abilities of that item
     */
    private static void attemptAddingAbilitySource(Map<Ability, ItemStack> sources, ItemStack itemStack) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof AbilityItem item)) {
            return;
        }

        for (Supplier<? extends Ability> ability : item.abilities()) {
            sources.putIfAbsent(ability.get(), itemStack);
        }
    }

    /**
     * tick player active effects and ensure ongoing abilities are still having their conditions met
     */
    public void updateEffects(AbilityContext context, Level level) {
        List<Identifier> finished = new ArrayList<>();
        for (Map.Entry<Identifier, ActiveEffectEntry> entry : activeEffects.entrySet()) {
            if (isFinished(entry.getValue(), level)) {
                finished.add(entry.getKey());
            }
        }

        for (Identifier identifier : finished) {
            ActiveEffectEntry entry = activeEffects.remove(identifier);
            if (entry != null && entry.onExpire() != null) {
                entry.onExpire().accept(context);
            }
        }

        if (tickingAbilities.isEmpty()) {
            return;
        }

        for (Ability ability : tickingAbilities) {
            ItemStack source = abilityItemStacks.get(ability);
            if (source == null) continue;
            context.add(AbilityContextKey.ITEM_STACK, source);

            boolean conditionsMet = ability.conditionsMet(context);
            boolean wasActive = activeAbilities.contains(ability);

            if (conditionsMet) {
                if (ability.trigger == AbilityTriggerType.TICKED) {
                    ability.activate(context);
                    activeAbilities.add(ability);
                } else if (!wasActive) {
                    ability.activate(context);
                    activeAbilities.add(ability);
                } else {
                    ability.applyBonuses(context);
                }
            } else if (wasActive) {
                ability.deactivate(context);
                activeAbilities.remove(ability);
            }
        }
    }

    /**
     * apply passive effects on joining, does a full rescan to make sure nothing terrible occurred
     */
    public void applyPassiveEffects(ServerPlayer player) {
        abilityItemStacks = Map.of();
        triggerTypeAbilities = Map.of();
        tickingAbilities = List.of();
        activeAbilities.clear();
        refresh(player);
    }

    /**
     * runs onFinish for active effects and triggered abilities, clearing the active effects map.
     * tracked sources are kept (they're rescanned on rejoin during applyPassiveEffects)
     */
    public void forceStopEffects(ServerPlayer player) {
        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, player);

        List<ActiveEffectEntry> effects = new ArrayList<>(activeEffects.values());
        activeEffects.clear();
        for (ActiveEffectEntry effect : effects) {
            if (effect.onExpire() != null) {
                effect.onExpire().accept(context);
            }
        }

        for (Ability ability : List.copyOf(activeAbilities)) {
            ability.deactivate(context);
        }

        activeAbilities.clear();
    }

    public BlockState getLookedAtState(Player player) {
        long now = player.level().getGameTime();
        if (now != lookCacheTick) {
            lookCacheTick = now;
            HitResult hit = player.pick(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), 0.0f, false);
            lookedAtState = hit instanceof BlockHitResult b && b.getType() == HitResult.Type.BLOCK
                    ? player.level().getBlockState(b.getBlockPos())
                    : Blocks.AIR.defaultBlockState();
        }
        return lookedAtState;
    }
}