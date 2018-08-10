package gridentertainment.net.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import gridentertainment.net.bakingapp.Models.Ingredients;
import gridentertainment.net.bakingapp.Models.WidgetModel;
import gridentertainment.net.bakingapp.RecipeContract;

public class RecipeDB extends SQLiteOpenHelper {
    Context context;

    public RecipeDB(Context context) {
        super(context, RecipeContract.DATABASE_NAME, null, 1);
        this.context = context;
    }

    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE recipe(id INTEGER PRIMARY KEY AUTOINCREMENT ,name TEXT , widget_id INTEGER )");
        db.execSQL("CREATE TABLE ingredient(id INTEGER PRIMARY KEY AUTOINCREMENT ,content TEXT , measure TEXT "
                + ", quantity REAL , recipe_id INTEGER )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void insertItem(WidgetModel model, int widgetId) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecipeContract.COLUMN_NAME, model.recipeTitle);
        values.put(RecipeContract.COLUMN_WIDGET_ID, Integer.valueOf(widgetId));
        long id = database.insert(RecipeContract.RECIPE_TABLE_NAME, null, values);
        ContentValues values2 = new ContentValues();
        for (int i = 0; i < model.ingredients.size(); i++) {
            values2.put(RecipeContract.COLUMN_INGREDIENT, ((Ingredients) model.ingredients.get(i)).getIngredient());
            values2.put(RecipeContract.COLUMN_MEASURE, ((Ingredients) model.ingredients.get(i)).getMeasure());
            values2.put(RecipeContract.COLUMN_QUANTITY, ((Ingredients) model.ingredients.get(i)).getQuantity());
            values2.put(RecipeContract.COLUMN_RECIPE_ID, id);
            database.insert(RecipeContract.INGREDIENT_TABLE_NAME, null, values2);
        }
    }

    public String getRecipeTitle(int widgetId) {
        String title = null;
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM recipe WHERE widget_id=" + widgetId, null)) {
            if (cursor.moveToFirst()) {
                do {
                    title = cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_NAME));
                } while (cursor.moveToNext());
            }
            return title;
        }
    }

    public ArrayList<Ingredients> getIngredients(int widgetId) {
        ArrayList<Ingredients> list = new ArrayList();
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT content,measure,quantity FROM ingredient join recipe on ingredient.recipe_id=recipe.id WHERE widget_id=" + widgetId, null)) {
            if (cursor.moveToFirst()) {
                do {
                    Ingredients ingredient = new Ingredients();
                    ingredient.setMeasure(cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_MEASURE)));
                    ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(RecipeContract.COLUMN_INGREDIENT)));
                    ingredient.setQuantity(cursor.getDouble(cursor.getColumnIndex(RecipeContract.COLUMN_QUANTITY)));
                    list.add(ingredient);
                } while (cursor.moveToNext());
            }
            return list;
        }

    }
}
