package com.hackingtrace.vancexu.aim;

import java.util.Calendar;

import com.hackingtrace.vancexu.R;
import com.hackingtrace.vancexu.task.TasksDbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AimEdit extends Activity {
	private static final String TAG = "AimEdit";
	private EditText mTitleText;
	private EditText mBodyText;
	private DatePicker mDatePicker;
	private String mDate;
	private Long mRowId;
	private AimsDbAdapter mDbHelper;
	private DatePicker.OnDateChangedListener dateSetListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aim_edit);
		setTitle(R.string.edit_aim);

		mDbHelper = new AimsDbAdapter(this);
		mDbHelper.open();

		mTitleText = (EditText) findViewById(R.id.aim_edit_title);
		mBodyText = (EditText) findViewById(R.id.aim_edit_body);
		try {
			mDatePicker = (DatePicker) findViewById(R.id.aim_edit_datePicker);
		} catch (ClassCastException e) {
			Log.e(TAG, "mDatePicker exception");
		}

		Button confirmButton = (Button) findViewById(R.id.aim_edit_confirm);
		Button cancelButton = (Button) findViewById(R.id.aim_edit_cancel);

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(AimsDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(AimsDbAdapter.KEY_ROWID)
					: null;
		}

		populateFields();

		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString(AimsDbAdapter.KEY_TITLE, mTitleText.getText()
						.toString());
				bundle.putString(AimsDbAdapter.KEY_BODY, mBodyText.getText()
						.toString());
				int d = mDatePicker.getDayOfMonth();
				int m = mDatePicker.getMonth() + 1;
				int y = mDatePicker.getYear();
				mDate = Integer.toString(y) + "-" + Integer.toString(m) + "-"
						+ Integer.toString(d);
				bundle.putString(AimsDbAdapter.KEY_DATE, mDate);
				if (mRowId != null) {
					bundle.putLong(AimsDbAdapter.KEY_ROWID, mRowId);
				}

				Intent mIntent = new Intent();
				mIntent.putExtras(bundle);
				setResult(RESULT_OK, mIntent);
				finish();
			}

		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}

		});

		dateSetListener = new DatePicker.OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
			}
		};
		
		Calendar c = Calendar.getInstance();
		mDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), dateSetListener);
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor Aim = mDbHelper.fetchAim(mRowId);
			startManagingCursor(Aim);
			mTitleText.setText(Aim.getString(Aim
					.getColumnIndexOrThrow(AimsDbAdapter.KEY_TITLE)));
			mBodyText.setText(Aim.getString(Aim
					.getColumnIndexOrThrow(AimsDbAdapter.KEY_BODY)));
			String[] dates = Aim.getString(
					Aim.getColumnIndexOrThrow(AimsDbAdapter.KEY_DATE)).split(
					"-");
			int y = Integer.parseInt(dates[0]);
			int m = Integer.parseInt(dates[1]) - 1;
			int d = Integer.parseInt(dates[2]);
			mDatePicker.init(y, m, d, dateSetListener);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(AimsDbAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();
		int d = mDatePicker.getDayOfMonth();
		int m = mDatePicker.getMonth() + 1;
		int y = mDatePicker.getYear();
		mDate = Integer.toString(y) + "-" + Integer.toString(m) + "-"
				+ Integer.toString(d);
//		Log.d(TAG, mDate);

		if (mRowId == null) {
			long id = mDbHelper.createAim(title, body, "false", mDate);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateAim(mRowId, title, body, "false", mDate);
		}
	}
}
