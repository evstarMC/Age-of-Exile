package com.robertx22.age_of_exile.gui.screens.dungeon;

import com.robertx22.age_of_exile.dimension.dungeon_data.DungeonData;
import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.library_of_exile.utils.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DungeonButton extends TexturedButtonWidget {

    public static int xSize = 32;
    public static int ySize = 32;

    static Identifier LOC = Ref.guiId("dungeon_button");
    static Identifier ICON = Ref.guiId("dungeon/icons/nether");

    MinecraftClient mc = MinecraftClient.getInstance();

    DungeonData dungeon;

    public DungeonButton(DungeonData dungeon, int xPos, int yPos) {
        super(xPos + 1, yPos + 1, xSize, ySize, 0, 0, ySize + 1, LOC, (button) -> {
        });

        this.dungeon = dungeon;
    }

    @Override
    public void renderButton(MatrixStack matrix, int x, int y, float ticks) {
        super.renderButton(matrix, x, y, ticks);

        // icon
        mc.getTextureManager()
            .bindTexture(ICON);
        drawTexture(matrix, this.x, this.y, 0, 0, 32, 32, 32, 32);

    }

    @Override
    public void renderToolTip(MatrixStack matrix, int x, int y) {
        if (isInside(x, y)) {
            List<Text> tooltip = new ArrayList<>();
            tooltip.addAll(dungeon.getTooltip());
            GuiUtils.renderTooltip(matrix, tooltip, x, y);
        }

    }

    public boolean isInside(int x, int y) {
        return GuiUtils.isInRect(this.x, this.y, xSize, ySize, x, y);
    }

}
