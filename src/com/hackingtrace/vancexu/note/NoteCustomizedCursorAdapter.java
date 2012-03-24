package com.hackingtrace.vancexu.note;


import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.hackingtrace.vancexu.R;

public class NoteCustomizedCursorAdapter extends CursorAdapter {
	private static final String TAG = "NoteCustomizedCursorAdapter";
	private final int MAX_BODY_LENGTH_TO_SHOW = 60; 

	public NoteCustomizedCursorAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		TextView title = (TextView) view.findViewById(R.id.note_row_title);
		TextView body = (TextView) view.findViewById(R.id.note_row_body);
		
		title.setText(cursor.getString(1));
		
		String sTime = cursor.getString(4);
		String sBody = cursor.getString(2);
		int bodyLength = sBody.length();
		if(bodyLength > MAX_BODY_LENGTH_TO_SHOW) {
			sBody = sBody.substring(0, MAX_BODY_LENGTH_TO_SHOW);
		}
//		SpannableString text = new SpannableString(sTime+sBody);
//		text.setSpan(new , start, end, flags)
		body.setText(Html.fromHtml("<b>"+sTime+"</b>"+" "+sBody));
		Log.d(TAG, "Im in bindView"+title.getText());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.note_row, parent, false);
		TextView title = (TextView) view.findViewById(R.id.note_row_title);
		TextView body = (TextView) view.findViewById(R.id.note_row_body);
		
		title.setText(cursor.getString(1));
		
		String sTime = cursor.getString(4);
		String sBody = cursor.getString(2);
		int bodyLength = sBody.length();
		if(bodyLength > MAX_BODY_LENGTH_TO_SHOW) {
			sBody = sBody.substring(0, MAX_BODY_LENGTH_TO_SHOW);
		}
//		SpannableString text = new SpannableString(sTime+sBody);
//		text.setSpan(new , start, end, flags)
		body.setText(Html.fromHtml("<b>"+sTime+"</b>"+" "+sBody));
		Log.d(TAG,"Im in newView"+title.getText());

		return view;
	}

}
