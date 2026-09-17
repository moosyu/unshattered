package io.github.moosyu.items.talismans;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public class RainbowYarnTalismanItem extends TalismanItem implements PassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("woolen_barrier", false);

    public RainbowYarnTalismanItem(Properties properties) {
        super(properties.component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER,
                        0,
                        0,
                        0,
                        true)
                )
        );
    }

    @Override
    public void onAbilityTriggered(ServerPlayer player, @Nullable LivingEntity target) {
        player.getData(UnshatteredAttachments.PLAYER_ABILITIES).addActiveEffect(ABILITY_IDENTIFIER, 600, player.level(), _ -> {});
    }

    @Override
    public void onAbilityFinished(ServerPlayer player, @Nullable LivingEntity target) {}

    @Override
    public boolean abilityConditionsMet(ServerPlayer player, @Nullable LivingEntity target) {
        // the cooldown has the same id as the ability, very smart but very dangerous
        return !player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).hasActiveEffect(ABILITY_IDENTIFIER);
    }

    @Override
    public boolean isOngoing() {
        return false;
    }
}
