package gridentertainment.net.bakingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import gridentertainment.net.bakingapp.Models.Ingredients;
import gridentertainment.net.bakingapp.Models.RecipeItem;

class RecipeViewHolder extends RecyclerView.ViewHolder
{
    private static final String TAG = "Holder";
    public ImageView imageView;
    public TextView textView;
    public LinearLayout linearLayout;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        textView = itemView.findViewById(R.id.rec_item_title);
        linearLayout = itemView.findViewById(R.id.linearLayout);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    public void bind(final RecipeItem recipe, final RecipeAdapter.OnItemClickListener listener, final int position) {
        textView.setText(recipe.getName() + " - " + recipe.getServings() + " servings");
        if(recipe.getImage()!=null)
        {
            try
            {
                Picasso.get()
                        .load(recipe.getImage())
                        .placeholder(R.drawable.placeholder)
                        .error(R.color.colorAccent)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                imageView.setImageBitmap(bitmap);
                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                linearLayout.setBackgroundColor(textSwatch.getRgb());
                                                textView.setTextColor(textSwatch.getBodyTextColor());
                                            }
                                        });
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                Log.i(TAG,"Error getting image");
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
            catch (Exception e)
            {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.placeholder);
            }
        }
        else
        {
            imageView.setImageResource(R.drawable.placeholder);
        }
        //suggestions in better handling?
    }
}
