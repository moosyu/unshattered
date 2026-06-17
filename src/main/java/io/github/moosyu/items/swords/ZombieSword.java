package io.github.moosyu.items.swords;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.helpers.CheckItemRequirementHelper;
import io.github.moosyu.items.UnshatteredSword;
import io.github.moosyu.packets.DeathSoundEffectPacket;
import io.github.moosyu.rarities.RarityTypes;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ZombieSword extends UnshatteredSword {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "zombie_sword_instant_heal");
    private static final ItemAbility INSTANT_HEAL_ABILITY = new ItemAbility("instant_heal",70, 10, 0, false);

    public ZombieSword(Properties properties) {
        super(properties.component(DataComponentRegistry.ITEM_ABILITY.get(), INSTANT_HEAL_ABILITY).component(DataComponentRegistry.RARITY.get(), RarityTypes.RARE).component(DataComponentRegistry.ITEM_CHARGES.get(), new ItemCharges(4, 4, 300)).attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributes.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "zombie_sword_damage"), 100, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(UnshatteredAttributes.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "zombie_sword_strength"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(UnshatteredAttributes.MANA.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "zombie_sword_mana"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.FAIL;
        }
        AttributeInstance maxHealthAttribute = player.getAttribute(UnshatteredAttributes.HEALTH.holder);
        PlayerAbilityEffectsAttachment abilities = player.getData(AttachmentRegistry.PLAYER_ABILITIES.get());
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemCharges itemCharges = itemStack.get(DataComponentRegistry.ITEM_CHARGES.get());
        if (maxHealthAttribute == null || itemCharges == null) {
            Unshattered.LOGGER.error("maxHealthAttribute or itemCharges are null (from zombie sword)");
            return InteractionResult.FAIL;
        }
        PlayerStateAttachment playerState = player.getData(AttachmentRegistry.PLAYER_STATE.get());
        if (!player.isCreative()) {
            if (!CheckItemRequirementHelper.passesManaCheck(player, INSTANT_HEAL_ABILITY.manaCost())
                    || player.getCooldowns().isOnCooldown(itemStack)
                    || !CheckItemRequirementHelper.passesChargesCheck(player, itemCharges)) {
                return InteractionResult.FAIL;
            } else {
                itemStack.set(DataComponentRegistry.ITEM_CHARGES.get(), itemCharges.decrementCharges());
                player.getCooldowns().addCooldown(itemStack, INSTANT_HEAL_ABILITY.cooldown());
                if (!abilities.hasActiveEffect(ABILITY_IDENTIFIER)) {
                    abilities.addActiveEffect(ABILITY_IDENTIFIER, itemCharges.rechargeTime(), level, p -> onRecharge(p, itemStack), player.getItemBySlot(hand.asEquipmentSlot()));
                }
                playerState.removeCurrentStat(PlayerStateAttachment.Stat.MANA, INSTANT_HEAL_ABILITY.manaCost());
                player.syncData(AttachmentRegistry.PLAYER_STATE.get());
            }

            playerState.addCurrentStat(PlayerStateAttachment.Stat.HEALTH, 120 + (maxHealthAttribute.getValue() * 0.05), maxHealthAttribute.getValue());
            player.syncData(AttachmentRegistry.PLAYER_STATE.get());
        }

        PacketDistributor.sendToPlayer((ServerPlayer) player, new DeathSoundEffectPacket());
        return InteractionResult.PASS;
    }

    private void onRecharge(Player player, ItemStack itemStack) {
        Level level = player.level();
        if (level.isClientSide()) return;
        ItemCharges itemCharges = itemStack.get(DataComponentRegistry.ITEM_CHARGES.get());
        itemStack.set(DataComponentRegistry.ITEM_CHARGES.get(), itemCharges.incrementCharges());
    }

    // so that changing data doesnt do the re-equip animation
    @Override
    public boolean shouldCauseReequipAnimation(@NonNull ItemStack oldStack, @NonNull ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void inventoryTick(@NonNull ItemStack itemStack, @NonNull ServerLevel level, @NonNull Entity owner, EquipmentSlot slot) {
        if (owner instanceof Player player) {
            PlayerAbilityEffectsAttachment abilities = player.getData(AttachmentRegistry.PLAYER_ABILITIES.get());
            ItemCharges itemCharges = itemStack.get(DataComponentRegistry.ITEM_CHARGES.get());
            if (itemCharges.currentCharges() < itemCharges.maxCharges() && !abilities.hasActiveEffect(ABILITY_IDENTIFIER)) {
                abilities.addActiveEffect(ABILITY_IDENTIFIER, itemCharges.rechargeTime(), level, p -> onRecharge(p, itemStack), player.getItemBySlot(slot));
            }
        }
    }
}
