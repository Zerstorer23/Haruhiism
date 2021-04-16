package com.haruhi.bismark439.haruhiism.AlarmScreens;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haruhi.bismark439.haruhiism.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static com.haruhi.bismark439.haruhiism.MainActivity.MAX_VOLUME;
import static com.haruhi.bismark439.haruhiism.MainActivity.currentVolume;
import static com.haruhi.bismark439.haruhiism.MainActivity.sendNotification;

public class Mikuru_Screen extends AppCompatActivity {
    private ViewGroup mainLayout;
MediaPlayer mMediaPlayer;
    MediaPlayer mMediaPlayer2;
    private int xDelta;
    private int yDelta;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    int remaining = 6;
    int [] tvs={R.id.line0,R.id.line1,R.id.line2,R.id.line3,R.id.line4,R.id.line5};
    Wordpuzzle wordset0;
    Wordpuzzle wordset1;
    Wordpuzzle wordset2 ;
    Wordpuzzle wordset3;
    Wordpuzzle wordset4 ;
    Wordpuzzle wordset5;
    String[] textFront;
    String[] textEnd;
    List<Integer> coords;
    boolean stop = false;
    Button giveup;
    int fails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mikuru__screen);
        mainLayout = (FrameLayout) findViewById(R.id.main);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
       String [] t1={getString(R.string.linef1),getString(R.string.linef2),getString(R.string.linef3),getString(R.string.linef4),getString(R.string.linef5),getString(R.string.linef6)};
        String [] t2={getString(R.string.line1),getString(R.string.line2),getString(R.string.line3),getString(R.string.line4),getString(R.string.line5),getString(R.string.line6)};
textFront=t1;
        textEnd=t2;
        giveup=(Button)findViewById(R.id.unlock_mikuru);
        wordset0 = new Wordpuzzle(getApplicationContext());
        wordset1 = new Wordpuzzle(getApplicationContext());
        wordset2 = new Wordpuzzle(getApplicationContext());
        wordset3 = new Wordpuzzle(getApplicationContext());
        wordset4= new Wordpuzzle(getApplicationContext());
        wordset5 = new Wordpuzzle(getApplicationContext());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer2=new MediaPlayer();
        sendNotification(getApplicationContext(),Mikuru_Screen.class);
        playSound(false);
        tv1 = (TextView)findViewById(R.id.text1);
        tv2 =(TextView) findViewById(R.id.text2);
        tv3 = (TextView)findViewById(R.id.text3);
        tv4 =(TextView) findViewById(R.id.text4);
        tv5 = (TextView)findViewById(R.id.text5);
        tv6 = (TextView)findViewById(R.id.text6);

        tv1.setOnTouchListener(onTouchListener());
        tv2.setOnTouchListener(onTouchListener());
        tv3.setOnTouchListener(onTouchListener());
        tv4.setOnTouchListener(onTouchListener());
        tv5.setOnTouchListener(onTouchListener());
        tv6.setOnTouchListener(onTouchListener());
        wordset0.initialise(0);
        wordset1.initialise(1);
        wordset2.initialise(2);
        wordset3.initialise(3);
        wordset4.initialise(4);
        wordset5.initialise(5);
        tv1.setText(wordset0.getRandom().word);
        tv2.setText(wordset1.getRandom().word);
        tv3.setText(wordset2.getRandom().word);
        tv4.setText(wordset3.getRandom().word);
        tv5.setText(wordset4.getRandom().word);
        tv6.setText(wordset5.getRandom().word);

        tv1.setTag(0+"");
        tv2.setTag(1+"");
        tv3.setTag(2+"");
        tv4.setTag(3+"");
        tv5.setTag(4+"");
        tv6.setTag(5+"");
coords= initialiseCoordinates();
        tv1.setTranslationX(coords.get(0));
        tv2.setTranslationX(coords.get(1));
        tv3.setTranslationX(coords.get(2));
        tv4.setTranslationX(coords.get(3));
        tv5.setTranslationX(coords.get(4));
        tv6.setTranslationX(coords.get(5));
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        for(int i=0;i<tvs.length;i++){
                            TextView test = (TextView)findViewById(tvs[i]);
                            boolean overlap = isViewOverlapping(test,view);
                            if(overlap){
                                System.out.println("Overlapped with line"+i);
                                System.out.println(String.valueOf(test.getText()));
                                doSomething(i,view);
                            }
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);

                        break;
                }
                mainLayout.invalidate();
                return true;
            }

        };
    }
    private boolean isViewOverlapping(View back, View moving) {
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];
        back.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        moving.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        back.getLocationOnScreen(firstPosition);
        moving.getLocationOnScreen(secondPosition);

        int fRight = back.getMeasuredWidth() + firstPosition[0];
        int fBot = firstPosition[1]+back.getMeasuredHeight(); //Bot higher
        float xx=secondPosition[0]+moving.getWidth()/2;
        float yy =secondPosition[1]+moving.getHeight()/2;

        if(xx>=firstPosition[0]&&xx<=fRight){
            if(yy>=firstPosition[1]&&yy<=fBot){
                System.out.println("X: "+firstPosition[0]+"~ "+fRight);
                System.out.println("Y: "+firstPosition[1]+" ~ "+fBot);
                System.out.println("vs X: "+ xx+" Y:"+yy);
                return true;
            }
        }
        return false;
    }
    private void doSomething(int i, View view){
        String tag = (String)view.getTag();
        if(tag.equals(i+"")) {
            String input = String.valueOf(((TextView)view).getText());
            TextView textView = (TextView)findViewById(tvs[i]);
            String t1 = textFront[i];
            String text = t1 + input + textEnd[i];
            Spannable spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#00c800")), t1.length(), (t1 + input).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannable, TextView.BufferType.SPANNABLE);
        view.setVisibility(GONE);
        remaining--;
        if(remaining==0){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer2.stop();
            mMediaPlayer2.release();
            playSound(true);
        }
        }else{
            fails++;
          Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(VIBRATOR_SERVICE);
          vibrator.vibrate(500);
        if(fails>2){
            giveup.setVisibility(View.VISIBLE);
        }
        }
    }
    public void onClickGiveup(View v){
try {
    if(mMediaPlayer.isPlaying())mMediaPlayer.stop();
    mMediaPlayer.release();
    if(mMediaPlayer2.isPlaying()) mMediaPlayer2.stop();
    mMediaPlayer2.release();
    playSound(true);
}catch (Exception e){
    e.printStackTrace();
    destroy();
}
    }
    private List<Integer> initialiseCoordinates(){
        List<Integer> coords = new ArrayList<Integer>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
       // int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        for(int i=0;i<6;i++){
            int a=(int)((float) width/(7)) * i;
        coords.add(a);
        }
        Collections.shuffle(coords);
    return coords;
    }
    public void destroy(){
        stop=true;
        AudioManager audio = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,0);
        AlarmScreen.demolish=true;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.cancel(001);
        finish();
    }
    public void playSound( boolean kyon){
        try {
            if(kyon){
                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("kinsoku/kyon_kinsoku.mp3");
                MediaPlayer tempPlayer = new MediaPlayer();
                tempPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                tempPlayer.prepare();
                tempPlayer.start();
                tempPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                    destroy();
            }
            });
            }else {
                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("kinsoku/kinsoku.mp3");
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mMediaPlayer.prepare();
                float log1 = (float) (Math.log(MAX_VOLUME - 100) / Math.log(MAX_VOLUME));
                mMediaPlayer.setVolume(1 - log1, 1 - log1);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();

                afd = getApplicationContext().getAssets().openFd("kinsoku/mikuru_cry.mp3");
                mMediaPlayer2.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mMediaPlayer2.prepare();
                mMediaPlayer2.setScreenOnWhilePlaying(true);
                log1 = (float) (Math.log(MAX_VOLUME - 90) / Math.log(MAX_VOLUME));
                mMediaPlayer2.setVolume(1 - log1, 1 - log1);
                mMediaPlayer2.setLooping(true);
                mMediaPlayer2.start();

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
