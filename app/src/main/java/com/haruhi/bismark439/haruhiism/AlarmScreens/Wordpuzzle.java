package com.haruhi.bismark439.haruhiism.AlarmScreens;

import android.content.Context;

import com.haruhi.bismark439.haruhiism.R;

import java.util.ArrayList;

/**
 * Created by Bismark439 on 17/01/2018.
 */

public class Wordpuzzle {
    ArrayList<piece> wordDB;
    String[] temp;
    String[] place;
    String[] tool;
    String[] verb;
    String[] sentence;
    String[] verb2;
    String[] adjective;

    public Wordpuzzle (Context context){
        this.wordDB=new ArrayList<piece>();
        String[] t1={context.getString(R.string.future),context.getString(R.string.kawai)};
        String[] t2={"TPDD",context.getString(R.string.q1),context.getString(R.string.q2),context.getString(R.string.q3),context.getString(R.string.q4),context.getString(R.string.q5)};
        String[] t3={context.getString(R.string.q6),context.getString(R.string.q7),context.getString(R.string.q8),context.getString(R.string.q9),context.getString(R.string.q0),context.getString(R.string.q10)};
        String[] t4={context.getString(R.string.q11),context.getString(R.string.q12),context.getString(R.string.q13),context.getString(R.string.q14),context.getString(R.string.q15),context.getString(R.string.q16)};
        String[] t5={context.getString(R.string.q19),context.getString(R.string.q18),context.getString(R.string.q20),context.getString(R.string.q21),context.getString(R.string.q22),context.getString(R.string.q23)};
        String[] t6={context.getString(R.string.q24),context.getString(R.string.q25),context.getString(R.string.q26),context.getString(R.string.q27),context.getString(R.string.q28),context.getString(R.string.q29)};
    place=t1;
    tool=t2;
    verb=t3;
    sentence=t4;
    verb2=t5;
    adjective=t6;
    }
    public void initialise(int type){
       String[] temp=null;
        switch (type){
            case 0:
                temp = place;
                break;
            case 1:
                temp=tool;
                break;

            case 2:
            temp=verb;
                break;

            case 3:
                temp=sentence;
                break;
            case 4:
                temp=verb2;
                break;

            case 5:
                temp=adjective;
                break;
        }

        for(int i=0;i<temp.length;i++){
            piece tt= new piece(temp[i],type);
            this.wordDB.add(tt);
        }
    }

    public piece getRandom(){
        int rand=(int)(Math.random()* wordDB.size());
        return wordDB.get(rand);
    }
    public void setType(int i){
        switch (i){
            case 0:
                temp = place;
                break;
            case 1:
                temp=tool;
                break;

            case 2:
                temp=verb;
                break;

            case 3:
                temp=sentence;
                break;
            case 4:
                temp=verb2;
                break;

            case 5:
                temp=adjective;
                break;
        }
    }
}

class piece {
    String word;
    int ansType;

    public piece (String word, int type){
        this.word=word;
        this.ansType=type;
    }
}

