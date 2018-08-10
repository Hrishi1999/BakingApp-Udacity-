package gridentertainment.net.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.RecipeItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private ArrayList<RecipeItem> recipe2;

    @Nullable
    private CountingIdlingResource mIdlingResource = new CountingIdlingResource("load");

    @VisibleForTesting
    public CountingIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerViewMain);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT && isTablet())
        {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        else if (orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        mAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setVisibility(View.INVISIBLE);

        final ArrayList<RecipeItem> recipes = new ArrayList<>();

        //used magic numbers here, what can be another possible way?
        //instead of 25?
        for (int i = 0; i < 25; i++) {
            recipes.add(new RecipeItem(Parcel.obtain()));
        }

        mAdapter.setRecipeList(recipes);

        if(savedInstanceState == null)
        {
            mIdlingResource.increment();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIHelper service =
                    retrofit.create(APIHelper.class);
            Call<ArrayList<RecipeItem>> call = service.getRecipes();

            call.enqueue(new Callback<ArrayList<RecipeItem>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<RecipeItem>> call, @NonNull Response<ArrayList<RecipeItem>> response) {
                    assert response.body() != null;
                    ArrayList<RecipeItem> recipes = response.body();
                    recipe2 = (ArrayList<RecipeItem>)response.body();
                    mAdapter.setRecipeList(recipes);
                    mRecyclerView.setAdapter(mAdapter);
                    mIdlingResource.decrement();
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<RecipeItem>> call, @NonNull Throwable t) {}
            });
        }
        else
        {
            recipe2 = savedInstanceState.getParcelableArrayList("recipeItem");
            mAdapter.setRecipeList(recipe2);
            mRecyclerView.setAdapter(mAdapter);
        }



        mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(MainActivity.this, mRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(recipe2.get(position).getName() != null)
                {
                    Intent details=new Intent(MainActivity.this,DetailsActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("steps",
                            (ArrayList<? extends Parcelable>)recipe2.get(position).getSteps());
                    bundle.putParcelableArrayList("ingredients",
                            (ArrayList<? extends Parcelable>)recipe2.get(position).getIngredients());
                    bundle.putString("recipe_name",recipe2.get(position).getName());
                    details.putExtra("bundle",bundle);
                    startActivity(details);
                }

            }
            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("recipeItem", recipe2);
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
