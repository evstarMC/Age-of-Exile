package com.robertx22.age_of_exile.database.data.skill_gem;

public enum SpellTag {
    projectile("Projectile"),
    movement("Movement"),
    damage("Damage"),
    heal("Heal"),
    arrow("Arrow"),
    shield("Shield"),
    area("Area"),
    aura("Aura"),
    totem("Totem");

    public String locname;

    SpellTag(String locname) {
        this.locname = locname;
    }
}
