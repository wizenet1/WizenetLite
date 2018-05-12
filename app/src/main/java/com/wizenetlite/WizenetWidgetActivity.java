package com.wizenetlite;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.Activities.MainActivity;
import com.Activities.R;
import com.Messages;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WizenetWidgetActivityConfigureActivity WizenetWidgetActivityConfigureActivity}
 */
public class WizenetWidgetActivity extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Messages msg = new Messages();
        CharSequence widgetText = WizenetWidgetActivityConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wizenet_widget_activity);

        // Set the text of the buttons.
        views.setTextViewText(R.id.test1, "it worked");
//        views.setTextViewText(R.id.msg1, msg.msgList.get(0));
//        views.setTextViewText(R.id.msg2, msg.msgList.get(1));
//        views.setTextViewText(R.id.msg3, msg.msgList.get(2));
//        views.setTextViewText(R.id.msg4, msg.msgList.get(3));

//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.msg1, pendingIntent );

        //views.setTextViewText(R.id.appwidget_text, widgetText);

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
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WizenetWidgetActivityConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

