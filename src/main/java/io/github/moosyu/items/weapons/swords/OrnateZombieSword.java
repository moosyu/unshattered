package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.Unshattered;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.util.CheckItemRequirement;
import io.github.moosyu.packets.ZombieSwordEffectsPacket;
import io.github.moosyu.rarities.UnshatteredRarities;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.DEDICATED_SERVER)
public class OrnateZombieSword extends UnshatteredSword {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "ornate_zombie_sword_instant_heal");
    private static final ItemAbility INSTANT_HEAL_ABILITY = new ItemAbility(ABILITY_IDENTIFIER,30, 10, 0, false);

    public OrnateZombieSword(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.ABILITY.get(), INSTANT_HEAL_ABILITY)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.EPIC)
                .component(UnshatteredDataComponents.CHARGES.get(), new ItemCharges(5, 5, 300))
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 600000)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "ornate_zombie_sword_damage"), 35, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "ornate_zombie_sword_strength"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(UnshatteredAttributeValues.MANA.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "ornate_zombie_sword_mana"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "ornate_zombie_sword_attack_speed"), -2.4f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()
                )
        );
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.FAIL;
        }
        AttributeInstance maxHealthAttribute = player.getAttribute(UnshatteredAttributeValues.HEALTH.holder);
        PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemCharges itemCharges = itemStack.get(UnshatteredDataComponents.CHARGES.get());
        if (maxHealthAttribute == null || itemCharges == null) {
            Unshattered.LOGGER.error("maxHealthAttribute or itemCharges are null (from ornate zombie sword)");
            return InteractionResult.FAIL;
        }
        PlayerStateAttachment playerState = player.getData(UnshatteredAttachments.PLAYER_STATE.get());
        if (!player.isCreative()) {
            if (!CheckItemRequirement.passesManaCheck(player, INSTANT_HEAL_ABILITY.manaCost())
                    || player.getCooldowns().isOnCooldown(itemStack)
                    || !CheckItemRequirement.passesChargesCheck(player, itemCharges, ABILITY_IDENTIFIER)) {
                return InteractionResult.FAIL;
            } else {
                itemStack.set(UnshatteredDataComponents.CHARGES.get(), itemCharges.decrementCharges());
                player.getCooldowns().addCooldown(itemStack, INSTANT_HEAL_ABILITY.cooldown());
                if (!abilities.hasActiveEffect(ABILITY_IDENTIFIER)) {
                    abilities.addActiveEffect(ABILITY_IDENTIFIER, itemCharges.rechargeTime(), level, p -> onRecharge(p, itemStack), player.getItemBySlot(hand.asEquipmentSlot()));
                }
                playerState.removeCurrentStat(PlayerStateAttachment.Stat.MANA, INSTANT_HEAL_ABILITY.manaCost(), player);
            }

            playerState.addCurrentStat(PlayerStateAttachment.Stat.HEALTH, 144 + (maxHealthAttribute.getValue() * 0.05), maxHealthAttribute.getValue(), player);
        }

        PacketDistributor.sendToPlayer((ServerPlayer) player, new ZombieSwordEffectsPacket());
        return InteractionResult.PASS;
    }

    private void onRecharge(Player player, ItemStack itemStack) {
        Level level = player.level();
        ItemCharges itemCharges = itemStack.get(UnshatteredDataComponents.CHARGES.get());
        if (level.isClientSide() || itemCharges == null) return;
        itemStack.set(UnshatteredDataComponents.CHARGES.get(), itemCharges.incrementCharges());
    }

    // so that changing data doesnt do the re-equip animation
    @Override
    public boolean shouldCauseReequipAnimation(@NonNull ItemStack oldStack, @NonNull ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void inventoryTick(@NonNull ItemStack itemStack, @NonNull ServerLevel level, @NonNull Entity owner, EquipmentSlot slot) {
        if (owner instanceof Player player) {
            PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
            ItemCharges itemCharges = itemStack.get(UnshatteredDataComponents.CHARGES.get());
            if (itemCharges.currentCharges() < itemCharges.maxCharges() && !abilities.hasActiveEffect(ABILITY_IDENTIFIER)) {
                abilities.addActiveEffect(ABILITY_IDENTIFIER, itemCharges.rechargeTime(), level, p -> onRecharge(p, itemStack), player.getItemBySlot(slot));
            }
        }
    }
}
