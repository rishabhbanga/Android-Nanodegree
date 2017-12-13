package com.innorb.recipeapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.innorb.recipeapp.R;
import com.innorb.recipeapp.activity.MainActivity;
import com.innorb.recipeapp.service.RecipeWidgetService;
import com.innorb.recipeapp.utility.Constants;
import com.innorb.recipeapp.utility.PrefManager;
import com.innorb.recipeapp.utility.Utility;

import java.util.Objects;

public class RecipeAppWidget extends AppWidgetProvider {

    private RemoteViews viewsUpdateRecipeWidget(Context context) {

        String widgetRecipeName = PrefManager.getStringPref(context, R.string.pref_widget_name);

        if(TextUtils.isEmpty(widgetRecipeName)) widgetRecipeName = context.getString(R.string.app_name);

        int id = PrefManager.getIntPref(context, R.string.pref_widget_id);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);

        if (id > 0) {

            views.setViewVisibility(R.id.widget_text_error, View.GONE);
            views.setViewVisibility(R.id.widget_listView, View.VISIBLE);

            Bitmap bitmap = Utility.bitmapTitleImage(context.getApplicationContext(), widgetRecipeName);
            if (bitmap != null) {
                views.setImageViewBitmap(R.id.recipe_widget_name, bitmap);

            }

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra(Constants.EXTRA_RECIPE_WIDGET_ID, id);
            intent.putExtra(Constants.EXTRA_RECIPE_NAME, widgetRecipeName);
            PendingIntent pendingIntent = PendingIntent.getActivities(context, id, new Intent[]{intent}, 0);
            views.setOnClickPendingIntent(R.id.recipe_widget_name, pendingIntent);

            Intent serviceIntent = new Intent(context, RecipeWidgetService.class);
            views.setRemoteAdapter(R.id.widget_listView, serviceIntent);

        } else {
            views.setViewVisibility(R.id.widget_listView, View.GONE);
            views.setViewVisibility(R.id.widget_text_error, View.VISIBLE);
            views.setTextViewText(R.id.widget_text_error, context.getString(R.string.widget_text_error));

            Bitmap bitmap = Utility.bitmapTitleImage(context.getApplicationContext(), widgetRecipeName);
            if (bitmap != null) {
                views.setImageViewBitmap(R.id.recipe_widget_name, bitmap);

            }

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, 0);
            views.setOnClickPendingIntent(R.id.widget_text_error,pendingIntent);
        }
        return views;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeAppWidget.class));

        String action = intent.getAction();
        if (Objects.equals(action, Constants.RECIPE_WIDGET_UPDATE)) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);
            for(int appWidgetId:appWidgetIds){
               appWidgetManager.partiallyUpdateAppWidget(appWidgetId, viewsUpdateRecipeWidget(context));
           }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        appWidgetManager.updateAppWidget(appWidgetIds, viewsUpdateRecipeWidget(context));
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }


}

