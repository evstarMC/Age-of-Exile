package com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.armors.leather;

import com.robertx22.age_of_exile.database.registry.ExileRegistryInit;

public class LeatherUniques implements ExileRegistryInit {

    @Override
    public void registerAll() {

        new BoneArmor().registerAll();

    }
}
