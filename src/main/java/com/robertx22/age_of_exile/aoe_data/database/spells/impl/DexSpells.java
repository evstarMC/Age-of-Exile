package com.robertx22.age_of_exile.aoe_data.database.spells.impl;

import com.robertx22.age_of_exile.aoe_data.database.exile_effects.adders.NegativeEffects;
import com.robertx22.age_of_exile.aoe_data.database.spells.PartBuilder;
import com.robertx22.age_of_exile.aoe_data.database.spells.SpellBuilder;
import com.robertx22.age_of_exile.database.data.skill_gem.SkillGemTag;
import com.robertx22.age_of_exile.database.data.spells.components.SpellConfiguration;
import com.robertx22.age_of_exile.database.data.spells.components.actions.SpellAction;
import com.robertx22.age_of_exile.database.data.spells.spell_classes.CastingWeapon;
import com.robertx22.age_of_exile.database.registry.ISlashRegistryInit;
import com.robertx22.age_of_exile.saveclasses.spells.calc.ValueCalculationData;
import com.robertx22.age_of_exile.uncommon.effectdatas.AttackPlayStyle;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import com.robertx22.age_of_exile.uncommon.utilityclasses.DashUtils;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

import java.util.Arrays;

public class DexSpells implements ISlashRegistryInit {

    public static String MULTI_SHOT_ID = "multi_shot";

    @Override
    public void registerAll() {

        SpellBuilder.of("arrow_storm", SpellConfiguration.Builder.arrowSpell(20, 20 * 25), "Arrow Storm",
            Arrays.asList(SkillGemTag.PROJECTILE, SkillGemTag.DAMAGE))
            .weaponReq(CastingWeapon.RANGED)

            .attackStyle(AttackPlayStyle.RANGED)
            .onCast(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1D, 1D))
            .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.createArrow(5D)))
            .onHit(PartBuilder.damage(ValueCalculationData.base(3), Elements.Physical))
            .onHit(PartBuilder.particleOnTick(3D, ParticleTypes.CLOUD, 3D, 0.1D))
            .onHit(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_HIT, 1D, 1D))
            .onHit(PartBuilder.damage(ValueCalculationData.base(3), Elements.Elemental))
            .onTick(PartBuilder.particleOnTick(5D, ParticleTypes.CRIT, 5D, 0.1D))
            .build();

        SpellBuilder.of("charged_arrow", SpellConfiguration.Builder.arrowSpell(20, 20 * 25), "Charged Arrow",
                Arrays.asList(SkillGemTag.PROJECTILE, SkillGemTag.DAMAGE))
                .weaponReq(CastingWeapon.RANGED)
                .attackStyle(AttackPlayStyle.RANGED)
                .onCast(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1D, 1D))
                .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.createArrow(1D)))
                .onHit(PartBuilder.damage(ValueCalculationData.scaleWithAttack(1.5F, 1), Elements.Thunder))

                .onHit(PartBuilder.addExileEffectToEnemiesInAoe(NegativeEffects.PETRIFY, 2D, 20 * 8D))
                .onHit(PartBuilder.aoeParticles(ParticleTypes.FIREWORK, 5D, 0.4D))

                .onHit(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_HIT, 1D, 1D))
                .onHit(PartBuilder.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, 1D, 1D))
                .onHit(PartBuilder.damage(ValueCalculationData.base(10), Elements.Thunder))

                .build();

        SpellBuilder.of("fire_arrow", SpellConfiguration.Builder.arrowSpell(10, 20 * 10), "Fire Arrow",
                Arrays.asList(SkillGemTag.PROJECTILE, SkillGemTag.DAMAGE))
                .weaponReq(CastingWeapon.RANGED)
                .attackStyle(AttackPlayStyle.RANGED)
                .onCast(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1D, 1D))
                .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.createArrow(1D)))
                .onHit(PartBuilder.damage(ValueCalculationData.scaleWithAttack(0.5F, 1), Elements.Fire))

                .onHit(PartBuilder.addExileEffectToEnemiesInAoe(NegativeEffects.BURN, 2D, 20 * 8D))
                .onHit(PartBuilder.aoeParticles(ParticleTypes.FLAME, 100D, 2D))

                .onHit(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_HIT, 1D, 1D))
                .onHit(PartBuilder.playSound(SoundEvents.ENTITY_GENERIC_BURN, 1D, 1D))
                .onHit(PartBuilder.damage(ValueCalculationData.scaleWithAttack(0.25F, 1), Elements.Fire))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.FLAME, 4D, 0.1D))
                .build();

        SpellBuilder.of("frost_arrow", SpellConfiguration.Builder.arrowSpell(10, 20 * 10), "Frost Arrow",
                Arrays.asList(SkillGemTag.PROJECTILE, SkillGemTag.DAMAGE))
                .weaponReq(CastingWeapon.RANGED)
                .attackStyle(AttackPlayStyle.RANGED)
                .onCast(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1D, 1D))
                .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.createArrow(1D)))
                .onHit(PartBuilder.damage(ValueCalculationData.scaleWithAttack(0.5F, 1), Elements.Nature))

                .onHit(PartBuilder.addExileEffectToEnemiesInAoe(NegativeEffects.FROSTBURN, 2D, 20 * 8D))
                .onHit(PartBuilder.aoeParticles(ParticleTypes.ITEM_SNOWBALL, 100D, 2D))

                .onHit(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_HIT, 1D, 1D))
                .onHit(PartBuilder.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1D, 1D))
                .onHit(PartBuilder.damage(ValueCalculationData.scaleWithAttack(0.25F, 1), Elements.Elemental))
                .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.ITEM_SNOWBALL, 4D, 0.1D))
                .build();

        SpellBuilder.of("poison_arrow", SpellConfiguration.Builder.arrowSpell(10, 20 * 10), "Poison Arrow",
            Arrays.asList(SkillGemTag.PROJECTILE, SkillGemTag.DAMAGE))
            .weaponReq(CastingWeapon.RANGED)
            .attackStyle(AttackPlayStyle.RANGED)
            .onCast(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1D, 1D))
            .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.createArrow(1D)))
            .onHit(PartBuilder.damage(ValueCalculationData.scaleWithAttack(0.5F, 1), Elements.Nature))

            .onHit(PartBuilder.addExileEffectToEnemiesInAoe(NegativeEffects.POISON, 2D, 20 * 8D))
            .onHit(PartBuilder.aoeParticles(ParticleTypes.ITEM_SLIME, 100D, 2D))

            .onHit(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_HIT, 1D, 1D))
            .onHit(PartBuilder.playSound(SoundEvents.ENTITY_SPLASH_POTION_BREAK, 1D, 1D))
            .onHit(PartBuilder.damage(ValueCalculationData.scaleWithAttack(0.25F, 1), Elements.Nature))
            .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.ITEM_SLIME, 4D, 0.1D))
            .build();

        SpellBuilder.of("explosive_arrow", SpellConfiguration.Builder.arrowSpell(10, 20 * 10), "Explosive Arrow",
            Arrays.asList(SkillGemTag.PROJECTILE, SkillGemTag.DAMAGE))
            .weaponReq(CastingWeapon.RANGED)
            .attackStyle(AttackPlayStyle.RANGED)
            .onCast(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1D, 1D))
            .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.createArrow(1D)))
            .onHit(PartBuilder.damage(ValueCalculationData.scaleWithAttack(0.5F, 2), Elements.Physical))

            .onHit(PartBuilder.aoeParticles(ParticleTypes.EXPLOSION, 1D, 0.1D))

            .onHit(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_HIT, 1D, 1D))
            .onHit(PartBuilder.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1D, 1D))
            .onHit(PartBuilder.damageInAoe(ValueCalculationData.scaleWithAttack(0.25F, 3), Elements.Physical, 2D))
            .onTick(PartBuilder.particleOnTick(1D, ParticleTypes.CRIT, 4D, 0.1D))
            .build();

        SpellBuilder.of("recoil_shot", SpellConfiguration.Builder.arrowSpell(10, 20 * 10), "Recoil Shot",
            Arrays.asList(SkillGemTag.PROJECTILE, SkillGemTag.DAMAGE))
            .weaponReq(CastingWeapon.RANGED)

            .attackStyle(AttackPlayStyle.RANGED)
            .onCast(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1D, 1D))
            .onCast(PartBuilder.justAction(SpellAction.SUMMON_PROJECTILE.createArrow(1D)))
            .onHit(PartBuilder.damage(ValueCalculationData.base(4), Elements.Physical))
            .onCast(PartBuilder.pushCaster(DashUtils.Way.BACKWARDS, DashUtils.Strength.MEDIUM_DISTANCE))
            .onHit(PartBuilder.addExileEffectToEnemiesInAoe(NegativeEffects.WOUNDS, 1D, 20 * 20D))
            .onHit(PartBuilder.playSound(SoundEvents.ENTITY_ARROW_HIT, 1D, 1D))
            .onHit(PartBuilder.damage(ValueCalculationData.base(3), Elements.Elemental)
            )
            .onTick(PartBuilder.particleOnTick(5D, ParticleTypes.CRIT, 5D, 0.1D)
            )
            .build();

    }
}
