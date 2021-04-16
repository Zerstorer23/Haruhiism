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

import static com.haruhi.bismark439.haruhiism.WallPaper.yukiSetting.loadLWP;

/**
 * Created by Bismark439 on 02/03/2018.
 */

public class yukiWall extends WallpaperService{
    private boolean mVisible;  // visible flag
    Canvas canvas;      // canvas reference
    int Drawspeed=33;   // thread call delay time 10 default
    int changePicSpeed = 47000;
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


        int columnSize = 100; //column size ; no of digit required to fill the screen
        int fontSize = 0;//24; //font size of the text which will fall

        Paint textPaint;
        Paint backPaint;
        String text ="SUZUMIYAHARUHI";// "涼宮ハルヒ";  // Text which need to be drawn
        char[] textChar = text.toCharArray(); // split the character of the text
        int textLength = textChar.length;   //length of the length text

        int[]  textPosition; // contain the position which will help to draw the text
        int text_color=Color.parseColor("#FF8BFF4A");
        Random rand = new Random(); //random generater
        int[]imgList = {R.string.haruhi_ascii3,R.string.ascii_mikuru,R.string.ascii_hrhLN,R.string.ascii_haruhi3,R.string.ascii_sos};
        int mImgIndex = 0;
        char[][] foregroundImage;
        CharSequence asciiString;
        int imageHeight;
        int ImageMultiply = 2;
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
            System.out.println("VisChangeCalled"+visible);
            text = loadLWP(getApplicationContext());
            textChar = text.toCharArray(); // split the character of the text
            textLength = textChar.length;
            fontSize = width/columnSize;
            if (visible) {
                drawFrame();
                changePicture();
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
            //this is necessay to remove the call back
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
                    // draw something
                    // canvas matrix draw code
                    canvasDraw();
                    //^^^^
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            // Reschedule the next redraw
            // this is the replacement for the invialidate funtion
            // every time call the drawFrame to draw the matrix
            mHandler.removeCallbacks(mDrawFrame);
            if (mVisible) {
                // set the execution delay
                mHandler.postDelayed(mDrawFrame, Drawspeed);
            }
        }
        public void changePicture()
            {
                constructImage();
                mImgIndex = rand.nextInt(imgList.length);
                System.out.println("Rand pic "+mImgIndex);
//                if(mImgIndex>=imgList.length)mImgIndex=0;
                mHandler.postDelayed(mChangePic,changePicSpeed);
        }

        public void initialise() {
            textPaint = new Paint();
            backPaint = new Paint();
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setColor(text_color);
            textPaint.setTextSize(fontSize);

            backPaint.setColor(background_color);
            backPaint.setAlpha(5); //set the alpha
            backPaint.setStyle(Paint.Style.FILL);
        }
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // some matrix variable
            // though not needed
            Paint paint = new Paint();
            paint.setColor(background_color);
            paint.setAlpha(255); //set the alpha
            paint.setStyle(Paint.Style.FILL);
            this.width = width + 20;
            this.height = height;
            System.out.println("onSurfaceChanged WIDTH: "+width+" HEIGHT "+height);
            fontSize = width/columnSize;
            constructImage();
            initialise();
            //initalise the textposiotn to zero
            textPosition = new int[columnSize+1]; //add one more drop
            for(int x = 0; x < columnSize; x++) {
                textPosition[x] = 1;
            }
            drawFrame();
            changePicture();
            canvas.drawRect(0, 0, width,height, paint);

        }
        //old matrix effect code
        void drawText()
        {
            //loop and paint
            for(int i =0 ;i<textPosition.length;i++)
            {
                // draw the text at the random position
                int x = i;
                int y = textPosition[i];
                if(x>=99 || y>= imageHeight * ImageMultiply ||foregroundImage[y][x]=='ㅡ'){
                    textPaint.setColor(Color.parseColor("#00c800"));
                    canvas.drawText("" + textChar[rand.nextInt(textLength)], x * fontSize, y * fontSize, textPaint);
                //
                }else{
                    textPaint.setColor(red);
                    //canvas.drawText(".", x * fontSize, y * fontSize, paint);
                    canvas.drawText(""+foregroundImage[y][x], x * fontSize, y * fontSize, textPaint);
                }

                // check if text has reached bottom or not
                //0.925 good
                if(textPosition[i]*fontSize > height && Math.random() > 0.900) //0.975
                    textPosition[i] = 0;   // change text position to zero when 0 when text is at the bottom

                textPosition[i]++; //increment the position array
            }

        }
        public void constructImage(){
            asciiString = getString(imgList[mImgIndex]);
            imageHeight = (asciiString.length() / 100);
            foregroundImage = new char [imageHeight * ImageMultiply][100];
          //  System.out.println("LENGTH "+asciiString.length());
            int modY = 0;
            for(int y = 0 ; y <imageHeight ; y++){
                for(int x = 0 ; x <100 ; x++){
                    foregroundImage[modY][x] = asciiString.charAt(x + y*100);
                    foregroundImage[modY+1][x] =  foregroundImage[modY][x];
                //    foregroundImage[modY+2][x] =  foregroundImage[modY][x];
                }
                modY = modY+ImageMultiply;
            }


        }
        //old martix effect code
        public void canvasDraw()
        {
            //Log.d("canvas ","drawing");
            //set the paint for the canvas
            canvas.drawRect(0, 0, width, height, backPaint);//draw rect to clear the canvas
            drawText(); // draw the canvas

        }

    }



}