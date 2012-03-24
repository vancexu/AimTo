package com.hackingtrace.vancexu.aim;

import java.util.Calendar;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.hackingtrace.vancexu.R;
import com.hackingtrace.vancexu.date.Date;

public class AimWatch extends Activity {
	private static final String TAG = "AimWatch";
	private TextView mBody;
	private TextView mTitle;
	private TextView mTarget;
	private TextView mRemain;
	private String mDate;
	private Long mRowId;
	private AimsDbAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aim_watch);
		mTitle = (TextView) findViewById(R.id.aim_title_watch);
		mTarget = (TextView) findViewById(R.id.aim_watch_targetdate);
		mRemain = (TextView) findViewById(R.id.aim_watch_daysremain);
		mBody = (TextView) findViewById(R.id.aim_watch_body);
		mBody.setMovementMethod(new ScrollingMovementMethod());

		mDbHelper = new AimsDbAdapter(this);
		mDbHelper.open();

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(AimsDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(AimsDbAdapter.KEY_ROWID)
					: null;
		}
		populateFields();
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor Aim = mDbHelper.fetchAim(mRowId);
			startManagingCursor(Aim);
			mTitle.setText(Aim.getString(Aim
					.getColumnIndexOrThrow(AimsDbAdapter.KEY_TITLE)));
			mBody.setText(Aim.getString(Aim
					.getColumnIndexOrThrow(AimsDbAdapter.KEY_BODY)));
			String date = Aim.getString(
					Aim.getColumnIndexOrThrow(AimsDbAdapter.KEY_DATE));
			mTarget.setText("Due Date: " + date);
			String dates[] = date.split("-");
			int y = Integer.parseInt(dates[0]);
			int m = Integer.parseInt(dates[1]) - 1;
			int d = Integer.parseInt(dates[2]);
			Date target = new Date(y, m, d);
			Calendar c = Calendar.getInstance();
			Date now = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
			int diff = Date.diff(target, now);
			mRemain.setText("Remain days: " + Integer.toString(diff));
		}
	}
}
