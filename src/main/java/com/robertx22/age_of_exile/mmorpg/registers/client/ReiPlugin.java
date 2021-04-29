package com.robertx22.age_of_exile.mmorpg.registers.client;

import com.robertx22.age_of_exile.mmorpg.ModRegistry;
import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.age_of_exile.player_skills.recipe_types.ShapelessCategory;
import com.robertx22.age_of_exile.player_skills.recipe_types.ReiShapelessDisplay;
import com.robertx22.age_of_exile.player_skills.recipe_types.recipe_types.AlchemyShapeless;
import com.robertx22.age_of_exile.player_skills.recipe_types.recipe_types.FoodShapeless;
import com.robertx22.age_of_exile.player_skills.recipe_types.recipe_types.SmithingShapeless;
import com.robertx22.age_of_exile.player_skills.recipe_types.recipe_types.TabletShapeless;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ReiPlugin implements REIPluginV0 {
    public static final Identifier FOOD = Ref.id("food");
    public static final Identifier TABLET = Ref.id("tablet");
    public static final Identifier ALCHEMY = Ref.id("alchemy");
    public static final Identifier SMITHING = Ref.id("smithing");

    @Override
    public Identifier getPluginIdentifier() {
        return Ref.id("rei_plugin");
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(FOOD, EntryStack.create(ModRegistry.MISC_ITEMS.COOKING_STATION));
        recipeHelper.registerWorkingStations(TABLET, EntryStack.create(ModRegistry.MISC_ITEMS.TABLET_STATION));
        recipeHelper.registerWorkingStations(ALCHEMY, EntryStack.create(ModRegistry.MISC_ITEMS.ALCHEMY_STATION));
        recipeHelper.registerWorkingStations(SMITHING, EntryStack.create(ModRegistry.MISC_ITEMS.SMITHING_STATION));
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new ShapelessCategory(FOOD, ModRegistry.MISC_ITEMS.COOKING_STATION, "Food"));
        recipeHelper.registerCategory(new ShapelessCategory(TABLET, ModRegistry.MISC_ITEMS.TABLET_STATION, "Tablet"));
        recipeHelper.registerCategory(new ShapelessCategory(ALCHEMY, ModRegistry.MISC_ITEMS.ALCHEMY_STATION, "Alchemy"));
        recipeHelper.registerCategory(new ShapelessCategory(SMITHING, ModRegistry.MISC_ITEMS.SMITHING_STATION, "Smithing"));
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(FOOD, FoodShapeless.class, ReiShapelessDisplay::new);
        recipeHelper.registerRecipes(TABLET, TabletShapeless.class, ReiShapelessDisplay::new);
        recipeHelper.registerRecipes(ALCHEMY, AlchemyShapeless.class, ReiShapelessDisplay::new);
        recipeHelper.registerRecipes(SMITHING, SmithingShapeless.class, ReiShapelessDisplay::new);
    }
}