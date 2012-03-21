package com.hackingtrace.vancexu.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackingtrace.vancexu.R;

public class CalendarHeadAdapter extends BaseAdapter{
	
	private Context mContext;
	
	public CalendarHeadAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		TextView dayView;
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.task_timeset_calendar_item, null);

		}
		dayView = (TextView) v.findViewById(R.id.task_timeset_date);
		dayView.setClickable(false);
		dayView.setFocusable(false);
		dayView.setText(days[position]);
		return v;
	}
	
	private String[] days = {
			"周日", "周一", "周二", "周三", "周四", "周五", "周六"
	};

}
