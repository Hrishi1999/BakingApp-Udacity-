package gridentertainment.net.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gridentertainment.net.bakingapp.Models.Ingredients;
import gridentertainment.net.bakingapp.Models.RecipeItem;
import gridentertainment.net.bakingapp.R;
import gridentertainment.net.bakingapp.RecipeViewHolder;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>
{

    private ArrayList<RecipeItem> mRecipeList;
    private ArrayList<Ingredients> mIngredientsList;
    private OnItemClickListener listener;
    private LayoutInflater mInflater;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(RecipeItem item);
    }

    public RecipeAdapter(Context context)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mRecipeList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.recitem, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position)
    {
        holder.bind(mRecipeList.get(position), listener);
    }

    @Override
    public int getItemCount()
    {
        return (mRecipeList == null) ? 0 : mRecipeList.size();
    }

    public void setRecipeList(ArrayList<RecipeItem> recipeList)
    {
        this.mRecipeList.clear();
        this.mRecipeList.addAll(recipeList);
        notifyDataSetChanged();
    }

    public ArrayList getRecipeList(){
        return new ArrayList<RecipeItem>(mRecipeList);
    }
}
