package com.example.android.bakeme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.bakeme.model.Recipe;
import com.example.android.bakeme.model.Ingredient;
import com.example.android.bakeme.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakeme.MainActivity.LOG_TAG;

public final class Utils {

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String SERVINGS = "servings";
    private final static String IMAGE = "image";
    private final static String INGREDIENTS = "ingredients";
    private final static String QUANTITY = "quantity";
    private final static String MEASURE = "measure";
    private final static String INGREDIENT = "ingredient";
    private final static String STEPS = "steps";
    private final static String SHORT_DESCRIPTION = "shortDescription";
    private final static String DESCRIPTION = "description";
    private final static String VIDEO_URL = "videoURL";
    private final static String THUMBNAIL_URL = "thumbnailURL";
    private final static String INGREDIENTS_JSON = "ingredients_json";
    private final static String RECIPE = "recipe";

    private Utils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movies JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Recipe> extractRecipeFromJson(String recipeJSON) {
        if (TextUtils.isEmpty(recipeJSON)) {
            return null;
        }

        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray recipesArray = new JSONArray(recipeJSON);

            for (int i = 0; i < recipesArray.length(); i++) {

                JSONObject currentRecipe = recipesArray.optJSONObject(i);
                int id = currentRecipe.optInt(ID);
                String name = currentRecipe.optString(NAME);
                int serving = currentRecipe.optInt(SERVINGS);
                String image = currentRecipe.optString(IMAGE);

                JSONArray ingredientsArray = currentRecipe.optJSONArray(INGREDIENTS);
                List<Ingredient> ingredients = new ArrayList<>();
                for (int j = 0; j < ingredientsArray.length(); j++) {

                    JSONObject currentIngredient = ingredientsArray.optJSONObject(j);
                    int quantity = currentIngredient.optInt(QUANTITY);
                    String measure = currentIngredient.optString(MEASURE);
                    String ingredientName = currentIngredient.getString(INGREDIENT);

                    Ingredient ingredient = new Ingredient(quantity, measure, ingredientName);
                    ingredients.add(ingredient);
                }

                JSONArray stepsArray = currentRecipe.optJSONArray(STEPS);
                List<Step> steps = new ArrayList<>();
                for (int j = 0; j < stepsArray.length(); j++) {

                    JSONObject currentStep = stepsArray.optJSONObject(j);
                    int stepId = currentStep.optInt(ID);
                    String shortDescription = currentStep.optString(SHORT_DESCRIPTION);
                    String description = currentStep.optString(DESCRIPTION);
                    String video = currentStep.optString(VIDEO_URL);
                    String stepImage = currentStep.optString(THUMBNAIL_URL);

                    Step step = new Step(stepId, shortDescription, description, video, stepImage);
                    steps.add(step);
                }

                Recipe recipe = new Recipe(id, name, serving, image, ingredients, steps);
                recipes.add(recipe);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the movies JSON results", e);
        }
        return recipes;
    }

    public static List<Recipe> fetchRecipeData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Recipe> recipes = extractRecipeFromJson(jsonResponse);
        return recipes;
    }

    public static void saveIngredients(List<Ingredient> ingredients, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ingredients);
        prefsEditor.putString(INGREDIENTS_JSON, json);
        prefsEditor.apply();
    }

    public static List<Ingredient> getIngredients(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(INGREDIENTS_JSON, "");
        Type type = new TypeToken<List<Ingredient>>() {
        }.getType();
        List<Ingredient> ingredients = gson.fromJson(json, type);
        return ingredients;
    }

    public static void saveRecipeName(String recipe, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(RECIPE, recipe);
        prefsEditor.apply();
    }

    public static String getRecipeName(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String recipe = appSharedPrefs.getString(RECIPE, "");
        return recipe;
    }
}