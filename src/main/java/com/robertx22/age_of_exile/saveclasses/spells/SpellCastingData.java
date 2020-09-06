package com.robertx22.age_of_exile.saveclasses.spells;

import com.robertx22.age_of_exile.capability.player.PlayerSpellCap;
import com.robertx22.age_of_exile.database.data.spells.spell_classes.bases.BaseSpell;
import com.robertx22.age_of_exile.database.data.spells.spell_classes.bases.SpellCastContext;
import com.robertx22.age_of_exile.database.data.spells.spell_classes.bases.configs.SC;
import com.robertx22.age_of_exile.database.registry.SlashRegistry;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Storable
public class SpellCastingData {

    @Store
    public int castingTicksLeft = 0;

    @Store
    public int castingTicksDone = 0;

    @Store
    public int lastSpellCastTimeInTicks = 0;

    public static Integer selectedSpell = 0; // this is just used on client, so client tells server which spell to cast

    @Store
    public String spellBeingCast = "";

    @Store
    private HashMap<Integer, String> bar = new HashMap<>();

    public HashMap<Integer, String> getBar() {
        for (int i = 0; i < 9; i++) {
            if (!bar.containsKey(i)) {
                bar.put(i, "");
            }
        }
        return bar;
    }

    @Store
    private HashMap<String, SpellData> spellDatas = new HashMap<>();

    public void cancelCast(PlayerEntity player) {
        try {
            SpellCastContext ctx = new SpellCastContext(player, 0, getSpellBeingCast());

            BaseSpell spell = getSpellBeingCast();
            if (spell != null && spell.goesOnCooldownIfCastCanceled()) {
                SpellData data = spellDatas.getOrDefault(spell.GUID(), new SpellData());

                int cd = spell.getCooldownInTicks(ctx);
                data.setCooldown(cd);
            }

            spellBeingCast = "";
            castingTicksLeft = 0;
            lastSpellCastTimeInTicks = 0;
            castingTicksDone = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clear() {
        bar.clear();
    }

    public BaseSpell getSelectedSpell() {
        return getSpellByNumber(selectedSpell);
    }

    public boolean isCasting() {
        return castingTicksLeft > 0 && SlashRegistry.Spells()
            .isRegistered(spellBeingCast);
    }

    public void onTimePass(PlayerEntity player, PlayerSpellCap.ISpellsCap spells, int ticks) {

        try {

            if (spellBeingCast != null && spellBeingCast.length() > 0) {
                BaseSpell spell = SlashRegistry.Spells()
                    .get(spellBeingCast);

                SpellCastContext ctx = new SpellCastContext(player, castingTicksDone, spell);

                if (!player.world.isClient) {
                    if (spell != null && spells != null && SlashRegistry.Spells()
                        .isRegistered(spell)) {

                        spell.onCastingTick(ctx);

                    }
                }

                tryCast(player, spells, ctx);

                castingTicksLeft--;
                castingTicksDone++;

                if (castingTicksLeft < 0) {
                    this.spellBeingCast = "";
                }
            }

            spellDatas.values()
                .forEach(x -> x.tickCooldown(ticks));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<String> getSpellsOnCooldown() {
        return spellDatas.entrySet()
            .stream()
            .filter(x -> !x.getValue()
                .cooldownIsReady())
            .map(x -> x.getKey())
            .collect(Collectors.toList());

    }

    public void setToCast(BaseSpell spell, PlayerEntity player) {
        SpellCastContext ctx = new SpellCastContext(player, 0, spell);

        this.spellBeingCast = spell.GUID();
        this.castingTicksLeft = spell.useTimeTicks(ctx);
        this.lastSpellCastTimeInTicks = spell.useTimeTicks(ctx);
        this.castingTicksDone = 0;
    }

    private void tryCast(PlayerEntity player, PlayerSpellCap.ISpellsCap spells, SpellCastContext ctx) {

        if (!spellBeingCast.isEmpty()) {
            if (castingTicksLeft <= 0) {
                BaseSpell spell = SlashRegistry.Spells()
                    .get(spellBeingCast);

                int timesToCast = (int) ctx.getConfigFor(spell)
                    .get(SC.TIMES_TO_CAST)
                    .get(ctx.skillGem);

                if (timesToCast == 1) {
                    spell.cast(ctx);
                }

                player.getMainHandStack()
                    .damage(1, player, x -> {
                        player.sendToolBreakStatus(player.getActiveHand());
                    });

                onSpellCast(spell, player, spells);

                spellBeingCast = "";

            }
        }

    }

    public BaseSpell getSpellBeingCast() {
        return SlashRegistry.Spells()
            .get(spellBeingCast);
    }

    public boolean canCast(BaseSpell spell, PlayerEntity player) {

        if (isCasting()) {
            return false;
        }

        if (spell == null) {
            return false;
        }

        SpellData data = getDataBySpell(spell);

        if (data.cooldownIsReady() == false) {
            return false;
        }

        SpellCastContext ctx = new SpellCastContext(player, 0, spell);

        return spell.canCast(ctx);

    }

    private void onSpellCast(BaseSpell spell, PlayerEntity player, PlayerSpellCap.ISpellsCap spells) {
        SpellData data = spellDatas.getOrDefault(spell.GUID(), new SpellData());

        SpellCastContext ctx = new SpellCastContext(player, 0, spell);

        if (spell.shouldActivateCooldown(player, spells)) {
            int cd = spell.getCooldownInTicks(ctx);
            data.setCooldown(cd);
        }

        spellDatas.put(spell.GUID(), data);

    }

    public SpellData getDataBySpell(BaseSpell spell) {

        String id = spell.GUID();

        if (spellDatas.containsKey(id) == false) {
            spellDatas.put(id, new SpellData());
        }

        return spellDatas.get(id);

    }

    public BaseSpell getSpellByNumber(int key) {

        String id = "";
        try {
            id = bar.getOrDefault(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id != null) {
            if (SlashRegistry.Spells()
                .isRegistered(id)) {
                return SlashRegistry.Spells()
                    .get(id);
            }

        }
        return null;
    }

}
