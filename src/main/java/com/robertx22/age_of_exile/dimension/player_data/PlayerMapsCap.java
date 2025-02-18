package com.robertx22.age_of_exile.dimension.player_data;

import com.robertx22.age_of_exile.capability.PlayerDamageChart;
import com.robertx22.age_of_exile.capability.bases.ICommonPlayerCap;
import com.robertx22.age_of_exile.database.data.tiers.base.Difficulty;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.dimension.DungeonDimensionJigsawFeature;
import com.robertx22.age_of_exile.dimension.PopulateDungeonChunks;
import com.robertx22.age_of_exile.dimension.dungeon_data.*;
import com.robertx22.age_of_exile.dimension.teleporter.portal_block.PortalBlockEntity;
import com.robertx22.age_of_exile.mmorpg.ModRegistry;
import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.age_of_exile.uncommon.utilityclasses.SignUtils;
import com.robertx22.age_of_exile.uncommon.utilityclasses.TeamUtils;
import com.robertx22.age_of_exile.vanilla_mc.packets.sync_cap.PlayerCaps;
import com.robertx22.divine_missions.mission_gen.MissionUtil;
import com.robertx22.divine_missions_addon.types.CompleteDungeonTask;
import com.robertx22.library_of_exile.utils.LoadSave;
import com.robertx22.library_of_exile.utils.RandomUtils;
import com.robertx22.library_of_exile.utils.Watch;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.*;

public class PlayerMapsCap implements ICommonPlayerCap {

    public static final Identifier RESOURCE = new Identifier(Ref.MODID, "player_maps");
    private static final String LOC = "maps";

    PlayerEntity player;

    public MapsData data = new MapsData();

    public int ticksinPortal = 0;
    public int highestTierDone = 0;

    public int getHighestTierPossibleToStart() {
        return highestTierDone + 1;
    }

    public PlayerMapsCap(PlayerEntity player) {
        this.player = player;
    }

    public void printDelveMapDebug() {
        NbtCompound nbt = new NbtCompound();
        toTag(nbt);
        System.out.print(nbt.toString());
    }

    public void onDungeonCompletedAdvanceProgress() {

        TeamUtils.getOnlineMembers(player)
            .forEach(e -> {
                MissionUtil.tryDoMissions(e, x -> {
                    if (x.getTaskEntry().task_type.equals(CompleteDungeonTask.ID)) {
                        x.increaseProgress();
                        return;
                    }
                });
            });

        int tier = 0;
        try {
            tier = data.dungeonData
                .getDifficulty().rank;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tier > highestTierDone) {
            highestTierDone = tier;
        }

    }

    public static Block TELEPORT_TO_PLACEHOLDER_BLOCK = Blocks.PLAYER_HEAD;

    static int cachedBorder = 0;

    static int getBorder(World world) {
        if (cachedBorder == 0) {
            cachedBorder = (int) (world.getWorldBorder()
                .getSize() / 2);
        }
        return cachedBorder;
    }

    public void onStartDungeon(TeamSize teamSize, BlockPos teleporterPos, String uuid) {

        try {

            TeamUtils.getOnlineTeamMembersInRange(player)
                .forEach(x -> PlayerDamageChart.clear(x));

            Watch total = new Watch();

            Watch first = new Watch();

            this.data.tel_pos = teleporterPos;

            // set the dungeon data for the chunk
            World dimWorld = player.world.getServer()
                .getWorld(RegistryKey.of(Registry.WORLD_KEY, data.dungeonData.dun_type.DIMENSION_ID));

            int border = getBorder(dimWorld);

            int X = RandomUtils.RandomRange(-border, border);
            int Z = RandomUtils.RandomRange(-border, border);

            ChunkPos cp = DungeonDimensionJigsawFeature.getSpawnChunkOf(new BlockPos(X, 0, Z));

            Watch ww = new Watch();

            //Chunk chunk = dimWorld.getChunk(cp.x, cp.z); // load it first

            ww.print("loading first chunk ");

            BlockPos tpPos = cp.getStartPos()
                .add(8, DungeonDimensionJigsawFeature.HEIGHT, 8); // todo, seems to not point to correct location?

            first.print("first part ");

            String moblist = "";

            if (true) {

                Watch w = new Watch();

                List<ChunkPos> check = PopulateDungeonChunks.getChunksAround(cp);

                boolean foundSpawn = false;
                boolean foundportalback = false;

                for (ChunkPos x : check) {

                    Set<Map.Entry<BlockPos, BlockEntity>> bes = new HashSet<>(dimWorld.getChunk(x.x, x.z)
                        .getBlockEntities()
                        .entrySet());

                    for (Map.Entry<BlockPos, BlockEntity> e : bes) {

                        if (e.getValue() instanceof SignBlockEntity) {
                            if (SignUtils.has("[moblist]", (SignBlockEntity) e.getValue())) {
                                moblist = SignUtils.removeBraces(SignUtils.getText((SignBlockEntity) e.getValue())
                                    .get(1));
                            }
                            if (SignUtils.has("[portal]", (SignBlockEntity) e.getValue())) {
                                dimWorld.setBlockState(e.getKey(), ModRegistry.BLOCKS.PORTAL.getDefaultState());
                                foundportalback = true;
                            }
                        } else if (dimWorld.getBlockState(e.getKey())
                            .getBlock() == TELEPORT_TO_PLACEHOLDER_BLOCK) {
                            tpPos = e.getKey();
                            dimWorld.breakBlock(tpPos, false);
                            foundSpawn = true;
                        }
                    }
                }

                if (!foundSpawn) {
                    player.sendMessage(new LiteralText("Bug: Couldnt find spawn position, you might be placed weirdly, and possibly die."), false);
                }
                if (!foundportalback) {
                    player.sendMessage(new LiteralText("Bug: Couldnt find portal back position."), false);
                }

                w.print("finding spawn");

            }

            List<BlockPos> list = new ArrayList<>();
            list.add(teleporterPos.south(2));
            list.add(teleporterPos.north(2));
            list.add(teleporterPos.east(2));
            list.add(teleporterPos.west(2));

            for (BlockPos x : list) {
                if (player.world.getBlockState(x)
                    .isAir() || player.world.getBlockState(x)
                    .getBlock() == ModRegistry.BLOCKS.PORTAL) {
                    player.world.breakBlock(x, false);
                    player.world.setBlockState(x, ModRegistry.BLOCKS.PORTAL.getDefaultState());
                    PortalBlockEntity be = (PortalBlockEntity) player.world.getBlockEntity(x);
                    be.data.dungeonPos = tpPos;
                    be.data.tpbackpos = teleporterPos.up();
                    be.data.dungeonType = data.dungeonData.dun_type;

                }
            }

            // first set the data so the mob levels can be set on spawn
            WorldDungeonCap data = Load.dungeonData(dimWorld);
            SingleDungeonData single = new SingleDungeonData(this.data.dungeonData, new QuestProgression(this.data.dungeonData.uuid, 20), player.getUuid()
                .toString());
            if (ExileDB.DungeonMobLists()
                .isRegistered(moblist)) {
                single.data.setMobList(moblist);
            }
            data.data.set(player, tpPos, single);

            single.data.team = teamSize;

            single.pop.startPopulating(cp);

            total.print("Total dungeon init");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public NbtCompound toTag(NbtCompound nbt) {
        LoadSave.Save(data, nbt, LOC);
        nbt.putInt("t", ticksinPortal);
        nbt.putInt("h", highestTierDone);
        return nbt;
    }

    @Override
    public void fromTag(NbtCompound nbt) {
        this.ticksinPortal = nbt.getInt("t");
        this.highestTierDone = nbt.getInt("h");

        try {
            this.data = LoadSave.Load(MapsData.class, new MapsData(), nbt, LOC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data == null) {
            data = new MapsData();
        }

    }

    @Override
    public PlayerCaps getCapType() {
        return PlayerCaps.MAPS;
    }

    public void createRandomDungeon(Difficulty diff) {

        this.data = new MapsData();
        data.isEmpty = false;

        DungeonData dun = new DungeonData();
        int lvl = Load.Unit(player)
            .getLevel();
        dun.randomize(lvl, diff);

        this.data.dungeonData = dun;

        this.syncToClient(player);
    }
}
