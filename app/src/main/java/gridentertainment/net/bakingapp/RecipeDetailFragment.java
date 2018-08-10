package gridentertainment.net.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.Ingredients;
import gridentertainment.net.bakingapp.Models.Steps;

public class RecipeDetailFragment extends android.support.v4.app.Fragment {

    ArrayList<Ingredients> ingredients;
    ArrayList<Steps> steps;
    FragmentListener listener;
    int currentpos;

    public RecipeDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        final int orientation = getResources().getConfiguration().orientation;
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        RecyclerView ingRv = rootView.findViewById(R.id.recyclerViewIng);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ingRv.setLayoutManager(layoutManager1);

        RecyclerView stepsRv = rootView.findViewById(R.id.recyclerViewSteps);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        stepsRv.setLayoutManager(layoutManager2);

        if(savedInstanceState != null)
        {
            ingredients = savedInstanceState.getParcelableArrayList("ingredients");
            steps = savedInstanceState.getParcelableArrayList("steps");
            currentpos = savedInstanceState.getInt("position");
        }
        else
        {
            final Bundle extra = getArguments();
            ingredients = extra.getParcelableArrayList("ingredients");
            steps = extra.getParcelableArrayList("steps");
            currentpos = 0;

        }

        ingRv.setAdapter(new IngredientsAdapter(getActivity(), ingredients));
        stepsRv.setAdapter(new StepsAdapter(getActivity(), steps));

         if(isTablet())
            {
                if(listener!=null)
            {
                listener.setStep(currentpos,steps);
            }
        }

        stepsRv.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), stepsRv, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                setPosition(position);
                if (!isTablet())
                {
                    Intent details = new Intent(getActivity(),StepDetailActivity.class);
                    bundle.putInt("index", position);
                    bundle.putParcelableArrayList("steps", steps);
                    details.putExtra("bundle2", bundle);
                    startActivity(details);
                }
                else if(isTablet())
                {
                    listener.setStep(position, steps);
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return rootView;
    }

    private void setPosition(int position)
    {
        this.currentpos = position;
    }

    public void setFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }


    public boolean isTablet() {
        try
        {
            DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
            float screenWidth  = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));
            return size >= 6;
        } catch(Throwable t) {
            return false;
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ingredients",ingredients);
        outState.putParcelableArrayList("steps",steps);
        outState.putInt("position",currentpos);
    }


}
