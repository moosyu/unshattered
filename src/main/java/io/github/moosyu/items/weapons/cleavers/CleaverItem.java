package io.github.moosyu.items.weapons.cleavers;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.Optional;
import java.util.Set;

import static io.github.moosyu.Unshattered.MODID;

public class CleaverItem extends Item implements PassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "cleaver_cleave");
    private final float radius;
    private final float cleaveDamageFraction;

    public CleaverItem(Properties properties, float radius, float cleaveDamageFraction) {
        super(properties.stacksTo(1).component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.CLEAVER));
        this.radius = radius;
        this.cleaveDamageFraction = cleaveDamageFraction;
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        if (context.get(AbilityContextKey.PLAYER).isEmpty() || context.get(AbilityContextKey.TARGET).isEmpty()) return;

        ServerPlayer player = context.get(AbilityContextKey.PLAYER).get();
        LivingEntity target = context.get(AbilityContextKey.TARGET).get();
        Level level = player.level();

        if (level.isClientSide()) return;

        PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);
        AABB boundingBox = new AABB(target.getX() - this.radius, target.getY() - this.radius, target.getZ() - this.radius, target.getX() + 1 + this.radius, target.getY() + 1 + this.radius, target.getZ() + 1 + this.radius);
        // blocks a cascade of every entity cleaved creating more cleaves
        abilities.addActiveEffect(ABILITY_IDENTIFIER, 0, level, null);

        Optional<AttributeInstance> finalDamageAttribute = UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder);

        // add the modifier ONCE, before the loop, not once per nearby entity
        finalDamageAttribute.ifPresent(attribute ->
                attribute.addTransientModifier(new AttributeModifier(
                        ABILITY_IDENTIFIER,
                        -(attribute.getValue() - (1 - cleaveDamageFraction)),
                        AttributeModifier.Operation.ADD_VALUE
                ))
        );

        try {
            for (Entity entity : level.getEntities(null, boundingBox)) {
                if (entity instanceof LivingEntity && !(entity instanceof Player) && entity != target) {
                    player.attack(entity);
                }
            }
        } finally {
            finalDamageAttribute.ifPresent(attribute -> attribute.removeModifier(ABILITY_IDENTIFIER));
        }
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);
            abilities.removeActiveEffect(ABILITY_IDENTIFIER, player);
        });
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER).map(player -> !player.getData(UnshatteredAttachments.PLAYER_ABILITIES).hasActiveEffect(ABILITY_IDENTIFIER)).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_DEAL_DAMAGE);
    }
}
