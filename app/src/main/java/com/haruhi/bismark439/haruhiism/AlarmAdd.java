package com.haruhi.bismark439.haruhiism;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

import static com.haruhi.bismark439.haruhiism.MainActivity.Alarms;
import static com.haruhi.bismark439.haruhiism.MainActivity.DEFAULT_VOLUME;
import static com.haruhi.bismark439.haruhiism.MainActivity.saveOne;


public class AlarmAdd extends AppCompatActivity {
    TimePicker alarmTimePicker;
    CheckBox repeatCheck;
    CheckBox VibrateCheck;
    SeekBar volSeekBar;
    Spinner minSpin;
    Spinner timeSpin;
    EditText nameEdit;

    String[] snzMinutes = {"3","5","10","15","30"};
    String[] snzTimes={"3","2","1","5","10"};

    boolean[] days = {true,true,true,true,true,false,false};
    boolean repeat=false;
    boolean Vibration = true;
    String songname="alarm1.mp3";
    String alarmname="Alarm";
    int sMin;
    int sTimes;
    int volume=DEFAULT_VOLUME;
    int wakerType=0;

    private AlarmManager alarmManager;
    public static final int DEFAULT_ALARM_REQUEST = 100;
    Button btnAddAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        repeatCheck=(CheckBox)findViewById(R.id.a_repeatCheck);
        volSeekBar=(SeekBar) findViewById(R.id.a_volseek);
        nameEdit=(EditText)findViewById(R.id.a_nameEdit);
//Repeat Box
            repeatCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   repeat = isChecked;
               }
           });
//Radio
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.waker_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {//TODO New Alarm
                    case R.id.waker_random:
                        wakerType=0;
                        break;
                    case R.id.waker_kyonsis:
                        wakerType=1;
                        break;
                    case R.id.waker_mikuru:
                        wakerType=2;
                        break;
                    case R.id.waker_koizumi:
                        wakerType=3;
                        break;
                    case R.id.waker_mikuruTea:
                        wakerType=4;
                        break;
                    case R.id.waker_haruhi_basic:
                        wakerType=5;
                        break;
                }
            }
        });
        //Vib Check
        VibrateCheck=(CheckBox)findViewById(R.id.a_vibrate);
//Repeat Box
        VibrateCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               Vibration = isChecked;
           }
       });
//Volume
        volSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volume= seekBar.getProgress();
                System.out.println("Seekbar volume: "+volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
            }
        });

        //Minutes Interval
        minSpin = (Spinner) findViewById(R.id.a_snzMinutes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, snzMinutes);
        minSpin.setAdapter(adapter);
        minSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sMin =Integer.parseInt(snzMinutes[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sMin=5;
            }
        });

//Times repeat
        timeSpin = (Spinner) findViewById(R.id.a_snzTimes);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, snzTimes);
        timeSpin.setAdapter(adapter2);
        timeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTimes =Integer.parseInt(snzTimes[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sTimes=3;
            }
        });
        alarmname=String.valueOf(nameEdit.getText());
        btnAddAlarm = (Button)findViewById(R.id.addAlarm);
        alarmTimePicker.setIs24HourView(false);
        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

    }
    public void onClickDay(View v){
        String tt =(String) v.getTag();
        int t=Integer.parseInt(tt);
        if(days[t]){
            days[t]=false;
            v.setBackgroundResource(R.drawable.my_border);
        }else{
            days[t]=true;
            v.setBackgroundResource(R.drawable.my_border_green);
        }
    }
    public void onSelectSong(View v){
        songname="alarm1.mp3";
    }
    public void OnToggleClicked(View view) {

    }
    public void onClose(View v){
        finish();
    }
    public void onDone(View v){
        int i =Alarms.getSize();
        int reqCode = getReqcode();
        long time;
        Calendar calendar = Calendar.getInstance();//Alarm time
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
        if(System.currentTimeMillis()>time)
        {
            if (calendar.AM_PM == 0)
                time = time + (1000*60*60*12);
            else
                time = time + (1000*60*60*24);
        }//done init

        //AlarmData temp = new AlarmData(time, reqCode,true);
        AlarmData temp = new AlarmData(calendar.getTimeInMillis(), reqCode,true);
        temp.days=days;
        temp.waker=wakerType;
        temp.volume=volume;
        Alarms.add(temp);
        saveOne(temp, i);
        setAlarm(getApplicationContext(),temp);

       finish();
    }
    public static void setAlarm(Context mContext, AlarmData temp){
       AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        int reqCode = temp.reqCode;
        long time = temp.time;
        intent.putExtra("requestCode",reqCode);
        System.out.println("Sent code[ADD]:"+reqCode);
        temp.printString();
        PendingIntent pi = PendingIntent.getBroadcast(mContext, reqCode, intent,PendingIntent.FLAG_UPDATE_CURRENT );//getactivity
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time-30000 ,AlarmManager.INTERVAL_DAY, pi);
    }

public static int getReqcode(){
    int code=DEFAULT_ALARM_REQUEST;
    if(Alarms.getSize()==0){
        return code;
    }
    boolean stat;
        do {
            stat=false;
            for(int i=0;i<Alarms.getSize();i++){
               // System.out.println(code+" vs "+Alarms.get(i).reqCode);
                if(code==Alarms.get(i).reqCode){
                    stat=true;
                    i=9999;
                }
            }
            if(stat){//Found duplicate, increment
                code++;
            }else{//No duplicate, return
                return code;
            }
        }while (stat);
    return -1;
}
}
