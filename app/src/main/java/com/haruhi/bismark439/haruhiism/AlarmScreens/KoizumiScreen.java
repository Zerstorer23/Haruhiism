package com.haruhi.bismark439.haruhiism.AlarmScreens;

import android.app.NotificationManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haruhi.bismark439.haruhiism.R;

import java.io.IOException;

import static com.haruhi.bismark439.haruhiism.MainActivity.MAX_VOLUME;
import static com.haruhi.bismark439.haruhiism.MainActivity.currentVolume;
import static com.haruhi.bismark439.haruhiism.MainActivity.sendNotification;

/**
 * Created by Bismark439 on 19/01/2018.
 */


public class KoizumiScreen extends AppCompatActivity{
RelativeLayout rel;
    int col = 10;
    int row = 10;
    int numTrue =0;
    int numFalse=0;
    MediaPlayer mMediaPlayer = new MediaPlayer();
    boolean[][] board = new boolean[row][col];
    int rPlayer;
    String[] players={"Haruhi","Mikuru","Nagato","Kyon","Asakura","Tsuruya"};
    int[] bitmaps={R.drawable.haruhi_prof,R.drawable.mikuru_prof,R.drawable.yuki_prof,R.drawable.kyon_prof,R.drawable.asakura_prof,R.drawable.tsuruya_prof};
    TextView scoreboard;
    AudioManager audio;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koizumi_screen);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        rel = (RelativeLayout)findViewById(R.id.koi_board);
        rPlayer = (int)(Math.random()*players.length);

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),bitmaps[rPlayer]);
        ImageView img =(ImageView) findViewById(R.id.pl2);
        img.setImageBitmap(bitmap);


        scoreboard = (TextView)findViewById(R.id.scoreboard);
        scoreboard.setText(" ● VS ○ ");
        int rand = (int)(Math.random()*60+20);
        generateBoard(rand);
        rel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setBoard();
            }
        });
        playSound(true,"");
        sendNotification(getApplicationContext(),KoizumiScreen.class);
    }

    public void setBoard(){
        float width =rel.getWidth();
        float height =rel.getHeight();
        float w=width/(col);
        float h=height/(row);
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                float y=i*h;
                float x=j*w;
                TextView tv = new TextView(this);
               if(board[i][j]){tv.setText("●");
               }else{
                   tv.setText("○");
               }
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                tv.setTranslationX(x);
                tv.setTranslationY(y);
                tv.setScaleX((float) 1.3);
                tv.setScaleY((float) 1.3);
                rel.addView(tv);
            }
        }
    }
    public void generateBoard(int C){// C 40?
        numTrue=0;
        numFalse=0;
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                int a = (int)(Math.random()*100)+1; // 1~100;
                if(a <=C){
                    board[i][j] = true;
                    numTrue++;
                }else{
                    board[i][j] = false;
                    numFalse++;
                }
            }
        }

        System.out.println(numTrue+" VS "+numFalse);
    }
    public void updateBoard(){
        Thread skillArcade = new Thread() {
            public void run() {
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                final TextView tv = (TextView)rel.getChildAt(i*col+j);
                final int y=i;
                final int x=j;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(board[y][x]){tv.setText("●");
                            }else{
                                tv.setText("○");
                            }
                            }
                        });
                    }


            }
        }
        };
        skillArcade.start();
    }
    public void onAnswer(View v){
                String tt =(String)v.getTag();
                boolean correct=false;
                if(tt.equals("1")){
                    if(numTrue>=numFalse){
                        correct=true;//Chose koizumi, corr
                        playSound(false,"Koizumi/voice_00129.wav");//mikuruno shobu
                    }else{
                        //but wrong
                        switch (rPlayer){
                            case 0://Haruhi
                                playSound(false,"haruhi/voice_00016.wav");
                                break;
                            case 1://mikuru
                                playSound(false,"mikuru/voice_00075.wav");//mikuruno shobu
                                break;
                            case 2://Yuki
                                playSound(false,"Nagato/voice_00049.wav");
                                break;
                            case 3://Kyon
                                playSound(false,"Kyon/voice_00100.wav");
                                break;
                            case 4://Asakura
                                playSound(false,"Asakura/voice_00161.wav");
                                break;
                            case 5://Tsuruya
                                playSound(false,"Tsuruya/voice_00182.wav");
                                break;
                        }
                    }
        } else{//Chose Mikuru
                  if(numTrue<=numFalse){
                      //Chose Mikuru, corr
                      switch (rPlayer){
                          case 0://Haruhi
                              playSound(false,"haruhi/voice_00026.wav");
                              break;
                          case 1://mikuru
                              playSound(false,"mikuru/voice_00070.wav"); //mikuru beam
                              break;
                          case 2://Yuki
                              playSound(false,"Nagato/voice_00050.wav");
                              break;
                          case 3://Kyon
                              playSound(false,"Kyon/voice_00095.wav");
                              break;
                          case 4://Asakura
                              playSound(false,"Asakura/voice_00157.wav");
                              break;
                          case 5://Tsuruya
                              playSound(false,"Tsuruya/voice_00185.wav");
                              break;
                      }
                      correct=true;
                  }else{
                      //but wrong
                      playSound(false,"Koizumi/voice_00125.wav");//mikuruno shobu
                  }
                }
        TextView tv =(TextView) findViewById(R.id.scoreboard);
         tv.setText(numTrue+" VS "+numFalse);
         System.out.println("Answer "+correct);
        showAnswer(correct);
    }
    public void showAnswer(final boolean correct){
        board = new boolean[row][col];
        for(int i=0;i<numTrue;i++){
            board[i/col][i%col]=true;
        }
        updateBoard();
    //    mMediaPlayer.pause();
        if(!correct){
            vibrator.vibrate(500);
        }else{
            LinearLayout lin = (LinearLayout)findViewById(R.id.kzm_buttoncont);
            lin.setVisibility(View.INVISIBLE);
        }
        new CountDownTimer(1000, 500) {//1000
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                if (correct) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    destroy();
                } else {
                     new Thread() {
                        public void run() {
                        //    mMediaPlayer.start();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    scoreboard.setText(" ● VS ○ ");
                                }
                            });
                            int rand = (int) (Math.random() * 40 + 30);
                            generateBoard(rand);
                            updateBoard();
                        }
                    }.start();
                }
            }
        }.start();
    }
    public void destroy(){
        audio = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,0);
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
                AssetFileDescriptor afd = getApplicationContext().getAssets().openFd("Alarm/toroden.mp3");
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
