package com.haruhi.bismark439.haruhiism.Widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.haruhi.bismark439.haruhiism.R;

import static com.haruhi.bismark439.haruhiism.MainActivity.Widgets;
import static com.haruhi.bismark439.haruhiism.Widgets.ConfigurationActivity.loadWidgets;
import static com.haruhi.bismark439.haruhiism.Widgets.ConfigurationActivity.saveWidgets;
import static com.haruhi.bismark439.haruhiism.Widgets.HaruhiWidgetProvider.getDays;

/**
 * Created by Bismark439 on 28/02/2018.
 */

public class DayCounterProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        System.out.println("Widget Count: "+count);
        if(Widgets.getSize()==0){
           loadWidgets(context);
            System.out.println("Widget load: "+Widgets.getSize());
        }
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            System.out.println("Widget ID: "+widgetId);
            WidgetData temp = Widgets.getByCode(widgetId);
            if(temp!=null){
                System.out.println("Found: "+widgetId);
                temp.printString();
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_haruhi);
                long days=getDays(temp.yy,temp.mmMod,temp.dd);
                remoteViews.setTextViewText(R.id.sinceWhen, temp.name);
                if(days == 0){
                    remoteViews.setTextViewText(R.id.daysText, "Today!");
                    remoteViews.setTextViewText(R.id.days2, " ");
                }else{
                           days = Math.abs(days);
                          remoteViews.setTextViewText(R.id.daysText, days+"");
                    remoteViews.setTextViewText(R.id.days2, context.getString(R.string.days));
                }
                remoteViews.setTextColor(R.id.daysText, temp.color);
                remoteViews.setTextColor(R.id.days2, temp.color);
                remoteViews.setTextColor(R.id.sinceWhen,temp.color);
                remoteViews.setImageViewResource(R.id.widgetImage,temp.picture);
                Intent intent = new Intent(context, DayCounterProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        System.out.println("WIDGET DELETED called");
        if(Widgets.getSize()==0){
            loadWidgets(context);
            System.out.println("Widget load: "+Widgets.getSize());
        }
        for(int i=0;i<Widgets.getSize();i++){
            boolean found = false;
            for(int x=0;x<appWidgetIds.length;x++){
                if(Widgets.get(i).reqCode==appWidgetIds[x]){
                    found=true;
                    x=appWidgetIds.length;
                }
            }
            if(!found){
                Widgets.widgetDB.remove(i);
                if(i>0)i--;
            }
        }
        System.out.println("Modified Widget load: "+Widgets.getSize());
        saveWidgets(context);
    }
}
