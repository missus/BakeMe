package com.example.android.bakeme.widget;


import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakeme.R;
import com.example.android.bakeme.Utils;
import com.example.android.bakeme.model.Ingredient;

import java.util.List;

public class ListWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListViews(this.getApplicationContext());
    }
}

class IngredientsListViews implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredients;

    public IngredientsListViews(Context context) {
        mContext = context;
        mIngredients = Utils.getIngredients(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_list_item);
        Ingredient ingredient = mIngredients.get(position);
        row.setTextViewText(R.id.name, ingredient.getName());
        row.setTextViewText(R.id.quantity, ingredient.getQuantity() + " " + ingredient.getMeasure());
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
