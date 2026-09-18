package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.packets.ClientsidePlayerSoundEffectPacket;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;
import java.util.Set;

public class DwarfTurtleTalisman extends TalismanItem implements PassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("turtle_stability");

    public DwarfTurtleTalisman(Properties properties) {
        super(properties.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.EPIC)
                .component(UnshatteredDataComponents.SELL_VALUE, 250000)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER,
                        0,
                        0,
                        0,
                        true)
                )
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER)
                .ifPresent(player -> {
                    player.getData(UnshatteredAttachments.PLAYER_STATE).setCancelledKnockback(true);
                    player.syncData(UnshatteredAttachments.PLAYER_STATE);
                });
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {}

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER).map(player -> !player.getData(UnshatteredAttachments.PLAYER_STATE.get()).isKnockbackCancelled()).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.ONGOING);
    }
}
