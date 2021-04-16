package com.haruhi.bismark439.haruhiism.Widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.haruhi.bismark439.haruhiism.R;

/**
 * Created by Bismark439 on 28/02/2018.
 */

public class NagatoInterfaceProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        System.out.println("NAGATO "+count);
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            System.out.println("NAGATO ID"+widgetId);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_nagato);
                Intent intent = new Intent(context, NagatoInterfaceProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }

}
