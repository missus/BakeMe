package com.example.android.bakeme;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakeme.adapter.RecipeAdapter;
import com.example.android.bakeme.loader.RecipeLoader;
import com.example.android.bakeme.model.Recipe;

import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int RECIPE_LOADER_ID = 1;
    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Recipe> recipes = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recipe_grid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns)));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RecipeAdapter(this, recipes, new RecipeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(Recipe.class.getSimpleName(), mAdapter.getItem(position));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            if (savedInstanceState != null) {
                mLoaderManager = getLoaderManager();
                mLoaderManager.initLoader(RECIPE_LOADER_ID, savedInstanceState, MainActivity.this);
            } else {
                mLoaderManager = getLoaderManager();
                getLoaderManager().restartLoader(RECIPE_LOADER_ID, null, this);
            }
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int i, Bundle bundle) {
        return new RecipeLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
        mAdapter.clear();
        if (recipes != null && !recipes.isEmpty()) {
            mAdapter = new RecipeAdapter(MainActivity.this, recipes, new RecipeAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra(Recipe.class.getSimpleName(), mAdapter.getItem(position));
                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mAdapter.clear();
    }
}

