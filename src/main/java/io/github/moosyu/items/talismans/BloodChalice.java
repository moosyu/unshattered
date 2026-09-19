package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.packets.ClientsidePlayerSoundEffectPacket;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;

public class BloodChalice extends TalismanItem implements PassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("pain_to_power");

    public BloodChalice(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER, 0, 0, 0, true))
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.EPIC)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 100000)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            PacketDistributor.sendToPlayer(player, new ClientsidePlayerSoundEffectPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.TRIDENT_HIT), 1.0f));
            PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
            AttributeInstance ferocity = player.getAttribute(UnshatteredAttributeValues.FEROCITY.holder);
            AttributeInstance health = player.getAttribute(UnshatteredAttributeValues.HEALTH.holder);

            if (ferocity == null || health == null) {
                return;
            }

            if (abilities.hasActiveEffect(ABILITY_IDENTIFIER)) {
                AttributeModifier prevModifier = ferocity.getModifier(ABILITY_IDENTIFIER);

                if (prevModifier != null) {
                    ferocity.removeModifier(ABILITY_IDENTIFIER);
                    ferocity.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, prevModifier.amount() + 20, AttributeModifier.Operation.ADD_VALUE));
                }
            } else {
                ferocity.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, 20, AttributeModifier.Operation.ADD_VALUE));
            }

            player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).addActiveEffect(ABILITY_IDENTIFIER,
                    600,
                    player.level(),
                    _ -> context.get(AbilityContextKey.PLAYER).ifPresent(_ -> ferocity.removeModifier(ABILITY_IDENTIFIER))
            );

            PlayerStateAttachment state = player.getData(UnshatteredAttachments.PLAYER_STATE);
            state.setStatValue(PlayerStateAttachment.Stat.HEALTH, state.getStatValue(PlayerStateAttachment.Stat.HEALTH) - (health.getValue() * 0.1), player);
        });
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {}

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return true;
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_STARTED_SNEAKING);
    }
}
