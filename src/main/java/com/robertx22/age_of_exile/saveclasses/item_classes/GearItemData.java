package com.robertx22.age_of_exile.saveclasses.item_classes;

import com.robertx22.age_of_exile.capability.entity.EntityCap;
import com.robertx22.age_of_exile.database.data.affixes.Affix;
import com.robertx22.age_of_exile.database.data.gear_types.bases.BaseGearType;
import com.robertx22.age_of_exile.database.data.rarities.GearRarity;
import com.robertx22.age_of_exile.database.data.requirements.bases.GearRequestedFor;
import com.robertx22.age_of_exile.database.data.salvage_outputs.SalvageOutput;
import com.robertx22.age_of_exile.database.data.unique_items.UniqueGear;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.saveclasses.ExactStatData;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.*;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_parts.*;
import com.robertx22.age_of_exile.uncommon.datasaving.StackSaving;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.ICommonDataItem;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.IRarity;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.age_of_exile.uncommon.utilityclasses.LevelUtils;
import com.robertx22.age_of_exile.uncommon.utilityclasses.TooltipUtils;
import com.robertx22.library_of_exile.utils.ItemstackDataSaver;
import com.robertx22.library_of_exile.utils.RandomUtils;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Storable
public class GearItemData implements ICommonDataItem<GearRarity> {

    // Stats
    @Store
    public BaseStatsData baseStats = new BaseStatsData();
    @Store
    public ImplicitStatsData imp = new ImplicitStatsData(); // implicit stats
    @Store
    public GearAffixesData affixes = new GearAffixesData();
    @Store
    public GearSocketsData sockets = new GearSocketsData();
    @Store
    public UniqueStatsData uniqueStats;

    // Stats

    // i added rename ideas to comments. As tiny as possible while still allowing people to understand kinda what it is
    // apparently people had big issues with many storage mods, So i should try minimize the nbt.
    @Store
    public String uniq_id = ""; // uniq_id

    @Store
    public String rarity = IRarity.COMMON_ID; // rar

    @Store
    public String item_id = ""; // item registry name

    @Store
    public int rp = -1; // pre_name rare prefix
    @Store
    public int rs = -1; // suf_name rare suffix

    @Store
    public int lvl = 1; // lvl

    @Store
    public String gear_type = "";

    @Store
    private float in = 0;

    @Store
    public boolean can_sal = true;

    @Store
    public boolean c = false; // corrupted

    public boolean isCorrupted() {
        return c;
    }

    public int getTier() {
// TODO
        return MathHelper.clamp(lvl / 10, 1, Integer.MAX_VALUE);
    }

    public int getEffectiveLevel() {
        return lvl + getRarity().bonus_effective_lvls;
    }

    public boolean canPlayerWear(EntityCap.UnitData data) {

        if (LevelUtils.tierToLevel(getTier()) > data.getLevel()) {
            return false;
        }

        return true;

        //return getRequirement().meetsReq(lvl, data);

    }

    public boolean isValidItem() {

        return ExileDB.GearTypes()
                .isRegistered(gear_type);
    }

    public boolean canGetAffix(Affix affix) {

        if (affix.only_one_per_item && affixes.containsAffix(affix)) {
            return false;
        }

        return affix.meetsRequirements(new GearRequestedFor(this));

    }

 
    public float getInstability() {
        return in;
    }

    public void setInstability(float insta) {
        this.in = insta;

        if (in < 0) {
            in = 0;
        }
    }

    public GearItemEnum getGearEnum() {

        if (this.isUnique()) {
            return GearItemEnum.UNIQUE;
        }

        return GearItemEnum.NORMAL;
    }

    @Override
    public String getRarityRank() {
        return rarity;
    }

    @Override
    public GearRarity getRarity() {
        return ExileDB.GearRarities()
                .get(this.rarity);
    }

    public Text name(ItemStack stack) {
        return stack.getName();
    }

    public void WriteOverDataThatShouldStay(GearItemData newdata) {

        newdata.can_sal = this.can_sal;

    }

    public BaseGearType GetBaseGearType() {
        return ExileDB.GearTypes()
                .get(gear_type);
    }

    public List<MutableText> GetDisplayName(ItemStack stack) {

        try {
            Formatting format = this.getRarity()
                    .textFormatting();

            if (useFullAffixedName()) {
                return getFullAffixedName();
            } else {
                if (isUnique()) {
                    return getUniqueName();
                } else {
                    return getTooManyAffixesName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Arrays.asList();
    }

    public boolean uniqueBaseStatsReplaceBaseStats() {
        return uniqueStats != null && this.uniqueStats.getUnique(this) != null && !uniqueStats.getUnique(this).base_stats.isEmpty();
    }

    private List<MutableText> getFullAffixedName() {
        List<MutableText> list = new ArrayList<>();
        Formatting format = this.getRarity()
                .textFormatting();

        MutableText text = new LiteralText("");

        if (affixes.hasPrefix()) {

            AffixData prefix = affixes.pre.get(0);

            text
                    .append(prefix.BaseAffix()
                            .locName()
                            .append(" "));
        }
        if (this.uniqueStats == null) {
            text.append(GetBaseGearType().locName());
        } else {
            text.append(uniqueStats.getUnique(this)
                    .locName()
            );
        }

        if (affixes.hasSuffix()) {
            AffixData suffix = affixes.suf.get(0);
            text.append(" ")
                    .append(suffix.BaseAffix()
                            .locName());
        }

        text.formatted(format);

        list.addAll(TooltipUtils.cutIfTooLong(text, format));

        return list;

    }

    private List<MutableText> getUniqueName() {
        List<MutableText> list = new ArrayList<>();
        Formatting format = this.getRarity()
                .textFormatting();

        UniqueGear uniq = this.uniqueStats.getUnique(this);

        MutableText txt = new LiteralText("").append(uniq.locName()
                .formatted(format));

        if (!uniq.replaces_name) {
            txt.append(new LiteralText(format + " ").append(GetBaseGearType().locName()
                    .formatted(format)));
        }

        list.addAll(TooltipUtils.cutIfTooLong(txt, format));

        return list;
    }

    private List<MutableText> getTooManyAffixesName() {
        List<MutableText> list = new ArrayList<>();
        Formatting format = this.getRarity()
                .textFormatting();

        Words prefix = RareItemAffixNames.getPrefix(this);
        Words suffix = RareItemAffixNames.getSuffix(this);

        if (prefix != null && suffix != null) {

            MutableText txt = new LiteralText("");

            txt.append(new LiteralText("").append(prefix.locName())
                    .append(" "));

            txt.append(new LiteralText("").append(suffix.locName())
                    .formatted(format));

            txt.append(new LiteralText(" ").append(GetBaseGearType().locName()));

            txt.formatted(format);

            list.addAll(TooltipUtils.cutIfTooLong(txt, format));

            return list;
        }

        return list;

    }

    private boolean useFullAffixedName() {

        if (isUnique() && affixes.getNumberOfAffixes() == 0) {
            return false;
        }
        if (affixes.getNumberOfPrefixes() > 1) {
            return false;
        }
        if (affixes.getNumberOfSuffixes() > 1) {
            return false;
        }
        return true;
    }

    public List<IStatsContainer> GetAllStatContainers() {

        List<IStatsContainer> list = new ArrayList<IStatsContainer>();

        IfNotNullAdd(baseStats, list);

        IfNotNullAdd(imp, list);

        affixes.getAllAffixesAndSockets()
                .forEach(x -> IfNotNullAdd(x, list));

        IfNotNullAdd(sockets, list);

        IfNotNullAdd(uniqueStats, list);

        return list;

    }

    public List<ExactStatData> GetAllStats() {
        return GetAllStats(true);
    }

    public List<ExactStatData> GetAllStats(boolean includebase) {

        List<ExactStatData> list = new ArrayList<>();
        for (IStatsContainer x : GetAllStatContainers()) {
            if (includebase == false && x instanceof BaseStatsData) {
                continue;
            }
            List<ExactStatData> stats = x.GetAllStats(this);

            stats.forEach(s -> {
                list.add(s);
            });

        }
        return list;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void BuildTooltip(TooltipContext ctx) {
        GearTooltipUtils.BuildTooltip(this, ctx.stack, ctx.tooltip, ctx.data);
    }

    public List<IRerollable> GetAllRerollable() {
        List<IRerollable> list = new ArrayList<IRerollable>();
        IfNotNullAdd(baseStats, list);

        affixes.getAllAffixesAndSockets()
                .forEach(x -> IfNotNullAdd(x, list));

        list.add(imp);

        IfNotNullAdd(uniqueStats, list);
        return list;
    }

    private <T> void IfNotNullAdd(T obj, List<T> list) {
        if (obj != null) {
            list.add(obj);
        }
    }

    @Override
    public List<ItemStack> getSalvageResult(float salvageBonus) {
        if (this.can_sal) {
            try {
                SalvageOutput sal = RandomUtils.weightedRandom(ExileDB.SalvageOutputs()
                        .getFiltered(x -> x.isForItem(this.lvl))
                );
                if (sal != null) {
                    return sal.getResult(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Arrays.asList(ItemStack.EMPTY);

        } else return Arrays.asList(ItemStack.EMPTY);
    }

    @Override
    public boolean isSalvagable(SalvageContext context) {

        return this.can_sal;
    }

    @Override
    public ItemstackDataSaver<GearItemData> getStackSaver() {
        return StackSaving.GEARS;
    }

    @Override
    public void saveToStack(ItemStack stack) {
        getStackSaver().saveTo(stack, this);
    }

    public boolean isWeapon() {
        try {
            if (GetBaseGearType()
                    .family()
                    .equals(BaseGearType.SlotFamily.Weapon)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBetterThan(GearItemData other) {

        if (other.lvl > lvl + 10) {
            return false;
        }
        return getRarity()
                .isHigherThan(other.getRarity());
    }

}
