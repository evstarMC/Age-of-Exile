package com.robertx22.age_of_exile.uncommon.enumclasses;

import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

public enum Elements {

    Physical(false, "Physical", Formatting.GOLD, "physical", "\u2726"),
    Fire(true, "Fire", Formatting.RED, "fire", "\u2600"),
    Water(true, "Water", Formatting.AQUA, "water", "\u2749"),
    Earth(true, "Earth", Formatting.DARK_GREEN, "earth", "\u273F"), /* Reminder: if i rename this to "earth" satte will get pissed off */

    Elemental(false, "Elemental", Formatting.LIGHT_PURPLE, "elemental", "\u269C"),
    All(false, "All", Formatting.LIGHT_PURPLE, "all", "\u273F");

    public boolean isSingleElement = true;

    Elements(boolean isSingleElement, String dmgname, Formatting format, String guidname, String icon) {

        this.isSingleElement = isSingleElement;
        this.dmgName = dmgname;
        this.format = format;
        this.guidName = guidname;
        this.icon = icon;
    }

    public String dmgName;
    public String guidName;
    public String icon;

    public Formatting format;

    public String getIconNameDmg() {
        return getIconNameFormat(dmgName) + " Damage";
    }

    public String getIconNameFormat() {
        return getIconNameFormat(dmgName);
    }

    public String getIconNameFormat(String str) {
        return this.format + this.icon + " " + str + Formatting.GRAY;
    }

    public boolean isPhysical() {
        return this == Physical;
    }

    public boolean isFire() {
        return this == Fire;
    }

    public boolean isElemental() {
        return this != Physical;
    }

    public boolean isWater() {
        return this == Water;
    }

    public boolean isNature() {
        return this == Earth;
    }

    private static List<Elements> allIncludingPhys = Arrays.asList(Physical, Fire, Water, Earth);
    private static List<Elements> allExcludingPhys = Arrays.asList(Fire, Water, Earth, Elemental);
    private static List<Elements> allSingleElementals = Arrays.asList(Fire, Water, Earth);

    public static List<Elements> getAllSingleElementals() {
        return allSingleElementals;
    }

    public static List<Elements> getAllSingleIncludingPhysical() {
        return allIncludingPhys;
    }

    public static List<Elements> getEverythingBesidesPhysical() {
        return allExcludingPhys;
    }

    public boolean elementsMatch(Elements other) {
        if (other == null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (other == All || this == All) {
            return true;
        }

        if (other == Elements.Elemental) {
            if (this != Elements.Physical) {
                return true;
            }
        }
        if (this == Elements.Elemental) {
            if (other != Elements.Physical) {
                return true;
            }
        }

        return false;
    }

}
