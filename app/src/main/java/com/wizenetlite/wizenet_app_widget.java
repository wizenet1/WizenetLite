package com.wizenetlite;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
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
        CharSequence widgetText = wizenet_app_widgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.wizenet_app_widget);

        // Insert the text to the buttons of widget.
        views.setTextViewText(R.id.btn1, msg.msgList.get(0).split(" ")[0]);
        views.setTextViewText(R.id.btn2, msg.msgList.get(1).split(" ")[0]);
        views.setTextViewText(R.id.btn3, msg.msgList.get(2).split(" ")[0]);
        views.setTextViewText(R.id.btn4, msg.msgList.get(3).split(" ")[0]);

        // Initialize on click for buttons.
        Intent intent1 = new Intent(context, wizenet_app_widget.class);
        Intent intent2 = new Intent(context, wizenet_app_widget.class);
        Intent intent3 = new Intent(context, wizenet_app_widget.class);
        Intent intent4 = new Intent(context, wizenet_app_widget.class);

        intent1.setAction("btn1");
        intent2.setAction("btn2");
        intent3.setAction("btn3");
        intent4.setAction("btn4");

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context, 0, intent4, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.btn1, pendingIntent1);
        views.setOnClickPendingIntent(R.id.btn2, pendingIntent2);
        views.setOnClickPendingIntent(R.id.btn3, pendingIntent3);
        views.setOnClickPendingIntent(R.id.btn4, pendingIntent4);

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

    /*
    private void initButtonColors(RemoteViews views){
        views.setInt(R.id.btn1, "setBackgroundColor", Color.BLUE);
        views.setInt(R.id.btn2, "setBackgroundColor", Color.BLUE);
    }*/

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);

        Messages msg = new Messages();
        views = new RemoteViews(context.getPackageName(), R.layout.wizenet_app_widget);
        //this.initButtonColors(views);
        // Get Database helper.
        final DatabaseHelper db = DatabaseHelper.getInstance(context);
        String action = intent.getAction().toLowerCase();
        final String actionName1 = "btn1";
        final String actionName2 = "btn2";
        final String actionName3 = "btn3";
        final String actionName4 = "btn4";

        // Find the relevant action to perform.
        switch(action) {
            case actionName1:
                db.getInstance(context).updateValue("IS_BUSY","1");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(0));
                views.setInt(R.id.btn1, "setBackgroundColor", Color.parseColor("#FFFFFF"));
                Toast.makeText(context, "button 1", Toast.LENGTH_LONG).show();
                break;

            case actionName2:
                db.getInstance(context).updateValue("IS_BUSY","1");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(1));
                views.setInt(R.id.btn2, "setBackgroundColor", Color.parseColor("#FFFFFF"));
                Toast.makeText(context, "button 2", Toast.LENGTH_LONG).show();
                break;

            case actionName3:
                db.getInstance(context).updateValue("IS_BUSY","1");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(2));
                views.setInt(R.id.btn3, "setBackgroundColor", Color.parseColor("#FFFFFF"));
                Toast.makeText(context, "button 3", Toast.LENGTH_LONG).show();
                break;

            case actionName4:
                db.getInstance(context).updateValue("IS_BUSY","1");
                db.getInstance(context).updateValue("BUSY_MESSAGE", msg.msgList.get(3));
                views.setInt(R.id.btn4, "setBackgroundColor", Color.parseColor("#FFFFFF"));
                Toast.makeText(context, "button 4", Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }

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

