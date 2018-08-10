package gridentertainment.net.bakingapp.Models;

import java.util.ArrayList;

public class WidgetModel {
    public String recipeTitle;
    public ArrayList<Ingredients> ingredients;

    public WidgetModel(String recipeTitle, ArrayList<Ingredients> ingredients) {
        this.recipeTitle = recipeTitle;
        this.ingredients = ingredients;
    }

}
