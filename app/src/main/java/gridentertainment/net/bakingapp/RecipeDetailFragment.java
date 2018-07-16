package gridentertainment.net.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.Ingredients;
import gridentertainment.net.bakingapp.Models.RecipeItem;
import gridentertainment.net.bakingapp.Models.Steps;

public class RecipeDetailFragment extends Fragment {

    private RecipeItem recipe;
    private IngredientsAdapter ingredientsAdapter;
    ArrayList<Ingredients> ingredients;
    ArrayList<Steps> steps;

    public RecipeDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        RecyclerView ingRv = (RecyclerView)rootView.findViewById(R.id.recyclerViewIng);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ingRv.setLayoutManager(layoutManager1);

        final Bundle extra = getArguments();
        ingredients = extra.getParcelableArrayList("ingredients");
        Toast.makeText(getActivity(), extra.getString("recipe_name"), Toast.LENGTH_SHORT).show();
        ingRv.setAdapter(new IngredientsAdapter(getActivity(), ingredients));

        RecyclerView stepsRv = (RecyclerView)rootView.findViewById(R.id.recyclerViewSteps);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        stepsRv.setLayoutManager(layoutManager2);

        steps = extra.getParcelableArrayList("steps");
        stepsRv.setAdapter(new StepsAdapter(getActivity(), steps));

        stepsRv.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), stepsRv, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent details = new Intent(getActivity(),StepDetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("video_url", steps.get(position).getVideoURL());
                bundle.putString("description", steps.get(position).getDescription());
                bundle.putString("image", steps.get(position).getThumbnailURL());

                details.putExtra("bundle2", bundle);
                startActivity(details);

            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        return rootView;
    }

}
