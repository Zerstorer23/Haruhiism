package com.haruhi.bismark439.haruhiism.WallPaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.haruhi.bismark439.haruhiism.R;

import java.util.Random;

/**
 * Created by Bismark439 on 02/03/2018.
 */

public class YukiNWall extends WallpaperService{
    private boolean mVisible;  // visible flag
    Canvas canvas;      // canvas reference
    int delayFast = 90;
    int delaySlow=  278;
    int delayNormal = 120;
    Paint textPaint = new Paint();
    Paint backPaint = new Paint();
    Context mcontext;   //reference to the current context
    final Handler mHandler = new Handler(); // this is to handle the thread
    int red = Color.parseColor("#CD5C5C");
    @Override
    public Engine onCreateEngine() {
        mcontext = this;  //set the current context
        //return the Engine Class
        return new LiveWall(); // this calls contain the wallpaper code
    }

    class LiveWall extends Engine
    {
        private final Runnable mDrawFrame = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private final Runnable mChangePic = new Runnable() {
            public void run() {
                changePicture();
            }
        };

        // ======== MATRIX LIVE WALLPAPER VARS
        int background_color= Color.parseColor("#FF000000");
        private int width = 1000000; //default initial width
        private int height = 100; //default initial height
        int changePicSpeed = 70000;
        int columnSize = 50; //column size ; no of digit required to fill the screen
        int fontSize = 24; //font size of the text which will fall
        int[]imgList = {R.string.YUKI_6,R.string.YUKI_disappear};
        int mImgIndex = 0;
        String text ="";// "涼宮ハルヒ";  // Text which need to be drawn

        int text_color=Color.parseColor("#FFFFFF");
        Random rand = new Random(); //random generater
        //Main Strings
        private String[]mStrings;
        private String stringSoFar;
        private int arrayIndex;

        //Sub Strings
        private String subString;
        private int mIndex;
        private int mDelay = 150; //Default 150ms delay

        //Beeping
        private boolean beep = true;
        //======================

        //Called when the surface is created
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        // remove thread
        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawFrame);
            mHandler.removeCallbacks(mChangePic);
        }


        //called when varaible changed
        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            fontSize = width/columnSize;
            if (visible) {
             //   changePicture();
                drawFrame();
            } else {
                //this is necessay to remove the call back
                mHandler.removeCallbacks(mDrawFrame);
               mHandler.removeCallbacks(mChangePic);
            }
        }

        //called when surface destroyed
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            //this is necessary to remove the call back
            mHandler.removeCallbacks(mDrawFrame);
            mHandler.removeCallbacks(mChangePic);
        }


        //this function contain the the main draw call
        /// this function need to call every time the code is executed
        // the thread call this function with some delay "drawspeed"
        public void drawFrame()
        {
            //getting the surface holder
            final SurfaceHolder holder = getSurfaceHolder();
            canvas = null;  // canvas
            try {
                canvas = holder.lockCanvas();  //get the canvas
                if (canvas != null) {
                    canvasDraw();
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            mHandler.removeCallbacks(mDrawFrame);
            if (mVisible) {
                // set the execution delay
         //      System.out.println("Delay "+mDelay);
                mHandler.postDelayed(mDrawFrame, mDelay);
            }
        }

        public void beep(){
         //   System.out.println("Beep called");
                if(beep){
                    drawText(stringSoFar);
                }else{
                    drawText(stringSoFar+"_");
                }
                beep=!beep;
        }
        public void changePicture()
        {
            initialise();
            mImgIndex++;
            if(mImgIndex>=imgList.length)mImgIndex=0;
            mHandler.postDelayed(mChangePic,changePicSpeed);
        }
        public void addCharacter(){
            String toDraw = "";
            mDelay=delayNormal;
            if(mStrings[arrayIndex].equals("YUKI.N>")){
                stringSoFar = appendText();
                toDraw = stringSoFar+"YUKI.N>_";
                arrayIndex++;
             }else if(mStrings[arrayIndex].equals("\n")){//New line mode
                stringSoFar = appendText();
                toDraw=stringSoFar;
                arrayIndex++;
                mDelay=delaySlow;
            } else{//SubString Mode
               if(subString==null){
                    subString = mStrings[arrayIndex];
                    stringSoFar = appendText();
                    if(subString.length()>10){
                        mDelay=delayFast;
                    }
                }//Init substring

                if(mIndex==0){
                    toDraw=stringSoFar+subString.substring(0,mIndex);
                }else{
                    toDraw=stringSoFar+subString.substring(0,mIndex)+"_";
                }
                mIndex++;
                if(mIndex > subString.length()) {
                    arrayIndex++;
                    mIndex=0;
                    subString=null;
                }
            }
            drawText(toDraw);
        }
        public void initialise() {
            mIndex = 0;
            arrayIndex = 0;
            text = getString(imgList[mImgIndex]);
            mStrings = text.toString().split(",");
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(text_color);
            textPaint.setTextSize(fontSize);

            backPaint.setColor(background_color);
            backPaint.setAlpha(255); //set the alpha
            backPaint.setStyle(Paint.Style.FILL);
        }
        public String appendText(){
            String text = "";
            for(int i=0;i<arrayIndex;i++){
                text=text+mStrings[i];
            }
            return text;
        }
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // some matrix variable
            // though not needed

            this.width = width + 20;
            this.height = height;
            fontSize = width/columnSize;
            changePicture();
            drawFrame();
            canvas.drawRect(0, 0, width,height, backPaint);

        }
        //old matrix effect code
        void drawText(String text) {
//            System.out.println("Draw text : "+text);
            int x = 0;
           int y = 7;
            for(int i =0 ;i<text.length();i++)
            {
                if(text.charAt(i)=='\n'){
                 y++;
                 y++;
                 x=0;
                }else{
             //   System.out.println("set:"+text.charAt(i)+":at x"+x+"/ y"+y);
                canvas.drawText("" +text.charAt(i), x * fontSize, y * fontSize, textPaint);
                x++;
                }
                if(x*fontSize > width-100){
                    x=0;
                    y++;
                }
            }
        }
        //old martix effect code
        public void canvasDraw()
        {
            //Log.d("canvas ","drawing");
            canvas.drawRect(0, 0, width, height, backPaint);//draw rect to clear the canvas

            if(arrayIndex < mStrings.length) {
                addCharacter(); // draw the canvas
            }else { //Wrote Everything
                stringSoFar = appendText();
                mDelay=250;
                beep();
            }

        }

    }



}