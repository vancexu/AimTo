package com.hackingtrace.vancexu.aim;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;

import com.hackingtrace.vancexu.R;
import com.hackingtrace.vancexu.date.Date;

public class AimCustomizedCursorAdapter extends CursorAdapter{
	private static final String TAG = "AimCustomizedCursorAdapter";

	public AimCustomizedCursorAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		Button dayRemains = (Button)view.findViewById(R.id.aim_remain1);
		CheckedTextView chkText = (CheckedTextView)view.findViewById(R.id.aim_chktext1);
		String dates[] = cursor.getString(4).split("-");
		int y = Integer.parseInt(dates[0]);
		int m = Integer.parseInt(dates[1]) - 1;
		int d = Integer.parseInt(dates[2]);
		Date targetDate = new Date(y, m, d);
		Calendar c = Calendar.getInstance();
		Date now = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
//		Log.d(TAG, now.toString());
		int diff = Date.diff(targetDate, now);
		
		final int checkedIndex = cursor.getColumnIndexOrThrow(AimsDbAdapter.KEY_STATE);
		String state = cursor.getString(checkedIndex);
		if (state.equals("true")) {
			chkText.setChecked(true);
			chkText.setTextColor(Color.GRAY);
			// Log.d("ISCHECKED","?true");
		}
		
		dayRemains.setText(Integer.toString(diff));
		chkText.setText(cursor.getString(1));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.aim_row, parent, false);
		Button dayRemains = (Button)view.findViewById(R.id.aim_remain1);
		CheckedTextView chkText = (CheckedTextView)view.findViewById(R.id.aim_chktext1);
		String dates[] = cursor.getString(4).split("-");
		int y = Integer.parseInt(dates[0]);
		int m = Integer.parseInt(dates[1]) - 1;
		int d = Integer.parseInt(dates[2]);
		Date targetDate = new Date(y, m, d);
		Calendar c = Calendar.getInstance();
		Date now = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
		int diff = Date.diff(targetDate, now);
		
		final int checkedIndex = cursor.getColumnIndexOrThrow(AimsDbAdapter.KEY_STATE);
		String state = cursor.getString(checkedIndex);
		if (state.equals("true")) {
			chkText.setChecked(true);
			chkText.setTextColor(Color.GRAY);
			chkText.setPaintFlags(chkText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}
		
		dayRemains.setText(Integer.toString(diff));
		chkText.setText(cursor.getString(1));
		
		return view;
	}

}
