package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.*;
import io.github.moosyu.packets.ClientsidePlayerSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;
import java.util.Set;

public class RainbowYarnTalisman extends Item implements PassiveAbilityItem {
    public static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("woolen_barrier");

    public RainbowYarnTalisman(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER)
                .ifPresent(player -> PacketDistributor.sendToPlayer(player,
                        new ClientsidePlayerSoundEffectPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOOL_PLACE),
                                1.4f
                        )
                ));
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        // 2%
        return context.get(AbilityContextKey.PLAYER).map(player -> player.getRandom().nextInt(50) == 0).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_INCOMING_DAMAGE);
    }

    @Override
    public Optional<AbilityTriggerResult> triggerResult() {
        return Optional.of(AbilityTriggerResult.CANCEL_EVENT);
    }
}
