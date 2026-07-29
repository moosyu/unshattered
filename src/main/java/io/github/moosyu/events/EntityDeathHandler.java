package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.DropTypes;
import io.github.moosyu.data.MobItemDropData;
import io.github.moosyu.data.MobRewardData;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.rarities.RarityTypes;
import io.github.moosyu.util.CollectionUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.concurrent.ThreadLocalRandom;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_SKILLS;
import static io.github.moosyu.data.DropTypes.getDropType;

@EventBusSubscriber(modid = MODID)
public class EntityDeathHandler {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        if (attacker instanceof Player player) {
            if (player.level().isClientSide()) return;
            PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
            Entity entity = event.getEntity();

            MobRewardData mobLoot = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType()).getData(UnshatteredDataMaps.COMBATABLE_MOBS_LOOT_DATA);
            if (mobLoot == null) {
                Unshattered.LOGGER.warn("entity {} is missing loot", entity.getPlainTextName());
                return;
            }

            AttributeInstance combatFortune = player.getAttribute(UnshatteredAttributeValues.COMBAT_FORTUNE.holder);
            if (combatFortune == null) {
                Unshattered.LOGGER.error("combat fortune is null! drops not rolled!");
                return;
            }

            boolean rolledAboveOccasional = false;
            // may end up doing it weighted instead of rolling them all at once, we'll see though
            for (MobItemDropData itemDrop : mobLoot.drops()) {
                double modifiedDropChance = (itemDrop.combatFortuneBoosted() ? itemDrop.baseDropChance() * (1 + (combatFortune.getValue() / 100)) : itemDrop.baseDropChance());
                if (itemDrop.baseDropChance() < 1.0) {
                    DropTypes type = getDropType(itemDrop.baseDropChance());
                    // so the player cant roll a bunch of super rare drops in a single go ever if they're really lucky
                    if (itemDrop.baseDropChance() < DropTypes.OCCASIONAL.minRate && !rolledAboveOccasional) {
                        rolledAboveOccasional = true;
                    } else continue;

                    if (ThreadLocalRandom.current().nextFloat(1.0f) < modifiedDropChance) {
                        if (itemDrop.combatFortuneBoosted()) {
                            RarityTypes itemRarity = itemDrop.item().components().getOrDefault(UnshatteredDataComponents.RARITY.get(), RarityTypes.COMMON);
                            player.sendSystemMessage(Component.empty()
                                    .append(Component.literal(Component.translatable("drop_type.message.unshattered." + type.key).getString().toUpperCase())
                                            .withStyle(style -> style.withColor(type.colour).withBold(true)))
                                    .append(Component.literal(" "))
                                    .append(Component.translatable(itemDrop.item().getDescriptionId())
                                            .withStyle(style -> style.withColor(itemRarity.getColour(1.0f)).withBold(false)))
                                    .append(Component.literal(" "))
                                    .append(Component.literal("(+" + Math.round(combatFortune.getValue()) + " " + UnshatteredAttributeValues.COMBAT_FORTUNE.symbol + " ")
                                            .append(Component.translatable(UnshatteredAttributeValues.COMBAT_FORTUNE.getTranslationKey()))
                                            .append(Component.literal(")"))
                                            .withStyle(style -> style.withColor(UnshatteredAttributeValues.COMBAT_FORTUNE.color).withBold(false))
                                    )
                            );
                        }
                    // for if you didnt get the drop
                    } else continue;
                }

                int dropAmount = itemDrop.minItemAmount() == itemDrop.maxItemAmount() ?
                        itemDrop.minItemAmount() :
                        ThreadLocalRandom.current().nextInt(itemDrop.minItemAmount(), itemDrop.maxItemAmount() + 1);

                CollectionUtil.givePlayerHarvestedItemStack(player, new ItemStack(itemDrop.item(), dropAmount));
            }

            if (mobLoot.experience() > 0.0f) {
                skills.addExp(mobLoot.skill(), mobLoot.experience(), player);
                player.syncData(PLAYER_SKILLS);
            }
        }
    }
}
