package gridentertainment.net.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.RecipeItem;
import gridentertainment.net.bakingapp.Models.WidgetModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigActivity extends AppCompatActivity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private ArrayList<RecipeItem> recipes;
    private Spinner spinner;

    public ConfigActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setTitle("Add Widget");
        setContentView(R.layout.activity_config);
        spinner= findViewById(R.id.spinner);

        Button add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Context context = ConfigActivity.this;
                int position=spinner.getSelectedItemPosition();

                WidgetModel model=new WidgetModel(recipes.get(position).getName(),
                        recipes.get(position).getIngredients());

                RecipeDB db=new RecipeDB(ConfigActivity.this);
                db.insertItem(model,mAppWidgetId);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RecipeAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIHelper service =
                retrofit.create(APIHelper.class);
        Call<ArrayList<RecipeItem>> call = service.getRecipes();

        call.enqueue(new Callback<ArrayList<RecipeItem>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<RecipeItem>> call, @NonNull Response<ArrayList<RecipeItem>> response) {
                assert response.body() != null;
                recipes = response.body();
                String[]values= new  String [recipes.size()];

                for(int i=0; i < recipes.size();i++)
                {
                    values[i]=recipes.get(i).getName();
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, values);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<RecipeItem>> call, @NonNull Throwable t) {}
        });
    }
}
