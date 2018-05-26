package com.wizenetlite;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.Activities.IncomingCallScreenActivity;
import com.Activities.R;
import com.DatabaseHelper;
import com.Messages;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link wizenet_app_widgetConfigureActivity Wizenet_widgetConfigureActivity}
 */
public class wizenet_app_widget extends AppWidgetProvider {

    public static RemoteViews views;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Messages msg = new Messages();
        // Construct the RemoteViews object

        RemoteViews views = createRemoteView(context, R.layout.wizenet_app_widget);

        // Insert the text to the buttons of widget.
        views.setTextViewText(R.id.btn1, msg.msgList.get(0));
        views.setTextViewText(R.id.btn2, msg.msgList.get(1));
        views.setTextViewText(R.id.btn3, msg.msgList.get(2));
        views.setTextViewText(R.id.btn4, msg.msgList.get(3));
        views.setTextViewText(R.id.btn5, msg.msgList.get(4));
        views.setTextViewText(R.id.btn6, msg.msgList.get(5));

        // Initialize on click for buttons.
        Intent intent1 = new Intent(context, wizenet_app_widget.class);
        Intent intent2 = new Intent(context, wizenet_app_widget.class);
        Intent intent3 = new Intent(context, wizenet_app_widget.class);
        Intent intent4 = new Intent(context, wizenet_app_widget.class);
        Intent intent5 = new Intent(context, wizenet_app_widget.class);
        Intent intent6 = new Intent(context, wizenet_app_widget.class);

        intent1.setAction("btn1");
        intent2.setAction("btn2");
        intent3.setAction("btn3");
        intent4.setAction("btn4");
        intent5.setAction("btn5");
        intent6.setAction("btn6");

        views.setOnClickPendingIntent(R.id.btn1, PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT));
        views.setOnClickPendingIntent(R.id.btn2, PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT));
        views.setOnClickPendingIntent(R.id.btn3, PendingIntent.getBroadcast(context, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT));
        views.setOnClickPendingIntent(R.id.btn4, PendingIntent.getBroadcast(context, 0, intent4, PendingIntent.FLAG_UPDATE_CURRENT));
        views.setOnClickPendingIntent(R.id.btn5, PendingIntent.getBroadcast(context, 0, intent5, PendingIntent.FLAG_UPDATE_CURRENT));
        views.setOnClickPendingIntent(R.id.btn6, PendingIntent.getBroadcast(context, 0, intent6, PendingIntent.FLAG_UPDATE_CURRENT));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews createRemoteView(Context context, int id) {
        return new RemoteViews(context.getPackageName(), id);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Messages msg = new Messages();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wizenet_app_widget);
        views.setInt(R.id.btn1, "setBackgroundColor", Color.parseColor("#D84626"));
        //AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, wizenet_app_widget.class), views);

        // Get Database helper.
        final DatabaseHelper db = DatabaseHelper.getInstance(context);
        String action = intent.getAction().toLowerCase();

        final String actionName1 = "btn1";
        final String actionName2 = "btn2";
        final String actionName3 = "btn3";
        final String actionName4 = "btn4";
        final String actionName5 = "btn5";
        final String actionName6 = "btn6";

        // Find the relevant action to perform.
        switch (action) {
            case actionName1:
                views.setInt(R.id.btn1, "setBackgroundColor", Color.parseColor("#D84626"));
                db.getInstance(context).updateValue("IS_BUSY_OPTION", "עסוק/ה");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(0));
                Toast.makeText(context, "button 1", Toast.LENGTH_LONG).show();
                break;

            case actionName2:
                db.getInstance(context).updateValue("IS_BUSY_OPTION", "עסוק/ה");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(1));
                views.setInt(R.id.btn2, "setBackgroundColor", Color.parseColor("#D84626"));
                Toast.makeText(context, "button 2", Toast.LENGTH_LONG).show();
                break;

            case actionName3:
                db.getInstance(context).updateValue("IS_BUSY_OPTION", "עסוק/ה");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(2));
                views.setInt(R.id.btn3, "setBackgroundColor", Color.parseColor("#D84626"));
                Toast.makeText(context, "button 3", Toast.LENGTH_LONG).show();
                break;

            case actionName4:
                db.getInstance(context).updateValue("IS_BUSY_OPTION", "עסוק/ה");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(3));
                views.setInt(R.id.btn4, "setBackgroundColor", Color.parseColor("#D84626"));
                Toast.makeText(context, "button 4", Toast.LENGTH_LONG).show();
                break;

            case actionName5:
                db.getInstance(context).updateValue("IS_BUSY_OPTION", "עסוק/ה");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(4));
                views.setInt(R.id.btn5, "setBackgroundColor", Color.parseColor("#D84626"));
                Toast.makeText(context, "button 5", Toast.LENGTH_LONG).show();
                break;

            case actionName6:
                db.getInstance(context).updateValue("IS_BUSY_OPTION", "ללא");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(5));
                views.setInt(R.id.btn6, "setBackgroundColor", Color.parseColor("#D84626"));
                Toast.makeText(context, "button 6", Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }

        super.onReceive(context, intent);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            wizenet_app_widgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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

