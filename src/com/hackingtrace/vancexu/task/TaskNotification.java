package com.hackingtrace.vancexu.task;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.hackingtrace.vancexu.R;

public class TaskNotification extends IntentService{
	
	public static final int NOTIFICATION_ID = 1;
	private static final int FROMTASKNOTIFICATION = 0;
	NotificationManager mNotificationManager;
	Notification notification;
	
	public TaskNotification() {
		super("TaskNotification");
		// TODO Auto-generated constructor stub
	}
	/*protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
		
		
	}*/

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		int icon = R.drawable.icon;
		CharSequence tickerText = "You got a task to do now";
		long when = System.currentTimeMillis();

		notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "AimTo Task";
		CharSequence contentText = "Hello World!";
		Intent notificationIntent = new Intent(this, AimToTaskActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, FROMTASKNOTIFICATION, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
}
