package com.haruhi.bismark439.haruhiism.AlarmScreens;

import android.app.NotificationManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.haruhi.bismark439.haruhiism.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;

import static com.haruhi.bismark439.haruhiism.MainActivity.MAX_VOLUME;
import static com.haruhi.bismark439.haruhiism.MainActivity.currentVolume;
import static com.haruhi.bismark439.haruhiism.MainActivity.sendNotification;

public class HaruhiBasic extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    AudioManager audio;
    ImageView frame;
    String[] HRHvoice;
    boolean loop = true;
    int[] aniList={R.drawable.ani1,R.drawable.ani2,R.drawable.ani3,R.drawable.ani4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haruhi_basic);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mMediaPlayer=new MediaPlayer();
        frame = findViewById(R.id.hrh_sos);
        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3141);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
       frame.startAnimation(rotate);
        AssetManager assetManager = getAssets();
        //Haruhi Image
        GifImageView hrhImg = findViewById(R.id.hrh_img);
        int nrand=(int)(Math.random()*aniList.length);
        hrhImg.setBackgroundResource(aniList[nrand]);
        try {
            HRHvoice = assetManager.list("haruhi");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendNotification(getApplicationContext(),HaruhiBasic.class);

        SwipeButton enableButton = (SwipeButton) findViewById(R.id.swipe_btn);
    enableButton.setOnActiveListener(new OnActiveListener() {
    @Override
    public void onActive() {
        destroy();
    }
});
        playSound();
    }
    public void destroy(){
        audio = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,0);
        if(mMediaPlayer.isPlaying()){mMediaPlayer.stop();}
        AlarmScreen.demolish=true;
        loop=false;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(001);
        finish();
    }
    public void playSound(){
        try {
                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("Alarm/bgm1.mp3");
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mMediaPlayer.prepare();
                float log1 = (float) (Math.log(MAX_VOLUME - 100) / Math.log(MAX_VOLUME));
                mMediaPlayer.setVolume(1 - log1, 1 - log1);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            //LOOP
                int rand=(int)(Math.random()*HRHvoice.length);
               playLoop(rand);
        }catch (IOException e){
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }

    public void playLoop(int rand){
        try {
            MediaPlayer mMediaPlayer=new MediaPlayer();
            AssetFileDescriptor afd =getApplicationContext().getAssets().openFd("haruhi/"+HRHvoice[rand]);
                mMediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                       if(loop){
                        int nrand=(int)(Math.random()*HRHvoice.length);
                        playLoop(nrand);}
                    }
                });
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
