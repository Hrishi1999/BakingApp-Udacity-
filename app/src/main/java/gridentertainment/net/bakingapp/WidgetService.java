package gridentertainment.net.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.Ingredients;

public class WidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }

    class ListProvider implements RemoteViewsFactory  {
        ArrayList<Ingredients> ingredients;
        Context mContext = null;
        int appWidgetId;

        ListProvider(Context context, Intent intent) {
            this.mContext = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 1);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            ingredients = new ArrayList();

            RecipeDB database = new RecipeDB(mContext);
            ingredients = database.getIngredients(appWidgetId);

        }

        @Override
        public void onDestroy() {

        }


        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list_item);
            views.setTextViewText(R.id.wd_item, ingredients.get(position).getIngredient());
            views.setTextViewText(R.id.wd_measure, ingredients.get(position).getQuantity()
                    + " " + ingredients.get(position).getMeasure());

            return views;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}