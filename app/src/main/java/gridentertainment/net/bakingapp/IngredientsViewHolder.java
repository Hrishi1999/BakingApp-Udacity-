package gridentertainment.net.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IngredientsViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public LinearLayout linearLayout;

    public IngredientsViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.title2);
        linearLayout = itemView.findViewById(R.id.linearLayout2);
    }
}
