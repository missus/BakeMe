package com.example.android.bakeme;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;
import android.widget.Toolbar;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void checkFirstRecipeElement() throws InterruptedException {
        onView(withId(R.id.recipe_grid)).perform(scrollToPosition(0)).check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.recipe_grid)).perform(scrollToPosition(0)).check(matches(hasDescendant(withId(R.id.image))));
    }

    @Test
    public void selectTheFirstRecipeAndCheckDetailsActivity() throws InterruptedException {
        onView(withId(R.id.recipe_grid)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_card)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_grid)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_grid)).check(matches(hasDescendant(withText("salt"))));
        onView(withId(R.id.step_grid)).check(matches(isDisplayed()));
        onView(withId(R.id.step_grid)).check(matches(hasDescendant(withText("Recipe Introduction"))));
    }

    @Test
    public void selectTheFirstRecipeFirstStepAndCheckStepFragment() throws InterruptedException {
        onView(withId(R.id.recipe_grid)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.step_grid)).check(matches(isDisplayed()));
        onView(withId(R.id.step_grid)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
        onView(withId(R.id.image)).check(matches(not(isDisplayed())));
        onView(withId(R.id.player)).check(matches(isDisplayed()));
        onView(withId(R.id.step)).check(matches(withText("Recipe Introduction")));
        onView(withId(R.id.description)).check(matches(withText("Recipe Introduction")));
    }
}
