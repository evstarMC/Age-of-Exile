package com.robertx22.age_of_exile.vanilla_mc.items.gearitems.weapons;

import com.robertx22.age_of_exile.uncommon.enumclasses.WeaponTypes;
import com.robertx22.age_of_exile.vanilla_mc.items.gearitems.bases.SingleTargetWeapon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public abstract class ModWeapon extends SingleTargetWeapon {

    public ModWeapon(ToolMaterial mat, Settings settings, WeaponTypes type) {
        super(mat, settings, type.locName());

        this.attackSpeed = type.getVanillaItemAttackSpeedModifier();
    }

    @Override
    public boolean isSuitableFor(BlockState blockIn) {
        return blockIn.getBlock() == Blocks.COBWEB;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        Block block = state.getBlock();

        if (block == Blocks.COBWEB) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANT && material != Material.MOSS_BLOCK && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
        }
    }

}
