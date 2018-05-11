package com.wizenetlite;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.Activities.IncomingCallScreenActivity;
import com.Activities.R;
import com.DatabaseHelper;
import com.Messages;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link Wizenet_widgetConfigureActivity Wizenet_widgetConfigureActivity}
 */
public class Wizenet_widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Messages msg = new Messages();
        CharSequence widgetText = Wizenet_widgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wizenet_widget);

        // Insert the text to the buttons of widget.
        views.setTextViewText(R.id.btn1, msg.msgList.get(0));
        views.setTextViewText(R.id.btn2, msg.msgList.get(1));

        // Initialize on click for buttons.
        Intent intent = new Intent(context, Wizenet_widget.class);
        intent.setAction("btn1");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn1, pendingIntent);
        views.setOnClickPendingIntent(R.id.btn2, pendingIntent);
        //views.setOnClickPendingIntent(R.id.btn2, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);

        // Get Database helper.
        final DatabaseHelper db = DatabaseHelper.getInstance(context);
        String action = intent.getAction().toLowerCase();
        String actionName = "btn1";

        if(actionName.equals(action)){
            db.get
            Toast.makeText(context, "no long or lat", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            Wizenet_widgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

