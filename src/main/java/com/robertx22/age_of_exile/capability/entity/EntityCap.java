package com.robertx22.age_of_exile.capability.entity;

import com.robertx22.age_of_exile.capability.bases.EntityGears;
import com.robertx22.age_of_exile.capability.bases.ICommonPlayerCap;
import com.robertx22.age_of_exile.capability.bases.INeededForClient;
import com.robertx22.age_of_exile.config.forge.ModConfig;
import com.robertx22.age_of_exile.damage_hooks.util.AttackInformation;
import com.robertx22.age_of_exile.database.data.EntityConfig;
import com.robertx22.age_of_exile.database.data.game_balance_config.GameBalanceConfig;
import com.robertx22.age_of_exile.database.data.gear_slots.GearSlot;
import com.robertx22.age_of_exile.database.data.mob_affixes.MobAffix;
import com.robertx22.age_of_exile.database.data.rarities.MobRarity;
import com.robertx22.age_of_exile.database.data.stats.types.generated.AttackDamage;
import com.robertx22.age_of_exile.database.data.stats.types.resources.energy.Energy;
import com.robertx22.age_of_exile.database.data.stats.types.resources.health.Health;
import com.robertx22.age_of_exile.database.data.tiers.base.Difficulty;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.dimension.dungeon_data.DungeonData;
import com.robertx22.age_of_exile.dimension.dungeon_data.WorldDungeonCap;
import com.robertx22.age_of_exile.event_hooks.player.OnLogin;
import com.robertx22.age_of_exile.mmorpg.registers.common.ModCriteria;
import com.robertx22.age_of_exile.saveclasses.CustomExactStatsData;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.saveclasses.unit.MobAffixesData;
import com.robertx22.age_of_exile.saveclasses.unit.ResourceType;
import com.robertx22.age_of_exile.saveclasses.unit.ResourcesData;
import com.robertx22.age_of_exile.saveclasses.unit.Unit;
import com.robertx22.age_of_exile.threat_aggro.ThreatData;
import com.robertx22.age_of_exile.uncommon.datasaving.CustomExactStats;
import com.robertx22.age_of_exile.uncommon.datasaving.Gear;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.age_of_exile.uncommon.datasaving.UnitNbt;
import com.robertx22.age_of_exile.uncommon.effectdatas.DamageEvent;
import com.robertx22.age_of_exile.uncommon.effectdatas.EventBuilder;
import com.robertx22.age_of_exile.uncommon.effectdatas.SpendResourceEvent;
import com.robertx22.age_of_exile.uncommon.enumclasses.AttackType;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import com.robertx22.age_of_exile.uncommon.enumclasses.PlayStyle;
import com.robertx22.age_of_exile.uncommon.enumclasses.WeaponTypes;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.IRarity;
import com.robertx22.age_of_exile.uncommon.localization.Chats;
import com.robertx22.age_of_exile.uncommon.utilityclasses.EntityTypeUtils;
import com.robertx22.age_of_exile.uncommon.utilityclasses.LevelUtils;
import com.robertx22.age_of_exile.uncommon.utilityclasses.OnScreenMessageUtils;
import com.robertx22.age_of_exile.uncommon.utilityclasses.WorldUtils;
import com.robertx22.age_of_exile.vanilla_mc.packets.sync_cap.PlayerCaps;
import com.robertx22.age_of_exile.vanilla_mc.packets.sync_cap.SyncCapabilityToClient;
import com.robertx22.age_of_exile.vanilla_mc.potion_effects.EntityStatusEffectsData;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.library_of_exile.utils.CLOC;
import com.robertx22.library_of_exile.utils.LoadSave;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;
import java.util.Random;
import java.util.UUID;

public class EntityCap {

    private static final String RARITY = "rarity";
    private static final String RACE = "race";
    private static final String LEVEL = "level";
    private static final String EXP = "exp";
    private static final String HP = "hp";
    private static final String UUID = "uuid";
    private static final String SET_MOB_STATS = "set_mob_stats";
    private static final String NEWBIE_STATUS = "is_a_newbie";
    private static final String EQUIPS_CHANGED = "eq_changed";
    private static final String TIER = "TIER";
    private static final String AFFIXES = "affix";
    private static final String SHOULD_SYNC = "do_sync";
    private static final String ENTITY_TYPE = "ENTITY_TYPE";
    private static final String RESOURCES_LOC = "res_loc";
    private static final String STATUSES = "statuses";
    private static final String SCROLL_BUFF_SEED = "sb_seed";
    private static final String COOLDOWNS = "cds";
    private static final String THREAT = "th";

    public interface UnitData extends ICommonPlayerCap, INeededForClient {

        EntityStatusEffectsData getStatusEffectsData();

        float getMaximumResource(ResourceType type);

        void onDeath();

        void setType();

        EntityTypeUtils.EntityClassification getType();

        ThreatData getThreat();

        void trySync();

        GearItemData setupWeaponData();

        void setEquipsChanged(boolean bool);

        CooldownsData getCooldowns();

        boolean isNewbie();

        void setNewbieStatus(boolean bool);

        boolean needsToBeGivenStats();

        boolean increaseRarity();

        Unit getUnit();

        void setUnit(Unit unit);

        void setRarity(String rarity);

        String getRarity();

        String getUUID();

        MobRarity getMobRarity();

        void setUUID(UUID id);

        MutableText getName();

        void tryRecalculateStats();

        void forceRecalculateStats(AttackInformation data);

        void forceRecalculateStats();

        void forceSetUnit(Unit unit);

        LivingEntity getEntity();

        boolean canUseWeapon(GearItemData gear);

        void onLogin(PlayerEntity player);

        int getSyncedMaxHealth();

        Difficulty getMapDifficulty();

        CustomExactStatsData getCustomExactStats();

        ResourcesData getResources();

        float getCurrentMana();

        void mobStatsAreSet();

        void attackWithWeapon(AttackInformation data);

        void mobBasicAttack(AttackInformation data);

        int getLevel();

        void setLevel(int lvl);

        boolean CheckIfCanLevelUp();

        boolean LevelUp(PlayerEntity player);

        boolean CheckLevelCap();

        void SetMobLevelAtSpawn(PlayerEntity player);

        int getExp();

        void setExp(int exp);

        int GiveExp(PlayerEntity player, int i);

        int getExpRequiredForLevelUp();

        EntityGears getCurrentGears();

        MobAffixesData getAffixData();

        int getBuffSeed();

        void randomizeBuffSeed();

    }

    public static class DefaultImpl implements UnitData {

        LivingEntity entity;

        transient EntityGears gears = new EntityGears();

        // sync these for mobs
        Unit unit = new Unit();
        String rarity = IRarity.COMMON_ID;
        String race = "";
        int level = 1;
        int exp = 0;
        int maxHealth = 0;
        MobAffixesData affixes = new MobAffixesData();
        int buffSeed = 0;

        public EntityStatusEffectsData statusEffects = new EntityStatusEffectsData();

        CooldownsData cooldowns = new CooldownsData();
        ThreatData threat = new ThreatData();

        EntityTypeUtils.EntityClassification type = EntityTypeUtils.EntityClassification.PLAYER;
        // sync these for mobs

        boolean setMobStats = false;
        String uuid = "";
        boolean isNewbie = true;
        boolean equipsChanged = true;
        boolean shouldSync = false;

        ResourcesData resources = new ResourcesData();
        CustomExactStatsData customExactStats = new CustomExactStatsData();

        public DefaultImpl(LivingEntity entity) {
            this.entity = entity;
        }

        @Override
        public void addClientNBT(NbtCompound nbt) {

            nbt.putInt(LEVEL, level);
            nbt.putString(RARITY, rarity);
            nbt.putString(RACE, race);
            nbt.putInt(SCROLL_BUFF_SEED, buffSeed);
            nbt.putInt(HP, (int) getUnit().getCalculatedStat(Health.getInstance())
                .getValue());
            nbt.putString(ENTITY_TYPE, this.type.toString());

            if (affixes != null) {
                LoadSave.Save(affixes, nbt, AFFIXES);
            }
            LoadSave.Save(statusEffects, nbt, STATUSES);
        }

        @Override
        public void loadFromClientNBT(NbtCompound nbt) {

            this.rarity = nbt.getString(RARITY);
            this.race = nbt.getString(RACE);
            this.level = nbt.getInt(LEVEL);
            this.buffSeed = nbt.getInt(SCROLL_BUFF_SEED);
            if (level < 1) {
                level = 1;
            }
            this.maxHealth = nbt.getInt(HP);

            try {
                String typestring = nbt.getString(ENTITY_TYPE);
                this.type = EntityTypeUtils.EntityClassification.valueOf(typestring);
            } catch (Exception e) {
                this.type = EntityTypeUtils.EntityClassification.OTHER;
            }

            this.affixes = LoadSave.Load(MobAffixesData.class, new MobAffixesData(), nbt, AFFIXES);
            if (affixes == null) {
                affixes = new MobAffixesData();
            }

            this.statusEffects = LoadSave.Load(EntityStatusEffectsData.class, new EntityStatusEffectsData(), nbt, STATUSES);
            if (statusEffects == null) {
                statusEffects = new EntityStatusEffectsData();
            }

        }

        @Override
        public NbtCompound toTag(NbtCompound nbt) {

            addClientNBT(nbt);

            nbt.putInt(EXP, exp);
            nbt.putString(UUID, uuid);
            nbt.putBoolean(SET_MOB_STATS, setMobStats);
            nbt.putBoolean(NEWBIE_STATUS, this.isNewbie);
            nbt.putBoolean(EQUIPS_CHANGED, equipsChanged);
            nbt.putBoolean(SHOULD_SYNC, shouldSync);

            LoadSave.Save(cooldowns, nbt, COOLDOWNS);

            if (unit != null) {
                UnitNbt.Save(nbt, unit);
            }

            if (customExactStats != null) {
                CustomExactStats.Save(nbt, customExactStats);
            }

            if (resources != null) {
                LoadSave.Save(resources, nbt, RESOURCES_LOC);
            }

            if (threat != null) {
                LoadSave.Save(threat, nbt, THREAT);
            }

            return nbt;

        }

        @Override
        public void fromTag(NbtCompound nbt) {

            loadFromClientNBT(nbt);

            this.exp = nbt.getInt(EXP);
            this.uuid = nbt.getString(UUID);
            this.setMobStats = nbt.getBoolean(SET_MOB_STATS);
            if (nbt.contains(NEWBIE_STATUS)) {
                this.isNewbie = nbt.getBoolean(NEWBIE_STATUS);
            }
            this.equipsChanged = nbt.getBoolean(EQUIPS_CHANGED);
            this.shouldSync = nbt.getBoolean(SHOULD_SYNC);

            this.unit = UnitNbt.Load(nbt);
            if (this.unit == null) {
                this.unit = new Unit();
            }

            cooldowns = LoadSave.Load(CooldownsData.class, new CooldownsData(), nbt, COOLDOWNS);
            if (cooldowns == null) {
                cooldowns = new CooldownsData();
            }

            try {
                this.resources = LoadSave.Load(ResourcesData.class, new ResourcesData(), nbt, RESOURCES_LOC);
                if (resources == null) {
                    resources = new ResourcesData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.customExactStats = CustomExactStats.Load(nbt);
            if (this.customExactStats == null) {
                this.customExactStats = new CustomExactStatsData();
            }

            this.threat = LoadSave.Load(ThreatData.class, new ThreatData(), nbt, THREAT);
            if (threat == null) {
                threat = new ThreatData();
            }
        }

        @Override
        public void setEquipsChanged(boolean bool) {
            this.equipsChanged = bool;
        }

        @Override
        public CooldownsData getCooldowns() {
            return cooldowns;
        }

        @Override
        public EntityStatusEffectsData getStatusEffectsData() {
            return this.statusEffects;
        }

        @Override
        public float getMaximumResource(ResourceType type) {

            if (type == ResourceType.blood) {
                return getUnit().bloodData()
                    .getValue();
            } else if (type == ResourceType.mana) {
                return getUnit().manaData()
                    .getValue();
            } else if (type == ResourceType.health) {
                return entity.getMaxHealth();
            } else if (type == ResourceType.energy) {
                return getUnit().energyData()
                    .getValue();
            }

            return 0;

        }

        @Override
        public void onDeath() {

            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;

                int expLoss = (int) (exp * ModConfig.get().Server.EXP_LOSS_ON_DEATH);

                if (expLoss > 0) {
                    this.exp = MathHelper.clamp(exp - expLoss, 0, Integer.MAX_VALUE);
                }

            }
        }

        @Override
        public void setType() {
            this.type = EntityTypeUtils.getType(entity);
        }

        @Override
        public EntityTypeUtils.EntityClassification getType() {
            return this.type;
        }

        @Override
        public ThreatData getThreat() {
            return threat;
        }

        @Override
        public void trySync() {
            if (this.shouldSync) {
                this.shouldSync = false;

                if (!Unit.shouldSendUpdatePackets(entity)) {
                    return;
                }
                Packets.sendToTracking(Unit.getUpdatePacketFor(entity, this), entity);
            }

        }

        @Override
        public PlayerCaps getCapType() {
            return PlayerCaps.ENTITY_DATA;
        }

        @Override
        public Unit getUnit() {
            return unit;
        }

        @Override
        public void setUnit(Unit unit) {
            this.unit = unit;
        }

        @Override
        public void setRarity(String rarity) {
            this.rarity = rarity;

            this.equipsChanged = true;
            this.shouldSync = true;
        }

        @Override
        public String getRarity() {
            return rarity;
        }

        @Override
        public String getUUID() {
            return uuid;
        }

        @Override
        public MobRarity getMobRarity() {
            String rar = rarity;
            if (!ExileDB.MobRarities()
                .isRegistered(rar)) {
                rar = IRarity.COMMON_ID;
            }
            return ExileDB.MobRarities()
                .get(rar);
        }

        @Override
        public void setUUID(UUID id) {
            uuid = id.toString();
        }

        @Override
        public MutableText getName() {

            if (entity instanceof PlayerEntity) {
                return new LiteralText("")
                    .append(entity.getDisplayName());

            } else {

                MobRarity rarity = ExileDB.MobRarities()
                    .get(getRarity());

                Formatting rarformat = rarity.textFormatting();

                MutableText name = new LiteralText("").append(entity.getDisplayName())
                    .formatted(rarformat);

                String icons = "";

                for (MobAffix x : getAffixData().getAffixes()) {
                    icons += CLOC.translate(x.locName());
                }
                if (!icons.isEmpty()) {
                    icons += " ";
                }

                MutableText finalName = new LiteralText(icons).append(
                    name);

                MutableText part = new LiteralText("")
                    .append(finalName)
                    .formatted(rarformat);

                MutableText tx = (part);

                return tx;

            }
        }

        @Override
        public void tryRecalculateStats() {

            if (unit == null) {
                unit = new Unit();
            }

            if (needsToRecalcStats()) {
                //Watch watch = new Watch();
                unit.recalculateStats(entity, this, null);
                //watch.print("stat calc for " + (entity instanceof PlayerEntity ? "player " : "mob "));
            }

        }

        @Override
        public void forceRecalculateStats(AttackInformation data) {
            unit.recalculateStats(entity, this, data);
        }

        @Override
        public void forceRecalculateStats() {

            if (unit == null) {
                unit = new Unit();
            }
            unit.recalculateStats(entity, this, null);
        }

        // This reduces stat calculation by about 4 TIMES!
        private boolean needsToRecalcStats() {
            return this.equipsChanged;
        }

        @Override
        public void forceSetUnit(Unit unit) {
            this.unit = unit;
        }

        @Override
        public GearItemData setupWeaponData() {
            return Gear.Load(entity.getMainHandStack());
        }

        @Override
        public boolean canUseWeapon(GearItemData weaponData) {
            return weaponData != null;
        }

        @Override
        public void onLogin(PlayerEntity player) {

            try {

                if (unit == null) {
                    unit = new Unit();
                }

                // check if newbie
                if (isNewbie()) {
                    setNewbieStatus(false);

                    if (ModConfig.get().Server.GET_STARTER_ITEMS) {
                        OnLogin.GiveStarterItems(player);
                    }

                    Load.favor(player)
                        .setFavor(ModConfig.get().Favor.STARTING_FAVOR); // newbie starting favor

                    Packets.sendToClient(player, new SyncCapabilityToClient(player, PlayerCaps.SPELLS));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean increaseRarity() {

            MobRarity rar = ExileDB.MobRarities()
                .get(rarity);

            if (rar.hasHigherRarity()) {
                rarity = rar.getHigherRarity()
                    .GUID();
                this.equipsChanged = true;
                this.shouldSync = true;
                this.forceRecalculateStats();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int getSyncedMaxHealth() {
            return this.maxHealth;
        }

        @Override
        public Difficulty getMapDifficulty() {

            if (WorldUtils.isMapWorldClass(entity.world)) {
                WorldDungeonCap data = Load.dungeonData(entity.world);
                return data.data.get(entity.getBlockPos()).data.getDifficulty();
            }

            return ExileDB.Difficulties()
                .getList()
                .stream()
                .min(Comparator.comparingInt(x -> x.rank))
                .get();

        }

        @Override
        public CustomExactStatsData getCustomExactStats() {
            return this.customExactStats;
        }

        @Override
        public ResourcesData getResources() {
            return this.resources;
        }

        @Override
        public float getCurrentMana() {
            return this.resources.getMana();
        }

        @Override
        public void mobStatsAreSet() {
            this.setMobStats = true;
        }

        @Override
        public void attackWithWeapon(AttackInformation data) {

            if (data.weaponData.GetBaseGearType()
                .getWeaponMechanic() != null) {

                GearSlot slot = data.weaponData.GetBaseGearType()
                    .getGearSlot();

                float cost = Energy.getInstance()
                    .scale(slot.energy_cost, getLevel());
                SpendResourceEvent event = new SpendResourceEvent(entity, ResourceType.energy, cost);
                event.calculateEffects();

                if (event.data.getNumber() > resources.getEnergy()) {
                    return;
                }

                event.Activate();

                if (data.weapon != null) {
                    data.weapon.damage(1, new Random(), null);
                }

                data.weaponData.GetBaseGearType()
                    .getWeaponMechanic()
                    .attack(data);

            }
        }

        @Override
        public void mobBasicAttack(AttackInformation data) {
            MobRarity rar = ExileDB.MobRarities()
                .get(data.getAttackerEntityData()
                    .getRarity());

            float multi = (float) (ModConfig.get().Server.VANILLA_MOB_DMG_AS_EXILE_DMG + (LevelUtils.getMaxLevelMultiplier(getLevel()) * (ModConfig.get().Server.VANILLA_MOB_DMG_AS_EXILE_DMG_AT_MAX_LVL - ModConfig.get().Server.VANILLA_MOB_DMG_AS_EXILE_DMG)));

            float vanilla = data.getAmount() * multi;

            float num = vanilla * rar.DamageMultiplier() * getMapDifficulty().dmg_multi;

            num *= ExileDB.getEntityConfig(entity, this).dmg_multi;

            num = new AttackDamage(Elements.Physical).scale(num, getLevel());

            PlayStyle style = PlayStyle.melee;

            if (data.getSource() != null && data.getSource()
                .isProjectile()) {
                style = PlayStyle.ranged;
            }

            DamageEvent dmg = EventBuilder.ofDamage(data, entity, data.getTargetEntity(), num)
                .setupDamage(AttackType.attack, WeaponTypes.none, style)
                .setIsBasicAttack()
                .build();

            dmg.Activate();

        }

        @Override
        public boolean isNewbie() {
            return isNewbie;
        }

        @Override
        public void setNewbieStatus(boolean bool) {
            isNewbie = bool;
        }

        @Override
        public boolean needsToBeGivenStats() {
            return this.setMobStats == false;
        }

        @Override
        public int getExpRequiredForLevelUp() {
            return LevelUtils.getExpRequiredForLevel(this.getLevel() + 1);
        }

        @Override
        public EntityGears getCurrentGears() {
            return gears;
        }

        @Override
        public MobAffixesData getAffixData() {
            return affixes;
        }

        @Override
        public int getBuffSeed() {
            return buffSeed;
        }

        @Override
        public void randomizeBuffSeed() {
            this.buffSeed = new Random().nextInt();
        }

        @Override
        public void SetMobLevelAtSpawn(PlayerEntity nearestPlayer) {
            this.setMobStats = true;

            if (WorldUtils.isMapWorldClass(entity.world)) {
                try {
                    BlockPos pos = entity.getBlockPos();
                    DungeonData data = Load.dungeonData(entity.world).data.get(pos).data;
                    if (!data.isEmpty()) {
                        this.setLevel(data.lv);
                        return;
                    } else {
                        System.out.print("A mob spawned in a dungeon world without a dungeon data nearby!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            setMobLvlNormally(entity, nearestPlayer);

        }

        private void setMobLvlNormally(LivingEntity entity, PlayerEntity nearestPlayer) {
            EntityConfig entityConfig = ExileDB.getEntityConfig(entity, this);

            int lvl = LevelUtils.determineLevel(entity.world, entity.getBlockPos(),
                nearestPlayer
            );

            setLevel(MathHelper.clamp(lvl, entityConfig.min_lvl, entityConfig.max_lvl));
        }

        @Override
        public int GiveExp(PlayerEntity player, int i) {

            MutableText txt = new LiteralText("+" + (int) i + " Experience").formatted(Formatting.GREEN);
            OnScreenMessageUtils.sendMessage((ServerPlayerEntity) player, txt, TitleS2CPacket.Action.ACTIONBAR);

            setExp(exp + i);

            if (exp > this.getExpRequiredForLevelUp()) {

                if (this.CheckIfCanLevelUp() && this.CheckLevelCap()) {
                    this.LevelUp(player);
                }

                return i;
            }
            return i;
        }

        @Override
        public boolean CheckIfCanLevelUp() {

            return getExp() >= getExpRequiredForLevelUp();

        }

        public int getRemainingExp() {
            int num = getExp() - getExpRequiredForLevelUp();

            if (num < 0) {
                num = 0;
            }
            return num;
        }

        @Override
        public boolean CheckLevelCap() {
            return getLevel() + 1 <= GameBalanceConfig.get().MAX_LEVEL;
        }

        @Override
        public boolean LevelUp(PlayerEntity player) {

            if (!CheckIfCanLevelUp()) {
                player.sendMessage(Chats.Not_enough_experience.locName(), false);
            } else if (!CheckLevelCap()) {
                player.sendMessage(Chats.Can_not_go_over_maximum_level.locName(), false);
            }

            if (CheckIfCanLevelUp() && CheckLevelCap()) {

                if (player instanceof ServerPlayerEntity) {
                    ModCriteria.PLAYER_LEVEL.trigger((ServerPlayerEntity) player);
                }

                // fully restore on lvlup

                getResources().restore(player, ResourceType.mana, Integer.MAX_VALUE);
                getResources().restore(player, ResourceType.health, Integer.MAX_VALUE);
                getResources().restore(player, ResourceType.blood, Integer.MAX_VALUE);

                // fully restore on lvlup

                this.setLevel(level + 1);
                setExp(getRemainingExp());

                OnScreenMessageUtils.sendLevelUpMessage(player, new LiteralText("Player"), level - 1, level);

                return true;
            }
            return false;
        }

        @Override
        public int getLevel() {

            return level;

        }

        @Override
        public void setLevel(int lvl) {

            level = MathHelper.clamp(lvl, 1, GameBalanceConfig.get().MAX_LEVEL);

            this.equipsChanged = true;
            this.shouldSync = true;

        }

        @Override
        public int getExp() {
            return exp;
        }

        @Override
        public void setExp(int exp) {
            this.exp = exp;
        }

        @Override
        public LivingEntity getEntity() {
            return entity;
        }

    }

}
