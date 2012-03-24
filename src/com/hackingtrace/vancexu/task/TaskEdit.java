package com.hackingtrace.vancexu.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hackingtrace.vancexu.R;

public class TaskEdit extends Activity {
	private static final String TAG = "AimToTaskEdit";
	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	private TasksDbAdapter mDbHelper;
	private String mAlarmTime;

	private static final int ACTIVITY_EDIT_TIME = 0;
	private static final int ACTIVITY_CONFIRM_ALARM_TIME = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_edit);
		setTitle(R.string.edit_task);

		Log.d(TAG, "on edit creat");
		mAlarmTime = "no alarm set";

		mDbHelper = new TasksDbAdapter(this);
		mDbHelper.open();

		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);

		Button confirmButton = (Button) findViewById(R.id.confirm);
		Button setTimeButton = (Button) findViewById(R.id.task_edit_set_time);

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(TasksDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(TasksDbAdapter.KEY_ROWID)
					: null;
		}

		populateFields();

		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString(TasksDbAdapter.KEY_TITLE, mTitleText.getText()
						.toString());
				bundle.putString(TasksDbAdapter.KEY_BODY, mBodyText.getText()
						.toString());
				bundle.putString(TasksDbAdapter.KEY_TIME, mAlarmTime);
				if (mRowId != null) {
					bundle.putLong(TasksDbAdapter.KEY_ROWID, mRowId);
				}

				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});

		/*
		 * cancelButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * public void onClick(View view) { Intent mIntent = new Intent();
		 * setResult(RESULT_CANCELED, mIntent); finish(); }
		 * 
		 * });
		 */

		setTimeButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(view.getContext(), TaskTimeSet.class);
				i.putExtra(TasksDbAdapter.KEY_ROWID, mRowId);
				Calendar c = Calendar.getInstance();
				Date d = c.getTime();
				// SimpleDateFormat sdf = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(d);
				// Log.d(TAG, s); 2012-3-22 09:47:52
				// Log.d(TAG, s);
				i.putExtra("date", now);
				// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(i, ACTIVITY_EDIT_TIME);
			}

		});
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor Task = mDbHelper.fetchTask(mRowId);
			startManagingCursor(Task);
			mTitleText.setText(Task.getString(Task
					.getColumnIndexOrThrow(TasksDbAdapter.KEY_TITLE)));
			mBodyText.setText(Task.getString(Task
					.getColumnIndexOrThrow(TasksDbAdapter.KEY_BODY)));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(TasksDbAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();

		if (mRowId == null) {
			long id = mDbHelper.createTask(title, body, "false");
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateTask(mRowId, title, body, "false");
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Log.d(TAG, "I am in " + requestCode + resultCode);
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == ACTIVITY_EDIT_TIME) {
			if (resultCode == RESULT_OK) {
				Log.d(TAG, "OK time set");
				String[] dateArr = intent.getStringExtra("date").split("-");
				// Log.d(TAG, dateArr[0]);
				String[] timeArr = intent.getStringExtra("time").split(":");
				int year = Integer.parseInt(dateArr[0]);
				int month = Integer.parseInt(dateArr[1]) - 1; // !!! -1 !!!
				int day = Integer.parseInt(dateArr[2]);
				int hour = Integer.parseInt(timeArr[0]);
				int minute = Integer.parseInt(timeArr[1]);
				// Log.d(TAG, ""+year+month+day+hour+minute);

				Calendar c = Calendar.getInstance();
				c.set(year, month, day, hour, minute, 0);
				Intent i = new Intent(TaskEdit.this, TaskAlarmReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						TaskEdit.this, ACTIVITY_CONFIRM_ALARM_TIME, i, 0);
				AlarmManager am;
				am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
						pendingIntent);
				TextView showTimeSet = (TextView) findViewById(R.id.task_edit_showtimeset);
				// String s = c.toString();
				Date d = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				mAlarmTime = sdf.format(d);
				// Log.d(TAG, s);
				showTimeSet.setText("Alarm you set: " + mAlarmTime);
			}
		}
	}
}
