package io.github.moosyu.abilities;

import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.damage.DamageUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;

public enum AbilityTriggerType {
    /**
     * if the ability needs to stay active until its conditions are no longer met (checked every tick).
     */
    ONGOING,
    /**
     * for abilities that need to be rerun every tick
     */
    TICKED,
    /**
     * checked in {@link io.github.moosyu.events.LivingIncomingDamageHandler} if the one being damaged in a player, both onAbilityTriggered and onAbilityFinished go off one after another
     */
    ENTITY_DEATH,
    /**
     * runs during TreeBreakInstance's finish with onAbilityTriggered firing before exp is tallied and blocks added to inventory and onAbilityFinished right at the end
     */
    TREE_BREAK_INSTANCE_FINISH,
    /**
     * runs in {@link DamageUtils#playerDealDamage(Player, LivingEntity, ItemTypes, double, boolean)} (when the player has dealt damage to a target) with onAbilityTriggered at the start (before damage calculations) and runs onAbilityFinished at the end, or if CANCEL_EVENT is a result, before the event is returned
     */
    PLAYER_DEAL_DAMAGE,
    /**
     * runs in {@link DamageUtils#damagePlayer(Player, double, ServerLevel, Component, boolean)} (when the player is about to/has received damage). onAbilityTriggered runs at the start and can cancel the event.
     */
    PLAYER_TAKE_DAMAGE,
    /**
     * runs in {@link io.github.moosyu.events.LivingIncomingDamageHandler} if the damage is being taken by a player. onAbilityTriggered runs at the start and the event can be cancelled.
     */
    PLAYER_INCOMING_DAMAGE,
    /**
     * runs in {@link io.github.moosyu.events.ClientInputUpdateHandler#onInputUpdate(MovementInputUpdateEvent)}. cannot be used to cancel sneaking and onAbilityTriggered and onAbilityFinished fire at the same time
     */
    PLAYER_STARTED_SNEAKING
    }
