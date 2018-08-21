package com.example.android.bakeme.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.bakeme.Utils;
import com.example.android.bakeme.model.Recipe;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private final String mUrl;

    public RecipeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        List<Recipe> recipes;
        if (mUrl == null) {
            return null;
        }
        recipes = Utils.fetchRecipeData(mUrl);
        return recipes;
    }
}