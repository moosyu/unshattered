package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DropReward {
    Codec<DropReward> CODEC = Type.CODEC.dispatch("type", DropReward::type, Type::codec);

    Type type();

    /**
     * @return the amount granted
     */
    float grant(ServerPlayer player, RandomSource random, @Nullable Double fortuneAmount);

    default String formatAmount(float amount) {
        return String.format("%,d", (int) amount);
    }

    enum Type implements StringRepresentable {
        ITEM(ItemReward.CODEC),
        COINS(CoinReward.CODEC),
        EXPERIENCE(ExperienceReward.CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final MapCodec<? extends DropReward> codec;

        Type(MapCodec<? extends DropReward> codec) {
            this.codec = codec;
        }

        public MapCodec<? extends DropReward> codec() {
            return codec;
        }

        @Override public @NonNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}