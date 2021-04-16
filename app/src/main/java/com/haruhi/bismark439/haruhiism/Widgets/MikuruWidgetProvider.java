package com.haruhi.bismark439.haruhiism.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.haruhi.bismark439.haruhiism.R;

import static com.haruhi.bismark439.haruhiism.Widgets.HaruhiWidgetProvider.MIKURU_WIDGET;
import static com.haruhi.bismark439.haruhiism.Widgets.HaruhiWidgetProvider.getDays;

/**
 * Created by Bismark439 on 19/01/2018.
 */

public class MikuruWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int count = appWidgetIds.length;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_haruhi);
        long days=getDays(2003,5,6);
        remoteViews.setTextViewText(R.id.daysText, days+"");
        remoteViews.setTextViewText(R.id.sinceWhen, context.getString(R.string.sinceMelancholy));
        remoteViews.setTextColor(R.id.daysText, Color.parseColor("#EEc80000"));
        remoteViews.setTextColor(R.id.days2, Color.parseColor("#EEc80000"));
        remoteViews.setTextColor(R.id.sinceWhen,Color.parseColor("#EEc80000"));

       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;*/
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.mikuruw);
        remoteViews.setImageViewBitmap(R.id.widgetImage,bitmap);

        Intent intent = new Intent(context, HaruhiWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setAction(MIKURU_WIDGET);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent,0 );//getactivity
        remoteViews.setOnClickPendingIntent(R.id.widgetImage, pi);

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}
