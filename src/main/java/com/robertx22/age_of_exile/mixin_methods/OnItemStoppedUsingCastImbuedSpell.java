package com.robertx22.age_of_exile.mixin_methods;

import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class OnItemStoppedUsingCastImbuedSpell {
    public static void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (stack.getItem() instanceof BowItem) {
            int i = stack.getItem()
                .getMaxUseTime(stack) - remainingUseTicks;
            float f = BowItem.getPullProgress(i);

            if (f == 1F) {
                if (Load.spells(user)
                    .getCastingData()
                    .tryCastImbuedSpell(user)) {
                    ci.cancel();
                }

            }
        }
    }
}
