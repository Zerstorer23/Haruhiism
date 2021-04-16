package com.haruhi.bismark439.haruhiism.AlarmScreens;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haruhi.bismark439.haruhiism.R;

import java.io.IOException;

import static com.haruhi.bismark439.haruhiism.MainActivity.MAX_VOLUME;
import static com.haruhi.bismark439.haruhiism.MainActivity.currentVolume;
import static com.haruhi.bismark439.haruhiism.MainActivity.sendNotification;

public class AlarmScreen extends AppCompatActivity {
public static boolean demolish=false;
    MediaPlayer mMediaPlayer=new MediaPlayer();
    int[] myNumber = new int[11];
    int idx=0;
    int[] ansNumber=new int[11];
    String[] denwa={"denwa1","denwa2","denwa3","denwa4","denwa5","denwa6","denwa7"};
    Vibrator vibrator;
    TextView mynum;
    TextView ansnum;
    LinearLayout keypad;
    boolean stopDenwa=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mynum=(TextView)findViewById(R.id.myNumber);
        ansnum=(TextView)findViewById(R.id.ansnum);
        keypad=(LinearLayout)findViewById(R.id.keypad);
        playSound(getApplicationContext(),"haruhi-chan.mp3",false,90);
        playDenwa("denwa1.mp3");
        generateNumber();
        vibrator = (Vibrator)getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        sendNotification(getApplicationContext(),AlarmScreen.class);
    }

    public void playSound(Context context,String songname, boolean manifold,int volume){
        try {
            AssetFileDescriptor afd =context.getAssets().openFd(songname);
            if(manifold){
                MediaPlayer tempPlayer=new MediaPlayer();
                tempPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                tempPlayer.prepare();
                tempPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                    }
                });
            }else{
             mMediaPlayer.stop();
            mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.setScreenOnWhilePlaying(true);
                float log1=(float)(Math.log(MAX_VOLUME-volume)/Math.log(MAX_VOLUME));
            mMediaPlayer.setVolume(1-log1,1-log1);
            mMediaPlayer.start();
            }
        }catch (IOException e){
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }
    public void onClickPad(View v){
        String tt=(String)v.getTag();
        int t=Integer.parseInt(tt);
        if(t==10){
            if(idx>0){
                idx--;
            }
        }else{
            myNumber[idx]=t;
            idx++;
            if(idx>=myNumber.length){
                if(checkAns()){//correct
                    mynum.setTextColor(Color.parseColor("#00c800"));
                  //  keypad.setVisibility(View.INVISIBLE);
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    playSound(getApplicationContext(),"wakateru.mp3",true,100);
                    new CountDownTimer(1000, 250) {//1000
                        public void onTick(long millisUntilFinished) {
                        }
                        public void onFinish() {
                            destroy();
                        }
                    }.start();
                }else{
                 vibrator.vibrate(500);
                 mynum.setTextColor(Color.parseColor("#ff0000"));
                 keypad.setVisibility(View.INVISIBLE);
                // playSound(getApplicationContext(),"itatidesu.mp3",true,100);
                 idx--;
                    new CountDownTimer(1000, 250) {//1000
                        public void onTick(long millisUntilFinished) {
                        }
                        public void onFinish() {
                            mynum.setTextColor(Color.parseColor("#000000"));
                            keypad.setVisibility(View.VISIBLE);
                        }
                    }.start();
                }
            }
        }
        updateScreen();
    }

    public void generateNumber(){
        ansNumber[0]=0;
        ansNumber[1]=9;
        ansNumber[2]=0;

        for(int i=3;i<ansNumber.length;i++){
        int rand=(int)(Math.random()*10);
        ansNumber[i]=rand;
        }

        for(int i=0;i<ansNumber.length;i++){
            if(i==3||i==7){
                ansnum.append(" ");
            }
            ansnum.append(ansNumber[i]+"");
        }

    }
    public boolean checkAns(){
        boolean ret=true;
        for(int i=0;i<myNumber.length;i++){
            if(myNumber[i]!=ansNumber[i]){
                ret=false;
                i=999;
            }
        }
        return ret;
    }
    public void updateScreen(){
        mynum.setText("");
        for(int i=0;i< idx;i++){
            if(i==3||i==7){
                mynum.append(" ");
            }
            mynum.append(myNumber[i]+"");
        }
        for(int i=idx;i< myNumber.length;i++){
            if(i==3||i==7){
                mynum.append(" ");
            }
            mynum.append("_ ");
        }
    }
    public void playDenwa( String songname){
        try {
            AssetFileDescriptor afd =getApplicationContext().getAssets().openFd(songname);
               final MediaPlayer tempPlayer=new MediaPlayer();
                tempPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                tempPlayer.prepare();
                tempPlayer.start();
                tempPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if(!stopDenwa){
                        mediaPlayer.release();
                        int rand=(int)(Math.random()*7);
                        playDenwa(denwa[rand]+".mp3");
                    }
                }
            });
        }catch (IOException e){
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }
    public void onCheat(View v){
        destroy();
}
    public void destroy(){
        AudioManager audio = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,0);
        stopDenwa=true;
        demolish=true;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.cancel(001);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("KEYCODE_BACK");
         //   showDialog("'BACK'");
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            System.out.println("KEYCODE_MENU");
       //     showDialog("'MENU'");
            return true;
        }
        return false;
    }
}
