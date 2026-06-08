package io.github.moosyu.items.swords;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.items.ItemTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import static io.github.moosyu.Unshattered.MODID;

public class RogueSword extends Item {
    public RogueSword(Properties properties) {
        super(properties.stacksTo(1).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.SWORD).attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributes.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "rogue_sword_damage"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // todo: make some sort of ability system
        return InteractionResult.PASS;
    }
}
