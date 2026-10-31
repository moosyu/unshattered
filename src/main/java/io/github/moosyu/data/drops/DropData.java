package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import static io.github.moosyu.data.drops.DropTypes.getDropType;

public record DropData(ItemRange itemRange, float dropChance) {
    public DropData(ItemRange itemRange) {
        this(itemRange, 1.0f);
    }

    public static Codec<DropData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemRange.CODEC.fieldOf("item_range").forGetter(DropData::itemRange),
                    Codec.FLOAT.fieldOf("drop_chance").forGetter(DropData::dropChance)
            ).apply(instance, DropData::new)
    );

    public boolean getNonGuaranteedDrop(boolean fortuneBoosted, boolean rolledAboveOccasional, Player player, @Nullable UnshatteredAttributeValues fortuneType) {
        double modifiedDropChance;
        double fortuneValue;
        RandomSource randomSource = player.getRandom();

        if (fortuneType == null) {
            return rolledAboveOccasional;
        }

        ItemStack dropStack = new ItemStack(itemRange().item(), itemRange().getDropAmount(randomSource));
        fortuneValue = player.getAttributeValue(fortuneType.holder);

        if (dropChance() < 1.0) {
            // non combat drops have a lot more chances to roll so their drop type display should reflect it
            DropTypes type = getDropType(fortuneType == UnshatteredAttributeValues.COMBAT_FORTUNE ? dropChance() : dropChance() * 50);
            // so the player cant roll a bunch of super rare drops in a single go ever if they're really lucky
            if (dropChance() < DropTypes.OCCASIONAL.minRate) {
                if (rolledAboveOccasional) {
                    return true;
                }
                rolledAboveOccasional = true;
            }

            if (fortuneBoosted) {
                modifiedDropChance = dropChance() * (1 + (fortuneValue / 100));
            } else {
                modifiedDropChance = dropChance();
            }

            if (randomSource.nextFloat() <= modifiedDropChance) {
                if (fortuneBoosted) {
                    UnshatteredRarity itemRarity = UnshatteredUtils.getItemRarity(itemRange().item().getDefaultInstance());
                    player.sendSystemMessage(Component.empty()
                            .append(Component.literal(Component.translatable("drop_type.message.unshattered." + type.key).getString().toUpperCase())
                                    .withStyle(style -> style.withColor(type.colour).withBold(true)))
                            .append(Component.literal(" "))
                            .append(Component.translatable(itemRange().item().getDescriptionId()).withStyle(style -> style.withColor(itemRarity.getColour()).withBold(false)))
                            .append(fortuneValue > 0 ?
                                    Component.literal(" (+" + Math.round(fortuneValue) + fortuneType.symbol + " ")
                                            .append(Component.translatable(fortuneType.getTranslationKey()))
                                            .append(Component.literal(")"))
                                            .withStyle(style -> style.withColor(fortuneType.color).withBold(false)) : Component.empty())
                            .append(dropStack.count() > 1 ? Component.literal(" x" + dropStack.count()) : Component.empty()).withColor(UnshatteredUtils.DARK_GRAY));
                }
                // for if you didnt get the drop (gives them another chance to get other rare drop)
            } else {
                return false;
            }
        } else {
            dropStack = new ItemStack(dropStack.getItem(), UnshatteredUtils.getItemsCount(fortuneValue, dropStack.count()));
        }

        UnshatteredUtils.addItemStackToInventory(player, dropStack);

        return rolledAboveOccasional;
    }
}
