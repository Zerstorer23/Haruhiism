package com.haruhi.bismark439.haruhiism;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Bismark439 on 14/01/2018.
 */

public class AlarmDB {

    ArrayList<AlarmData> alarmDB;

    public AlarmDB() {
        this.alarmDB = new ArrayList<AlarmData>();
    }
    public int getSize(){
        return this.alarmDB.size();
    }
    public void add(AlarmData aa){
        this.alarmDB.add(aa);
    }
    public AlarmData get(int i){
        return this.alarmDB.get(i);
    }
    public AlarmData getByCode(int req){
        for(int i=0;i<getSize();i++){
            if(get(i).reqCode==req){
                return get(i);
            }
        }
        return null;
    }
}
class AlarmData {//implements Parcelable{
    int reqCode;
    boolean[] days = new boolean[7];
    boolean repeat=false;
    boolean Vibration = true;
    String songname="alarm1.mp3";
    String alarmname="Alarm";
    int sMin;
    int sTimes;
    int volume = 12;
    long time;
    int hh;
    int mm;
    boolean enabled=false;
int waker=0;
    public AlarmData (int id){
        this.reqCode=id;
    }
    public AlarmData() {
        this.enabled=true;
    }
    public AlarmData(long time, int reqCode, boolean en) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        this.hh = calendar.get(Calendar.HOUR_OF_DAY);
        this.mm = calendar.get(Calendar.MINUTE);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY,this.hh);
        calendar2.set(Calendar.MINUTE,this.mm);
        this.time=calendar2.getTimeInMillis();
        this.reqCode=reqCode;
        this.enabled=en;
    }
    public void printString(){
        System.out.println("=====PRINTING ALARM DATA: =====");
        System.out.println(hh+": "+mm+" Code: "+reqCode+" Enabled: "+enabled+" ");
        for(int i=0;i<days.length;i++){
            System.out.print(days[i]);
        }
        System.out.println("");
        System.out.println("======================");
    }


}
