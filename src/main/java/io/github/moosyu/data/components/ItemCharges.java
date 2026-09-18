package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.dialogue.DialogueChoice;
import io.github.moosyu.data.dialogue.DialogueEventTypes;
import io.github.moosyu.data.dialogue.DialogueFlagRequirements;
import io.github.moosyu.data.dialogue.DialogueNode;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

/**
 * @param currentCharges charges the item gets initially
 * @param maxCharges max charges the item can reach
 * @param rechargeTime the time to recharge a single charge
 */
public record ItemCharges(int currentCharges, int maxCharges, int rechargeTime) {
    /**
     * create item charges with the current charges at the max
     * @param maxCharges max charges the item can reach
     * @param rechargeTime the time to recharge a single charge
     */
    public ItemCharges(int maxCharges, int rechargeTime) {
        this(maxCharges, maxCharges, rechargeTime);
    }

    public static final Codec<ItemCharges> CODEC = RecordCodecBuilder.create(itemChargesInstance -> itemChargesInstance.group(
            Codec.INT.fieldOf("current_charges").forGetter(ItemCharges::currentCharges),
            Codec.INT.fieldOf("max_charges").forGetter(ItemCharges::maxCharges),
            Codec.INT.fieldOf("recharge_time").forGetter(ItemCharges::rechargeTime)
        ).apply(itemChargesInstance, ItemCharges::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemCharges> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ItemCharges::currentCharges,
            ByteBufCodecs.INT, ItemCharges::maxCharges,
            ByteBufCodecs.INT, ItemCharges::rechargeTime,
            ItemCharges::new
    );

    public ItemCharges incrementCharges() {
        int newCharges = this.currentCharges + 1;
        return new ItemCharges(newCharges, this.maxCharges, this.rechargeTime);
    }

    public ItemCharges decrementCharges() {
        int newCharges = Math.max(this.currentCharges - 1, 0);
        return new ItemCharges(newCharges, this.maxCharges, rechargeTime);
    }
}
