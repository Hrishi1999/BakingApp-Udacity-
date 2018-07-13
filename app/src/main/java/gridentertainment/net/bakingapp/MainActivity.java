package gridentertainment.net.bakingapp;

import android.content.res.Configuration;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerViewMain);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        ArrayList<RecipeItem> recipes = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            recipes.add(new RecipeItem(Parcel.obtain()));
        }
        mAdapter.setRecipeList(recipes);

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
                ArrayList<RecipeItem> movies = response.body();
                mAdapter.setRecipeList(movies);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<RecipeItem>> call, @NonNull Throwable t) {
                Log.e("BAKING!!!", t.toString());
            }
        });

    }
}
