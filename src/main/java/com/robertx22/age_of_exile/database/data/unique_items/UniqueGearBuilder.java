package com.robertx22.age_of_exile.database.data.unique_items;

import com.robertx22.age_of_exile.aoe_data.base.DataGenKey;
import com.robertx22.age_of_exile.database.data.StatModifier;
import com.robertx22.age_of_exile.database.data.gear_types.bases.BaseGearType;
import com.robertx22.age_of_exile.database.registry.SlashRegistry;

import java.util.List;

public class UniqueGearBuilder {

    UniqueGear uniq = new UniqueGear();

    public static UniqueGearBuilder of(String id, String locname, String desc, DataGenKey<BaseGearType> gearType) {

        UniqueGearBuilder b = new UniqueGearBuilder();
        b.uniq.langName = locname;
        b.uniq.langDesc = desc;
        b.uniq.guid = id;

        b.uniq.serBaseGearType = SlashRegistry.GearTypes()
            .getFromSerializables(gearType);
        b.uniq.gearType = gearType.GUID();

        b.uniq.itemID = b.uniq.getResourceLocForItemForSerialization();

        return b;

    }

    public UniqueGearBuilder stats(List<StatModifier> stats) {
        this.uniq.uniqueStats = stats;
        return this;
    }

    public UniqueGear build() {
        assert !uniq.uniqueStats.isEmpty();

        uniq.addToSerializables();
        return uniq;
    }
}