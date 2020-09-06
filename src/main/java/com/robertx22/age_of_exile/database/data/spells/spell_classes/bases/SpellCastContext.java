package com.robertx22.age_of_exile.database.data.spells.spell_classes.bases;

import com.robertx22.age_of_exile.capability.entity.EntityCap;
import com.robertx22.age_of_exile.capability.player.PlayerSpellCap;
import com.robertx22.age_of_exile.database.data.spells.spell_classes.bases.configs.EntityCalcSpellConfigs;
import com.robertx22.age_of_exile.database.data.spells.spell_classes.bases.configs.PreCalcSpellConfigs;
import com.robertx22.age_of_exile.database.data.spells.spell_classes.bases.configs.SC;
import com.robertx22.age_of_exile.saveclasses.item_classes.CalculatedSpellData;
import com.robertx22.age_of_exile.saveclasses.spells.IAbility;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public class SpellCastContext {

    public final LivingEntity caster;
    public final EntityCap.UnitData data;
    public final PlayerSpellCap.ISpellsCap spellsCap;
    public final int ticksInUse;
    public final BaseSpell spell;
    public boolean isLastCastTick;
    public boolean castedThisTick = false;
    public CalculatedSpellData skillGem;

    public EntityCalcSpellConfigs configForSummonedEntities;

    private HashMap<String, PreCalcSpellConfigs> cacheMap = new HashMap<>();

    public PreCalcSpellConfigs getConfigFor(IAbility ability) {

        if (!cacheMap.containsKey(ability.GUID())) {

            PreCalcSpellConfigs pre = ability.getPreCalcConfig();

            if (ability.getAbilityType() == IAbility.Type.SPELL) {
                pre.modifyByUserStats(this);
            }

            cacheMap.put(ability.GUID(), pre);
        }

        return cacheMap.get(ability.GUID());
    }

    private void calcSpellData() {
        this.skillGem = new CalculatedSpellData();
        skillGem.level = this.data.getLevel();
        skillGem.spell_id = spell.GUID();
    }

    public SpellCastContext(LivingEntity caster, int ticksInUse, CalculatedSpellData spell) {
        this(caster, ticksInUse, spell.getSpell());
    }

    public SpellCastContext(LivingEntity caster, int ticksInUse, BaseSpell spell) {
        this.caster = caster;
        this.ticksInUse = ticksInUse;

        this.spell = spell;

        this.data = Load.Unit(caster);

        if (caster instanceof PlayerEntity) {
            this.spellsCap = Load.spells((PlayerEntity) caster);
        } else {
            this.spellsCap = new PlayerSpellCap.DefaultImpl();
        }

        calcSpellData();

        this.configForSummonedEntities = new EntityCalcSpellConfigs(this.skillGem);

        if (spell != null) {
            int castTicks = (int) getConfigFor(spell).get(SC.CAST_TIME_TICKS)
                .get(skillGem);
            this.isLastCastTick = castTicks == ticksInUse;
        }

    }
}
