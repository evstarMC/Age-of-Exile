package com.robertx22.mine_and_slash.database.data.spells.spell_classes.fire;

import com.robertx22.mine_and_slash.database.data.gearitemslots.bases.BaseGearType;
import com.robertx22.mine_and_slash.database.data.spells.entities.single_target_bolt.FireballEntity;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.SpellTooltips;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.BaseSpell;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.SpellCastContext;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.cast_types.SpellCastType;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.configs.ImmutableSpellConfigs;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.configs.PreCalcSpellConfigs;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.configs.SC;
import com.robertx22.mine_and_slash.mmorpg.registers.common.ModSounds;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

public class FireballSpell extends BaseSpell {

    private FireballSpell() {
        super(
            new ImmutableSpellConfigs() {

                @Override
                public SpellCastType castType() {
                    return SpellCastType.PROJECTILE;
                }

                @Override
                public SoundEvent sound() {
                    return ModSounds.FIREBALL.get();
                }

                @Override
                public Elements element() {
                    return Elements.Fire;
                }
            }.summonsEntity(world -> new FireballEntity(world))
                .rightClickFor(AllowedAsRightClickOn.MAGE_WEAPON));
    }

    @Override
    public BaseGearType.PlayStyle getPlayStyle() {
        return BaseGearType.PlayStyle.INT;
    }

    @Override
    public PreCalcSpellConfigs getPreCalcConfig() {
        PreCalcSpellConfigs c = new PreCalcSpellConfigs();

        c.set(SC.MANA_COST, 5, 15);
        c.set(SC.BASE_VALUE, 6, 25);
        c.set(SC.SHOOT_SPEED, 0.4F, 0.6F);
        c.set(SC.PROJECTILE_COUNT, 1, 1);
        c.set(SC.CAST_TIME_TICKS, 0, 0);
        c.set(SC.COOLDOWN_TICKS, 15, 10);
        c.set(SC.CDR_EFFICIENCY, 0, 0);
        c.set(SC.DURATION_TICKS, 60, 80);

        c.setMaxLevel(16);

        return c;
    }

    public static FireballSpell getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public String GUID() {
        return "fireball";
    }

    @Override
    public List<Text> GetDescription(TooltipInfo info, SpellCastContext ctx) {

        List<Text> list = new ArrayList<>();

        list.add(SpellTooltips.singleTargetProjectile());

        list.addAll(getCalculation(ctx).GetTooltipString(info, ctx));

        return list;

    }

    @Override
    public Words getName() {
        return Words.Fireball;
    }

    private static class SingletonHolder {
        private static final FireballSpell INSTANCE = new FireballSpell();
    }
}