package com.example.android.bakeme.model;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    private final int mId;
    private final String mName;
    private final int mServing;
    private final String mImage;
    private final List<Ingredient> mIngredients;
    private final List<Step> mSteps;

    public Recipe(int id, String name, int serving, String image, List<Ingredient> ingredients, List<Step> steps) {
        mId = id;
        mName = name;
        mServing = serving;
        mImage = image;
        mIngredients = ingredients;
        mSteps = steps;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getServing() {
        return mServing;
    }

    public String getImage() {
        return mImage;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }
}
