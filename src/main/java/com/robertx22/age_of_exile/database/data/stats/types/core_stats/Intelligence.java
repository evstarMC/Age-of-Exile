package com.robertx22.age_of_exile.database.data.stats.types.core_stats;

import com.robertx22.age_of_exile.database.OptScaleExactStat;
import com.robertx22.age_of_exile.database.data.stats.types.core_stats.base.BaseCoreStat;
import com.robertx22.age_of_exile.database.data.stats.types.resources.magic_shield.MagicShield;
import com.robertx22.age_of_exile.database.data.stats.types.resources.mana.Mana;
import com.robertx22.age_of_exile.uncommon.enumclasses.ModType;

import java.util.Arrays;

public class Intelligence extends BaseCoreStat {

    private Intelligence() {
        super(Arrays.asList(
            new OptScaleExactStat(5, 5, Mana.getInstance(), ModType.FLAT),
            new OptScaleExactStat(2, 2, MagicShield.getInstance(), ModType.LOCAL_INCREASE)
        ));
        this.statGroup = StatGroup.CORE;
    }

    public static final Intelligence INSTANCE = new Intelligence();
    public static String GUID = "intelligence";

    @Override
    public String GUID() {
        return GUID;
    }

    @Override
    public String locNameForLangFile() {
        return "Intelligence";
    }
}




