package com.robertx22.age_of_exile.loot.generators;

import com.robertx22.age_of_exile.config.forge.ModConfig;
import com.robertx22.age_of_exile.loot.LootInfo;
import com.robertx22.age_of_exile.loot.blueprints.ItemBlueprint;
import com.robertx22.age_of_exile.uncommon.enumclasses.LootType;
import com.robertx22.age_of_exile.vanilla_mc.items.loot_crate.LootCrateData;
import net.minecraft.item.ItemStack;

public class CurrencyLootGen extends BaseLootGen<ItemBlueprint> {

    public CurrencyLootGen(LootInfo info) {
        super(info);
    }

    @Override
    public float baseDropChance() {
        float chance = (float) ModConfig.get().DropRates.CURRENCY_DROPRATE;

        return chance;

    }

    @Override
    public LootType lootType() {
        return LootType.Currency;
    }

    @Override
    public boolean condition() {
        if (info.favorRank != null) {
            if (!info.favorRank.drop_currency) {
                return false;
            }
        }
        return info.level > 10;
    }

    @Override
    public ItemStack generateOne() {

        LootCrateData data = new LootCrateData();
        data.type = LootType.Currency;
        data.tier = info.tier;

        return data.createStack();

    }

}
