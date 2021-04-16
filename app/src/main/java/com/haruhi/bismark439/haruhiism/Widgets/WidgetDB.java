package com.haruhi.bismark439.haruhiism.Widgets;

import java.util.ArrayList;

/**
 * Created by Bismark439 on 14/01/2018.
 */

public class WidgetDB {

    ArrayList<WidgetData> widgetDB;

    public WidgetDB() {
        this.widgetDB = new ArrayList<WidgetData>();
    }
    public int getSize(){
        return this.widgetDB.size();
    }
    public void add(WidgetData aa){
        this.widgetDB.add(aa);
    }
    public void reset(){
        this.widgetDB.clear();
    }
    public WidgetData get(int i){
        return this.widgetDB.get(i);
    }
    public WidgetData getByCode(int req){
        for(int i=0;i<getSize();i++){
            if(get(i).reqCode==req){
                return get(i);
            }
        }
        return null;
    }
}

class WidgetData {
    int reqCode;
    String name=" ";
    int yy;
    int mmMod;
    int dd;
    int color;
    int picture;
    public WidgetData (int id){
        this.reqCode=id;
    }
    public WidgetData(String name) {
        this.name = name;
    }
    public void printString(){
        System.out.println("=====PRINTING WIDGET DATA: =====");
        System.out.println("Code: "+reqCode+"  Name: "+name);
        System.out.println("Y: "+yy+" M: "+mmMod+" D: "+dd);
        System.out.println("Color : #"+color);
        System.out.println("============================");
    }
    public void setDate(int[]date){
        this.yy = date[0];
        this.mmMod=date[1]-1;
        this.dd=date[2];
    }

}
