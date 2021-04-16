package com.haruhi.bismark439.haruhiism.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.widget.RemoteViews;

import com.haruhi.bismark439.haruhiism.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Bismark439 on 12/01/2018.
 */

public class HaruhiWidgetProvider extends AppWidgetProvider {
    public static String KYON_KUN_DENWA = "KyonKunDenwa";
    public static String MIKURU_WIDGET = "MikuruWidget";
    public static String[] HRHvoice;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int count = appWidgetIds.length;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_haruhi);
        long days=getDays(2020,10,25);
        System.out.println(days+"일 측정");
        if(days < 0){
            System.out.println("아직 남았음");
            String txt =  context.getString(R.string.beforelast);
            days = Math.abs(days);
            remoteViews.setTextViewText(R.id.daysText, days+"");
            remoteViews.setTextViewText(R.id.sinceWhen,txt);
        }else if(days == 0){
            remoteViews.setTextViewText(R.id.daysText, context.getString(R.string.todaydesu));
            remoteViews.setTextViewText(R.id.days2, " ");
        }else{
            remoteViews.setTextViewText(R.id.daysText, days+"");
            remoteViews.setTextViewText(R.id.sinceWhen, context.getString(R.string.sincelast));
            remoteViews.setTextViewText(R.id.days2, context.getString(R.string.days));
        }


        remoteViews.setTextColor(R.id.daysText, Color.parseColor("#eeed7e10"));
        remoteViews.setTextColor(R.id.days2, Color.parseColor("#eeed7e10"));
        remoteViews.setTextColor(R.id.sinceWhen,Color.parseColor("#eeed7e10"));

  /*      BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.haruhi1,options);
        remoteViews.setImageViewBitmap(R.id.widgetImage,bitmap);*/

        Intent intent = new Intent(context, HaruhiWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        remoteViews.setOnClickPendingIntent(R.id.widgetImage, getPendingSelfIntent(context, KYON_KUN_DENWA));

        for (int widgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        if (KYON_KUN_DENWA.equals(intent.getAction())) {
            AssetManager assetManager = context.getAssets();
            try {
               HRHvoice = assetManager.list("haruhi");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
playSound(context,"haruhi");
        }else if (MIKURU_WIDGET.equals(intent.getAction())) {
            AssetManager assetManager = context.getAssets();
            try {
                HRHvoice = assetManager.list("mikuru");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            playSound(context,"mikuru");
        }

    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static long getDays(int y, int m, int d){
        Date today = new Date ( );
        Calendar cal = Calendar.getInstance ();//TimeZone.getTimeZone("GMT"));
        // Set as today
        cal.setTime ( today );
        System.out.println ( "Received Cal: "+y + "Y / " + (m+1) + "M / " + d+ " D" );
        System.out.println ( "Today Cal: "+cal.get ( Calendar.YEAR ) + "Y / " + ( cal.get ( Calendar.MONTH ) + 1 ) + "M / " + cal.get ( Calendar.DATE ) + " D" );
        if(cal.get ( Calendar.YEAR )==y&& cal.get ( Calendar.MONTH ) == m &&cal.get ( Calendar.DATE )==d){
            return 0;
        }
        Calendar cal2 = Calendar.getInstance ();//TimeZone.getTimeZone("GMT") );
       //Month has offset -1. June = 5
        cal2.set(y,m,d,0,0,0);//YY MM DD
        long calMil = cal.getTimeInMillis();
        long calMil2 = cal2.getTimeInMillis();
        System.out.println("cal1 time "+calMil);
        System.out.println("cal2 time "+calMil2);
        long count = ((calMil - calMil2) / (1000 * 60 * 60 * 24) );//offset 40000
        return count;
    }
    public void playSound(Context context,String folder){
            MediaPlayer mMediaPlayer=new MediaPlayer();
            try {
                int rand=(int)(Math.random()*HRHvoice.length);
                System.out.println("RANDOM "+rand+" / "+HRHvoice.length);
                AssetFileDescriptor afd =context.getAssets().openFd(folder+"/"+HRHvoice[rand]);
                mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                    }
                });
            }catch (IOException e){
                e.getLocalizedMessage();
                e.printStackTrace();
            }
        }

}