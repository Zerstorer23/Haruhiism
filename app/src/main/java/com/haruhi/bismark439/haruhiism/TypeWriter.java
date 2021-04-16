package com.haruhi.bismark439.haruhiism;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

/**
 * Created by Bismark439 on 02/03/2018.
 */


public class TypeWriter extends android.support.v7.widget.AppCompatTextView {
    //Main Strings
    private String[]mStrings;
    private String stringSoFar;
    private int arrayIndex;

    //Sub Strings
    private String subString;
    private int mIndex;
    private long mDelay = 150; //Default 150ms delay

    //Beeping
    private boolean beep = true;
    private int repeat = 50;

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            if(mStrings[arrayIndex].equals("YUKI.N>")){
                stringSoFar = appendText();
                setText(stringSoFar+"YUKI.N>_");
                arrayIndex++;
                if(arrayIndex < mStrings.length) {
                    mHandler.postDelayed(characterAdder, mDelay);
                }
            }else if(mStrings[arrayIndex].equals("\n")){//New line mode
                stringSoFar = appendText();
                setText(stringSoFar);
                arrayIndex++;
                mDelay = 780;
                if(arrayIndex < mStrings.length) {
                    mHandler.postDelayed(characterAdder, mDelay);
                }
            } else{//SubString Mode
                if(subString==null){
                    subString = mStrings[arrayIndex];
                    stringSoFar = appendText();
                    if(subString.length()>10){
                        mDelay=90;
                    }else{
                        mDelay=150;
                    }
                }
                if(mIndex==0){
                    setText(stringSoFar+subString.substring(0,mIndex));
                }else{
                    setText(stringSoFar+subString.substring(0,mIndex)+"_");
                }
                mIndex++;
                if(mIndex <= subString.length()) {
                    mHandler.postDelayed(characterAdder, mDelay);
                }else{
                    arrayIndex++;
                    mIndex=0;
                    subString=null;
                    if(arrayIndex < mStrings.length) {
                        mHandler.postDelayed(characterAdder, mDelay);
                    }else { //Wrote Everything
                        System.out.println("Send beeper");
                        stringSoFar = appendText();
                        mDelay=300;
                        repeat=500;
                        mHandler.postDelayed(beeper, mDelay);
                    }
                }
            }


        }
    };
    private Runnable beeper = new Runnable() {
        @Override
        public void run() {
            if(repeat>0){
                if(beep){
                    setText(stringSoFar);
                }else{
                    setText(stringSoFar+"_");
                }
                beep=!beep;
                repeat--;
                mHandler.postDelayed(beeper, mDelay);
            }
           // System.out.println(repeat);
        }
    };
    public void animateText(CharSequence text) {
        mIndex = 0;
        arrayIndex = 0;
        repeat = 0;
        mStrings = text.toString().split(",");
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }
    public String appendText(){
        String text = "";
        for(int i=0;i<arrayIndex;i++){
            text=text+mStrings[i];
        }
        return text;
    }
    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}