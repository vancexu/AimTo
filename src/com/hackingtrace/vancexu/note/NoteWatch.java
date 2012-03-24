package com.hackingtrace.vancexu.note;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.hackingtrace.vancexu.R;

public class NoteWatch extends Activity {
	private static final String TAG = "NoteWatch";
	private TextView mBody;
	private TextView mTitle;
	private TextView mTime;
	private Long mRowId;
	private NotesDbAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_watch);
		mTitle = (TextView) findViewById(R.id.note_watch_title);
		mTime = (TextView) findViewById(R.id.note_watch_time);
		mBody = (TextView) findViewById(R.id.note_watch_body);
		mBody.setMovementMethod(new ScrollingMovementMethod());

		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(NotesDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
					: null;
		}
		populateFields();
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor Note = mDbHelper.fetchNote(mRowId);
			startManagingCursor(Note);
			mTitle.setText(Note.getString(Note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
			mTime.setText("ʱ�䣺" + Note.getString(Note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_TIME)));
			mBody.setText(Note.getString(Note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
		}
	}
}

