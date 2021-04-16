package com.haruhi.bismark439.haruhiism;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import static android.content.Context.ALARM_SERVICE;
import static com.haruhi.bismark439.haruhiism.MainActivity.sharedEditor;

/*
 */
public class AlarmListView extends LinearLayout {
    TextView name;
    TextView time;
    TextView repeat;
    LinearLayout days;
    ToggleButton toggle;


    AlarmData alarmData;
    boolean init=false;
    private int position;
    private  AlarmManager alarmManager;

    public AlarmListView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.res_alarm_list_view, this);
        time = (TextView)layout.findViewById(R.id.cr_time);
        toggle = (ToggleButton) findViewById(R.id.cr_toggle);
        repeat=(TextView)findViewById(R.id.cr_repeat);
        alarmManager=(AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(init) {
                    if (isChecked) {
                        enableAlarm(getContext(),alarmData);
                        alarmData.enabled=true;
                        sharedEditor.putBoolean("list"+position+"enabled", true);
                        sharedEditor.commit();
                    } else {
                        disableAlarm(getContext(),alarmData);
                        alarmData.enabled=false;
                        sharedEditor.putBoolean("list"+position+"enabled", false);
                        sharedEditor.commit();
                    }
                }
            }
        });
days=(LinearLayout)findViewById(R.id.cr_days);
    }

    public boolean setData(AlarmData alarmData, int position){
        this.alarmData = alarmData;
        this.position = position;
        String hh = convertDec(alarmData.hh);
        String mm = convertDec(alarmData.mm);
        time.setText(hh+":"+mm);
        String wakerName="";
        switch (alarmData.waker){//TODO New Alarm
            case 0:
                wakerName=getContext().getString(R.string.random);
                break;
            case 1:
                wakerName=getContext().getString(R.string.kyonsis);
                break;
            case 2:
                wakerName=getContext().getString(R.string.asahinaMikuru);
                break;
            case 3:
                wakerName=getContext().getString(R.string.koizumi_and_sos);
                break;
            case 4:
                wakerName=getContext().getString(R.string.mikurunotea);
                break;
            case 5:
                wakerName=getContext().getString(R.string.hrhNoPuzzle);
                break;
        }
  repeat.setText(wakerName);      
//        repeat.setText(position+" ["+alarmData.reqCode+"]");
      if(alarmData.enabled!=toggle.isChecked()){toggle.toggle();}
init=true;
        for(int i=0;i<7;i++){
            TextView day = (TextView) days.findViewWithTag(i + "");
            if(alarmData.days[i]) {
                day.setTextColor(Color.parseColor("#00c800"));
            }else{
            day.setTextColor(Color.parseColor("#000000"));
            }
        }

        return true;
    }
    public void disableAlarm(Context mContext, AlarmData temp){
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        int reqCode = temp.reqCode;
        intent.putExtra("requestCode",reqCode);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);

        System.out.println("Disabled code:"+reqCode);
    }
    public void enableAlarm(Context mContext, AlarmData temp){
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        int reqCode = temp.reqCode;
        long time = temp.time;
        intent.putExtra("requestCode",reqCode);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, reqCode, intent,PendingIntent.FLAG_UPDATE_CURRENT );//getactivity
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time-30000 ,AlarmManager.INTERVAL_DAY, pi);

        temp.printString();
        System.out.println("Sent code[ADD]:"+reqCode);
    }
public String convertDec(int i){
    String ret =i+"";
    if(i<10){
        ret="0"+i;
    }
    return ret;
}
}
