package com.example.android.bakeme.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import static com.example.android.bakeme.widget.RecipeWidget.updateIngredientsWidget;

public class WidgetService extends IntentService {

    public final static String ACTION_UPDATE_INGREDIENTS_WIDGET = "com.example.android.bakeme.action.update_ingredients_widget";

    public WidgetService() {
        super("WidgetService");
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS_WIDGET.equals(action)) {
                handleActionUpdateIngredientsWidget();
            }
        }
    }

    private void handleActionUpdateIngredientsWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        updateIngredientsWidget(this, appWidgetManager, appWidgetIds);
    }
}
