package gridentertainment.net.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.Steps;

public class DetailsActivity extends AppCompatActivity implements FragmentListener{

    private FragmentManager fragmentManager;
    private RecipeDetailFragment recipeDetailFragment;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState==null)
        {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            title = bundle.getString("recipe_name");
        }
        else
        {
            title = savedInstanceState.getString("title");
            recipeDetailFragment = (RecipeDetailFragment) fragmentManager.getFragment(savedInstanceState, "detail");
        }
        setTitle(title);

        if(isTablet())
        {
            recipeDetailFragment.setFragmentListener(this);
        }

        if(recipeDetailFragment != null)
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_details_container, recipeDetailFragment).commit();
        }

    }

    @Override
    public void setStep(int index, ArrayList<Steps> steps) {
        if(isTablet())
        {
            StepDetailFragment stepDetailsFragment = new StepDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("steps", steps);
            stepDetailsFragment.setFragmentListener(this);
            bundle.putInt("index", index);
            bundle.putBoolean("tablet", true);
            stepDetailsFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.step_details_container, stepDetailsFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isTablet() && recipeDetailFragment!=null)
        {
            try{
                fragmentManager.putFragment(outState, "detail", recipeDetailFragment);
            }catch (NullPointerException e) {}
        }
        outState.putString("title", title);
    }

    public boolean isTablet() {
        try {

            DisplayMetrics dm = this.getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));
            return size >= 6;
        }
        catch (Throwable t) {
            return false;
        }
    }
}
