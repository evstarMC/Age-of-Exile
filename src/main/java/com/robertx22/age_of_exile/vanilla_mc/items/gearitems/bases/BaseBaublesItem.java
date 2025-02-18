package com.robertx22.age_of_exile.vanilla_mc.items.gearitems.bases;

import com.robertx22.age_of_exile.aoe_data.datapacks.models.IAutoModel;
import com.robertx22.age_of_exile.aoe_data.datapacks.models.ItemModelManager;
import com.robertx22.age_of_exile.uncommon.interfaces.IAutoLocName;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public abstract class BaseBaublesItem extends Item implements IAutoLocName, IAutoModel {

    public BaseBaublesItem(Settings settings, String locname) {

        super(settings);
        this.locname = locname;
    }

    @Override
    public final String locNameForLangFile() {
        return locname;
    }

    String locname;

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public AutoLocGroup locNameGroup() {
        return AutoLocGroup.Gear_Items;
    }

    @Override
    public String locNameLangFileGUID() {
        return getFormatedForLangFile(Registry.ITEM.getId(this)
            .toString());
    }

    @Override
    public void generateModel(ItemModelManager manager) {
        manager.generated(this);
    }

    @Override
    public String GUID() {
        return "";
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

}
