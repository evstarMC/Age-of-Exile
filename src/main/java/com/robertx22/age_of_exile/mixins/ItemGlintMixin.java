package com.robertx22.age_of_exile.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.robertx22.age_of_exile.config.forge.ClientConfigs;
import com.robertx22.age_of_exile.config.forge.ModConfig;
import com.robertx22.age_of_exile.database.data.rarities.GearRarity;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.uncommon.datasaving.Gear;
import com.robertx22.age_of_exile.uncommon.datasaving.StackSaving;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ItemGlintMixin {

    @Inject(method = "drawSlot(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/screen/slot/Slot;)V", at = @At(value = "HEAD"))
    private void drawMyGlint(MatrixStack matrices, Slot slot, CallbackInfo ci) {

        try {
            HandledScreen screen = (HandledScreen) (Object) this;

            if (ModConfig.get().client.RENDER_ITEM_RARITY_BACKGROUND) {
                ItemStack stack = slot.getStack();

                GearRarity rar = null;

                if (Gear.has(stack)) {

                    GearItemData gear = Gear.Load(stack);

                    rar = gear.getRarity();
                }

                if (StackSaving.STAT_SOULS.has(stack)) {
                    try {
                        rar = ExileDB.GearRarities()
                            .get(StackSaving.STAT_SOULS.loadFrom(stack).rar);
                    } catch (Exception e) {

                    }
                }

                if (rar == null) {
                    return;
                }

                RenderSystem.enableBlend();
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, ModConfig.get().client.ITEM_RARITY_OPACITY); // transparency

                Identifier tex = rar
                    .getGlintTextureFull();

                if (ModConfig.get().client.ITEM_RARITY_BACKGROUND_TYPE == ClientConfigs.GlintType.BORDER) {
                    tex = rar
                        .getGlintTextureBorder();
                }

                MinecraftClient.getInstance()
                    .getTextureManager()
                    .bindTexture(tex);

                screen.drawTexture(matrices, slot.x, slot.y, 0, 0, 16, 16, 16, 16);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
                RenderSystem.disableBlend();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
