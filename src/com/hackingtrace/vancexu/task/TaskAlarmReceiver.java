package com.hackingtrace.vancexu.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TaskAlarmReceiver extends BroadcastReceiver {
	private static final String TAG = "AimToTaskAlarmReceiver";
	
	public void onReceive(Context context, Intent intent)
	{
//		Toast.makeText(context, "Your task in AimTo is fired", Toast.LENGTH_LONG).show();
//		Log.d(TAG, "I am in Alarm");
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, TaskNotification.class);
        context.startService(intent);
	}
}
