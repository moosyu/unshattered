package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.data.components.DataComponentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingEquipmentChangeHandler {
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack itemStack = event.getTo();
            SkillRequirement itemSkillRequirement = itemStack.get(DataComponentRegistry.SKILL_REQUIREMENT.get());
            // possibly the worst way to do this but A: any good ways will be annoying to get working and B: the delay kind of reminds
            // me of real skyblock which puts a smile on my face
            // this also dupes in creative mode which gives me the impression there is a way to dupe using this in survival but whatever
            if (itemSkillRequirement != null) {
                PlayerSkillsAttachment playerSkills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
                EquipmentSlot slotPlaced = event.getSlot();
                if ((slotPlaced == EquipmentSlot.HEAD
                        || slotPlaced == EquipmentSlot.CHEST
                        || slotPlaced == EquipmentSlot.LEGS
                        || slotPlaced == EquipmentSlot.FEET
                ) && itemSkillRequirement.level() > playerSkills.getLevel(playerSkills.getExp(itemSkillRequirement.skill()))) {
                    player.setItemSlot(slotPlaced, event.getFrom());
                    player.getInventory().add(itemStack);
                    player.sendSystemMessage(Component.literal(itemSkillRequirement.skill().getName() + " level " + itemSkillRequirement.level() + " is required to equip this armour piece!").withColor(0xFFFF5555));
                }
            }
        }
    }
}
