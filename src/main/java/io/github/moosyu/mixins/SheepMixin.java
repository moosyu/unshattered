package io.github.moosyu.mixins;

import io.github.moosyu.data.MiscFlags;
import io.github.moosyu.data.attachments.PlayerFlagsAttachment;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.items.enchantments.UnshatteredEnchantmentEffects;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.items.enchantments.UnshatteredEnchantments;
import io.github.moosyu.util.UnshatteredUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Sheep.class)
public class SheepMixin {
    private Player shearer;
    private static final Item[] WOOL = {
            Items.WHITE_WOOL,
            Items.ORANGE_WOOL,
            Items.MAGENTA_WOOL,
            Items.LIGHT_BLUE_WOOL,
            Items.YELLOW_WOOL,
            Items.LIME_WOOL,
            Items.PINK_WOOL,
            Items.GRAY_WOOL,
            Items.LIGHT_GRAY_WOOL,
            Items.CYAN_WOOL,
            Items.PURPLE_WOOL,
            Items.BLUE_WOOL,
            Items.BROWN_WOOL,
            Items.GREEN_WOOL,
            Items.RED_WOOL,
            Items.BLACK_WOOL
    };

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void captureShearer(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        this.shearer = player;
    }

    @Inject(method = "shear", at = @At("HEAD"), cancellable = true)
    private void onShear(ServerLevel level, SoundSource soundSource, ItemStack tool, CallbackInfo ci) {
        ci.cancel();

        if (shearer != null) {
            Sheep sheep = (Sheep)(Object)this;
            ItemRange drops = null;

            level.playSound(null, sheep, SoundEvents.SHEEP_SHEAR, soundSource, 1.0f, 1.0f);
            sheep.setSheared(true);

            shearer.getData(UnshatteredAttachments.PLAYER_SKILLS.get()).addExp(PlayerSkillsAttachment.Skill.FARMING, 3, shearer);
            shearer.syncData(UnshatteredAttachments.PLAYER_SKILLS.get());


            for (Object2IntMap.Entry<Holder<Enchantment>> entry : shearer.getMainHandItem().getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet()) {
                Optional<ResourceKey<Enchantment>> key = entry.getKey().unwrapKey();
                if (key.isEmpty()) continue;

                Optional<UnshatteredEnchantmentEffects.UnshatteredSimpleEffect> effect = UnshatteredEnchantmentEffects.getEffect(key.get());

                if (effect.isPresent() && effect.get() == UnshatteredEnchantmentEffects.EFFECTS.get(UnshatteredEnchantments.RAINBOW)) {
                    PlayerFlagsAttachment playerFlagsAttachment = shearer.getData(UnshatteredAttachments.PLAYER_FLAGS.get());
                    if (playerFlagsAttachment.hasFlag(MiscFlags.RAINBOW_TALISMAN_OBTAINED)) {
                        drops = new ItemRange(WOOL[level.getRandom().nextInt(WOOL.length)], 1, 3);
                    } else {
                        drops = new ItemRange(WOOL[1], 1, 3);

                        playerFlagsAttachment.addFlag(MiscFlags.RAINBOW_TALISMAN_OBTAINED);
                        shearer.syncData(UnshatteredAttachments.PLAYER_FLAGS);
                    }
                }
            }

            if (drops == null) {
                drops = new ItemRange(Items.WHITE_WOOL, 1, 3);
            }

            UnshatteredUtils.givePlayerHarvestedItemStack(shearer, new ItemStack(drops.item(), drops.getDropAmount(level.getRandom())));
            shearer = null;
        }

    }
}