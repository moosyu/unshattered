package io.github.moosyu.items.swords;

import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.attachments.PlayerAbilitiesAttachment;
import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.helpers.CheckItemRequirementHelper;
import io.github.moosyu.items.ItemTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RogueSword extends Item {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "rogue_sword_speed_boost");

    public RogueSword(Properties properties) {
        super(properties.stacksTo(1).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.SWORD).component(DataComponentRegistry.ITEM_ABILITY.get(), true).attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributes.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "rogue_sword_damage"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        final int ABILITY_LENGTH = 600;
        final int MANA_COST = 50;
        final int ABILITY_COOLDOWN = 100;

        if (level.isClientSide()) return InteractionResult.FAIL;
        AttributeInstance movementSpeedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        PlayerAbilitiesAttachment playerAbilities = player.getData(AttachmentRegistry.PLAYER_ABILITIES.get());
        if (player.getCooldowns().isOnCooldown(this.getDefaultInstance()) || !CheckItemRequirementHelper.passesManaCheck(player, MANA_COST) || movementSpeedAttribute == null) return InteractionResult.FAIL;
        if (playerAbilities.hasEffect(ABILITY_IDENTIFIER)) {
            playerAbilities.setEffectExpiryTime(ABILITY_IDENTIFIER, ABILITY_LENGTH, level);
        } else {
            playerAbilities.addEffect(ABILITY_IDENTIFIER, ABILITY_LENGTH, level);
            movementSpeedAttribute.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, 0.05, AttributeModifier.Operation.ADD_VALUE));
        }
        player.getData(AttachmentRegistry.PLAYER_STATE.get()).removeCurrentStat(PlayerStateAttachment.Stat.MANA, MANA_COST);
        player.getCooldowns().addCooldown(this.getDefaultInstance(), ABILITY_COOLDOWN);
        return InteractionResult.PASS;
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        PlayerAbilitiesAttachment abilities = player.getData(AttachmentRegistry.PLAYER_ABILITIES.get());
        if (abilities.effectFinished(ABILITY_IDENTIFIER, level)) {
            AttributeInstance movementSpeedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementSpeedAttribute != null) movementSpeedAttribute.removeModifier(ABILITY_IDENTIFIER);
            abilities.removeEffect(ABILITY_IDENTIFIER);
        }
    }
}
