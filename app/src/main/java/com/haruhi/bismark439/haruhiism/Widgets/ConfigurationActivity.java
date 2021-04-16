package com.haruhi.bismark439.haruhiism.Widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;

import com.haruhi.bismark439.haruhiism.R;

import java.util.Calendar;
import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.haruhi.bismark439.haruhiism.MainActivity.PREF_NAME;
import static com.haruhi.bismark439.haruhiism.MainActivity.Widgets;
import static com.haruhi.bismark439.haruhiism.MainActivity.sharedEditor;
import static com.haruhi.bismark439.haruhiism.MainActivity.sharedPref;
import static com.haruhi.bismark439.haruhiism.Widgets.HaruhiWidgetProvider.getDays;

/**
 * Created by Bismark439 on 28/02/2018.
 */

public class ConfigurationActivity extends Activity{
    int mAppWidgetId;
    int[] date = new int[3];
    static int myColor = Color.parseColor("#c80000c8");
    static int[] drawables = {R.drawable.haruhi_yuutsu,R.drawable.haruhi1,R.drawable.mikuruw,R.drawable.nagato_chan};
    int myImg=0;
    static String[] dNames = {"Haruhi","Haruhi-Shoshitsu","Mikuru","Nagato"};
    TextView exName;
    TextView exDay;
    TextView exDay2;
    EditText yy;
    EditText mm;
    EditText dd;
    EditText name;
    ImageView exImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity_widget);
        setResult(RESULT_CANCELED);
        //INITIALISE
        exName=findViewById(R.id.exName);
        exDay=findViewById(R.id.exDays);
        exImg=findViewById(R.id.exImage);
        exDay2=findViewById(R.id.exDays2);
    yy=findViewById(R.id.dateYear);
    mm=findViewById(R.id.dateMonth);
    dd = findViewById(R.id.dateDay);
    name = findViewById(R.id.dateName);

        Date today = new Date ( );
        Calendar cal = Calendar.getInstance ();
        cal.setTime ( today );
        date[0]=cal.get(Calendar.YEAR);
        date[1]=cal.get(Calendar.MONTH)+1;
        date[2]=cal.get(Calendar.DATE);

        EditText[] list = {yy,mm,dd};
        for(int i=0;i<3;i++){
            final int index=i;
            list[i].addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    try{
                        String text = String.valueOf(s);
                        date[index] =Integer.parseInt(text) ;
                    }catch (Exception e){
                        date[index]= 6;
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }

        //Spiner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dNames);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myImg = position;
                updateExample();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //INITIALISE
            updateExample();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

// If you receive an intent without the appropriate ID, then the system should kill this Activity//
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }


    }
    //Date
    public void onDateUp(View v){
        int index = Integer.parseInt((String)v.getTag());
        switch (index){
            case 0:
                date[index]++;
                break;
            case 1:
                date[index]++;
                if(date[index]>12){
                    date[index]=1;
                }
                break;
            case 2:
                Calendar cal = Calendar.getInstance ();
                cal.set(date[0],date[1]-1,1);//YY MM DD
                int maxDay = cal.getActualMaximum(Calendar.DATE);
                date[index]++;
                if(date[2]>maxDay){
                    date[2]=1;
                }
                break;
        }
       checkValidity();
        updateExample();
    }
    public void onDateDown(View v){
        int index = Integer.parseInt((String)v.getTag());
        switch (index){
            case 0:
                date[index]--;
                break;
            case 1:
                date[index]--;
                if(date[index]<1){
                    date[index]=12;
                }
                break;
            case 2:
                date[index]--;
                Calendar cal = Calendar.getInstance ();
                cal.set(date[0],date[1]-1,1);//YY MM DD
                int maxDay = cal.getActualMaximum(Calendar.DATE);
                if(date[index]<1){
                    date[index]=maxDay;
                }
                break;
        }
        checkValidity();
        updateExample();
    }
    public void checkValidity(){
        Calendar cal = Calendar.getInstance ();
        cal.set(date[0],date[1]-1,1);//YY MM DD
       System.out.println("Validity"+cal.get(Calendar.MONTH));
        int maxDay = cal.getActualMaximum(Calendar.DATE);
if(date[2]>maxDay||date[2]<1){
    date[2]=maxDay;
}
if(date[1]>12||date[1]<1){
            date[1]=12;
}
    }

    //Color
public void onCustomColor(View v){
    AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, myColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // color is the color selected by the user.
                 myColor=color;
                updateExample();
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
    });
dialog.show();

    }
public void onPresetColor(View v){
if(v==null)return;
    myColor=Color.parseColor((String)v.getTag());
updateExample();
}

public void updateExample(){
    exName.setTextColor(myColor);
    exName.setText(String.valueOf(name.getText()));
    exDay.setTextColor(myColor);
    exDay2.setTextColor(myColor);
    exImg.setImageResource(drawables[myImg]);

    yy.setText(date[0]+"");
    mm.setText(date[1]+"");
    dd.setText(date[2]+"");
}

        public void onClick(View v) {
            // TODO Auto-generated method stub
            Context context = getApplicationContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_haruhi);
            String test = String.valueOf(name.getText());
            long days=getDays(date[0],date[1]-1,date[2]);
            if(days == 0){
                remoteViews.setTextViewText(R.id.daysText, "Today!");
                remoteViews.setTextViewText(R.id.days2, " ");
            }else{
                days = Math.abs(days);
                remoteViews.setTextViewText(R.id.daysText, days+"");
            }
            remoteViews.setTextViewText(R.id.sinceWhen, test);
            remoteViews.setTextColor(R.id.daysText, myColor);
            remoteViews.setTextColor(R.id.days2, myColor);
            remoteViews.setTextColor(R.id.sinceWhen,myColor);
            remoteViews.setImageViewResource(R.id.widgetImage,drawables[myImg]);
//But after 30 min it calls default update
WidgetData wg = new WidgetData(test);
wg.setDate(date);
wg.reqCode = mAppWidgetId;
wg.color=myColor;
wg.picture=drawables[myImg];
saveOneWidget(wg,Widgets.getSize());//Init = 0
            wg.printString();
            Widgets.add(wg);

//Final updates
            appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }

    public static void loadWidgets(Context context){
        Widgets.reset();
        if(sharedPref==null){
            sharedPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
            sharedEditor = sharedPref.edit();
        }
        int size = sharedPref.getInt("wsize", 0);
        if(size !=0)
            for(int i = 0 ; i < size; i ++ ){
                String name = sharedPref.getString("wlist"+i+"name", " ");
                WidgetData temp = new WidgetData(name);
                temp.yy = sharedPref.getInt("wlist"+i+"yy", 0);
                temp.mmMod = sharedPref.getInt("wlist"+i+"mm", 0);
                temp.dd = sharedPref.getInt("wlist"+i+"dd", 0);
                temp.reqCode = sharedPref.getInt("wlist"+i+"reqCode", 0);
                temp.color = sharedPref.getInt("wlist"+i+"color", 0);
                temp.picture = sharedPref.getInt("wlist"+i+"img", 0);
                Widgets.add(temp);
            }
    }
    public static void saveWidgets(Context context){
        if(sharedPref==null){
            sharedPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
            sharedEditor = sharedPref.edit();
        }
        for(int i=0;i<Widgets.getSize();i++){
            WidgetData temp=Widgets.get(i);
            sharedEditor.putInt("wlist"+i+"yy", temp.yy);
            sharedEditor.putInt("wlist"+i+"mm", temp.mmMod);
            sharedEditor.putInt("wlist"+i+"dd", temp.dd);
            sharedEditor.putInt("wlist"+i+"reqCode", temp.reqCode);
            sharedEditor.putInt("wlist"+i+"color", temp.color);
            sharedEditor.putInt("wlist"+i+"img", temp.picture);
            sharedEditor.putString("wlist"+i+"name", temp.name);
        }
        sharedEditor.putInt("wsize", Widgets.getSize());
        sharedEditor.commit();
    }
    public static void saveOneWidget(WidgetData temp, int i){
        sharedEditor.putInt("wlist"+i+"yy", temp.yy);
        sharedEditor.putInt("wlist"+i+"mm", temp.mmMod);
        sharedEditor.putInt("wlist"+i+"dd", temp.dd);
        sharedEditor.putInt("wlist"+i+"reqCode", temp.reqCode);
        sharedEditor.putInt("wlist"+i+"color", temp.color);
        sharedEditor.putInt("wlist"+i+"img", temp.picture);
        sharedEditor.putString("wlist"+i+"name", temp.name);
        sharedEditor.putInt("wsize", i+1);
        sharedEditor.commit();
    }

}
