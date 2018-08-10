package gridentertainment.net.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.Ingredients;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.RecyclerHolder> {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<Ingredients> ingredients;


    public IngredientsAdapter(Context context , ArrayList<Ingredients> ingredients) {
        this.context = context;
        this.ingredients=ingredients;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public IngredientsAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.ing_item,null);
        return new IngredientsAdapter.RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

        holder.name.setText(ingredients.get(position).getIngredient());
        holder.quantity.setText(""+ingredients.get(position).getQuantity());
        holder.measure.setText(ingredients.get(position).getMeasure());

    }


    @Override
    public int getItemCount()
    {
        return (ingredients == null) ? 0 : ingredients.size();
    }


    class RecyclerHolder extends RecyclerView.ViewHolder {

        private TextView name, quantity, measure;

        RecyclerHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            measure = itemView.findViewById(R.id.measure);
        }
    }

}