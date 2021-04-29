package com.robertx22.age_of_exile.uncommon.auto_comp;

import com.google.common.collect.Multimap;
import com.robertx22.age_of_exile.config.forge.ModConfig;
import com.robertx22.age_of_exile.config.forge.parts.AutoCompatibleItemConfig;
import com.robertx22.age_of_exile.config.forge.parts.AutoConfigItemType;
import com.robertx22.age_of_exile.database.data.gear_slots.GearSlot;
import com.robertx22.age_of_exile.database.data.gear_types.bases.BaseGearType;
import com.robertx22.age_of_exile.database.registry.Database;
import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.age_of_exile.uncommon.testing.Watch;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.stream.Collectors;

public class ItemAutoPowerLevels {

    private static HashMap<String, ItemAutoPowerLevels> STRONGEST = new HashMap<>();
    public static HashMap<Item, AutoConfigItemType> CACHED = new HashMap<>();
    public static HashMap<Item, Float> CACHED_FLOATS = new HashMap<>();

    public ItemAutoPowerLevels(Item item, BaseGearType slot) {

        try {
            this.item = item;
            ItemStack itemAsStack = new ItemStack(item);
            Multimap<EntityAttribute, EntityAttributeModifier> stats = item.getAttributeModifiers(slot.getVanillaSlotType());
            this.statAmount = stats.size();

            double mainAttribute = 0, mainAttributeMultiplier = 1;
            if (item instanceof SwordItem){
                SwordItem sword = (SwordItem) item;
                // Main attribute: DAMAGE
                mainAttribute = sword.getAttackDamage();
                // Multiplier: ATTACK SPEED
                Optional<EntityAttributeModifier> attribute = stats.get(EntityAttributes.GENERIC_ATTACK_SPEED).stream().findFirst();
                if (attribute.isPresent()) mainAttributeMultiplier = -(float)attribute.get().getValue();
            }
            // axes
            else if (item instanceof AxeItem){
                AxeItem axe = ((AxeItem) item);
                // Main attribute: DAMAGE
                mainAttribute = axe.getAttackDamage();
                // Multiplier: ATTACK SPEED
                Optional<EntityAttributeModifier> attribute = stats.get(EntityAttributes.GENERIC_ATTACK_SPEED).stream().findFirst();
                if (attribute.isPresent()) mainAttributeMultiplier = -(float)attribute.get().getValue();
            }
            else if (item instanceof BowItem){
                BowItem bow = (BowItem) item;
                // Main attribute: RANGE (default 15 = main attribute of 6)
                mainAttribute = bow.getRange()/2.5;
                // Multiplier: DRAW SPEED
                mainAttributeMultiplier = ((72000 - bow.getMaxUseTime(itemAsStack))/10.0); // DRAW SPEED
            }
            else if (item instanceof CrossbowItem){
                CrossbowItem crossbow = (CrossbowItem) item;
                // Main attribute: RANGE (crossbows 8)
                mainAttribute = crossbow.getRange();
                // Multiplier: DRAW SPEED
                mainAttributeMultiplier = crossbow.getMaxUseTime(itemAsStack);
            }
            else {
                // some items only differ by durability, so make the more durable ones have higher value
                mainAttribute = (int)(item.getMaxDamage() / 250F);
            }
            System.out.println("---- " + item.toString());

            // build mainFactor
            if (mainAttributeMultiplier < 1) mainAttributeMultiplier = 1;
            int baseFactor = (int)(mainAttribute*mainAttributeMultiplier);
            System.out.println("main: " + mainAttribute +  " mux: " + mainAttributeMultiplier);

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
            System.out.println("base: " + baseFactor + " dura: " + durabilityFactor + " rarity: " + rarityFactor);

            // get max caps
            int MAX_SINGLE_STAT_VALUE = ModConfig.get().autoCompatibleItems.MAX_SINGLE_STAT_VALUE;
            int MAX_TOTAL_STATS = ModConfig.get().autoCompatibleItems.MAX_TOTAL_STATS;

            // add all factors
            int sum = 0;
            for (int factor : new int[]{baseFactor, rarityFactor, durabilityFactor}) sum += MathHelper.clamp(factor, -MAX_SINGLE_STAT_VALUE, MAX_SINGLE_STAT_VALUE);
            totalStatNumbers = MathHelper.clamp(sum, 0, MAX_TOTAL_STATS);
            System.out.println("totalStatNumbers: " + totalStatNumbers);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static float getFloatValueOf(Item item) {

        if (STRONGEST.isEmpty()) {
            return 0F;
        }

        if (CACHED_FLOATS.containsKey(item)) {
            return CACHED_FLOATS.get(item);
        }

        List<BaseGearType> slots = Database.GearTypes()
            .getList()
            .stream()
            .filter(x -> BaseGearType.isGearOfThisType(x, item))
            .collect(Collectors.toList());

        float val = 0;

        for (BaseGearType slot : slots) {
            ItemAutoPowerLevels power = new ItemAutoPowerLevels(item, slot);

            ItemAutoPowerLevels best = getStrongestOf(slot.getGearSlot());

            if (best == null) {
                System.out.println("No best item for slot: " + slot.getGearSlot()
                    .GUID());
                return 0F;
            }

            val += power.divideBy(best);

        }

        val /= slots.size();

        CACHED_FLOATS.put(item, val);

        return val;
    }

    @Nullable
    public static AutoConfigItemType getHandCustomizedType(Item item) {

        if (!ModConfig.get().autoCompatibleItems.ENABLE_MANUAL_TWEAKS) return null;

//        if (item == Items.BOW || item == Items.CROSSBOW) return ModConfig.get().autoCompatibleItems.TIER_0;

        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            ToolMaterial mat = tool.getMaterial();

            if (mat == ToolMaterials.WOOD) {
                return ModConfig.get().autoCompatibleItems.WOOD;
            }
            if (mat == ToolMaterials.STONE) {
                return ModConfig.get().autoCompatibleItems.STONE;
            }

        } else if (item instanceof ArmorItem) {
            ArmorItem tool = (ArmorItem) item;
            ArmorMaterial mat = tool.getMaterial();

            if (mat == ArmorMaterials.LEATHER) {
                return ModConfig.get().autoCompatibleItems.LEATHER;
            }

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

    public boolean isStrongerThan(ItemAutoPowerLevels other) {
        return totalStatNumbers > other.totalStatNumbers;
    }

    public float divideBy(ItemAutoPowerLevels other) {
        return totalStatNumbers / other.totalStatNumbers;
    }

    public Item item;
    public int statAmount = 0;
    public float totalStatNumbers = 0;

    public static ItemAutoPowerLevels getStrongestOf(GearSlot slot) {
        return STRONGEST.get(slot.GUID());
    }

    public static void setupHashMaps() {
        Set<BaseGearType> types = new HashSet<>(Database.GearTypes().getList());

        Watch watch = new Watch();

        Registry.ITEM
            .stream()
            .filter(x -> !Ref.MODID.equals(Registry.ITEM.getId(x).getNamespace()) && !(x instanceof BlockItem))
            .forEach(item -> {
                try {
                    for (BaseGearType slot : types) {
                        if (BaseGearType.isGearOfThisType(slot, item)) {

                            ItemAutoPowerLevels current = new ItemAutoPowerLevels(item, slot);

                            ItemAutoPowerLevels strongest = STRONGEST.getOrDefault(slot.GUID(), current);

                            if (current.isStrongerThan(strongest)) strongest = current;

                            STRONGEST.put(slot.getGearSlot().GUID(), strongest);

                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        watch.print("[Setting up auto compatibility config power levels] ");

    }

}
