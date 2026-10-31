package io.github.moosyu.abilities;

import io.github.moosyu.abilities.data.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredAbilities {
    public static final ResourceKey<Registry<Ability>> ABILITY_KEY = ResourceKey.createRegistryKey(UnshatteredUtils.getUnshatteredIdentifier("ability"));
    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(ABILITY_KEY, MODID);
    public static final Registry<Ability> REGISTRY = ABILITIES.makeRegistry(builder -> builder.sync(true));

    public static final DeferredHolder<Ability, Blazeborn> BLAZEBORN = ABILITIES.register("blazeborn", Blazeborn::new);

    public static final DeferredHolder<Ability, ParkEnthusiast> TREECAPITATOR_PARK_ENTHUSIAST = ABILITIES.register("treecapitator_park_enthusiast", () -> new ParkEnthusiast(50));

    public static final DeferredHolder<Ability, ParkEnthusiast> SPRUCE_AXE_PARK_ENTHUSIAST = ABILITIES.register("spruce_axe_park_enthusiast", () -> new ParkEnthusiast(25));

    public static final DeferredHolder<Ability, FishOutOfWater> FISH_OUT_OF_WATER = ABILITIES.register("fish_out_of_water", FishOutOfWater::new);

    public static final DeferredHolder<Ability, FishOutOfLava> FISH_OUT_OF_LAVA = ABILITIES.register("fish_out_of_lava", FishOutOfLava::new);

    public static final DeferredHolder<Ability, ExplosiveArrows> EXPLOSIVE_ARROWS = ABILITIES.register("explosive_arrows", ExplosiveArrows::new);

    public static final DeferredHolder<Ability, DrillEngineBoost> MITHRIL_PLATED_DRILL_ENGINE_UBERCHARGED = ABILITIES.register("ubercharged", () -> new DrillEngineBoost(8, 5));

    public static final DeferredHolder<Ability, Frugal> FRUGAL = ABILITIES.register("frugal", Frugal::new);

    public static final DeferredHolder<Ability, Lifesteal> BAT_TALISMAN_LIFESTEAL = ABILITIES.register("bat_artifact_lifesteal", () -> new Lifesteal(0.05f));

    public static final DeferredHolder<Ability, DecreaseMiningAbilityCooldown> MITHRIL_INFUSED_FUEL_TANK_DECREASE_MINING_ABILITY_COOLDOWN = ABILITIES.register("mithril_infused_fuel_tank_decrease_mining_ability_cooldown", () -> new DecreaseMiningAbilityCooldown(-0.2f));

    public static final DeferredHolder<Ability, PainToPower> PAIN_TO_POWER = ABILITIES.register("pain_to_power", PainToPower::new);

    public static final DeferredHolder<Ability, Accrual> ACCRUAL = ABILITIES.register("accrual", Accrual::new);

    public static final DeferredHolder<Ability, Ability> STABILITY = ABILITIES.register("stability", () -> new Ability(new ItemAbility(),
            AbilityTriggerType.PLAYER_INCOMING_KNOCKBACK,
            new AbilityResults().add(AbilityResult.CANCEL_EVENT, true))
    );

    public static final DeferredHolder<Ability, HeavyStrike> HEAVY_STRIKE = ABILITIES.register("heavy_strike", HeavyStrike::new);

    public static final DeferredHolder<Ability, ApianAegis> APIAN_AEGIS = ABILITIES.register("apian_aegis", ApianAegis::new);

    public static final DeferredHolder<Ability, Ability> BOTTOMLESS = ABILITIES.register("bottomless", () -> new Ability(
            new ItemAbility(),
            AbilityTriggerType.PLAYER_USE_PROJECTILE_WEAPON_AMMO,
            new AbilityResults().add(AbilityResult.CANCEL_EVENT, true))
    );

    public static final DeferredHolder<Ability, Ability> SAVING_GRACE = ABILITIES.register("saving_grace", () -> new Ability(
            new ItemAbility(),
            AbilityTriggerType.PLAYER_TAKE_DAMAGE,
            new AbilityResults().add(AbilityResult.CANCEL_COIN_LOSS, true)
    ));

    public static final DeferredHolder<Ability, WoolenBarrier> WOOLEN_BARRIER = ABILITIES.register("woolen_barrier", WoolenBarrier::new);

    public static final DeferredHolder<Ability, Incremental> PROMISING_AXE_INCREMENTAL = ABILITIES.register("promising_axe_incremental", () -> new Incremental(
            List.of(new Incremental.AttributeStage(UnshatteredAttributeValues.SWEEP.holder, 1)),
            15,
            200,
            "tooltip.unshattered.incremental.blocks_broken",
            AbilityTriggerType.PLAYER_BREAK_MINING_BLOCK
            )
    );

    public static final DeferredHolder<Ability, Incremental> PROMISING_PICKAXE_INCREMENTAL = ABILITIES.register("promising_pickaxe_incremental", () -> new Incremental(
                    List.of(new Incremental.AttributeStage(UnshatteredAttributeValues.MINING_SPEED.holder, 3)),
                    25,
                    100,
                    "tooltip.unshattered.incremental.blocks_broken",
                    AbilityTriggerType.PLAYER_BREAK_MINING_BLOCK
            )
    );

    public static final DeferredHolder<Ability, MindBlowing> MINDBLOWING = ABILITIES.register("mindblowing", MindBlowing::new);

    public static final DeferredHolder<Ability, Greed> GREED = ABILITIES.register("greed", Greed::new);

    public static final DeferredHolder<Ability, Cleave> RUSTY_CLEAVER_CLEAVE = ABILITIES.register("rusty_cleaver_cleave", () -> new Cleave(2.0f, 0.1f));

    public static final DeferredHolder<Ability, Cleave> GOLDEN_CLEAVER_CLEAVE = ABILITIES.register("golden_cleaver_cleave", () -> new Cleave(3.0f, 0.2f));

    public static final DeferredHolder<Ability, Cleave> SUPER_CLEAVER_CLEAVE = ABILITIES.register("super_cleaver_cleave", () -> new Cleave(3.0f, 0.3f));

    public static final DeferredHolder<Ability, Cleave> HYPER_CLEAVER_CLEAVE = ABILITIES.register("hyper_cleaver_cleave", () -> new Cleave(4.0f, 0.4f));

    public static final DeferredHolder<Ability, Cleave> GIANT_CLEAVER_CLEAVE = ABILITIES.register("giant_cleaver_cleave", () -> new Cleave(5.0f, 0.5f));

    public static final DeferredHolder<Ability, Rotten> ROTTEN = ABILITIES.register("rotten", Rotten::new);

    public static final DeferredHolder<Ability, TitaniumFanatic> RUSTED_TITANIUM_PICKAXE_TITANIUM_FANATIC = ABILITIES.register("rusted_titanium_pickaxe_titanium_fanatic", () ->
            new TitaniumFanatic(15)
    );

    public static final DeferredHolder<Ability, TitaniumFanatic> TITANIUM_PICKAXE_TITANIUM_FANATIC = ABILITIES.register("titanium_pickaxe_titanium_fanatic", () ->
            new TitaniumFanatic(20)
    );

    public static final DeferredHolder<Ability, MithrilMiner> BROKEN_MITHRIL_PICKAXE_MITHRIL_MINER = ABILITIES.register("broken_mithril_pickaxe_mithril_miner", () ->
            new MithrilMiner(20)
    );

    public static final DeferredHolder<Ability, MithrilMiner> BANDAGED_MITHRIL_PICKAXE_MITHRIL_MINER = ABILITIES.register("bandaged_mithril_pickaxe_mithril_miner", () ->
            new MithrilMiner(25)
    );

    public static final DeferredHolder<Ability, MithrilMiner> MITHRIL_PICKAXE_MITHRIL_MINER = ABILITIES.register("mithril_pickaxe_mithril_miner", () ->
            new MithrilMiner(30)
    );

    public static final DeferredHolder<Ability, Lifesteal> BAT_ARTIFACT_LIFESTEAL = ABILITIES.register("bat_artifact_lifesteal", () -> new Lifesteal(0.15f));

    public static final DeferredHolder<Ability, BeachBoy> SPEEDY_LINE_BEACH_BOY = ABILITIES.register("speedy_line_beach_boy", () ->
            new BeachBoy(new BeachBoy.LineAttributeGroup(UnshatteredAttributeValues.FISHING_SPEED.holder,
                    new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("speedy_line_fishing_speed"),
                            10,
                            AttributeModifier.Operation.ADD_VALUE
                    )
            ))
    );

    public static final DeferredHolder<Ability, Moskstraumen> MOSKSTRAUMEN = ABILITIES.register("moskstraumen", Moskstraumen::new);

    public static final DeferredHolder<Ability, SpeedBoost> SPEED_BOOST = ABILITIES.register("speed_boost", SpeedBoost::new);

    public static final DeferredHolder<Ability, InstantHeal> ZOMBIE_SWORD_INSTANT_HEAL = ABILITIES.register("zombie_sword_instant_heal", () -> new InstantHeal(new ItemAbility(24, 10, 0), 40));

    public static final DeferredHolder<Ability, InstantHeal> ORNATE_ZOMBIE_SWORD_INSTANT_HEAL = ABILITIES.register("ornate_zombie_sword_instant_heal", () -> new InstantHeal(new ItemAbility(30, 10, 0), 50));

    public static final DeferredHolder<Ability, InstantHeal> FLORID_ZOMBIE_SWORD_INSTANT_HEAL = ABILITIES.register("florid_zombie_sword_instant_heal", () -> new InstantHeal(new ItemAbility(36, 10, 0), 60));
}
