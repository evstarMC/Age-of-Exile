package com.robertx22.age_of_exile.capability.player;

import com.robertx22.age_of_exile.capability.bases.ICommonPlayerCap;
import com.robertx22.age_of_exile.database.data.skill_gem.SkillGemData;
import com.robertx22.age_of_exile.database.data.spells.components.Spell;
import com.robertx22.age_of_exile.database.registry.Database;
import com.robertx22.age_of_exile.saveclasses.spells.SpellCastingData;
import com.robertx22.age_of_exile.saveclasses.spells.skill_gems.SkillGemsData;
import com.robertx22.age_of_exile.vanilla_mc.packets.sync_cap.PlayerCaps;
import com.robertx22.library_of_exile.utils.LoadSave;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class PlayerSpellCap {

    private static final String PLAYER_SPELL_DATA = "player_spells_data";
    private static final String GEMS = "gems";

    public abstract static class ISpellsCap implements ICommonPlayerCap {

        public abstract Spell getSpellByNumber(int key);

        public abstract SpellCastingData getCastingData();

        public abstract SkillGemsData getSkillGemData();

    }

    public static class DefaultImpl extends ISpellsCap {
        SpellCastingData spellCastingData = new SpellCastingData();

        SkillGemsData skillGems = new SkillGemsData();

        @Override
        public CompoundTag toTag(CompoundTag nbt) {

            try {
                LoadSave.Save(spellCastingData, nbt, PLAYER_SPELL_DATA);
                LoadSave.Save(skillGems, nbt, GEMS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return nbt;

        }

        @Override
        public PlayerCaps getCapType() {
            return PlayerCaps.SPELLS;
        }

        @Override
        public void fromTag(CompoundTag nbt) {

            try {
                this.spellCastingData = LoadSave.Load(SpellCastingData.class, new SpellCastingData(), nbt, PLAYER_SPELL_DATA);

                if (spellCastingData == null) {
                    spellCastingData = new SpellCastingData();
                }

                this.skillGems = LoadSave.Load(SkillGemsData.class, new SkillGemsData(), nbt, GEMS);

                if (skillGems == null) {
                    skillGems = new SkillGemsData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Spell getSpellByNumber(int key) {
            ItemStack gem = this.skillGems.getSkillGemOf(key);

            SkillGemData data = SkillGemData.fromStack(gem);

            if (data != null) {
                return Database.Spells()
                    .get(data.getSkillGem()
                        .spell_id);
            }

            return null;
        }

        @Override
        public SpellCastingData getCastingData() {
            return this.spellCastingData;
        }

        @Override
        public SkillGemsData getSkillGemData() {
            return this.skillGems;
        }

    }

}
