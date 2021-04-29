package com.robertx22.age_of_exile.player_skills.recipe_types;

import com.robertx22.age_of_exile.mmorpg.registers.client.ReiPlugin;
import com.robertx22.age_of_exile.vanilla_mc.blocks.CraftingWidgetComponent;
import com.robertx22.age_of_exile.vanilla_mc.blocks.CraftingWidgetComponentType;
import com.robertx22.age_of_exile.vanilla_mc.blocks.alchemy.AlchemyContainer;
import com.robertx22.age_of_exile.vanilla_mc.blocks.alchemy.AlchemyScreen;
import com.robertx22.age_of_exile.vanilla_mc.blocks.cooking_station.CookingContainer;
import com.robertx22.age_of_exile.vanilla_mc.blocks.cooking_station.CookingScreen;
import com.robertx22.age_of_exile.vanilla_mc.blocks.smithing.SmithingContainer;
import com.robertx22.age_of_exile.vanilla_mc.blocks.tablet.TabletStationContainer;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Arrow;
import me.shedaniel.rei.api.widgets.BurningFire;
import me.shedaniel.rei.api.widgets.Slot;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShapelessCategory implements RecipeCategory<ReiShapelessDisplay> {
    Identifier id;
    Item item;
    String name;

    public ShapelessCategory(Identifier id, Item item, String name) {
        this.id = id;
        this.item = item;
        this.name = name;
    }

    @Override
    @NotNull
    public EntryStack getLogo() {
        return EntryStack.create(item);
    }

    @Override
    @NotNull
    public String getCategoryName() { return name; }

    @Override
    @NotNull
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public @NotNull List<Widget> setupDisplay(ReiShapelessDisplay recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        // get components
        CraftingWidgetComponent[] components = {};
        int y = bounds.getY();
        if (this.id == ReiPlugin.ALCHEMY) {
            components = AlchemyContainer.components;
            y -= 15;
        }
        else if (this.id == ReiPlugin.FOOD) {
            components = CookingContainer.components;
            y -= 5;
        }
        else if (this.id == ReiPlugin.SMITHING) {
            components = SmithingContainer.components;
            y -= 5;
        }
        else if (this.id == ReiPlugin.TABLET) {
            components = TabletStationContainer.components;
//            bounds.y -= -5;
        }

        // fill widgets
        Point origin = new Point(bounds.getX()-10, y-5);
        List<List<EntryStack>> inputs = recipeDisplay.getInputEntries();
        int inputIndex = 0;
        for (CraftingWidgetComponent c : components){
            Point p = new Point(origin.x + c.x, origin.y + c.y);

            if (c.type == CraftingWidgetComponentType.INPUT_SLOT) {
                Slot w = Widgets.createSlot(p).markInput();
                w.entries(inputs.get(inputIndex));
                inputIndex++;
                widgets.add(w);
            }
            else if (c.type == CraftingWidgetComponentType.ARROW) widgets.add(Widgets.createArrow(p));
            else if (c.type == CraftingWidgetComponentType.FUEL_SLOT) {
                p.y += -13;
                BurningFire fire = Widgets.createBurningFire(p);
                fire.animationDurationMS(6000);
                widgets.add(fire);
            }
            else if (c.type == CraftingWidgetComponentType.OUTPUT_SLOT) {
                widgets.add(Widgets.createSlot(p).entries(recipeDisplay.getResultingEntries().get(0)).markOutput());
                widgets.add(Widgets.createResultSlotBackground(p));
            }
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        if (this.id == ReiPlugin.ALCHEMY) return 80;
        else if (this.id == ReiPlugin.FOOD) return 70;
        else if (this.id == ReiPlugin.SMITHING) return 70;
        else if (this.id == ReiPlugin.TABLET) return 100;
        return 80;
    }
}
