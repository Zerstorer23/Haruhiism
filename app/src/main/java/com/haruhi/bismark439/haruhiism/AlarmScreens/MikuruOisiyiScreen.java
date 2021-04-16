package com.haruhi.bismark439.haruhiism.AlarmScreens;

import android.app.NotificationManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.haruhi.bismark439.haruhiism.R;

import java.io.IOException;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.haruhi.bismark439.haruhiism.MainActivity.MAX_VOLUME;
import static com.haruhi.bismark439.haruhiism.MainActivity.currentVolume;
import static com.haruhi.bismark439.haruhiism.MainActivity.sendNotification;

public class MikuruOisiyiScreen extends AppCompatActivity {
    VideoView video;
    static boolean mAutoIncrement = false;
    static ProgressBar progressBar;
    Vibrator vibrator;
    AudioManager audio;
    MediaPlayer mMediaPlayer;
    ImageView img;
    int weightFront;
    int weightMid;
    int weightEnd;
    TextView announce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mikuru_oisiyi_screen);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mMediaPlayer=new MediaPlayer();
        sendNotification(getApplicationContext(),MikuruOisiyiScreen.class);

        video = (VideoView)findViewById(R.id.videoView);
        img = (ImageView) findViewById(R.id.oi_thumbnail);
progressBar= (ProgressBar)findViewById(R.id.teaProgress);
announce = (TextView)findViewById(R.id.oi_announce);
        vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
       final String pathIn = "android.resource://" + getPackageName() + "/" + R.raw.tea_in;
        final String pathLoop = "android.resource://" + getPackageName() + "/" + R.raw.tea_loop;
        setTime();
playSound(true,"");
        video.setVideoURI(Uri.parse(pathIn));
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video.setVideoURI(Uri.parse(pathLoop));
                video.start();
            }
        });
Button bt = (Button)findViewById(R.id.PourButton);
        bt.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN&&!mAutoIncrement){
                    mAutoIncrement=true;
                    video.setVideoURI(Uri.parse(pathIn));
                    video.setVisibility(VISIBLE);
                    img.setVisibility(INVISIBLE);
                    video.start();
                    CountDownTimer cdt = new CountDownTimer(10000, 76) {//1000
                        public void onTick(long millisUntilFinished) {
                           if(mAutoIncrement){progressBar.setProgress(progressBar.getProgress()+3);}else{this.cancel();}

                           if(progressBar.getProgress()==100){
                               makeDecision();
                               this.cancel();
                           }
                        }
                        public void onFinish() {

                        }
                    }.start();
                }

                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL) && mAutoIncrement ){
                    mAutoIncrement = false;
                    video.stopPlayback();
                    makeDecision();
                }
                return false;
            }
        });
    }
public void setTime(){
        int rand = (int)(Math.random()*49)+24;//24 to 93;
    weightMid = 20;
     weightFront = rand;
 weightEnd = 100-rand-weightMid;
    TextView tv1 = (TextView)findViewById(R.id.oi_front);
    TextView tv2 = (TextView)findViewById(R.id.oi_mid);
    TextView tv3 =(TextView) findViewById(R.id.oi_end);
    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
    param.weight=weightFront;
    tv1.setLayoutParams(param);

    param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
    param.weight=weightMid;
    tv2.setLayoutParams(param);

    param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
    param.weight=weightEnd;
    tv3.setLayoutParams(param);

    //System.out.println("Progress: /Front: "+weightFront+" / end: "+(100-weightEnd));
}

 public void makeDecision(){
        int ans = progressBar.getProgress();
      //  System.out.println("Progress: "+ans+" /Front: "+weightFront+" / end: "+(100-weightEnd));
        if(ans<weightFront){
            announce.setText(R.string.pourtoolittle);
            playSound(false,"mikuru/voice_00079.wav");//watashinde iyi deska?
            vibrator.vibrate(500);
            progressBar.setProgress(0);
        }else if(ans>100-weightEnd){
            announce.setText(R.string.pourtomuch);
            playSound(false,"mikuru/voice_00078.wav");//watashinde iyi deska?
            vibrator.vibrate(500);
            progressBar.setProgress(0);
        } else{//Solved puzzle
            playSound(false,"Etc/oisii.mp3");
            new CountDownTimer(1875, 1000) {//1000
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    destroy();
                }
            }.start();
        }

 }
    public void destroy(){
        audio = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,0);
        if(mMediaPlayer.isPlaying()){mMediaPlayer.stop();}
        AlarmScreen.demolish=true;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(001);
        finish();
    }
    public void playSound( boolean main,String songname){
        try {
            if(!main){
                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd(songname);
                MediaPlayer tempPlayer = new MediaPlayer();
                tempPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                tempPlayer.prepare();
                tempPlayer.start();
                tempPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                    }
                });
            }else {
                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("Alarm/bgm1.mp3");
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mMediaPlayer.prepare();
                float log1 = (float) (Math.log(MAX_VOLUME - 100) / Math.log(MAX_VOLUME));
                mMediaPlayer.setVolume(1 - log1, 1 - log1);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            }
        }catch (IOException e){
            e.getLocalizedMessage();
            e.printStackTrace();
        }
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
