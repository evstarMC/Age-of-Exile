package com.robertx22.age_of_exile.vanilla_mc.blocks.alchemy;

import com.robertx22.age_of_exile.vanilla_mc.blocks.BaseTileContainer;
import com.robertx22.age_of_exile.vanilla_mc.blocks.CraftingWidgetComponent;
import com.robertx22.age_of_exile.vanilla_mc.blocks.CraftingWidgetComponentType;
import com.robertx22.age_of_exile.vanilla_mc.blocks.slots.OutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class AlchemyContainer extends BaseTileContainer {
    static public CraftingWidgetComponent[] components = {
            new CraftingWidgetComponent(57, 38, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(80, 31, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(103, 38, CraftingWidgetComponentType.INPUT_SLOT),
            new CraftingWidgetComponent(80, 72, CraftingWidgetComponentType.OUTPUT_SLOT)
    };
    Inventory tile;

    public AlchemyContainer(int i, PlayerInventory invPlayer, Inventory inventory, BlockPos pos) {
        super(AlchemyTile.totalSlots(), null, i, invPlayer);
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
