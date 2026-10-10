package io.github.moosyu.util;

import io.github.moosyu.Unshattered;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.drops.BlockBreakData;
import io.github.moosyu.data.drops.DropData;
import io.github.moosyu.data.drops.DropTypes;
import io.github.moosyu.data.fishing.FishingEntry;
import io.github.moosyu.data.fishing.FishingWeightEntry;
import io.github.moosyu.data.recipes.ForgeRecipe;
import io.github.moosyu.data.recipes.ForgeRecipeInput;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.drops.DropTypes.getDropType;

// generic utilities i use in multiple places
public final class UnshatteredUtils {
    // text stuff
    public static DecimalFormat oneDecimalFormat = new DecimalFormat("0.#");
    public static final int BLACK = 0xFF000000;
    public static final int DARK_BLUE = 0xFF0000AA;
    public static final int DARK_GREEN = 0xFF00AA00;
    public static final int DARK_AQUA = 0xFF00AAAA;
    public static final int DARK_RED = 0xFFAA0000;
    public static final int PURPLE = 0xFFAA00AA;
    public static final int GOLD = 0xFFFFAA00;
    public static final int GRAY = 0xFFAAAAAA;
    public static final int DARK_GRAY = 0xFF555555;
    public static final int BLUE = 0xFF5555FF;
    public static final int GREEN = 0xFF55FF55;
    public static final int CYAN = 0xFF55FFFF;
    public static final int RED = 0xFFFF5555;
    public static final int MAGENTA = 0xFFFF55FF;
    public static final int YELLOW = 0xFFFFDE2F;
    public static final int WHITE = 0xFFFFFFFF;

    public static final Item[] WOOL_TYPES = {
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

    /**
     * pointless but i reckon it makes things more clear. not even sure if right is 1 i just guessed lol.
     */
    public enum MouseButton {
        LEFT(0),
        RIGHT(1);

        private final int button;

        public int getButton() {
            return button;
        }

        MouseButton(int button) {
            this.button = button;
        }
    }

    /**
     * @param input string input to be converted to component
     * @param baseColor text colour to be used when section's colour is unspecified
     * @return parses components to work in bbcode-esque format where you can select colours with [c=...] [/c] in hexcode format as well as [i][/i] for italics, [b][/b] for bold and [p] for the player's name
     */
    public static Component parseStyledText(String input, int baseColor, Player player) {
        MutableComponent result = Component.empty();
        Matcher matcher = Pattern.compile("\\[p]|\\[(/?)([cib])(?:=(0x[0-9A-Fa-f]{1,8}))?]").matcher(input);

        record Open(String tag, Style style) {}

        Deque<Open> stack = new ArrayDeque<>();
        Style base = Style.EMPTY.withColor(baseColor & 0xFFFFFF);
        Supplier<Style> current = () -> stack.isEmpty() ? base : stack.peek().style();
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                result.append(Component.literal(input.substring(lastEnd, matcher.start())).withStyle(current.get()));
            }

            String match = matcher.group();

            if (match.equals("[p]")) {
                result.append(Component.literal(player.getName().getString()).withStyle(current.get()));
            } else {
                boolean closing = !matcher.group(1).isEmpty();
                String tag = matcher.group(2);

                if (closing) {
                    if (!stack.isEmpty() && stack.peek().tag().equals(tag)) {
                        stack.pop();
                    } else {
                        // shows a random closing tag as plain text instead of trying to consume a random closing tag
                        result.append(Component.literal(match).withStyle(current.get()));
                    }
                } else {
                    Style style = current.get();
                    switch (tag) {
                        case "b" -> style = style.withBold(true);
                        case "i" -> style = style.withItalic(true).withColor(UnshatteredUtils.GRAY);
                        case "c" -> {
                            if (matcher.group(3) == null) {
                                result.append(Component.literal(match).withStyle(current.get()));
                                lastEnd = matcher.end();
                                continue;
                            }
                            int color = (int) (Long.parseLong(matcher.group(3).substring(2), 16) & 0xFFFFFF);
                            style = style.withColor(color);
                        }
                    }
                    stack.push(new Open(tag, style));
                }
            }

            lastEnd = matcher.end();
        }

        if (lastEnd < input.length()) {
            result.append(Component.literal(input.substring(lastEnd)).withStyle(current.get()));
        }

        return result;
    }

    /**
     * <a href="https://www.geeksforgeeks.org/dsa/converting-decimal-number-lying-between-1-to-3999-to-roman-numerals/">taken from geeksforgeeks 💋</a>
     * @param x the value to be converted to a roman numeral (has to be in the range 1-3999)
     * @return a roman numeral string
     */
    public static String convertTextToRomanNumeral(int x) {
        int[] base = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        String[] sym = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};

        // to store result
        StringBuilder res = new StringBuilder();

        // Loop from the right side to find
        // the largest smaller base value
        int i = base.length - 1;
        while (x > 0) {
            int div = x / base[i];
            while (div > 0) {
                res.append(sym[i]);
                div--;
            }

            // Repeat the process for remainder
            x = x % base[i];
            i--;
        }

        return res.toString();
    }

    /**
     * @param input string that may need conversion
     * @return lowercase string with spaces replaced with underscores
     */
    public static String convertToSnakeCase(String input) {
        return input.replace(" ", "_").toLowerCase();
    }

    // sounds

    /**
     * @param player the player having the sound played
     * @param soundEvent the sound event of the sound (usually taken from SoundEvents)
     * @param soundSource the source of the sound for volume settings
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public static void playClientsideSound(Player player, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        Level level = player.level();
        if (level.isClientSide()) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, soundSource, volume, pitch, false);
        }
    }

    /**
     * @param player the player having the sound played
     * @param soundEvent the sound event of the sound (usually taken from SoundEvents)
     * @param soundSource the source of the sound for volume settings
     * @param volume the volume of the sound
     */
    public static void playClientsideSound(Player player, SoundEvent soundEvent, SoundSource soundSource, float volume) {
        Level level = player.level();
        if (level.isClientSide()) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, soundSource, volume, 1.0f, false);
        }
    }

    // misc

    /**
     * helper to get opacity in a more comfortable way. taken from <a href="https://stackoverflow.com/a/28483738">stack overflow</a>.
     * @param color hex colour without opacity set
     * @param opacity opacity, (1.0f is 100% opacity and 0.0f is 0%)
     * @return the hex colour with its opacity modified accordingly
     */
    public static int getOpacityColor(int color, float opacity) {
        return ((int)(opacity * 255) << 24) | (color & 0xFFFFFF);
    }

    /**
     * @param path identifier path
     * @return an identifier with unshattered's modid as the namespace (like withDefaultNamespace)
     */
    public static Identifier getUnshatteredIdentifier(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    /**
     * @param fortuneAmount the fortune of whatever type is being used
     * @param baseDropAmount base drop amount dropped when you break the block (usually itll be one but you never know)
     * @return amount of whatever the player should be given
     */
    public static int getItemsCount(double fortuneAmount, int baseDropAmount) {
        double prevHundred = (Math.floor(fortuneAmount / 100.0));
        int guaranteedDrops = (int) prevHundred + baseDropAmount;
        double nextHundredDiff = fortuneAmount - (prevHundred * 100);

        if (new Random().nextDouble(100.0d) < nextHundredDiff) {
            return guaranteedDrops + 1;
        } else {
            return guaranteedDrops;
        }
    }

    // dialogue

    /**
     * @param dialogueTreeIdentifier identifier probably created using {@link #getUnshatteredIdentifier(String)}
     * @param dialogueNodeName name of the node
     * @return an identifier for the dialogue node which will look something like initatior_name/dialogue_tree/node_nmae
     */
    public static Identifier createDialogueNodeIdentifier(Identifier dialogueTreeIdentifier, @NonNull String dialogueNodeName) {
        return getUnshatteredIdentifier(dialogueTreeIdentifier.getPath() + "/" +  dialogueNodeName);
    }

    // block drop methods

    /**
     * should be used instead of Inventory#add when adding items that were harvested by the player
     * @param player player having the item added
     * @param itemStack itemstack being added to inventory
     */
    public static void givePlayerHarvestedItemStack(Player player, ItemStack itemStack) {
        if (itemStack.isEmpty()) return;

        ((ServerPlayer) player).getStats().increment(player, Stats.ITEM_PICKED_UP.get(itemStack.getItem()), itemStack.count());

        if (!player.getInventory().add(itemStack)) {
            player.drop(itemStack, false);
        }
    }

    public static void addBlockBrokenResultToInventory(Holder<Block> blockHolder, Player player, UnshatteredAttributeValues fortuneType) {
        BlockBreakData blockBreakData = blockHolder.getData(UnshatteredDataMaps.BLOCK_BREAK_DATA);

        if (blockBreakData == null) {
            Unshattered.LOGGER.error("block broken ({}) without defined drop data.", blockHolder.getRegisteredName());
            return;
        }

        boolean rolledAboveOccasional = false;

        for (DropData blockDropData : blockBreakData.dropData()) {
            rolledAboveOccasional = UnshatteredUtils.getNonGuaranteedDrop(blockDropData,
                    true,
                    rolledAboveOccasional,
                    player,
                    fortuneType
            );
        }
    }

    public static boolean getNonGuaranteedDrop(DropData dropData, boolean fortuneBoosted, boolean rolledAboveOccasional, Player player, @Nullable UnshatteredAttributeValues fortuneType) {
        double modifiedDropChance;
        double fortuneValue;
        RandomSource randomSource = player.getRandom();

        if (fortuneType == null) return rolledAboveOccasional;

        ItemStack dropStack = new ItemStack(dropData.itemRange().item(), dropData.itemRange().getDropAmount(randomSource));
        fortuneValue = player.getAttributeValue(fortuneType.holder);

        if (dropData.dropChance() < 1.0) {
            DropTypes type = getDropType(dropData.dropChance());
            // so the player cant roll a bunch of super rare drops in a single go ever if they're really lucky
            if (dropData.dropChance() < DropTypes.OCCASIONAL.minRate) {
                if (rolledAboveOccasional) {
                    return true;
                }
                rolledAboveOccasional = true;
            }

            if (fortuneBoosted) {
                modifiedDropChance = dropData.dropChance() * (1 + (fortuneValue / 100));
            } else {
                modifiedDropChance = dropData.dropChance();
            }

            if (randomSource.nextFloat() <= modifiedDropChance) {
                if (fortuneBoosted) {
                    UnshatteredRarity itemRarity = UnshatteredUtils.getItemRarity(dropData.itemRange().item().getDefaultInstance());
                    player.sendSystemMessage(Component.empty()
                            .append(Component.literal(Component.translatable("drop_type.message.unshattered." + type.key).getString().toUpperCase())
                                    .withStyle(style -> style.withColor(type.colour).withBold(true)))
                            .append(Component.literal(" "))
                            .append(Component.translatable(dropData.itemRange().item().getDescriptionId())
                                    .withStyle(style -> style.withColor(itemRarity.getColour(1.0f)).withBold(false)))
                            .append(fortuneValue > 0 ?
                                    Component.literal(" (+" + Math.round(fortuneValue) + fortuneType.symbol + " ")
                                            .append(Component.translatable(fortuneType.getTranslationKey()))
                                            .append(Component.literal(")"))
                                            .withStyle(style -> style.withColor(fortuneType.color).withBold(false)) : Component.empty()
                            ));
                }
                // for if you didnt get the drop (gives them another chance to get other rare drop)
            } else {
                return false;
            }
        } else {
            dropStack = new ItemStack(dropStack.getItem(), UnshatteredUtils.getItemsCount(fortuneValue, dropStack.count()));
        }

        UnshatteredUtils.givePlayerHarvestedItemStack(player, dropStack);

        return rolledAboveOccasional;
    }

    // item requirements

    /**
     * @param player the player having their mana checked
     * @param manaCost the mana requirement to do whatever
     * @return true if the player passes false if they dont + text saying the player doesn't meet the requirement
     */
    public static boolean passesManaCheck(Player player, int manaCost) {
        double playerManaAmount = player.getData(UnshatteredAttachments.PLAYER_STATE.get()).getStatValue(PlayerStateAttachment.Stat.MANA);
        if (playerManaAmount < manaCost) {
            player.sendSystemMessage(Component.literal("You don't have enough mana to use this " + "(" + ((int) (playerManaAmount)) + "/" + manaCost + ").").withColor(RED));
            return false;
        }
        return true;
    }

    /**
     * @param player the player using the item
     * @param itemCharges the item's charges
     * @param rechargeIdentifier identifier for the recharge ability to check time until expiration
     * @return true if the player passes false if they dont + text saying the player doesn't have any charges and how long they have to wait until it recharges.
     */
    public static boolean passesChargesCheck(Player player, ItemCharges itemCharges, Identifier rechargeIdentifier) {
        if (itemCharges.currentCharges() <= 0) {
            player.sendSystemMessage(
                    Component.literal("You don't have any charges left! Wait " + (((player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).expiryTimeTicks(rechargeIdentifier) - player.level().getGameTime()) / 20) + 1) + "s.")
                            .withColor(RED));
            return false;
        }
        return true;
    }

    // abilities

    /**
     * triggers all non-ongoing passive item abilities if their conditions are met and they have the correct trigger type. doesnt interact with {@link PassiveAbilityItem#triggerResult()}.
     * @return a list of items that had their abilities triggered. REMEMBER TO USE THIS LIST TO CLEAN UP!!
     */
    public static List<PassiveAbilityItem> triggerInstantPassiveAbilities(Player player, AbilityTriggerType triggerType, AbilityContext context) {
        List<PassiveAbilityItem> triggered = new ArrayList<>();

        for (ItemStack item : player.getData(UnshatteredAttachments.PLAYER_ABILITIES).getStoredNonOngoingItems()) {
            if (item.getItem() instanceof PassiveAbilityItem passiveAbilityItem && passiveAbilityItem.triggerTypes().contains(triggerType) && passiveAbilityItem.abilityConditionsMet(context)) {
                passiveAbilityItem.onAbilityTriggered(context);
                triggered.add(passiveAbilityItem);
            }
        }
        return triggered;
    }

    /**
     * @param player player having the attribute modified/checked
     * @param attribute the attribute to get instance of
     * @return an optional attribute instance of the selected attribute to modify
     */
    public static Optional<AttributeInstance> getAttributeInstance(Player player, Holder<Attribute> attribute) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            Unshattered.LOGGER.error("{} is null (from getAttributeInstance)", attribute.getRegisteredName());
            return Optional.empty();
        }
        return Optional.of(attributeInstance);
    }

    /**
     * @param player the player looking
     * @param reach player's reach, probably either BLOCK_INTERACTION_RANGE or ENTITY_INTERACTION_RANGE attributes
     * @return a block hit result if the raycast hit a block, Optional.empty() if it didn't
     */
    public static Optional<BlockHitResult> getLookedAtBlock(ServerPlayer player, double reach) {
        Vec3 eyePosition = player.getEyePosition();
        BlockHitResult result = player.level().clip(new ClipContext(eyePosition,
                eyePosition.add(player.getViewVector(1.0F).scale(reach)),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player)
        );

        return result.getType() == HitResult.Type.BLOCK ? Optional.of(result) : Optional.empty();
    }

    public static <T extends LivingEntity> Optional<AttributeSupplier> getDefaultAttributes(T entity) {
        @SuppressWarnings("unchecked")
        EntityType<T> type = (EntityType<T>) entity.getType();
        return Optional.of(DefaultAttributes.getSupplier(type));
    }

    // enchantments

    public static int getEnchantmentLevel(ItemStack itemStack, Level level, ResourceKey<Enchantment> enchantment) {
        return itemStack.getEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment));
    }

    // fishing

    public static double calculateTableWeight(Map<?, FishingWeightEntry> selectedMap) {
        return selectedMap.values().stream().mapToDouble(FishingWeightEntry::weight).sum();
    }

    public static <T> Map<T, FishingWeightEntry> filterFishingEntries(Map<T, FishingWeightEntry> entries, ServerPlayer player) {
        return entries.entrySet().stream()
                .filter(entry -> fishingRequirementsMet(entry.getValue(), player))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public static boolean fishingRequirementsMet(FishingEntry entry, ServerPlayer player) {
        boolean metCondition = entry.condition().map(condition -> condition.test(player)).orElse(true);
        boolean metLevelRequirement;

        if (entry.fishingLevelRequirement().isPresent()) {
            PlayerSkillsAttachment playerSkillsAttachment = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
            metLevelRequirement = playerSkillsAttachment.getLevel(playerSkillsAttachment.getExp(PlayerSkillsAttachment.Skill.FISHING)) >= entry.fishingLevelRequirement().get();
        } else {
            metLevelRequirement = true;
        }

        return metCondition && metLevelRequirement;
    }

    public static boolean canAffordCoins(int price, Player player) {
        return player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get()).getCoins() >= price;
    }

    public static boolean canTradeItems(List<ItemStack> requiredItems, Player player) {
        for (ItemStack ingredient : requiredItems) {
            int totalAvailable = player.getInventory().clearOrCountMatchingItems(
                    itemStack -> ItemStack.isSameItemSameComponents(itemStack, ingredient),
                    0,
                    player.inventoryMenu.getCraftSlots()
            );
            if (totalAvailable < ingredient.getCount()) {
                return false;
            }
        }
        return true;
    }

    /**
     * assumes {@link #canAffordCoins(int, Player)} was run to check whether the transaction can be made
     */
    public static void spendCoins(int price, Player player) {
        player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get()).removeCoins(price);
        player.syncData(UnshatteredAttachments.PLAYER_CURRENCY);
    }

    /**
     * assumes {@link #canTradeItems(List, Player)} was run to check whether the transaction can be made
     */
    public static void tradeItems(List<ItemStack> requiredItems, Player player) {
        if (player.level().isClientSide()) {
            return;
        }

        for (ItemStack ingredient : requiredItems) {
            player.getInventory().clearOrCountMatchingItems(
                    itemStack -> ItemStack.isSameItemSameComponents(itemStack, ingredient),
                    ingredient.getCount(),
                    player.inventoryMenu.getCraftSlots()
            );
        }
    }

    public static ItemType getItemType(ItemStack itemStack) {
        ItemType itemType = itemStack.typeHolder().getData(UnshatteredDataMaps.ITEM_TYPE_DATA);
        if (itemType == null) {
             return ItemType.ITEM;
        }

        return itemType;
    }

    public static UnshatteredRarity getItemRarity(ItemStack itemStack) {
        UnshatteredRarity rarity = itemStack.typeHolder().getData(UnshatteredDataMaps.ITEM_RARITY_DATA);
        if (rarity == null) {
            return UnshatteredRarity.COMMON;
        }

        return rarity;
    }

    public static int getItemSellValue(ItemStack itemStack) {
        Integer sellValue = itemStack.typeHolder().getData(UnshatteredDataMaps.ITEM_SELL_VALUE_DATA);
        if (sellValue == null) {
            return 0;
        }

        return sellValue;
    }

    /**
     * @return the fuel amount of a SINGLE item in the provided itemstack
     */
    public static int getItemFuelAmount(ItemStack itemStack) {
        Integer fuelAmount = itemStack.typeHolder().getData(UnshatteredDataMaps.ITEM_FUEL_VALUE_DATA);
        if (fuelAmount == null) {
            return 0;
        }

        return fuelAmount;
    }

    /**
     * for running clientside for an error message and then serverside to make sure no funny business went on
     */
    public static boolean canForgeItem(Player player, ForgeRecipe forgeRecipe) {
        return forgeRecipe.matches(ForgeRecipeInput.getRecipeInput(player), player.level());
    }
}