package com.robertx22.age_of_exile.vanilla_mc.blocks.tablet;

import com.robertx22.age_of_exile.vanilla_mc.blocks.BaseTileContainer;
import com.robertx22.age_of_exile.vanilla_mc.blocks.CraftingWidgetComponent;
import com.robertx22.age_of_exile.vanilla_mc.blocks.CraftingWidgetComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;

public class TabletStationContainer extends BaseTileContainer {
    static public CraftingWidgetComponent[] components = {
            new CraftingWidgetComponent(44, 17, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(44, 38, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(44, 59, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(44, 98, CraftingWidgetComponentType.FUEL_SLOT),
            new CraftingWidgetComponent(121, 38, CraftingWidgetComponentType.OUTPUT_SLOT)
    };
    Inventory tile;

    public TabletStationContainer(int i, PlayerInventory invPlayer, Inventory inventory, BlockPos pos) {
        super(TabletStationTile.totalSlots(), null, i, invPlayer);
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
