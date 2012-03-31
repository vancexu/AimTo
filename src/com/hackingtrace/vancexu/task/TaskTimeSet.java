package com.hackingtrace.vancexu.task;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hackingtrace.vancexu.R;

public class TaskTimeSet extends Activity {
	private static final String TAG = "AimToTaskTimeSet";
	// private Long mRowId;
	// private TasksDbAdapter mDbHelper;
	private Calendar month;
	private CalendarAdapter adapter;
	private String resultDate;
	private TimePicker timePicker;
	LinearLayout itemUsedToRecord;
	LinearLayout mItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_timeset);
		month = Calendar.getInstance();
		onNewIntent(getIntent());

		// items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);

		GridView gridview = (GridView) findViewById(R.id.task_timeset_gridview);
		gridview.setAdapter(adapter);

		GridView gridviewHead = (GridView) findViewById(R.id.task_timeset_gridview_head);
		CalendarHeadAdapter headAdapter = new CalendarHeadAdapter(this);
		gridviewHead.setAdapter(headAdapter);

		TextView title = (TextView) findViewById(R.id.task_timeset_title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		TextView previous = (TextView) findViewById(R.id.task_timeset_previous);
		previous.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (month.get(Calendar.MONTH) == month
						.getActualMinimum(Calendar.MONTH)) {
					month.set((month.get(Calendar.YEAR) - 1),
							month.getActualMaximum(Calendar.MONTH), 1);
				} else {
					month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
				}
				refreshCalendar();
			}
		});

		TextView next = (TextView) findViewById(R.id.task_timeset_next);
		next.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (month.get(Calendar.MONTH) == month
						.getActualMaximum(Calendar.MONTH)) {
					month.set((month.get(Calendar.YEAR) + 1),
							month.getActualMinimum(Calendar.MONTH), 1);
				} else {
					month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
				}
				refreshCalendar();

			}
		});

		itemUsedToRecord = (LinearLayout) findViewById(R.id.task_timeset_calendar_item);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				/*itemUsedToRecord.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.task_timeset_item_background));*/
//				refreshCalendar();
//				Log.d(TAG, ""+month.get(Calendar.DAY_OF_MONTH)); 
//				Log.d(TAG, ""+position);
				TextView date = (TextView) v
						.findViewById(R.id.task_timeset_date);
//				LinearLayout it = (LinearLayout) parent.findViewWithTag(tag);
				if (date instanceof TextView && !date.getText().equals("")) {

					String day = date.getText().toString();
					if (day.length() == 1) {
						day = "0" + day;
					}
					// return chosen date as string format
					resultDate = android.text.format.DateFormat.format(
							"yyyy-MM", month) + "-" + day;
					// Log.d(TAG, resultDate); 2012-03-28
				}
				mItem = (LinearLayout) v
						.findViewById(R.id.task_timeset_calendar_item);
				mItem.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.task_timeset_item_background_clicked));
				/*itemUsedToRecord = (LinearLayout) v
						.findViewById(R.id.task_timeset_calendar_item);*/
			}
		});

		timePicker = (TimePicker) findViewById(R.id.task_timeset_timePicker);
		timePicker.setIs24HourView(true);

		Button confirmBtn = (Button) findViewById(R.id.task_timeset_confirm_btn);
		Button cancelBtn = (Button) findViewById(R.id.task_timeset_cancel_btn);

		confirmBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("date", resultDate);
//				Log.d(TAG, resultDate);
				int h = timePicker.getCurrentHour();
				int m = timePicker.getCurrentMinute();
				String time = "" + h + ":" + m;
//				Log.d(TAG, time);
				intent.putExtra("time", time);

				if (getParent() == null) {
					setResult(Activity.RESULT_OK, intent);
				} else {
					getParent().setResult(Activity.RESULT_OK, intent);
				}
				// setResult(RESULT_OK, intent);
				finish();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public void refreshCalendar() {
		TextView title = (TextView) findViewById(R.id.task_timeset_title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public void onNewIntent(Intent intent) {
		String date = intent.getStringExtra("date");
		String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
		month.set(Integer.parseInt(dateArr[0]),
				Integer.parseInt(dateArr[1]) - 1, Integer.parseInt(dateArr[2]));
		resultDate = date;
		// Log.d(TAG, "on New Intent" + resultDate);
	}

}
