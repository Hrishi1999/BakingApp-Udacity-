package gridentertainment.net.bakingapp;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.RecipeItem;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIHelper {
    @GET("baking.json")
    Call<ArrayList<RecipeItem>> getRecipes();
}