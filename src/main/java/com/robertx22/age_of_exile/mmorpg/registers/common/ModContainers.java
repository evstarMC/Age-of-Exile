package com.robertx22.age_of_exile.mmorpg.registers.common;

import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.age_of_exile.player_skills.items.backpacks.BackpackContainer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModContainers {

    public Identifier BACKPACK = id("backpack");
    public ScreenHandlerType<BackpackContainer> BACKPACK_TYPE = ScreenHandlerRegistry.registerExtended(BACKPACK, BackpackContainer::new);

    public Identifier GEAR_SALVAGE = id("salvage");
    public Identifier GEAR_SOCKET = id("socket");
    public Identifier SCRIBE_BUFF = id("scribe_buff");
    public Identifier COOKING_STATION = id("cooking");
    public Identifier TABLET_STATION = id("tablet");
    public Identifier ALCHEMY_STATION = id("alchemy");
    public Identifier SMITHING_STATION = id("smithing");

    Identifier id(String id) {
        return new Identifier(Ref.MODID, id);
    }

    @SuppressWarnings("deprecation")
    public ModContainers() {
        register(GEAR_SALVAGE);
        register(GEAR_SOCKET);
        register(SCRIBE_BUFF);
        register(COOKING_STATION);
        register(TABLET_STATION);
        register(ALCHEMY_STATION);
        register(SMITHING_STATION);
    }

    @SuppressWarnings("deprecation")
    void register(Identifier ide) {

        ContainerProviderRegistry.INSTANCE.
            registerFactory(ide, (syncId, identifier, player, buf) -> {

                final World world = player.world;
                final BlockPos pos = buf.readBlockPos();
                return world.getBlockState(pos)
                    .createScreenHandlerFactory(player.world, pos)
                    .createMenu(syncId, player.inventory, player);
            });

    }
}
