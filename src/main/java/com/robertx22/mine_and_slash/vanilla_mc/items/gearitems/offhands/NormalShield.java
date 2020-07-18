package com.robertx22.mine_and_slash.vanilla_mc.items.gearitems.offhands;

import com.robertx22.mine_and_slash.database.data.gearitemslots.bases.BaseGearType;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.uncommon.interfaces.IAutoLocName;
import com.robertx22.mine_and_slash.uncommon.interfaces.IGearItem;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.ItemUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;

import net.minecraftforge.fml.DistExecutor;


import java.util.HashMap;

public class NormalShield extends ShieldItem implements IAutoLocName, IGearItem {

    public static HashMap<Integer, Item> Items = new HashMap<Integer, Item>();

    public Identifier resource = new Identifier("");

    public NormalShield(BaseGearType slot) {

        super(properties());

        resource = getResource(slot);

    }

    static Settings properties() {

        Settings p = ItemUtils.getDefaultGearProperties();

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            p.setISTER(ShieldRenderer::new);
        });

        return p;
    }

    public static Identifier getResource(BaseGearType slot) {
        return new Identifier(Ref.MODID, "items/shields/" + slot.GUID());
    }

    @Override
    public AutoLocGroup locNameGroup() {
        return AutoLocGroup.Gear_Items;
    }

    @Override
    public String locNameForLangFile() {
        return "Shield";
    }

    @Override
    public String locNameLangFileGUID() {
        return getFormatedForLangFile(this.getRegistryName()
            .toString());
    }

    @Override
    public String GUID() {
        return "";
    }

    @Override
    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return true;
    }

}