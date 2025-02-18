package com.robertx22.age_of_exile.uncommon.utilityclasses;

import com.robertx22.age_of_exile.capability.entity.EntityCap;
import com.robertx22.age_of_exile.database.data.gear_slots.GearSlot;
import com.robertx22.age_of_exile.database.data.rarities.GearRarity;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.Rarity;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.StatRequirement;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.ICommonDataItem;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.age_of_exile.uncommon.wrappers.SText;
import com.robertx22.library_of_exile.utils.CLOC;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TooltipUtils {

    public static String CHECKMARK = Formatting.GREEN + "\u2714";
    public static String X = Formatting.RED + "\u2716";

    public static MutableText color(Formatting format, MutableText comp) {
        return new LiteralText(format + "").append(comp);
    }

    public static void addRequirements(List<Text> tip, int lvl, StatRequirement req, EntityCap.UnitData data) {

        if (data.getLevel() >= lvl) {
            tip.add(new LiteralText(Formatting.GREEN + "" + Formatting.BOLD + StatRequirement.CHECK_YES_ICON + Formatting.GRAY)
                .append(Formatting.GRAY + " Level Min: " + lvl + " "));

        } else {
            tip.add(new LiteralText(Formatting.RED + "" + Formatting.BOLD + StatRequirement.NO_ICON + Formatting.GRAY)
                .append(Formatting.GRAY + " Level Min: " + lvl + " ")
            );
        }
        tip.addAll(req
            .GetTooltipString(lvl, data));
    }

    public static void addSocketNamesLine(List<Text> tip, GearItemData gear) {
        if (gear.sockets.sockets.size() > 0) {
            tip.add(new LiteralText("Gemmed").formatted(Formatting.LIGHT_PURPLE));
        }
    }

    public static void addEmpty(List<Text> tooltip) {
        tooltip.add(CLOC.blank(""));
    }

    public static List<String> compsToStrings(List<Text> list) {
        return list.stream()
            .map(x -> x.asString())
            .collect(Collectors.toList());
    }

    public static MutableText level(int lvl) {
        return new LiteralText(Formatting.YELLOW + "").append(Words.Level.locName())
            .append((": " + lvl))
            .formatted(Formatting.YELLOW);

    }

    public static List<MutableText> cutIfTooLong(MutableText comp) {
        List<String> stringList = cutIfTooLong(CLOC.translate(comp));
        return stringList.stream()
            .map(x -> new SText(x))
            .collect(Collectors.toList());

    }

    public static List<MutableText> cutIfTooLong(MutableText comp, Formatting format) {
        List<String> stringList = cutIfTooLong(CLOC.translate(comp));
        return stringList.stream()
            .map(x -> new SText(x).formatted(format))
            .collect(Collectors.toList());

    }

    // private static final Pattern PATTERN = Pattern.compile("(?)§[0-9A-FK-OR]");

    public static List<String> cutIfTooLong(String str) {

        List<String> list = new ArrayList<>();

        Formatting format = null;

        char[] array = str.toCharArray();

        int start = 0;
        int i = 0;

        Formatting formattouse = null;

        for (Character c : array) {

            if (c.equals('§')) {
                format = Formatting.byCode(array[i + 1]);
            }

            if (i == str.length() - 1) {
                String cut = str.substring(start);
                if (cut.startsWith(" ")) {
                    cut = cut.substring(1);
                }
                if (formattouse != null) {
                    cut = formattouse + cut;
                    format = null;
                    formattouse = null;
                }
                list.add(cut);
            } else if (i - start > 28 && c == ' ') {
                String cut = str.substring(start, i);
                if (start > 0) {
                    cut = cut.substring(1);
                }

                if (format != null) {
                    formattouse = format;
                }

                list.add(cut);

                start = i;
            }
            i++;
        }

        return list;
    }

    public static MutableText itemBrokenText(ItemStack stack, ICommonDataItem data) {

        if (data != null) {

            if (RepairUtils.isItemBroken(stack)) {
                MutableText comp = new LiteralText(X + " ").append(Words.Broken.locName());
                return comp;
            }

        }

        return null;
    }

    public static List<Text> removeDoubleBlankLines(List<Text> list) {
        return removeDoubleBlankLines(list, 5000);
    }

    public static List<Text> removeDoubleBlankLines(List<Text> list, int minLinesCutAllBlanks) {

        List<Text> newt = new ArrayList();

        boolean lastIsEmpty = false;

        boolean alwaysRemoveEmpty = list.size() > minLinesCutAllBlanks;

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i)
                .getString()
                .length() > 2) {
                lastIsEmpty = false;
                newt.add(list.get(i));
            } else {

                if ((lastIsEmpty || alwaysRemoveEmpty)) {

                } else {
                    newt.add(list.get(i));
                }

                lastIsEmpty = true;

            }
        }

        list.clear();

        list.addAll(newt);

        return newt;
    }

    public static MutableText rarity(Rarity rarity) {

        return new LiteralText(rarity.textFormatting() + "")
            .append(rarity.locName())
            .formatted(rarity.textFormatting());
    }

    public static MutableText rarityShort(Rarity rarity) {
        return (new LiteralText(rarity.textFormatting() + "").append(rarity.locName()));
    }

    public static MutableText tier(int tier) {
        return Words.Tier.locName()
            .append(": " + LevelUtils.tierToRomanNumeral(tier));

    }

    public static MutableText gearSlot(GearSlot slot) {
        return new LiteralText("Item Type: ").formatted(Formatting.WHITE)
            .append(slot.locName()
                .formatted(Formatting.AQUA));
    }

    public static MutableText gearTier(int tier) {
        return new LiteralText("Item Tier: ").formatted(Formatting.WHITE)
            .append(new LiteralText(LevelUtils.tierToRomanNumeral(tier)).formatted(Formatting.AQUA));
    }

    public static MutableText gearRarity(GearRarity rarity) {
        return new LiteralText("Rarity: ").formatted(Formatting.WHITE)
            .append(rarity.locName()
                .formatted(rarity.textFormatting()));
    }

    public static MutableText gearLevel(int lvl) {
        return new LiteralText("Level: ")
            .formatted(Formatting.WHITE)
            .append(new LiteralText(lvl + "")
                .formatted(Formatting.YELLOW));
    }

    public static MutableText dragOntoGearToUse() {
        return new LiteralText("[Drag onto gear to use]").formatted(Formatting.AQUA, Formatting.BOLD);
    }
}
