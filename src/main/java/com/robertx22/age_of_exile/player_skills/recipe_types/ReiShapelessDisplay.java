package com.robertx22.age_of_exile.player_skills.recipe_types;

import com.robertx22.age_of_exile.player_skills.recipe_types.base.StationShapeless;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReiShapelessDisplay implements RecipeDisplay {
    private final StationShapeless display;
    private final List<List<EntryStack>> input;
    private final List<EntryStack> output;
    private final Identifier recipeCategory;

    public ReiShapelessDisplay(StationShapeless recipe) {
        this.display = recipe;
        this.input = EntryStack.ofIngredients(recipe.getPreviewInputs());
        this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
        this.recipeCategory = recipe.recipeCategory;
    }

    public Optional<Recipe<?>> getOptionalRecipe() {
        return Optional.ofNullable(this.display);
    }

    @NotNull
    public Optional<Identifier> getRecipeLocation() { return Optional.ofNullable(this.display).map(StationShapeless::getId); }

    @NotNull
    public List<List<EntryStack>> getInputEntries() {
        return this.input;
    }

    @NotNull
    public List<List<EntryStack>> getResultingEntries() {
        return Collections.singletonList(this.output);
    }

    @NotNull
    public List<List<EntryStack>> getRequiredEntries() {
        return this.input;
    }

    @Override
    public @NotNull Identifier getRecipeCategory() { return this.recipeCategory; }

    public int getWidth() {
        return this.display.getPreviewInputs()
            .size() > 4 ? 3 : 2;
    }

    public int getHeight() {
        return this.display.getPreviewInputs()
            .size() > 4 ? 3 : 2;
    }
}
