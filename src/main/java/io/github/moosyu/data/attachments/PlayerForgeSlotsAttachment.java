package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * for empty slots itemStack is empty, end time can be whatever, probably 0
 */
public record PlayerForgeSlotsAttachment(int availableSlots, List<ForgeSlot> slots) {
    public static final int MAX_SLOTS = 6;

    public PlayerForgeSlotsAttachment {
        List<ForgeSlot> fixed = new ArrayList<>(MAX_SLOTS);
        for (int i = 0; i < MAX_SLOTS; i++) {
            fixed.add(i < slots.size() ? slots.get(i) : ForgeSlot.EMPTY_SLOT);
        }
        slots = List.copyOf(fixed);
        availableSlots = Math.min(availableSlots, MAX_SLOTS);
    }

    /**
     * @param startTime gotten with Instant.now().getEpochSecond()
     */
    public record ForgeSlot(long startTime, long forgingDuration, Optional<ItemStack> itemStack) {
        public static ForgeSlot EMPTY_SLOT = new ForgeSlot(0L, 0L,Optional.empty());

        public static Codec<ForgeSlot> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.LONG.fieldOf("end_time").forGetter(ForgeSlot::startTime),
                        Codec.LONG.fieldOf("recipe_length").forGetter(ForgeSlot::forgingDuration),
                        ItemStack.CODEC.optionalFieldOf("itemstack").forGetter(ForgeSlot::itemStack)
                ).apply(instance, ForgeSlot::new)
        );

        public static StreamCodec<RegistryFriendlyByteBuf, ForgeSlot> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.LONG, ForgeSlot::startTime,
                ByteBufCodecs.LONG, ForgeSlot::forgingDuration,
                ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional), ForgeSlot::itemStack,
                ForgeSlot::new
        );
    }

    public static Codec<PlayerForgeSlotsAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("available_slots").forGetter(PlayerForgeSlotsAttachment::availableSlots),
                    ForgeSlot.CODEC.listOf().fieldOf("forge_slot").forGetter(PlayerForgeSlotsAttachment::slots)
            ).apply(instance, PlayerForgeSlotsAttachment::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, PlayerForgeSlotsAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlayerForgeSlotsAttachment::availableSlots,
            ForgeSlot.STREAM_CODEC.apply(ByteBufCodecs.list(MAX_SLOTS)), PlayerForgeSlotsAttachment::slots,
            PlayerForgeSlotsAttachment::new
    );
}
