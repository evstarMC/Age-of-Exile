package com.robertx22.age_of_exile.vanilla_mc.blocks.smithing;

import com.robertx22.age_of_exile.vanilla_mc.blocks.BaseTileContainer;
import com.robertx22.age_of_exile.vanilla_mc.blocks.CraftingWidgetComponent;
import com.robertx22.age_of_exile.vanilla_mc.blocks.CraftingWidgetComponentType;
import com.robertx22.age_of_exile.vanilla_mc.blocks.bases.VanillaFuelSlot;
import com.robertx22.age_of_exile.vanilla_mc.blocks.cooking_station.CookingTile;
import com.robertx22.age_of_exile.vanilla_mc.blocks.slots.OutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class SmithingContainer extends BaseTileContainer {
    static public CraftingWidgetComponent[] components = {
            new CraftingWidgetComponent(37, 26, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(55, 26, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(73, 26, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(55, 62, CraftingWidgetComponentType.FUEL_SLOT),
            new CraftingWidgetComponent(114, 43, CraftingWidgetComponentType.OUTPUT_SLOT)
    };
    Inventory tile;

    public SmithingContainer(int i, PlayerInventory invPlayer, Inventory inventory, BlockPos pos) {
        super(CookingTile.totalSlots(), null, i, invPlayer);
        this.tile = inventory;
        this.pos = pos;
        tile.onOpen(invPlayer.player);
        this.buildComponents(components, inventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return tile.canPlayerUse(player);
    }

}
