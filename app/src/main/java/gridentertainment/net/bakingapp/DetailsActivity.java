package gridentertainment.net.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("data2", getIntent().getExtras().getParcelable("data"));
        recipeDetailFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_details_container, recipeDetailFragment)
                .commit();
    }
}
