package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public class TitaniumFanatic extends Ability {
    private final int fortuneBoost;

    public TitaniumFanatic(int fortuneBoost) {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);

        this.fortuneBoost = fortuneBoost;
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        Optional<Player> optionalPlayer = context.get(AbilityContextKey.PLAYER);
        return optionalPlayer.map(player ->
                player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).getLookedAtState(player) == UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK.get().defaultBlockState()
        ).orElse(false);
    }

    @Override
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        return List.of(new UnshatteredUtils.AttributeGroup(UnshatteredAttributeValues.MINING_FORTUNE.holder, new AttributeModifier(id(), fortuneBoost, AttributeModifier.Operation.ADD_VALUE)));
    }
}
