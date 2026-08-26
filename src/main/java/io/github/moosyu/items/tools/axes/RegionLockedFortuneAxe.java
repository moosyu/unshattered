package io.github.moosyu.items.tools.axes;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.items.UnshatteredInstantPassiveAbilityItem;
import io.github.moosyu.util.AbilityUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static io.github.moosyu.Unshattered.MODID;

// may make this more general one day but im really running low on time at the moment so just foraging fortune rn ig
public class RegionLockedFortuneAxe extends UnshatteredAxeTool implements UnshatteredInstantPassiveAbilityItem {
    private final ResourceKey<Region> region;
    private final Identifier abilityIdentifier;
    private final float foragingFortuneAmount;

    public RegionLockedFortuneAxe(Properties properties, float foragingFortuneAmount, float sweepAmount, float miningSpeed, ResourceKey<Region> region, Identifier abilityIdentifier) {
        this.foragingFortuneAmount = foragingFortuneAmount;
        this.region = region;
        this.abilityIdentifier = abilityIdentifier;

        super(properties.component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(abilityIdentifier, 0, 0, 0, true))
                        .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.SWEEP.holder,
                                new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "region_locked_sweep"), sweepAmount, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND).build()
                ),
                miningSpeed
        );
    }

    @Override
    public void onAbilityTriggered(Player player, LivingEntity target) {
        AbilityUtils.getAttributeInstance(player, UnshatteredAttributeValues.FORAGING_FORTUNE.holder).ifPresent(attribute ->
                attribute.addTransientModifier(new AttributeModifier(abilityIdentifier, foragingFortuneAmount, AttributeModifier.Operation.ADD_VALUE))
        );
    }

    @Override
    public void onAbilityFinished(Player player, LivingEntity target) {
        AbilityUtils.getAttributeInstance(player, UnshatteredAttributeValues.FORAGING_FORTUNE.holder).ifPresent(attribute ->
                attribute.removeModifier(abilityIdentifier)
        );
    }

    @Override
    public boolean abilityConditionsMet(Player player, LivingEntity target) {
        return player.getData(UnshatteredAttachments.PLAYER_REGION.get()).regionKey() == region;
    }
}
