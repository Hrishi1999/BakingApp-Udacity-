package gridentertainment.net.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gridentertainment.net.bakingapp.Models.Ingredients;
import gridentertainment.net.bakingapp.Models.RecipeItem;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder>
{

    private List<Ingredients> mIngregientsList;
    private OnItemClickListener listener;
    private LayoutInflater mInflater;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(RecipeItem item);
    }

    public IngredientsAdapter(Context context)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mIngregientsList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.ing_item, parent, false);
        IngredientsViewHolder viewHolder = new IngredientsViewHolder(view);
        return viewHolder;
}

    @Override
    public void onBindViewHolder(@NonNull final IngredientsViewHolder holder, final int position)
    {
        holder.textView.setText(mIngregientsList.get(position).toString());
    }

    @Override
    public int getItemCount()
    {
        return (mIngregientsList == null) ? 0 : mIngregientsList.size();
    }

    public void setIngredientsList(ArrayList<Ingredients> ingList)
    {
        this.mIngregientsList.clear();
        this.mIngregientsList.addAll(ingList);
        notifyDataSetChanged();
    }

    public ArrayList getIngredientsList(){
        return new ArrayList<Ingredients>(mIngregientsList);
    }
}