package com.haruhi.bismark439.haruhiism;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.haruhi.bismark439.haruhiism.MainActivity.saveAll;
import static com.haruhi.bismark439.haruhiism.MainActivity.Alarms;

public class AdapterAlarm extends BaseAdapter {
	
	Context mContext;
	ArrayList<String> mData;
	LayoutInflater mInflate;
	AlarmManager alarmManager;
	public AdapterAlarm(Context context) {
		mContext = context;
		alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		mInflate = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return Alarms.getSize();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return Alarms.get(position).reqCode;
	}

	public boolean removeData(int position){
		AlarmData temp =Alarms.alarmDB.remove(position);//.printString();
		Intent intent = new Intent(mContext, AlarmReceiver.class);
		int reqCode = temp.reqCode;
		intent.putExtra("requestCode",reqCode);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pi);

		saveAll();
		notifyDataSetChanged();
		return false;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AlarmListView layoutSingleAlarmItem = (AlarmListView) convertView;
		
		if (layoutSingleAlarmItem == null) {
			layoutSingleAlarmItem = new AlarmListView(mContext);
		//	layoutSingleAlarmItem.setOnRemoveButtonClickListener(onRemoveButtonClickListner);
		}
		layoutSingleAlarmItem.setData(Alarms.get(position), position);
		final int p=position;
		final Vibrator vibrator = (Vibrator)mContext.getSystemService(VIBRATOR_SERVICE);
		layoutSingleAlarmItem.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				System.out.println("Delete called on "+p);
				removeData(p);
				vibrator.vibrate(200);
				return true;
			}
		});
		return layoutSingleAlarmItem;
	}
	

}
