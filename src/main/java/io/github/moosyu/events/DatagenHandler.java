package io.github.moosyu.events;

import io.github.moosyu.blocks.BreakableTitaniumBlock;
import io.github.moosyu.blocks.TalkingRockBlock;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.dialogue.*;
import io.github.moosyu.data.dialogue.events.*;
import io.github.moosyu.data.fishing.FishingCondition;
import io.github.moosyu.data.fishing.TriggerMiscReward;
import io.github.moosyu.data.fishing.rewards.CoinReward;
import io.github.moosyu.data.quests.Quest;
import io.github.moosyu.data.quests.QuestTypes;
import io.github.moosyu.data.regen.RegenCondition;
import io.github.moosyu.data.regen.RegenPaths.*;
import io.github.moosyu.data.regions.*;
import io.github.moosyu.data.datagen.*;
import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.npcs.*;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.joml.Vector2i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class DatagenHandler {
    @SubscribeEvent
    public static void onGatherDataClient(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new UnshatteredModelProvider(packOutput));
        generator.addProvider(true, new UnshatteredEquipmentAssetProvider(packOutput));
        generator.addProvider(true, new UnshatteredBlockTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredItemTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredEntityTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredDataMapProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredSoundDefinitionsProvider(packOutput));
        event.createProvider(UnshatteredRecipeProvider.Runner::new);

        event.createDatapackRegistryObjects(
                new RegistrySetBuilder().add(DataPackRegistryHandler.REGION_REGISTRY_KEY, bootstrap -> {
                    bootstrap.register(UnshatteredRegions.PLAINS_REGION, new Region(0xFF71AD1C, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.DEFAULT_REGION, new Region(0xFFFFFFFF, RegionTemperatureTypes.COLD, false));
                    bootstrap.register(UnshatteredRegions.FORGOTTEN_DWELLING, new Region(0xFFCCD9E3, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.HYTHE, new Region(0xFFB1FA96, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.SKY, new Region(0xFF96E3FA, RegionTemperatureTypes.COLD, false));
                }).add(DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY, bootstrap -> {
                    HolderGetter<Region> regions = bootstrap.lookup(DataPackRegistryHandler.REGION_REGISTRY_KEY);

                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-2048, -2048), new Vector2i(2048, 2048), Integer.MIN_VALUE, 200, 0), UnshatteredRegions.DEFAULT_REGION, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-2048, -2048), new Vector2i(2048, 2048), 200, Integer.MAX_VALUE, 0), UnshatteredRegions.SKY, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-846, 688), new Vector2i(-1119, 825), 1), UnshatteredRegions.PLAINS_REGION, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-1035, 837), new Vector2i(-1070, 805), 2), UnshatteredRegions.FORGOTTEN_DWELLING, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-864, 707), new Vector2i(-937, 792), 2), UnshatteredRegions.HYTHE, regions);

                }).add(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY, bootstrap -> {
                    registerDialogueTree(bootstrap,
                            TalkingRockBlock.ROCK_IDENTIFIER,
                            new DialogueTree(List.of(
                                    createDialogueOriginWithSelfFlag(
                                            0,
                                            new DialogueNode(Component.literal("hi, im a rock"),
                                                    true,
                                                    new DialogueChoice(Component.literal("interesting"),
                                                            new DialogueNode(Component.literal("im glad you think so :)"))
                                                    ),
                                                    new DialogueChoice(Component.literal("..."))
                                            ),
                                            TalkingRockBlock.HI_MESSAGE_IDENTIFIER),
                                    createDialogueOriginWithSelfFlag(1,
                                            new DialogueNode(Component.literal("you've already spoken to me"),
                                                    true,
                                                    new DialogueChoice(Component.literal("i know right"),
                                                            new DialogueNode(Component.literal("does seem that way"),
                                                                    false,
                                                                    new DialogueChoice(Component.literal("..."),
                                                                            new GiveItemDialogueEvent(BuiltInRegistries.ITEM
                                                                                    .wrapAsHolder(Items.DIAMOND), 1)
                                                                    )
                                                            )
                                                    )
                                            ),
                                            List.of(TalkingRockBlock.HI_MESSAGE_IDENTIFIER),
                                            List.of(),
                                            List.of(),
                                            TalkingRockBlock.HI2_MESSAGE_IDENTIFIER
                                    ),
                                    createDialogueOrigin(2,
                                            new DialogueNode(Component.literal("find my pages"),
                                                    true,
                                                    new DialogueChoice(Component.literal("i guess"),
                                                            List.of(TalkingRockBlock.ROCKS_QUEST),
                                                            new StartQuestDialogueEvent(TalkingRockBlock.ROCKS_QUEST)),
                                                    new DialogueChoice(Component.literal("no thanks"),
                                                            new DialogueNode(Component.literal("yeah ok i see how it is between us now"))
                                                    )

                                            ),
                                            List.of(TalkingRockBlock.HI2_MESSAGE_IDENTIFIER),
                                            List.of(TalkingRockBlock.ROCKS_QUEST)
                                    )
                            ))
                    );

                    // got claude to make me a little webpage to help create these so i wouldnt go off the rails (sorry ocean) so this is just gonna look a little nasty
                    registerDialogueTree(bootstrap,
                            JotraelineGreatforgeNPC.JOTRAELINE_GREATFORGE_IDENTIFIER,
                            new DialogueTree(List.of(new DialogueTreeOrigin(0,
                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.introduction"),
                                            Optional.of(List.of(
                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_drills_choice"),
                                                            Optional.of(
                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_drills"),
                                                                            Optional.of(List.of(
                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                            Optional.of(
                                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.introduction_go_on"),
                                                                                                            Optional.of(List.of(
                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_sorry_choice"),
                                                                                                                            Optional.of(
                                                                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_sorry"),
                                                                                                                                            Optional.of(List.of(
                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                            Optional.of(
                                                                                                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.introduction_flow"),
                                                                                                                                                                            Optional.of(List.of(
                                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_yes_choice"),
                                                                                                                                                                                            Optional.of(
                                                                                                                                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_yes"),
                                                                                                                                                                                                            false,
                                                                                                                                                                                                            new DialogueChoice(Component.literal("..."), new OpenDrillAttachmentEvent())
                                                                                                                                                                                                    )
                                                                                                                                                                                            ),
                                                                                                                                                                                            Optional.empty(), List.of(JotraelineGreatforgeNPC.INTRODUCTION_MESSAGE_IDENTIFIER),
                                                                                                                                                                                            Optional.empty()),
                                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_no_choice"),
                                                                                                                                                                                            Optional.of(
                                                                                                                                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_no"),
                                                                                                                                                                                                            Optional.of(List.of(
                                                                                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                                                                                            Optional.of(
                                                                                                                                                                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.introduction_scowl"),
                                                                                                                                                                                                                                            true,
                                                                                                                                                                                                                                            new DialogueChoice(Component.literal("...")))
                                                                                                                                                                                                                            ),
                                                                                                                                                                                                                            Optional.empty(),
                                                                                                                                                                                                                            List.of(JotraelineGreatforgeNPC.ANGRY_IDENTIFIER),
                                                                                                                                                                                                                            Optional.empty()
                                                                                                                                                                                                                    )
                                                                                                                                                                                                            )),
                                                                                                                                                                                                            false
                                                                                                                                                                                                    )
                                                                                                                                                                                            ),
                                                                                                                                                                                            Optional.empty(), List.of(),
                                                                                                                                                                                            Optional.empty())
                                                                                                                                                                            )),
                                                                                                                                                                            true)),
                                                                                                                                                            Optional.empty(), List.of(),
                                                                                                                                                            Optional.empty())
                                                                                                                                            )),
                                                                                                                                            false)),
                                                                                                                            Optional.empty(), List.of(),
                                                                                                                            Optional.empty())
                                                                                                            )),
                                                                                                            true)),
                                                                                            Optional.empty(), List.of(),
                                                                                            Optional.empty())
                                                                            )),
                                                                            false)),
                                                            Optional.empty(), List.of(),
                                                            Optional.empty()),
                                                    new DialogueChoice(Component.literal("..."),
                                                            Optional.of(
                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.introduction_smrik"),
                                                                            Optional.of(List.of(
                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_sure_choice"),
                                                                                            Optional.empty(),
                                                                                            Optional.empty(), List.of(JotraelineGreatforgeNPC.INTRODUCTION_MESSAGE_IDENTIFIER),
                                                                                            Optional.of(new OpenDrillAttachmentEvent())),
                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.jotraeline_greatforge.player_no_thanks_choice"))
                                                                            )),
                                                                            true)
                                                            ),
                                                            Optional.empty(),
                                                            List.of(JotraelineGreatforgeNPC.INTRODUCTION_MESSAGE_IDENTIFIER),
                                                            Optional.empty())
                                            )),
                                            true),
                                    Optional.empty(),
                                    List.of()),
                                    createDialogueOrigin(2,
                                            new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.still_angry"),
                                                    true,
                                                    new DialogueChoice(Component.literal("..."), new StartQuestDialogueEvent(JotraelineGreatforgeNPC.APOLOGY_TOUR))
                                            ),
                                            List.of(JotraelineGreatforgeNPC.ANGRY_IDENTIFIER),
                                            List.of()
                                    ),
                                    createDialogueOrigin(1,
                                            new DialogueNode(Component.translatable("dialogue.unshattered.jotraeline_greatforge.greeting"), true, new DialogueChoice(Component.literal("..."), new OpenDrillAttachmentEvent())),
                                            List.of(JotraelineGreatforgeNPC.INTRODUCTION_MESSAGE_IDENTIFIER),
                                            List.of()
                                    )
                            ))
                    );

                    registerDialogueTree(bootstrap,
                            WoolWeaverNPC.WOOL_WEAVER_IDENTIFIER,
                            new DialogueTree(List.of(new DialogueTreeOrigin(1,
                                    new DialogueNode(Component.translatable("dialogue.unshattered.wool_weaver.greeting"),
                                            true,
                                            new DialogueChoice(Component.translatable("dialogue.unshattered.wool_weaver.player_yeah_choice"),
                                                    new DialogueNode(Component.translatable("dialogue.unshattered.wool_weaver.player_yeah"),
                                                            false,
                                                            new DialogueChoice(Component.literal("..."),
                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.wool_weaver.meant_to_be"),
                                                                            true,
                                                                            new DialogueChoice(Component.translatable("dialogue.unshattered.wool_weaver.player_woolhead_choice"),
                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.wool_weaver.player_woolhead"),
                                                                                            false,
                                                                                            new DialogueChoice(Component.literal("..."),
                                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.wool_weaver.woolhead"),
                                                                                                            true,
                                                                                                            new DialogueChoice(Component.literal("..."), List.of(WoolWeaverNPC.WOOLHEAD_IDENTIFIER), new OpenStoreMenuEvent(UnshatteredEntities.WOOL_WEAVER.get()))
                                                                                                    )
                                                                                            )
                                                                                    )
                                                                            ),
                                                                            new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_no_choice"),
                                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.wool_weaver.vibrant"))
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            ),
                                            new DialogueChoice(Component.translatable("dialogue.unshattered.wool_weaver.player_no_choice"),
                                                    new DialogueNode(Component.translatable("dialogue.unshattered.wool_weaver.player_no"),
                                                            false,
                                                            new DialogueChoice(Component.literal("..."),
                                                                    new DialogueNode(Component.translatable("dialogue.unshattered.wool_weaver.really"),
                                                                            true,
                                                                            new DialogueChoice(Component.translatable("dialogue.unshattered.wool_weaver.okay"), new OpenStoreMenuEvent(UnshatteredEntities.WOOL_WEAVER.get())),
                                                                            new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_no_choice"))
                                                                    )
                                                            )
                                                    )
                                            )
                                    ),
                                    List.of(WoolWeaverNPC.INTRODUCTION_MESSAGE_IDENTIFIER))
                            ))
                    );

                    registerDialogueTree(bootstrap,
                            BubuNPC.BUBU_IDENTIFIER,
                            new DialogueTree(List.of(new DialogueTreeOrigin(0,
                                    new DialogueNode(Component.translatable("dialogue.unshattered.bubu_introduction"),
                                            true,
                                            new DialogueChoice(Component.literal("..."),
                                                    new OpenStoreMenuEvent(UnshatteredEntities.BUBU.get()))
                                    )
                            )))
                    );

                    registerDialogueTree(bootstrap,
                            ForgerNPC.FORGER_IDENTIFIER,
                            new DialogueTree(List.of(
                                    new DialogueTreeOrigin(0,
                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_introduction"), true,
                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.forger_player_name_choice"),
                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_name"), false,
                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.forger_player_forge"),
                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_forge"), true,
                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.player_divan"),
                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_divan"), true,
                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_divan_2"), true,
                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.player_dwarven_lords"),
                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords"), true,
                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_2"), true,
                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_3"), true,
                                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_4"), true,
                                                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_dialogue_finished"), true,
                                                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_yes_choice"), new OpenForgeEvent()),
                                                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_no_choice"))
                                                                                                                                                                                            )
                                                                                                                                                                                    )
                                                                                                                                                                            )
                                                                                                                                                                    )
                                                                                                                                                            )
                                                                                                                                                    )
                                                                                                                                            )
                                                                                                                                    )
                                                                                                                            )
                                                                                                                    ),
                                                                                                                    new DialogueChoice(Component.literal("..."), new OpenForgeEvent())
                                                                                                            )
                                                                                                    )
                                                                                            )
                                                                                    ),
                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.player_dwarven_lords"),
                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords"), true,
                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_2"), true,
                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_3"), true,
                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_4"), true,
                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_dialogue_finished"), true,
                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_yes_choice"), new OpenForgeEvent()),
                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_no_choice"))
                                                                                                                                                            )
                                                                                                                                                    )
                                                                                                                                            )
                                                                                                                                    )
                                                                                                                            )
                                                                                                                    )
                                                                                                            )
                                                                                                    )
                                                                                            )
                                                                                    ),
                                                                                    new DialogueChoice(Component.literal("..."), new OpenForgeEvent())
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.forger_player_forge"),
                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_forge"), true,
                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.player_divan"),
                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_divan"), true,
                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_divan_2"), true,
                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.player_dwarven_lords"),
                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords"), true,
                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_2"), true,
                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_3"), true,
                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_4"), true,
                                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_dialogue_finished"), true,
                                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_yes_choice")),
                                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_no_choice"))
                                                                                                                                                                            )
                                                                                                                                                                    )
                                                                                                                                                            )
                                                                                                                                                    )
                                                                                                                                            )
                                                                                                                                    )
                                                                                                                            )
                                                                                                                    )
                                                                                                            )
                                                                                                    ),
                                                                                                    new DialogueChoice(Component.literal("..."), new OpenForgeEvent())
                                                                                            )
                                                                                    )
                                                                            )
                                                                    ),
                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.player_dwarven_lords"),
                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords"), true,
                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_2"), true,
                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_3"), true,
                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_lords_4"), true,
                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.player_divan"),
                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_divan"), true,
                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_divan_2"), true,
                                                                                                                                                                    new DialogueChoice(Component.literal("..."),
                                                                                                                                                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_dialogue_finished"), true,
                                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_yes_choice")),
                                                                                                                                                                                    new DialogueChoice(Component.translatable("dialogue.unshattered.generic.player_no_choice"))
                                                                                                                                                                            )
                                                                                                                                                                    )
                                                                                                                                                            )
                                                                                                                                                    )
                                                                                                                                            )
                                                                                                                                    ),
                                                                                                                                    new DialogueChoice(Component.literal("..."), new OpenForgeEvent())
                                                                                                                            )
                                                                                                                    )
                                                                                                            )
                                                                                                    )
                                                                                            )
                                                                                    )
                                                                            )
                                                                    ),
                                                                    new DialogueChoice(Component.literal("..."), new OpenForgeEvent())
                                                            )
                                                    )
                                            ),
                                            Optional.empty(),
                                            List.of(ForgerNPC.FORGER_INTRODUCTION)
                                    ),
                                    new DialogueTreeOrigin(1,
                                            new DialogueNode(Component.translatable("dialogue.unshattered.forger_player_return"),
                                                    true,
                                                    new DialogueChoice(Component.literal("..."), new OpenForgeEvent())
                                            ),
                                            Optional.of(new DialogueFlagRequirements(List.of(ForgerNPC.FORGER_INTRODUCTION), List.of())),
                                            List.of()
                                    )
                            ))
                    );

                    registerDialogueTree(bootstrap,
                            JennyNPC.JENNY_IDENTIFIER,
                            new DialogueTree(List.of(new DialogueTreeOrigin(2,
                                    new DialogueNode(Component.translatable("dialogue.unshattered.jenny_introduction"),
                                            true,
                                            new DialogueChoice(Component.literal("..."), new OpenStoreMenuEvent(UnshatteredEntities.JENNY.get()))),
                                    List.of(JennyNPC.JENNY_INTRODUCTION))
                            ))
                    );
                }).add(DataPackRegistryHandler.QUEST_REGISTRY_KEY, bootstrap -> {
                    createQuest(bootstrap, TalkingRockBlock.ROCKS_QUEST, QuestTypes.NOVICE, new GiveItemDialogueEvent(BuiltInRegistries.ITEM.wrapAsHolder(Items.STONE), 1));

                    bootstrap.register(ResourceKey.create(DataPackRegistryHandler.QUEST_REGISTRY_KEY, JotraelineGreatforgeNPC.APOLOGY_TOUR), new Quest(QuestTypes.NOVICE, Optional.empty()));
                }).add(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, bootstrap -> {
                    createRegenPathWithBlocks(bootstrap, "stone", List.of(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get(),
                                    UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(),
                                    Blocks.BEDROCK
                            ), 120
                    );

                    createRegenPathWithBlocks(bootstrap, "coal", List.of(UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );

                    createRegenPathWithBlocks(bootstrap, "iron", List.of(UnshatteredBlocks.BREAKABLE_IRON_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );

                    createRegenPathWithBlocks(bootstrap, "copper", List.of(UnshatteredBlocks.BREAKABLE_COPPER_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );

                    createRegenPathWithBlocks(bootstrap,"gold", List.of(UnshatteredBlocks.BREAKABLE_GOLD_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );

                    createRegenPathWithBlocks(bootstrap, "redstone", List.of(UnshatteredBlocks.BREAKABLE_REDSTONE_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );

                    createRegenPathWithBlocks(bootstrap, "emerald", List.of(UnshatteredBlocks.BREAKABLE_EMERALD_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );

                    createRegenPathWithBlocks(bootstrap, "diamond", List.of(UnshatteredBlocks.BREAKABLE_DIAMOND_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );

                    createRegenPathWithBlocks(bootstrap, "lapis", List.of(UnshatteredBlocks.BREAKABLE_LAPIS_ORE_BLOCK.get(),
                                    UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );

                    createRegenPathWithBlocks(bootstrap, "pure_diamond", List.of(UnshatteredBlocks.PURE_DIAMOND_BLOCK.get(),
                            Blocks.BEDROCK),
                            200
                    );

                    createRegenPathWithBlocks(bootstrap, "obsidian", List.of(UnshatteredBlocks.BREAKABLE_OBSIDIAN_BLOCK.get(),
                                    Blocks.BEDROCK),
                            240
                    );

                    createRegenPathWithBlocks(bootstrap, "fig_wood", List.of(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get(),
                            Blocks.AIR),
                            200
                    );

                    createRegenPathWithBlocks(bootstrap, "oak_wood", List.of(UnshatteredBlocks.BREAKABLE_OAK_LOG_BLOCK.get(),
                                    Blocks.AIR),
                            200
                    );

                    createRegenPathWithBlocks(bootstrap, "birch_wood", List.of(UnshatteredBlocks.BREAKABLE_BIRCH_LOG_BLOCK.get(),
                                    Blocks.AIR),
                            200
                    );

                    createRegenPathWithBlocks(bootstrap, "spruce_wood", List.of(UnshatteredBlocks.BREAKABLE_SPRUCE_LOG_BLOCK.get(),
                                    Blocks.AIR),
                            200
                    );

                    createRegenPathWithBlocks(bootstrap, "jungle_wood", List.of(UnshatteredBlocks.BREAKABLE_JUNGLE_LOG_BLOCK.get(),
                                    Blocks.AIR),
                            200
                    );

                    createRegenPathWithBlocks(bootstrap, "acacia_wood", List.of(UnshatteredBlocks.BREAKABLE_ACACIA_LOG_BLOCK.get(),
                                    Blocks.AIR),
                            200
                    );

                    createRegenPathWithBlocks(bootstrap, "dark_oak_wood", List.of(UnshatteredBlocks.BREAKABLE_DARK_OAK_LOG_BLOCK.get(),
                                    Blocks.AIR),
                            200
                    );

                    createRegenPathWithBlocks(bootstrap, "oak_leaf", List.of(Blocks.OAK_LEAVES,
                                    Blocks.AIR),
                            200
                    );

                    createRegenPath(bootstrap,
                            "soft_mithril",
                            List.of(UnshatteredBlocks.BREAKABLE_SOFT_MITHRIL_BLOCK.get().defaultBlockState(), Blocks.BEDROCK.defaultBlockState()),
                            220,
                            1,
                            new RegenPath.RegenVariant(UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK.get()
                                    .defaultBlockState()
                                    .setValue(BreakableTitaniumBlock.ORIGIN,
                                            BreakableTitaniumBlock.MithrilOrigin.SOFT
                                    ),
                                    0.005f
                            ),
                            null
                    );

                    createRegenPath(bootstrap,
                            "hard_mithril",
                            List.of(UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK.get().defaultBlockState(), Blocks.BEDROCK.defaultBlockState()),
                            220,
                            1,
                            new RegenPath.RegenVariant(UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK.get()
                                    .defaultBlockState()
                                    .setValue(BreakableTitaniumBlock.ORIGIN,
                                            BreakableTitaniumBlock.MithrilOrigin.HARD
                                    ),
                                    0.005f
                            ),
                            null
                    );

                    createRegenPath(bootstrap,
                            "cobbled_mithril",
                            List.of(UnshatteredBlocks.BREAKABLE_COBBLED_MITHRIL_BLOCK.get().defaultBlockState(), Blocks.BEDROCK.defaultBlockState()),
                            220,
                            1,
                            new RegenPath.RegenVariant(UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK.get()
                                    .defaultBlockState()
                                    .setValue(BreakableTitaniumBlock.ORIGIN,
                                            BreakableTitaniumBlock.MithrilOrigin.COBBLED
                                    ),
                                    0.005f
                            ),
                            null
                    );

                    createRegenPathWithBlocks(bootstrap, "ice", List.of(UnshatteredBlocks.BREAKABLE_ICE_BLOCK.get(), Blocks.AIR), 220);

                    createCropRegenPath(bootstrap, Blocks.WHEAT, UnshatteredBlocks.BREAKABLE_WHEAT_BLOCK.get(), 7);

                    createCropRegenPath(bootstrap,
                            Blocks.POTATOES,
                            UnshatteredBlocks.BREAKABLE_POTATO_BLOCK.get(),
                            4,
                            new RegenPath.RegenVariant(UnshatteredBlocks.GIANT_POTATO.get().defaultBlockState(), 0.01f),
                            null
                    );

                    createCropRegenPath(bootstrap,
                            Blocks.CARROTS,
                            UnshatteredBlocks.BREAKABLE_CARROT_BLOCK.get(),
                            4,
                            new RegenPath.RegenVariant(UnshatteredBlocks.GIANT_CARROT.get().defaultBlockState(), 0.01f),
                            null
                    );


                    createRegenPathWithBlocks(bootstrap, "pumpkin", List.of(UnshatteredBlocks.BREAKABLE_PUMPKIN_BLOCK.get(), Blocks.AIR), 560);

                    createRegenPathWithBlocks(bootstrap, "melon", List.of(UnshatteredBlocks.BREAKABLE_MELON_BLOCK.get(), Blocks.AIR), 560);

                    createRegenPathWithBlocks(bootstrap, "sand", List.of(UnshatteredBlocks.BREAKABLE_SAND_BLOCK.get(), Blocks.SANDSTONE), 180);

                    createRegenPathWithBlocks(bootstrap, "red_mushroom", List.of(UnshatteredBlocks.BREAKABLE_RED_MUSHROOM.get(), Blocks.AIR), 400);

                    createRegenPathWithBlocks(bootstrap, "brown_mushroom", List.of(UnshatteredBlocks.BREAKABLE_BROWN_MUSHROOM.get(), Blocks.AIR), 400);

                    createRegenPathWithBlocks(bootstrap, "red_mushroom_block", List.of(UnshatteredBlocks.BREAKABLE_RED_MUSHROOM_BLOCK.get(), Blocks.AIR), 600);

                    createRegenPathWithBlocks(bootstrap, "brown_mushroom_block", List.of(UnshatteredBlocks.BREAKABLE_BROWN_MUSHROOM_BLOCK.get(), Blocks.AIR), 600);

                    createRegenPath(bootstrap,
                            "cactus",
                            List.of(UnshatteredBlocks.BREAKABLE_CACTUS_BLOCK.get().defaultBlockState(),
                                    Blocks.AIR.defaultBlockState()
                            ),
                            500,
                            1,
                            null,
                            RegenCondition.NOT_ABOVE_AIR
                    );

                    createRegenPath(bootstrap,
                            "sugarcane",
                            List.of(UnshatteredBlocks.BREAKABLE_SUGARCANE_BLOCK.get().defaultBlockState(), Blocks.AIR.defaultBlockState()),
                            500,
                            1,
                            null,
                            RegenCondition.NOT_ABOVE_AIR
                    );

                    createRegenPathWithBlocks(bootstrap, "cocoa", List.of(UnshatteredBlocks.BREAKABLE_COCOA_BEAN_BLOCK.get(), Blocks.AIR), 600);
                }).add(DataPackRegistryHandler.FISHING_MISC_REWARD_KEY, bootstrap -> {
                    bootstrap.register(createMiscRewardResourceKey("good_catch"), new CoinReward(25000,
                            5000,
                            "good_catch",
                            0xFF810AF3,
                            100.0f,
                            Optional.of(FishingCondition.NONE),
                            20.0d,
                            Optional.of(1))
                    );

                    bootstrap.register(createMiscRewardResourceKey("great_catch"), new CoinReward(100000,
                            250000,
                            "great_catch",
                            0xFFFFAA00,
                            1000.0f,
                            Optional.of(FishingCondition.NONE),
                            5.0d,
                            Optional.of(5))
                    );

                    bootstrap.register(createMiscRewardResourceKey("outstanding_catch"), new CoinReward(500000,
                            1000000,
                            "outstanding_catch",
                            0xFFFF55FF,
                            10000.0f,
                            Optional.of(FishingCondition.NONE),
                            1.0d,
                            Optional.of(10))
                    );
                })
        );
    }

    private static void registerRegionBoundary(BootstrapContext<RegionBoundary> bootstrap, BoundaryCoordinates boundaryCoordinates, ResourceKey<Region> region, HolderGetter<Region> regions) {
        Holder<Region> regionHolder = regions.getOrThrow(region);

        bootstrap.register(
                ResourceKey.create(DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY, UnshatteredUtils.getUnshatteredIdentifier(region.identifier().getPath() + "_bounds")),
                new RegionBoundary(regionHolder, boundaryCoordinates)
        );
    }

    private static void registerDialogueTree(BootstrapContext<DialogueTree> bootstrap, Identifier dialogueTreeIdentifier, DialogueTree dialogueTree) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY, dialogueTreeIdentifier), dialogueTree);
    }

    private static DialogueTreeOrigin createDialogueOrigin(int priority, DialogueNode dialogueNode, List<Identifier> requiredFlags, List<Identifier> excludedFlags) {
        return new DialogueTreeOrigin(priority, dialogueNode, Optional.of(new DialogueFlagRequirements(requiredFlags, excludedFlags)), List.of());
    }

    /**
     * @return a dialogue tree origin with itself as a flag along with any extra flags to be tracked
     */
    private static DialogueTreeOrigin createDialogueOriginWithSelfFlag(int priority, DialogueNode dialogueNode, List<Identifier> requiredFlags, List<Identifier> excludedFlags, List<Identifier> extraSetFlags, Identifier treeOriginIdentifier) {
        List<Identifier> combinedSetFlags = new ArrayList<>(1 + extraSetFlags.size());
        combinedSetFlags.add(treeOriginIdentifier);
        combinedSetFlags.addAll(extraSetFlags);

        return new DialogueTreeOrigin(priority, dialogueNode, Optional.of(new DialogueFlagRequirements(requiredFlags, excludedFlags)), combinedSetFlags);
    }

    /**
     * @return a dialogue tree origin with itself as a flag to be tracked
     */
    private static DialogueTreeOrigin createDialogueOriginWithSelfFlag(int priority, DialogueNode dialogueNode, Identifier treeOriginIdentifier) {
        return new DialogueTreeOrigin(priority, dialogueNode, List.of(treeOriginIdentifier));
    }

    private static void createQuest(BootstrapContext<Quest> bootstrap, Identifier questIdentifier, QuestTypes questTypes, DialogueTriggeredEvent questCompleteEvent) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.QUEST_REGISTRY_KEY, questIdentifier), new Quest(questTypes, Optional.of(questCompleteEvent)));
    }

    private static void createQuest(BootstrapContext<Quest> bootstrap, Identifier questIdentifier, QuestTypes questTypes) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.QUEST_REGISTRY_KEY, questIdentifier), new Quest(questTypes));
    }

    private static void createRegenPathWithBlocks(BootstrapContext<RegenPath> bootstrap, String identifier, List<Block> blocks, int regenTicks) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, UnshatteredUtils.getUnshatteredIdentifier(identifier)),
                new RegenPath(blocks.stream().map(Block::defaultBlockState).toList(), regenTicks));
    }

    private static void createRegenPath(BootstrapContext<RegenPath> bootstrap, String identifier, List<BlockState> blocks, int regenTicks, int stagesIncremented, @Nullable RegenPath.RegenVariant variant, @Nullable RegenCondition condition) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, UnshatteredUtils.getUnshatteredIdentifier(identifier)),
                new RegenPath(blocks, regenTicks, stagesIncremented, Optional.ofNullable(variant), Optional.ofNullable(condition)));
    }

    private static void createRegenPath(BootstrapContext<RegenPath> bootstrap, String identifier, List<BlockState> blocks, int regenTicks) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, UnshatteredUtils.getUnshatteredIdentifier(identifier)),
                new RegenPath(blocks, regenTicks));
    }

    /**
     * @param blockStateCrop crop that presumably has seven unbreakable stages to get block states from
     * @param finalStageCrop the final growth stage
     */
    private static void createCropRegenPath(BootstrapContext<RegenPath> bootstrap, Block blockStateCrop, Block finalStageCrop, int stages) {
        createCropRegenPath(bootstrap, blockStateCrop, finalStageCrop, stages, null, null);
    }

    /**
     * @param blockStateCrop crop that presumably has seven unbreakable stages to get block states from
     * @param finalStageCrop the final growth stage
     */
    private static void createCropRegenPath(BootstrapContext<RegenPath> bootstrap, Block blockStateCrop, Block finalStageCrop, int stages, RegenPath.RegenVariant regenVariant, @Nullable RegenCondition condition) {
        List<BlockState> blockStates = new ArrayList<>();
        blockStates.add(finalStageCrop.defaultBlockState());

        for (int stage = stages - 1; stage >= 0; stage--) {
            blockStates.add(blockStateCrop.defaultBlockState().setValue(CropBlock.AGE, stage));
        }

        createRegenPath(bootstrap, BuiltInRegistries.BLOCK.getKey(finalStageCrop).getPath(), blockStates, 200, stages - 1, regenVariant, condition);
    }

    private static ResourceKey<TriggerMiscReward> createMiscRewardResourceKey(String path) {
        return ResourceKey.create(DataPackRegistryHandler.FISHING_MISC_REWARD_KEY, UnshatteredUtils.getUnshatteredIdentifier(path));
    }
}
