package com.robertx22.age_of_exile.database.data.currency.base;

import com.robertx22.age_of_exile.config.forge.ModConfig;
import com.robertx22.age_of_exile.database.data.currency.loc_reqs.BaseLocRequirement;
import com.robertx22.age_of_exile.database.data.currency.loc_reqs.LocReqContext;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.uncommon.datasaving.Gear;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.age_of_exile.uncommon.utilityclasses.TooltipUtils;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public interface ICurrencyItemEffect {

    public abstract ItemStack internalModifyMethod(ItemStack stack, ItemStack currency);

    public abstract List<BaseLocRequirement> requirements();

    public default float getInstability() {
        return 0;
    }

    public default float getBreakChance() {
        return 0;
    }

    public enum StationType {
        MODIFY, SOCKET
    }

    StationType forStation();

    default boolean canItemBeModified(LocReqContext context) {

        if (context.isGear()) {
            GearItemData gear = (GearItemData) context.data;
            if (gear.getInstability() >= ModConfig.get().Server.MAX_INSTABILITY) {
                return false;
            }
        }

        for (BaseLocRequirement req : requirements()) {
            if (req.isNotAllowed(context)) {
                return false;
            }

        }
        return true;
    }

    public default void addToTooltip(List<Text> tooltip) {

        if (Screen.hasShiftDown()) {
            tooltip.add(TooltipUtils.color(Formatting.RED, Words.Requirements.locName()
                .append(": ")));

            for (BaseLocRequirement req : requirements()) {
                tooltip.add(TooltipUtils.color(Formatting.RED,
                    new LiteralText(" * ").append(req.getText())
                ));
            }
        } else {
            tooltip.add(TooltipUtils.color(Formatting.GREEN, Words.PressShiftForRequirements.locName()));

        }
    }

    default ResultItem modifyItem(LocReqContext context) {
        if (context.effect.canItemBeModified(context)) {
            ItemStack copy = context.stack.copy();
            copy = context.effect.internalModifyMethod(copy, context.Currency);

            if (context.isGear()) {
                if (context.effect.getInstability() > 0) {
                    GearItemData gear = Gear.Load(copy);
                    gear.setInstability(gear.getInstability() + context.effect.getInstability());
                    Gear.Save(copy, gear);
                }
            }

            if (RandomUtils.roll(context.effect.getBreakChance())) {
                return new ResultItem(new ItemStack(Items.GUNPOWDER), ModifyResult.BREAK);
            }

            return new ResultItem(copy, ModifyResult.SUCCESS);
        } else {
            return new ResultItem(ItemStack.EMPTY, ModifyResult.NONE);
        }

    }

}
