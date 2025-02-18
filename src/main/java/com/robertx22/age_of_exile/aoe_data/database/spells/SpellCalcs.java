package com.robertx22.age_of_exile.aoe_data.database.spells;

import com.robertx22.age_of_exile.database.data.stats.types.resources.health.Health;
import com.robertx22.age_of_exile.database.data.value_calc.ValueCalcBuilder;
import com.robertx22.age_of_exile.database.data.value_calc.ValueCalculation;

public class SpellCalcs {

    public static void init() {

    }

    public static ValueCalculation POISON = ValueCalcBuilder.of("poison")
        .attackScaling(0.05F, 0.05F)
        .build();
    public static ValueCalculation BLEED = ValueCalcBuilder.of("bleed")
        .attackScaling(0.2F, 0.2F)
        .build();

    public static ValueCalculation BURN = ValueCalcBuilder.of("burn")
        .attackScaling(0.2F, 0.2F)
        .build();

    public static ValueCalculation PETRIFY = ValueCalcBuilder.of("petrify")
        .attackScaling(0.2F, 1F)
        .build();
    public static ValueCalculation TORMENT = ValueCalcBuilder.of("torment")
        .attackScaling(0.2F, 0.6F)
        .build();
    public static ValueCalculation DESPAIR = ValueCalcBuilder.of("despair")
        .attackScaling(0.2F, 0.6F)
        .build();
    public static ValueCalculation DIRECT_ARROW_HIT = ValueCalcBuilder.of("direct_arrow_hit")
        .attackScaling(0.2F, 0.6F)
        .build();

    public static ValueCalculation GONG_STRIKE = ValueCalcBuilder.of("gong_strike")
        .statScaling(Health.getInstance(), 0.1F, 0.25F)
        .build();

    public static ValueCalculation WHIRLWIND = ValueCalcBuilder.of("whirlwind")
        .attackScaling(0.2F, 0.6F)
        .build();
    public static ValueCalculation BREATH = ValueCalcBuilder.of("breath")
        .attackScaling(0.2F, 0.6F)
        .build();

    public static ValueCalculation ARROW_STORM = ValueCalcBuilder.of("arrow_storm")
        .attackScaling(0.3F, 0.6F)
        .build();
    public static ValueCalculation EXPLOSIVE_ARROW = ValueCalcBuilder.of("explosive_arrow")
        .attackScaling(0.5F, 1.5F)
        .build();
    public static ValueCalculation POISON_ARROW = ValueCalcBuilder.of("poison_arrow")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation RANGER_TRAP = ValueCalcBuilder.of("ranger_trap")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation AWAKEN_MANA = ValueCalcBuilder.of("awaken_mana")
        .baseValue(20, 100)
        .build();
    public static ValueCalculation HUNTER_POTION_HEAL = ValueCalcBuilder.of("hunter_pot_heal")
        .baseValue(20, 100)
        .build();
    public static ValueCalculation WISH = ValueCalcBuilder.of("wish")
        .baseValue(20, 80)
        .build();
    public static ValueCalculation NATURE_BALM = ValueCalcBuilder.of("nature_balm")
        .baseValue(1, 5)
        .build();
    public static ValueCalculation FIREBALL = ValueCalcBuilder.of("fireball")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation POWER_CHORD = ValueCalcBuilder.of("power_chord")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation SHOOTING_STAR = ValueCalcBuilder.of("shooting_star")
        .attackScaling(0.5F, 1.25F)
        .build();
    public static ValueCalculation TIDAL_STRIKE = ValueCalcBuilder.of("tidal_strike")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation FIRE_NOVA = ValueCalcBuilder.of("fire_nova")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation METEOR = ValueCalcBuilder.of("meteor")
        .attackScaling(0.75F, 2F)
        .build();
    public static ValueCalculation FLAME_STRIKE = ValueCalcBuilder.of("flame_strike")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation HEALING_AURA = ValueCalcBuilder.of("healing_aura")
        .baseValue(4, 6)
        .build();
    public static ValueCalculation HEART_OF_ICE = ValueCalcBuilder.of("heart_of_ice")
        .baseValue(10, 50)
        .build();
    public static ValueCalculation FROST_NOVA = ValueCalcBuilder.of("frost_nova")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation POISON_CLOUD = ValueCalcBuilder.of("poison_cloud")
        .attackScaling(0.25F, 0.5F)
        .build();
    public static ValueCalculation POISON_BALL = ValueCalcBuilder.of("poisonball")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation ICEBALL = ValueCalcBuilder.of("iceball")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation SHOUT_WARN = ValueCalcBuilder.of("shout_warn")
        .statScaling(Health.getInstance(), 0.05F, 0.1F)
        .build();
    public static ValueCalculation CHARGED_BOLT = ValueCalcBuilder.of("charged_bolt")
        .attackScaling(0.5F, 1F)
        .build();
    public static ValueCalculation EXECUTE = ValueCalcBuilder.of("execute")
        .attackScaling(1F, 2F)
        .build();
    public static ValueCalculation CHARGE = ValueCalcBuilder.of("charge")
        .attackScaling(0.3F, 0.6F)
        .build();
    public static ValueCalculation TAUNT = ValueCalcBuilder.of("taunt")
        .statScaling(Health.getInstance(), 0.05F, 0.1F)
        .build();
    public static ValueCalculation PULL = ValueCalcBuilder.of("pull")
        .attackScaling(0.2F, 0.3F)
        .build();
    public static ValueCalculation SHRED = ValueCalcBuilder.of("shred")
        .attackScaling(0.3F, 0.6F)
        .build();
    public static ValueCalculation TOTEM_HEAL = ValueCalcBuilder.of("totem_heal")
        .baseValue(2, 3)
        .build();
    public static ValueCalculation TOTEM_GUARD = ValueCalcBuilder.of("totem_guard")
        .baseValue(3, 6)
        .build();
    public static ValueCalculation TOTEM_MANA = ValueCalcBuilder.of("totem_mana")
        .baseValue(3, 6)
        .build();
    public static ValueCalculation CURSE = ValueCalcBuilder.of("curse")
        .attackScaling(0.2F, 0.6F)
        .build();
    public static ValueCalculation BLACK_HOLE = ValueCalcBuilder.of("black_hole")
        .attackScaling(0.2F, 1F)
        .build();
    public static ValueCalculation CHILLING_FIELD = ValueCalcBuilder.of("chilling_field")
        .attackScaling(0.1F, 0.5F)
        .build();
    public static ValueCalculation SMOKE_BOMB = ValueCalcBuilder.of("lose_aggro")
        .baseValue(20, 40)
        .build();

}
