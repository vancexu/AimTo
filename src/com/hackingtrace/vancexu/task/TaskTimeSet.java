package com.hackingtrace.vancexu.task;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.hackingtrace.vancexu.R;

public class TaskTimeSet extends Activity {
	private Long mRowId;
	private TasksDbAdapter mDbHelper;
	private Calendar month;
	private CalendarAdapter adapter;

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

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				TextView date = (TextView) v
						.findViewById(R.id.task_timeset_date);
				if (date instanceof TextView && !date.getText().equals("")) {

					Intent intent = new Intent();
					String day = date.getText().toString();
					if (day.length() == 1) {
						day = "0" + day;
					}
					// return chosen date as string format
					intent.putExtra(
							"date",
							android.text.format.DateFormat.format("yyyy-MM",
									month) + "-" + day);
					setResult(RESULT_OK, intent);
					finish();
				}

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
		month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]),
				Integer.parseInt(dateArr[2]));
	}

}
