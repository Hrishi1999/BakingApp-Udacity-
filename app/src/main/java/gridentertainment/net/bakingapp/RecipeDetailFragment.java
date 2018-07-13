package gridentertainment.net.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.Ingredients;
import gridentertainment.net.bakingapp.Models.RecipeItem;

public class RecipeDetailFragment extends Fragment {

    private RecipeItem recipe;
    private IngredientsAdapter ingredientsAdapter;

    public RecipeDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        RecyclerView ingRv = (RecyclerView)rootView.findViewById(R.id.recyclerViewIng);

        ArrayList<Ingredients> ingredients = (ArrayList<Ingredients>)recipe.getIngredients();
        ingredientsAdapter = new IngredientsAdapter(getActivity().getApplicationContext());
        ingredientsAdapter.setIngredientsList(ingredients);
        ingRv.setAdapter(ingredientsAdapter);

        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable("data2");
            Toast.makeText(getActivity().getApplicationContext(), recipe.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
