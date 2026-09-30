package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public record PlayerPowderAttachment(PowderBalance mithril, PowderBalance gemstone, PowderBalance glacite) {
    public record Powder(PowderType powderType, int powderAmount) {
        public static Codec<Powder> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        PowderType.CODEC.fieldOf("powder_type").forGetter(Powder::powderType),
                        Codec.INT.fieldOf("powder_amount").forGetter(Powder::powderAmount)
                ).apply(instance, Powder::new)
        );
    }

    public record PowderBalance(int total, int current) {
        public static final PowderBalance ZERO = new PowderBalance(0, 0);

        public static final Codec<PowderBalance> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("total").forGetter(PowderBalance::total),
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("current").forGetter(PowderBalance::current)
                ).apply(instance, PowderBalance::new)
        );

        public static final StreamCodec<ByteBuf, PowderBalance> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, PowderBalance::total,
                ByteBufCodecs.VAR_INT, PowderBalance::current,
                PowderBalance::new
        );

        public PowderBalance {
            total = Math.max(0, total);
            current = Mth.clamp(current, 0, total);
        }

        public PowderBalance add(int amount) {
            if (amount <= 0) return this;

            int newTotal = Math.min(Integer.MAX_VALUE, total + amount);
            return new PowderBalance(newTotal, Math.min(Integer.MAX_VALUE, current + (newTotal - total)));
        }

        public boolean canAfford(int amount) {
            return amount >= 0 && current >= amount;
        }

        public PowderBalance remove(int amount) {
            return canAfford(amount) ? new PowderBalance(total, current - amount) : this;
        }
    }

    public static final PlayerPowderAttachment EMPTY = new PlayerPowderAttachment(PowderBalance.ZERO, PowderBalance.ZERO, PowderBalance.ZERO);

    public enum PowderType implements StringRepresentable {
        MITHRIL,
        GEMSTONE,
        GLACITE;

        @Override
        public @NonNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public static Codec<PowderType> CODEC = StringRepresentable.fromEnum(PowderType::values);
    }

    public static final Codec<PlayerPowderAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PowderBalance.CODEC.optionalFieldOf("mithril_powder", PowderBalance.ZERO).forGetter(PlayerPowderAttachment::mithril),
                    PowderBalance.CODEC.optionalFieldOf("gemstone_powder", PowderBalance.ZERO).forGetter(PlayerPowderAttachment::gemstone),
                    PowderBalance.CODEC.optionalFieldOf("glacite_powder", PowderBalance.ZERO).forGetter(PlayerPowderAttachment::glacite)
            ).apply(instance, PlayerPowderAttachment::new)
    );

    public static final StreamCodec<ByteBuf, PlayerPowderAttachment> STREAM_CODEC = StreamCodec.composite(
            PowderBalance.STREAM_CODEC, PlayerPowderAttachment::mithril,
            PowderBalance.STREAM_CODEC, PlayerPowderAttachment::gemstone,
            PowderBalance.STREAM_CODEC, PlayerPowderAttachment::glacite,
            PlayerPowderAttachment::new
    );

    public PowderBalance get(PowderType type) {
        return switch (type) {
            case MITHRIL -> mithril;
            case GEMSTONE -> gemstone;
            case GLACITE -> glacite;
        };
    }

    public PlayerPowderAttachment getAttachment(PowderType type, PowderBalance balance) {
        return switch (type) {
            case MITHRIL -> new PlayerPowderAttachment(balance, gemstone, glacite);
            case GEMSTONE -> new PlayerPowderAttachment(mithril, balance, glacite);
            case GLACITE -> new PlayerPowderAttachment(mithril, gemstone, balance);
        };
    }

    public void addPowder(Player player, PowderType type, int amount) {
        PlayerPowderAttachment data = player.getData(UnshatteredAttachments.PLAYER_POWDER);
        player.setData(UnshatteredAttachments.PLAYER_POWDER, data.getAttachment(type, data.get(type).add(amount)));
    }

    /**
     * @return true if powder was spent successfully
     */
    public boolean trySpendPowder(Player player, PowderType type, int amount) {
        PlayerPowderAttachment data = player.getData(UnshatteredAttachments.PLAYER_POWDER);
        PowderBalance balance = data.get(type);

        if (!balance.canAfford(amount)) {
            return false;
        }

        player.setData(UnshatteredAttachments.PLAYER_POWDER, data.getAttachment(type, balance.remove(amount)));
        return true;
    }
}
