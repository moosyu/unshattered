package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.damage.DamageUtils;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.packets.ClientsidePlayerSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.network.PacketDistributor;

public class PainToPower extends Ability {
    public PainToPower() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_STARTED_SNEAKING);
    }

    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance ferocity = player.getAttribute(UnshatteredAttributeValues.FEROCITY.holder);
            AttributeInstance health = player.getAttribute(UnshatteredAttributeValues.MAX_HEALTH.holder);

            if (ferocity == null || health == null) {
                return;
            }

            PacketDistributor.sendToPlayer(player, new ClientsidePlayerSoundEffectPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.TRIDENT_HIT), 1.0f));

            PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
            if (abilities.activeEffects.containsKey(identifier())) {
                AttributeModifier prevModifier = ferocity.getModifier(identifier());

                if (prevModifier != null) {
                    ferocity.removeModifier(identifier());
                    ferocity.addTransientModifier(new AttributeModifier(identifier(), prevModifier.amount() + 20, AttributeModifier.Operation.ADD_VALUE));
                }
            } else {
                ferocity.addTransientModifier(new AttributeModifier(identifier(), 20, AttributeModifier.Operation.ADD_VALUE));
            }

            abilities.addActiveEffect(this, 600, player.level());

            if (DamageUtils.damagePlayer(player,
                    (health.getValue() * 0.3),
                    Component.literal("☠ " + player.getName().getString() + " had their soul consumed by the blood chalice!"),
                    true,
                    new DamageSource(player.registryAccess()
                            .lookupOrThrow(Registries.DAMAGE_TYPE)
                            .getOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, UnshatteredUtils.getUnshatteredIdentifier("blood_chalice")))
                    )
            )) {
                ferocity.removeModifier(identifier());
                abilities.removeActiveEffect(identifier(), player);
            }
        });
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance ferocity = player.getAttribute(UnshatteredAttributeValues.FEROCITY.holder);
            if (ferocity != null) {
                ferocity.removeModifier(identifier());
            }
        });
    }
}
