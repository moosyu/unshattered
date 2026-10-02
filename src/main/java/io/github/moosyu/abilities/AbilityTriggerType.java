package io.github.moosyu.abilities;

import io.github.moosyu.damage.DamageUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
     * runs in {@link DamageUtils#playerDealDamage(Player, LivingEntity, double, boolean, ItemStack, boolean)} (when the player has dealt damage to a target) with onAbilityTriggered at the start (before damage calculations) and runs onAbilityFinished at the end, or if CANCEL_EVENT is a result, before the event is returned
     */
    PLAYER_DEAL_DAMAGE,
    /**
     * runs in {@link DamageUtils#damagePlayer(Player, double, Component, boolean, DamageSource)} (Player, double, ServerLevel, Component, boolean, DamageSource)} (when the player is about to/has received damage). onAbilityTriggered runs at the start and can cancel the event.
     */
    PLAYER_TAKE_DAMAGE,
    /**
     * runs in {@link io.github.moosyu.events.LivingIncomingDamageHandler} if the damage is being taken by a player. onAbilityTriggered runs at the start and the event can be cancelled.
     */
    PLAYER_INCOMING_DAMAGE,
    /**
     * runs in {@link io.github.moosyu.events.ClientInputUpdateHandler#onInputUpdate(MovementInputUpdateEvent)}. cannot be used to cancel sneaking. onAbilityTriggered and onAbilityFinished fire at the same time.
     */
    PLAYER_STARTED_SNEAKING,
    /**
     * runs in {@link io.github.moosyu.mixins.ProjectileWeaponItemMixin#useAmmo(ItemStack, ItemStack, LivingEntity, boolean, CallbackInfoReturnable)}. can be used to cancel.
     */
    PLAYER_USE_PROJECTILE_WEAPON_AMMO,
    /**
     * runs in {@link io.github.moosyu.events.BlockBreakHandler} inside the mining branch, cannot be used to cancel.
     */
    PLAYER_BREAK_MINING_BLOCK,
    /**
     * runs in {@link io.github.moosyu.events.TreeSweepHandler#trySweep(Level, BlockPos, Player)}, added specifically for incremental items
     */
    PLAYER_BREAK_SWEEP_BLOCK,
    /**
     * runs in {@link io.github.moosyu.events.BlockBreakHandler} inside the mining branch, only for a trigger result to modify the powder
     */
    PLAYER_MODIFY_POWDER,
    /**
     * runs in {@link io.github.moosyu.items.tools.UnshatteredMiningToolBase#use(Level, Player, InteractionHand)}, only for a trigger result to modify cooldown
     */
    PLAYER_USE_MINING_ABILITY,
    /**
     * runs in {@link io.github.moosyu.events.LivingKnockBackHandler}, only tries for cancel
     */
    PLAYER_INCOMING_KNOCKBACK
}