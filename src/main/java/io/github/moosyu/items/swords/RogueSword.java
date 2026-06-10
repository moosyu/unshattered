package io.github.moosyu.items.swords;

import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.ToolAbility;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.helpers.CheckItemRequirementHelper;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.items.UnshatteredSword;
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

import java.util.function.Consumer;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RogueSword extends UnshatteredSword {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "rogue_sword_speed_boost");
    private static final ToolAbility SPEED_BOOST_ABILITY = new ToolAbility("speed_boost",50, 100, 600, false);

    public RogueSword(Properties properties) {
        super(properties.component(DataComponentRegistry.ITEM_ABILITY.get(), SPEED_BOOST_ABILITY).attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributes.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "rogue_sword_damage"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.FAIL;
        AttributeInstance movementSpeedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        PlayerAbilityEffectsAttachment playerAbilities = player.getData(AttachmentRegistry.PLAYER_ABILITIES.get());
        if (player.getCooldowns().isOnCooldown(this.getDefaultInstance()) || !CheckItemRequirementHelper.passesManaCheck(player, SPEED_BOOST_ABILITY.manaCost()) || movementSpeedAttribute == null) return InteractionResult.FAIL;
        if (playerAbilities.hasEffect(ABILITY_IDENTIFIER)) {
            playerAbilities.setEffectExpiryTime(ABILITY_IDENTIFIER, SPEED_BOOST_ABILITY.duration(), level, this::onSpeedBoostExpire);
        } else {
            playerAbilities.addEffect(ABILITY_IDENTIFIER, SPEED_BOOST_ABILITY.duration(), level, this::onSpeedBoostExpire);
            movementSpeedAttribute.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, 0.05, AttributeModifier.Operation.ADD_VALUE));
        }
        if (!player.isCreative()) player.getData(AttachmentRegistry.PLAYER_STATE.get()).removeCurrentStat(PlayerStateAttachment.Stat.MANA, SPEED_BOOST_ABILITY.manaCost());
        player.getCooldowns().addCooldown(this.getDefaultInstance(), SPEED_BOOST_ABILITY.cooldown());
        return InteractionResult.PASS;
    }

    private void onSpeedBoostExpire(Player player) {
        if (player.level().isClientSide()) return;
        AttributeInstance movementSpeedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeedAttribute != null) movementSpeedAttribute.removeModifier(ABILITY_IDENTIFIER);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        PlayerAbilityEffectsAttachment abilities = player.getData(AttachmentRegistry.PLAYER_ABILITIES.get());
        if (!abilities.hasAnyEffect() || level.isClientSide()) return;
        abilities.tickEffects(level, player);
    }
}
