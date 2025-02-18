package com.robertx22.age_of_exile.gui.overlays;

import com.robertx22.age_of_exile.mmorpg.SyncedToClientValues;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class AreaLevelIndicator {

    public static void draw(MatrixStack matrix, int x, int y) {

        MinecraftClient mc = MinecraftClient.getInstance();

        String text = "Area Level: ";

        String lvltext = SyncedToClientValues.areaLevel + "";

        int arealvl = SyncedToClientValues.areaLevel;
        TextColors color = TextColors.GREEN;

        int playerlvl = Load.Unit(mc.player)
            .getLevel();

        if (arealvl - 10 > playerlvl) {
            color = TextColors.RED;
            lvltext += "!!";
        } else if (arealvl - 5 > playerlvl) {
            color = TextColors.YELLOW;
            lvltext += "!";
        }
        text += lvltext;

        int width = mc.textRenderer.getWidth(text);

        drawPrettyLevelText(text, matrix, x - width / 2F, y + 12, color);
    }

    public enum TextColors {
        RED(13313553),
        YELLOW(15252255),
        GREEN(8453920);
        public int color;

        TextColors(int color) {
            this.color = color;
        }
    }

    public static void drawPrettyLevelText(String string, MatrixStack matrix, float m, float n, TextColors color) {

        MinecraftClient mc = MinecraftClient.getInstance();

        // copied from how vanilla renders the total experience level text
        mc.textRenderer
            .draw(matrix, string, (m + 1), n, 0);
        mc.textRenderer
            .draw(matrix, string, (m - 1), n, 0);
        mc.textRenderer
            .draw(matrix, string, m, (n + 1), 0);
        mc.textRenderer
            .draw(matrix, string, m, (n - 1), 0);
        mc.textRenderer
            .draw(matrix, string, m, n, color.color);
    }

}
