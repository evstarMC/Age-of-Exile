package com.robertx22.age_of_exile.aoe_data.database.scroll_buffs;

import com.robertx22.age_of_exile.aoe_data.database.stats.Stats;
import com.robertx22.age_of_exile.database.data.StatModifier;
import com.robertx22.age_of_exile.database.data.scroll_buff.ScrollBuff;
import com.robertx22.age_of_exile.database.data.stats.types.offense.SpellDamage;
import com.robertx22.age_of_exile.database.data.stats.types.resources.health.HealthRegen;
import com.robertx22.age_of_exile.database.data.stats.types.resources.mana.ManaRegen;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import com.robertx22.age_of_exile.uncommon.enumclasses.ModType;
import com.robertx22.age_of_exile.uncommon.enumclasses.PlayStyle;
import com.robertx22.library_of_exile.registry.ExileRegistryInit;

public class ScrollBuffsAdder implements ExileRegistryInit {

    @Override
    public void registerAll() {

        ScrollBuff.of("mage", "Of the Mage", "Veneficus",
            new StatModifier(10, 30, SpellDamage.getInstance()),
            new StatModifier(10, 30, ManaRegen.getInstance(), ModType.PERCENT)
        );
        ScrollBuff.of("hunter", "Of the Hunter", "Venator",
            new StatModifier(10, 30, Stats.STYLE_DAMAGE.get(PlayStyle.ranged)),
            new StatModifier(10, 30, Stats.CRIT_DAMAGE.get(), ModType.PERCENT)
        );
        ScrollBuff.of("warrior", "Of the Warrior", "Torpent",
            new StatModifier(10, 30, Stats.STYLE_DAMAGE.get(PlayStyle.melee)),
            new StatModifier(8, 25, Stats.ATTACK_SPEED.get(), ModType.FLAT)
        );
        ScrollBuff.of("paladin", "Of the Paladin", "Fortissimus",
            new StatModifier(10, 30, Stats.STYLE_DAMAGE.get(PlayStyle.melee)),
            new StatModifier(10, 30, HealthRegen.getInstance(), ModType.PERCENT)
        );

        ScrollBuff.of("crit", "Of Criticals", "Verum",
            new StatModifier(10, 30, Stats.CRIT_DAMAGE.get()),
            new StatModifier(10, 30, Stats.SPELL_CRIT_DAMAGE.get())
        );

        ScrollBuff.of("costly_spell_dmg", "Of Sacrificial Magic", "Feodo",
            new StatModifier(20, 40, SpellDamage.getInstance()),
            new StatModifier(10, 30, Stats.MANA_COST.get())
        );

        eleDmg("fire_dmg", "Of Firestorms", "Ignis", Elements.Fire);
        eleDmg("cold_dmg", "Of Snowstorms", "Frigus", Elements.Water);
        eleDmg("poison_dmg", "Of Calamity", "Venemun", Elements.Earth);
        eleDmg("light_dmg", "Of Sunshine", "Lux", Elements.Earth);
        eleDmg("dark_dmg", "Of Curses", "Maledictum", Elements.Earth);

    }

    void eleDmg(String id, String name, String desc, Elements ele) {
        ScrollBuff.of(id, name, desc,
            new StatModifier(10, 50, Stats.ELEMENTAL_DAMAGE.get(ele), ModType.FLAT)
        );
    }
}
