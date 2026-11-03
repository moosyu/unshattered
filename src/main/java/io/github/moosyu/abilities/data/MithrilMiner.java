package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.datagen.UnshatteredBlockTagsProvider;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public class MithrilMiner extends Ability {
    private final float speedBoost;

    public MithrilMiner(float speedBoost) {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);

        this.speedBoost = speedBoost;
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        Optional<Player> optionalPlayer = context.get(AbilityContextKey.PLAYER);
        return optionalPlayer.map(player ->
                player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).getLookedAtState(player).is(UnshatteredBlockTagsProvider.MITHRIL_BLOCKS)
        ).orElse(false);
    }

    @Override
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        return List.of(new UnshatteredUtils.AttributeGroup(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(id(), speedBoost, AttributeModifier.Operation.ADD_VALUE)));
    }
}
