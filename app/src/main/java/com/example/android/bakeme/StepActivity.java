package com.example.android.bakeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.bakeme.model.Recipe;

public class StepActivity extends AppCompatActivity {

    private Recipe mRecipe;
    private int mPosition;
    private int mMaxPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mRecipe = (Recipe) intent.getSerializableExtra(Recipe.class.getSimpleName());
        mPosition = intent.getIntExtra(DetailsActivity.POSITION, 0);
        mMaxPosition = mRecipe.getSteps().size() - 1;
        setTitle(mRecipe.getName());
        setContentView(R.layout.step_activity);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(Recipe.class.getSimpleName(), mRecipe);
            arguments.putInt(DetailsActivity.POSITION, mPosition);
            StepFragment fragment = new StepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.step_detail_container, fragment).commitNow();
        }
        final Button previous = findViewById(R.id.previous);
        final Button next = findViewById(R.id.next);
        previous.setEnabled(mPosition != 0);
        next.setEnabled(mPosition != mMaxPosition);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition -= 1;
                refreshStepFragment();
                v.setEnabled(mPosition != 0);
                next.setEnabled(mPosition != mMaxPosition);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition += 1;
                refreshStepFragment();
                v.setEnabled(mPosition != mMaxPosition);
                previous.setEnabled(mPosition != 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Recipe.class.getSimpleName(), mRecipe);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshStepFragment() {
        Bundle arguments = new Bundle();
        arguments.putSerializable(Recipe.class.getSimpleName(), mRecipe);
        arguments.putInt(DetailsActivity.POSITION, mPosition);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container, fragment).commitNow();
    }
}

