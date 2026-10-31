package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.packets.ClientsidePlayerSoundEffectPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;

public class WoolenBarrier extends Ability {
    public WoolenBarrier() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_INCOMING_DAMAGE, new AbilityResults().add(AbilityResult.CANCEL_EVENT, true));
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        // 2%
        return context.get(AbilityContextKey.PLAYER).map(player -> player.getRandom().nextInt(50) == 0).orElse(false);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER)
                .ifPresent(player -> PacketDistributor.sendToPlayer(player,
                        new ClientsidePlayerSoundEffectPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOOL_PLACE),
                                1.4f
                        )
                ));
    }
}
