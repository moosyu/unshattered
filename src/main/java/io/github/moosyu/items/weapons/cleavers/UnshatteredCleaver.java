package io.github.moosyu.items.weapons.cleavers;

import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredCleaver extends Item implements UnshatteredPassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "cleaver_cleave");
    protected final float radius;

    public UnshatteredCleaver(Properties properties, float radius) {
        super(properties.stacksTo(1).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.CLEAVER));
        this.radius = radius;
    }

    @Override
    public void onAbilityTriggered(Player player, LivingEntity target) {
        Level level = player.level();
        if (level.isClientSide()) return;
        PlayerAbilityEffectsAttachment abilities = player.getData(AttachmentRegistry.PLAYER_ABILITIES);
        AABB boundingBox = new AABB(target.getX() - this.radius, target.getY() - this.radius, target.getZ() - this.radius, target.getX() + 1 + this.radius, target.getY() + 1 + this.radius, target.getZ() + 1 + this.radius);
        // already miss-using this... sorry but i dont really want to make another attachment and adding like a cleaving state would feel weirder
        abilities.addActiveEffect(
                ABILITY_IDENTIFIER,
                0,
                level,
                _ -> {},
                player.getItemInHand(InteractionHand.MAIN_HAND)
        );
        for (Entity entity : level.getEntities(null, boundingBox)) {
            if (entity instanceof LivingEntity && !(entity instanceof Player) && !(entity == target)) {
                player.getAttribute(UnshatteredAttributes.FINAL_DAMAGE_MODIFIER.holder).setBaseValue(player.getAttribute(UnshatteredAttributes.FINAL_DAMAGE_MODIFIER.holder).getValue() - 0.7);
                player.attack(entity);
            }
        }
        abilities.removeActiveEffect(ABILITY_IDENTIFIER, player);
    }

    @Override
    public boolean abilityConditionsMet(Player player, LivingEntity target) {
        return !player.getData(AttachmentRegistry.PLAYER_ABILITIES).hasActiveEffect(ABILITY_IDENTIFIER);
    }
}
