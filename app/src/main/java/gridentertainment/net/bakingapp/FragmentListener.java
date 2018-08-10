package gridentertainment.net.bakingapp;

import java.util.ArrayList;

import gridentertainment.net.bakingapp.Models.Steps;

public interface FragmentListener {
    void setStep(int index , ArrayList<Steps> steps);
}
