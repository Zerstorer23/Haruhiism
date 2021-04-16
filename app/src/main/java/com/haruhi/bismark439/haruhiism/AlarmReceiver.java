package com.haruhi.bismark439.haruhiism;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.widget.Toast;

import com.haruhi.bismark439.haruhiism.AlarmScreens.AlarmScreen;
import com.haruhi.bismark439.haruhiism.AlarmScreens.HaruhiBasic;
import com.haruhi.bismark439.haruhiism.AlarmScreens.KoizumiScreen;
import com.haruhi.bismark439.haruhiism.AlarmScreens.MikuruOisiyiScreen;
import com.haruhi.bismark439.haruhiism.AlarmScreens.Mikuru_Screen;

import java.util.Calendar;
import java.util.Date;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.haruhi.bismark439.haruhiism.MainActivity.DEFAULT_VOLUME;
import static com.haruhi.bismark439.haruhiism.MainActivity.PREF_NAME;
import static com.haruhi.bismark439.haruhiism.MainActivity.currentVolume;


/**
 * Created by Bismark439 on 13/01/2018.
 */

public class AlarmReceiver  extends BroadcastReceiver
{//TODO New Alarm
    public static int MAX_WAKER_INDEX = 5; //최고 인덱스, 하루히= 5 AlarmAdd onCreate참조
    private AlarmManager alarmManager;
    private AlarmDB pAlarms;
    @Override
    public void onReceive(Context context, Intent intent) {
        int reqCode = intent.getIntExtra("requestCode", 0);
        System.out.println("Received code:" + reqCode);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        pAlarms= new AlarmDB();
        pLoadAlarms(context);
        System.out.println("Received Signal:" + intent.getAction());
        if(Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())){
            System.out.println("Replaced Package");
         //   toast(context,"Replaced Package");
            System.out.println("Alarms size:"+pAlarms.getSize());
            for(int i=0;i<pAlarms.getSize();i++){
                if(pAlarms.get(i).enabled){
                 //   toast(context,pAlarms.get(i).reqCode+" is enabled");
                    System.out.println(pAlarms.get(i).reqCode+" is enabled");
                    setAlarm(context,pAlarms.get(i));}
            }
        }else if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
          System.out.println("Alarms size:"+pAlarms.getSize());
           for(int i=0;i<pAlarms.getSize();i++){
               if(pAlarms.get(i).enabled)setAlarm(context,pAlarms.get(i));
           }
        }else {
            AlarmData temp = pAlarms.getByCode(reqCode);
          if(temp!=null) temp.printString();
            int today = getTodayDate();
            System.out.println(temp.hh + ":" + temp.mm + "Code: " + reqCode + " Today:" + today + " RecordedDay" + temp.days[today]);
            if (temp.days[today]) {
                Date todate = new Date ( );
                Calendar cal = Calendar.getInstance ();//TimeZone.getTimeZone("GMT"));
                // Set as today
                cal.setTime ( todate );
System.out.println("Received time "+temp.hh+":"+temp.mm+" vs "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
                if((temp.hh==cal.get(Calendar.HOUR_OF_DAY))&&((temp.mm>=(cal.get(Calendar.MINUTE)-1))&&(temp.mm<=(cal.get(Calendar.MINUTE)+1)))){

                    Intent alarmIntent = null;
          System.out.println(temp.reqCode+" Received Waker: "+temp.waker);
        if(temp.waker==0){
            temp.waker=(int)(Math.random()*MAX_WAKER_INDEX+1);//1~4
            System.out.println(temp.reqCode+" Generated Waker: "+temp.waker);
            if(temp.waker==2){temp.waker=4;}//무작위에서 2 제외. 강제로 4
        }
                    switch (temp.waker){//TODO New Alarm
                        case 1:
                            alarmIntent = new Intent(context, AlarmScreen.class);
                            break;
                        case 2:
                        alarmIntent = new Intent(context, Mikuru_Screen.class);
                            break;
                        case 3:
                            alarmIntent = new Intent(context, KoizumiScreen.class);
                            break;
                        case 4:
                            alarmIntent = new Intent(context, MikuruOisiyiScreen.class);
                            break;
                        case 5:
                            alarmIntent = new Intent(context, HaruhiBasic.class);
                            break;
                    }
                    if(alarmIntent==null)alarmIntent = new Intent(context, MikuruOisiyiScreen.class);
                   AudioManager audio = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
                    currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                    int volume = (int)((float)(temp.volume)/15 * (float)audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC,volume,0);
                    System.out.println(volume+" And raw volume~ "+temp.volume);
                alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(context, alarmIntent, null);
                }
            }
        }
    }
    public int getTodayDate(){
        Calendar calendar = Calendar.getInstance();
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK) ;
        if(dayNum==1){
            //Sunday
            dayNum=6;
        }else {dayNum=dayNum-2;}
        System.out.println("Today = "+dayNum);
        return dayNum;
    }
    public void setAlarm(Context mContext, AlarmData temp){
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        int reqCode = temp.reqCode;
        long time = temp.time;
        intent.putExtra("requestCode",reqCode);
        System.out.println("REBOOT: Sent code[bADD]b:"+reqCode);
        temp.printString();
        PendingIntent pi = PendingIntent.getBroadcast(mContext, reqCode, intent,PendingIntent.FLAG_UPDATE_CURRENT );//getactivity
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time ,AlarmManager.INTERVAL_DAY, pi);
    }
    public void pLoadAlarms(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        pAlarms.alarmDB.clear();
        int size = sharedPref.getInt("size", 0);
        if(size !=0)
            for(int i = 0 ; i < size; i ++ ){
                long time=sharedPref.getLong("list"+i+"time", 0);
                int reqCode = sharedPref.getInt("list"+i+"reqCode", 0);
                boolean enabled= sharedPref.getBoolean("list"+i+"enabled", false);
                AlarmData temp = new AlarmData(time, reqCode,enabled);
             //   temp.sTimes = sharedPref.getInt("list"+i+"repeat",0);
              //  temp.sMin = sharedPref.getInt("list"+i+"interval",0);
                temp.waker = sharedPref.getInt("list"+i+"waker",0);
                temp.volume = sharedPref.getInt("list"+i+"volume",DEFAULT_VOLUME);
                boolean[] days=new boolean[7];
                for(int d=0;d<days.length;d++){
                    days[d]=sharedPref.getBoolean("list"+i+"DAY"+d, false);
                }
                temp.days=days;
                pAlarms.add(temp);
            }
    }
    public void toast(Context context, String a) {
        Toast.makeText(context, a, Toast.LENGTH_LONG).show();
    }
}