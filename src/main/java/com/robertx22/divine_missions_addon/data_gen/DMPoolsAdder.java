package com.robertx22.divine_missions_addon.data_gen;

import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.divine_missions.data_gen.builders.PoolBuilder;
import com.robertx22.divine_missions.database.condition_types.IsModLoaded;
import com.robertx22.divine_missions.database.db_types.Pool;
import com.robertx22.divine_missions_addon.types.AoeLevelCondition;
import com.robertx22.library_of_exile.registry.ExileRegistryInit;

import java.util.Arrays;

public class DMPoolsAdder implements ExileRegistryInit {

    @Override
    public void registerAll() {

        PoolBuilder.of("aoe_tasks", 250, Pool.PoolType.TASKS, Pool.PickType.PICK_ONE,
                Arrays.asList(
                    DMTasksAdder.COMPLETE_DUNGEON
                )
            )
            .addCondition(IsModLoaded.of(Ref.MODID))
            .addCondition(AoeLevelCondition.of(25, Integer.MAX_VALUE))
            .buildForGods();

        PoolBuilder.of("dungeon_keys", 200, Pool.PoolType.REWARDS, Pool.PickType.PICK_ONE,
                Arrays.asList(
                    DMRewardsAdder.DUNGEON_KEYS_LOOT_TABLE
                )
            )
            .addCondition(AoeLevelCondition.of(10, Integer.MAX_VALUE))
            .addCondition(IsModLoaded.of(Ref.MODID))
            .buildForGods();

        PoolBuilder.of("favor", 1000, Pool.PoolType.REWARDS, Pool.PickType.ALWAYS_INCLUDE,
                Arrays.asList(
                    DMRewardsAdder.FAVOR
                )
            )
            .addCondition(IsModLoaded.of(Ref.MODID))
            .buildForGods();

        PoolBuilder.of("aoe_exp", 1000, Pool.PoolType.REWARDS, Pool.PickType.ALWAYS_INCLUDE,
                Arrays.asList(DMRewardsAdder.AOE_EXP)
            )
            .addCondition(IsModLoaded.of(Ref.MODID))
            .buildForGods();

    }
}
