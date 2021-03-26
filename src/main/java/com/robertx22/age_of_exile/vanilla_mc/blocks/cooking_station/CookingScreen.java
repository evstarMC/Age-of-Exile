package com.robertx22.age_of_exile.vanilla_mc.blocks.cooking_station;

import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.age_of_exile.vanilla_mc.blocks.ModificationGui;
import com.robertx22.library_of_exile.gui.HelpButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CookingScreen extends ModificationGui<CookingContainer, CookingTile> {

    // This is the resource location for the background image
    private static final Identifier texture = new Identifier(Ref.MODID, "textures/gui/cooking/cooking_station.png");

    public CookingScreen(CookingContainer cont, PlayerInventory invPlayer, MutableText comp) {
        super(texture, cont, invPlayer, new LiteralText(""), CookingTile.class);
        backgroundWidth = 176;
        backgroundHeight = 207;
    }

    @Override
    protected void init() {
        super.init();

        List<Text> list = new ArrayList<>();

        this.addButton(new HelpButton(list, this.x + 5, this.y + 5));

        int x = this.x + 58;
        int y = this.y + 16;

    }

}