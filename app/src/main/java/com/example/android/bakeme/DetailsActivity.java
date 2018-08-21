package com.example.android.bakeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.bakeme.adapter.IngredientAdapter;
import com.example.android.bakeme.adapter.StepAdapter;
import com.example.android.bakeme.model.Recipe;
import com.example.android.bakeme.widget.WidgetService;


public class DetailsActivity extends AppCompatActivity {
    private RecyclerView mStepRecyclerView;
    private StepAdapter mStepAdapter;

    private RecyclerView mIngredientRecyclerView;
    private IngredientAdapter mIngredientAdapter;

    private Recipe mRecipe;
    private boolean mIsTablet;

    public static final String POSITION = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        mRecipe = (Recipe) getIntent().getSerializableExtra(Recipe.class.getSimpleName());
        mIsTablet = getResources().getBoolean(R.bool.isTablet);
        if (findViewById(R.id.step_detail_container) != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(Recipe.class.getSimpleName(), mRecipe);
            arguments.putInt(POSITION, 0);
            StepFragment fragment = new StepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.step_detail_container, fragment).commitNow();
        }

        setTitle(mRecipe.getName());
        mIngredientRecyclerView = findViewById(R.id.ingredients_grid);
        mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mIngredientAdapter = new IngredientAdapter(this, mRecipe.getIngredients());
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);
        mIngredientRecyclerView.setHasFixedSize(false);
        mIngredientRecyclerView.setNestedScrollingEnabled(false);

        mStepRecyclerView = findViewById(R.id.step_grid);
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mStepAdapter = new StepAdapter(this, mRecipe.getSteps(),
                new StepAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (mIsTablet) {
                            mStepAdapter.mSelected = position;
                            mStepAdapter.notifyDataSetChanged();
                            Bundle arguments = new Bundle();
                            arguments.putSerializable(Recipe.class.getSimpleName(), mRecipe);
                            arguments.putInt(DetailsActivity.POSITION, position);
                            StepFragment fragment = new StepFragment();
                            fragment.setArguments(arguments);
                            DetailsActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container, fragment).commitNow();
                        } else {
                            Intent intent = new Intent(DetailsActivity.this, StepActivity.class);
                            intent.putExtra(Recipe.class.getSimpleName(), mRecipe);
                            intent.putExtra(POSITION, position);
                            startActivity(intent);
                        }
                    }
                }
        );
        mStepRecyclerView.setAdapter(mStepAdapter);
        mStepRecyclerView.setHasFixedSize(false);
        mStepRecyclerView.setNestedScrollingEnabled(false);
        if (mIsTablet) {
            mStepAdapter.mSelected = 0;
            mStepAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_widget_add) {
            Utils.saveIngredients(mRecipe.getIngredients(), this.getApplicationContext());
            Utils.saveRecipeName(mRecipe.getName(), this.getApplicationContext());
            WidgetService.startActionUpdateWidget(this.getApplicationContext());
        }
        return super.onOptionsItemSelected(item);
    }
}
