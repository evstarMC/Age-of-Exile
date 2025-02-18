package com.robertx22.age_of_exile.database.data;

import com.google.gson.JsonObject;
import com.robertx22.age_of_exile.database.data.stats.Stat;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.saveclasses.ExactStatData;
import com.robertx22.age_of_exile.uncommon.enumclasses.ModType;
import com.robertx22.library_of_exile.registry.serialization.IByteBuf;
import com.robertx22.library_of_exile.registry.serialization.ISerializable;
import info.loenwind.autosave.annotations.Factory;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

@Storable
public class StatModifier implements ISerializable<StatModifier>, IByteBuf<StatModifier> {

    @Store
    public float min = 0;
    @Store
    public float max = 0;

    @Store
    public String stat;
    @Store
    public String type;

    public static StatModifier EMPTY = new StatModifier();

    @Factory
    private StatModifier() {

    }

    @Override
    public StatModifier getFromBuf(PacketByteBuf buf) {
        StatModifier mod = new StatModifier();
        mod.min = buf.readFloat();
        mod.max = buf.readFloat();

        mod.stat = buf.readString(100);
        mod.type = buf.readString(100);
        return mod;
    }

    @Override
    public void toBuf(PacketByteBuf buf) {
        buf.writeFloat(min);
        buf.writeFloat(max);

        buf.writeString(stat, 100);
        buf.writeString(type, 100);

    }

    public StatModifier(float firstMin, float firstMax, Stat stat) {
        this.min = firstMin;
        this.max = firstMax;
        this.stat = stat.GUID();
        this.type = ModType.FLAT.name();
    }

    public StatModifier(float firstMin, float firstMax, String stat, ModType type) {
        this.min = firstMin;
        this.max = firstMax;
        this.stat = stat;
        this.type = type.name();
    }

    public StatModifier(float firstMin, float firstMax, Stat stat, ModType type) {

        this.min = firstMin;
        this.max = firstMax;
        this.stat = stat.GUID();
        this.type = type.name();
    }

    public StatModifier(float firstMin, float firstMax, float secondMin, float secondMax, String stat, ModType type) {

        this.min = firstMin;
        this.max = firstMax;
        this.stat = stat;
        this.type = type.name();
    }

    public Stat GetStat() {
        return ExileDB.Stats()
            .get(stat);
    }

    public MutableText getRangeToShow(int lvl) {

        int fmin = (int) min;
        int fmax = (int) max;

        if (getModType().isFlat()) {
            fmin = (int) GetStat().scale(min, lvl);
            fmax = (int) GetStat().scale(max, lvl);
        }
        String text = fmin + "/" + fmax;

        if (GetStat().IsPercent() || getModType().isLocalIncrease()) {
            text = text + "%";
        } else if (getModType().isGlobalIncrease()) {
            text = text + " Global";
        }

        return new LiteralText("(").formatted(Formatting.GREEN)
            .append(text)
            .append(")");

    }

    public ModType getModType() {
        return ModType.fromString(type);
    }

    @Override
    public JsonObject toJson() {

        JsonObject json = new JsonObject();

        json.addProperty("min", min);
        json.addProperty("max", max);

        json.addProperty("stat", stat);
        json.addProperty("type", ModType.valueOf(type).id);

        return json;
    }

    @Override
    public StatModifier fromJson(JsonObject json) {

        float firstMin = json.get("min")
            .getAsFloat();
        float firstMax = json.get("max")
            .getAsFloat();

        String stat = json.get("stat")
            .getAsString();

        ModType type = ModType.fromString(json.get("type")
            .getAsString());

        return new StatModifier(firstMin, firstMax, stat, type);

    }

    public List<Text> getEstimationTooltip(int lvl) {

        List<Text> list = new ArrayList<>();

        Text txt = getRangeToShow(lvl).append(" ")
            .append(GetStat().locName());

        list.add(txt);

        return list;
    }

    public ExactStatData ToExactStat(int percent, int lvl) {
        return ExactStatData.fromStatModifier(this, percent, lvl);
    }

}
