package io.github.moosyu.blocks;

import io.github.moosyu.data.regen.ChanceModifiedVariant;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public class BreakableTitaniumBlock extends Block implements ChanceModifiedVariant {
    // so regen paths still know what mithril to return to when titanium is broken
    public enum MithrilOrigin implements StringRepresentable {
        SOFT, HARD, COBBLED;

        @Override
        public @NonNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public static final EnumProperty<MithrilOrigin> ORIGIN = EnumProperty.create("origin", MithrilOrigin.class);

    public BreakableTitaniumBlock(Properties properties) {
        super(properties.destroyTime(65.0f)
                .sound(SoundType.STONE)
        );

        registerDefaultState(stateDefinition.any().setValue(ORIGIN, MithrilOrigin.SOFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORIGIN);
    }

    @Override
    public float getModifiedRollChance(ServerPlayer player) {
        return 1.0f;
    }
}
