package com.robertx22.age_of_exile.uncommon.auto_comp;

import com.google.common.collect.Multimap;
import com.robertx22.age_of_exile.config.forge.ModConfig;
import com.robertx22.age_of_exile.config.forge.parts.AutoCompatibleItemConfig;
import com.robertx22.age_of_exile.config.forge.parts.AutoConfigItemType;
import com.robertx22.age_of_exile.database.data.gear_types.bases.BaseGearType;
import com.robertx22.age_of_exile.database.registry.Database;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.MathHelper;

import java.util.*;
import java.util.stream.Collectors;

public class ItemAutoPowerLevels {

    private static final HashMap<String, PowerLevel> STRONGEST = new HashMap<>();
    public static HashMap<Item, AutoConfigItemType> CACHED = new HashMap<>();
    public static HashMap<Item, Float> CACHED_FLOAT_LEVEL_RANGE = new HashMap<>();

    public static class PowerLevel {
        public Item item;
        public float formulatedPowerLevel = 0;

        public PowerLevel(Item item, BaseGearType slot){
            try {
                this.item = item;
                ItemStack itemAsStack = new ItemStack(item);
                Multimap<EntityAttribute, EntityAttributeModifier> stats = item.getAttributeModifiers(slot.getVanillaSlotType());

                double mainAttribute = 0, mainAttributeMultiplier = 1;
                if (item instanceof SwordItem){
                    SwordItem sword = (SwordItem) item;
                    // Main attribute: DAMAGE
                    mainAttribute = sword.getAttackDamage() *2.0;
                    // Multiplier: ATTACK SPEED
                    Optional<EntityAttributeModifier> attribute = stats.get(EntityAttributes.GENERIC_ATTACK_SPEED).stream().findFirst();
                    if (attribute.isPresent()) mainAttributeMultiplier = 5.0/-(float)attribute.get().getValue();
                }
                // axes
                else if (item instanceof AxeItem){
                    AxeItem axe = ((AxeItem) item);
                    // Main attribute: DAMAGE
                    mainAttribute = axe.getAttackDamage() *2.0;
                    // Multiplier: ATTACK SPEED
                    Optional<EntityAttributeModifier> attribute = stats.get(EntityAttributes.GENERIC_ATTACK_SPEED).stream().findFirst();
                    if (attribute.isPresent()) mainAttributeMultiplier = 5.0/-(float)attribute.get().getValue();
                }
                else if (item instanceof BowItem){
                    BowItem bow = (BowItem) item;
                    // Main attribute: RANGE (default 15)
                    mainAttribute = bow.getRange()/2.5;
                    // Multiplier: DRAW SPEED
                    mainAttributeMultiplier = ((72000 - bow.getMaxUseTime(itemAsStack))/10.0); // DRAW SPEED
                }
                else if (item instanceof CrossbowItem){
                    CrossbowItem crossbow = (CrossbowItem) item;
                    // Main attribute: RANGE (default 8)
                    mainAttribute = crossbow.getRange() / 1.3f;
                    // Multiplier: DRAW SPEED (default 28)
                    mainAttributeMultiplier = 28.0f / crossbow.getMaxUseTime(itemAsStack);
                }
                else if (item instanceof ShieldItem){
                    ShieldItem shield = (ShieldItem) item;
                    // Main attribute: DAMAGE
                    mainAttribute = shield.getMaxDamage() / 100F;
                    mainAttributeMultiplier = 2.0;
                }
                else {
                    // some items only differ by durability, so make the more durable ones have higher value
                    mainAttribute = (int)(item.getMaxDamage() / 250F);
                }
                // build mainFactor
                if (mainAttribute < 1) mainAttribute = 1;
                if (mainAttributeMultiplier < 1) mainAttributeMultiplier = 1;
                int baseFactor = (int)(mainAttribute*mainAttributeMultiplier);

                // add rarity factor
                int rarityFactor = 0;
                Rarity rarity = item.getRarity(itemAsStack);
                if (rarity == Rarity.EPIC) rarityFactor = 25;
                else if (rarity == Rarity.RARE) rarityFactor = 10;
                else if (rarity == Rarity.UNCOMMON) rarityFactor = 5;

                // add durability factor
                int durabilityFactor = 2;
                int durability = item.getMaxDamage();
                if (durability > 2000) durabilityFactor = 20; // NETHERITE
                else if (durability > 1600) durabilityFactor = 10; // DIAMOND
                else if (durability > 300) durabilityFactor = 6; // IRON
                else if (durability > 150) durabilityFactor = 4; // STONE
                else if (durability > 60) durabilityFactor = 2; // WOOD
                else durabilityFactor = 5; // GOLD

                // get max caps
                int MAX_SINGLE_STAT_VALUE = ModConfig.get().autoCompatibleItems.MAX_SINGLE_STAT_VALUE;
                int MAX_TOTAL_STATS = ModConfig.get().autoCompatibleItems.MAX_TOTAL_STATS;

                // add all factors
                int sum = 0;
                for (int factor : new int[]{baseFactor, rarityFactor, durabilityFactor}) sum += MathHelper.clamp(factor, -MAX_SINGLE_STAT_VALUE, MAX_SINGLE_STAT_VALUE);

                // formulate power level with maxCapFactor
                int maxCapMultipler = MAX_TOTAL_STATS / MAX_SINGLE_STAT_VALUE; // defaults to 4
                formulatedPowerLevel = MathHelper.clamp(sum * maxCapMultipler, 0, MAX_TOTAL_STATS);

                System.out.println("[" + item.toString() + "]" + " " + mainAttribute +  " x " + mainAttributeMultiplier + " = base: " + baseFactor + ", dura: " + durabilityFactor + ", rarity: " + rarityFactor + " ===== " + formulatedPowerLevel);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static float getFloatValueOf(Item item) {
        if (CACHED_FLOAT_LEVEL_RANGE.containsKey(item)) return CACHED_FLOAT_LEVEL_RANGE.get(item);

        List<BaseGearType> types = Database.GearTypes()
            .getList()
            .stream()
            .filter(x -> BaseGearType.isGearOfThisType(x, item))
            .collect(Collectors.toList());

        // get type
        BaseGearType type = null;
        for (BaseGearType t : types) if (BaseGearType.isGearOfThisType(t, item)) {  type = t; break; }
        if (type == null) return 0;

        // formulate float level range
        PowerLevel power = new PowerLevel(item, type);
        float floatLevelRange = power.formulatedPowerLevel / ModConfig.get().autoCompatibleItems.MAX_TOTAL_STATS;
        CACHED_FLOAT_LEVEL_RANGE.put(item, floatLevelRange);

        return floatLevelRange;
    }

    @Nullable
    public static AutoConfigItemType getHandCustomizedType(Item item) {
        if (!ModConfig.get().autoCompatibleItems.ENABLE_MANUAL_TWEAKS) return null;

        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            ToolMaterial mat = tool.getMaterial();
            if (mat == ToolMaterials.WOOD) return ModConfig.get().autoCompatibleItems.WOOD;
            else if (mat == ToolMaterials.STONE) return ModConfig.get().autoCompatibleItems.STONE;
        } else if (item instanceof ArmorItem) {
            ArmorItem armor = (ArmorItem) item;
            if (armor.getMaterial() == ArmorMaterials.LEATHER) return ModConfig.get().autoCompatibleItems.LEATHER;
        }

        return null;
    }

    public static AutoConfigItemType getPowerClassification(Float val) {
        AutoCompatibleItemConfig config = ModConfig.get().autoCompatibleItems;
        if (config.TIER_5.isInRange(val)) return config.TIER_5;
        else if (config.TIER_4.isInRange(val)) return config.TIER_4;
        else if (config.TIER_3.isInRange(val)) return config.TIER_3;
        else if (config.TIER_2.isInRange(val)) return config.TIER_2;
        else if (config.TIER_1.isInRange(val)) return config.TIER_1;
        else return config.TIER_0;
    }

    public static AutoConfigItemType getPowerClassification(Item item) {
        if (CACHED.containsKey(item)) return CACHED.get(item);

        AutoConfigItemType handmade = getHandCustomizedType(item);
        if (handmade != null) return handmade;

        AutoConfigItemType type = getPowerClassification(getFloatValueOf(item));
        CACHED.put(item, type);

        return type;
    }


}
