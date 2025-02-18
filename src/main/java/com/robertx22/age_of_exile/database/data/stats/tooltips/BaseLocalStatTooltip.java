package com.robertx22.age_of_exile.database.data.stats.tooltips;

import com.robertx22.age_of_exile.database.data.stats.name_regex.StatNameRegex;
import com.robertx22.age_of_exile.saveclasses.item_classes.tooltips.TooltipStatInfo;
import com.robertx22.age_of_exile.saveclasses.item_classes.tooltips.TooltipStatWithContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class BaseLocalStatTooltip implements IStatTooltipType {

    @Override
    public List<Text> getTooltipList(Formatting format, TooltipStatWithContext ctx) {
        TooltipStatInfo info = ctx.statinfo;

        List<Text> list = new ArrayList<Text>();
        if (true) {
            String icon = "\u25CF";
            list.add(new LiteralText(icon + " ")
                .append(info.stat.locName())
                .append(": ")
                .formatted(format != null ? format : Formatting.WHITE)
                .append(new LiteralText((int) info.firstValue + "")
                    .formatted(Formatting.GRAY)));

            return list;

        }
        String icon = Formatting.RED + info.stat.icon + " ";

        if (ctx.statinfo.stat.is_long) {
            icon = "";
        }

        MutableText txt = new LiteralText(StatNameRegex.BASIC_LOCAL
            .translate(format, ctx, info.type, info.firstValue, info.stat));

        if (ctx.statinfo.stat.is_long) {
            return longStat(ctx, txt);
        }

        if (ctx.showStatRanges()) {
            txt.append(" ")
                .append(NormalStatTooltip.getPercentageView(ctx.statinfo.percent));
        }

        list.add(txt);

        if (info.shouldShowDescriptions()) {
            list.addAll(info.stat.getCutDescTooltip());
        }

        return list;

    }
}
