package com.robertx22.mine_and_slash.database.data.spells.blocks.thorn_bush;

import com.robertx22.mine_and_slash.capability.entity.EntityCap;
import com.robertx22.mine_and_slash.database.data.spells.blocks.base.BaseSpellTileEntity;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.configs.SC;
import com.robertx22.mine_and_slash.mmorpg.registers.common.ModTileEntities;
import com.robertx22.mine_and_slash.saveclasses.spells.EntitySpellData;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.effectdatas.SpellDamageEffect;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.EntityFinder;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.SoundUtils;
import com.robertx22.mine_and_slash.vanilla_mc.packets.particles.ParticleEnum;
import com.robertx22.mine_and_slash.vanilla_mc.packets.particles.ParticlePacketData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ThornBushTileEntity extends BaseSpellTileEntity {

    public ThornBushTileEntity() {
        super(ModTileEntities.THORN_BUSH.get());
    }

    @Override
    public void onTick() {

        EntitySpellData sdata = getSpellData();
        int TICK_RATE = sdata.configs.get(SC.TICK_RATE)
            .intValue();
        int RADIUS = sdata.configs.get(SC.RADIUS)
            .intValue();

        if (this.data.ticksExisted > durationInTicks() == false) {

            if (data.ticksExisted % TICK_RATE == 0) {

                LivingEntity caster = data.getCaster(world);

                if (caster == null) {
                    return;
                }

                EntityCap.UnitData data = Load.Unit(caster);

                ParticleEnum.sendToClients(
                    pos, world, new ParticlePacketData(pos, ParticleEnum.THORNS).radius(RADIUS)
                        .motion(new Vec3d(0, 0, 0))
                        .amount(25));

                List<LivingEntity> entities = EntityFinder.start(
                    caster, LivingEntity.class, new Vec3d(pos).add(0.5F, 0, 0.5F))
                    .radius(RADIUS)
                    .height(2)
                    .build();

                entities.forEach(target -> {
                    SpellDamageEffect dmg = getSetupSpellDamage(target);
                    dmg.Activate();

                    SoundUtils.playSound(target, SoundEvents.BLOCK_WET_GRASS_BREAK, 1, 1);

                });

            }
        }

    }

}

