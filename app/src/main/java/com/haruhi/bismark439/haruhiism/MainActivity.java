package com.haruhi.bismark439.haruhiism;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.haruhi.bismark439.haruhiism.Widgets.WidgetDB;

import static com.haruhi.bismark439.haruhiism.AlarmAdd.setAlarm;
import static com.haruhi.bismark439.haruhiism.AlarmScreens.AlarmScreen.demolish;

public class MainActivity extends AppCompatActivity {
   public static AlarmDB Alarms = new AlarmDB();
    public static WidgetDB Widgets = new WidgetDB();
    private AlarmManager alarmManager;
    AdapterAlarm arrayAdapterAlarmList;
    ListView listViewAlarm;
    public static final int MAX_VOLUME=100;
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor sharedEditor;
    public static final String PREF_NAME = "HARUHI_PREF";
    static final int DEFAULT_VOLUME=10;
    public static int currentVolume;
    boolean easter = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demolish=false;
        sharedPref = getApplicationContext().getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sharedEditor = sharedPref.edit();
        listViewAlarm	= (ListView)findViewById(R.id.listViewAlarm);
        arrayAdapterAlarmList = new AdapterAlarm(getApplicationContext());
        listViewAlarm.setAdapter(arrayAdapterAlarmList);
        loadAlarms();
        for(int i=0;i<Alarms.getSize();i++){
            if(Alarms.get(i).enabled){
                setAlarm(getApplicationContext(),Alarms.get(i));
            }
        }
        arrayAdapterAlarmList.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayAdapterAlarmList.notifyDataSetChanged();
        System.out.println("notifyDataSetChanged called");

        if(demolish){
            finish();
        }
    }

    public void onAddAlarm(View v){
    Intent intent = new Intent(getApplicationContext(), AlarmAdd.class);
    startActivity(intent);
}
    public void onEasterEgg(View v){
        TypeWriter type = findViewById(R.id.type);
        if(!easter){
            type.setVisibility(View.VISIBLE);
            type.animateText(getString(R.string.YUKI_6));
        }else{
            type.setVisibility(View.GONE);
        }
        easter=!easter;
    }
    public static void sendNotification(Context context, Class sendClass){
        int mNotificationId = 001;
        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.haruhi_3)
                        .setContentTitle(context.getString(R.string.haruhialarm))
                        .setContentText(context.getString(R.string.clicktodisarm))
                        .setOngoing(true);

        // Create pending intent, mention the Activity which needs to be
        //triggered when user clicks on notification(StopScript.class in this case)

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, sendClass).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);


        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
    public static void loadAlarms(){
    Alarms.alarmDB.clear();
    int size = sharedPref.getInt("size", 0);
    if(size !=0)
        for(int i = 0 ; i < size; i ++ ){
            long time=sharedPref.getLong("list"+i+"time", 0);
            int reqCode = sharedPref.getInt("list"+i+"reqCode", 0);
            boolean enabled= sharedPref.getBoolean("list"+i+"enabled", false);
            AlarmData temp = new AlarmData(time, reqCode,enabled);
            temp.waker = sharedPref.getInt("list"+i+"waker",0);
            temp.volume = sharedPref.getInt("list"+i+"volume",DEFAULT_VOLUME);
            boolean[] days=new boolean[7];
            for(int d=0;d<days.length;d++){
                days[d]=sharedPref.getBoolean("list"+i+"DAY"+d, false);
            }
            temp.days=days;
            Alarms.add(temp);
        }
}
    public static void saveAll(){
        for(int i=0;i<Alarms.getSize();i++){
            AlarmData temp=Alarms.get(i);
            System.out.println("SaveAll: "+i);
            temp.printString();
            sharedEditor.putLong("list"+i+"time", temp.time);
            sharedEditor.putInt("list"+i+"reqCode", temp.reqCode);
            sharedEditor.putBoolean("list"+i+"enabled", temp.enabled);
            sharedEditor.putInt("list"+i+"waker", temp.waker);
            sharedEditor.putInt("list"+i+"volume", temp.volume);
            for(int d=0;d<temp.days.length;d++){
                sharedEditor.putBoolean("list"+i+"DAY"+d, temp.days[d]);
            }
        }

        sharedEditor.putInt("size", Alarms.getSize());
        sharedEditor.commit();
    }
    public static void saveOne(AlarmData temp, int i){
        sharedEditor.putLong("list"+i+"time", temp.time);
        sharedEditor.putInt("list"+i+"reqCode", temp.reqCode);
        sharedEditor.putBoolean("list"+i+"enabled", temp.enabled);
        sharedEditor.putInt("list"+i+"waker", temp.waker);
        sharedEditor.putInt("list"+i+"volume", temp.volume);
        for(int d=0;d<temp.days.length;d++){
            sharedEditor.putBoolean("list"+i+"DAY"+d, temp.days[d]);
        }
        sharedEditor.putInt("size", Alarms.getSize());
        sharedEditor.commit();
    }
//Also check AlarmReceiver class, AlarmListView class

}
