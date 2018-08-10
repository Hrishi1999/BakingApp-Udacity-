package gridentertainment.net.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.Steps;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.RecyclerHolder> {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<Steps> steps;


    public StepsAdapter(Context context, ArrayList<Steps> steps) {
        this.context = context;
        this.steps = steps;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.steps_item, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {
        holder.title.setText(steps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout root;

        RecyclerHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_name);
            root = itemView.findViewById(R.id.linearLayoutX);
        }
    }


}

