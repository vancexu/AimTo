package com.hackingtrace.vancexu.note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.hackingtrace.vancexu.R;

public class NoteEdit extends Activity {
	private static final String TAG = "NoteEdit";
	private EditText mTitleText;
	private EditText mBodyText;
	private RatingBar mRatingbar;
	private Long mRowId;
	private float mRating;
	private String mTime;
	private NotesDbAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit);
		setTitle(R.string.edit_note);

		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();

		mTitleText = (EditText) findViewById(R.id.note_edit_title);
		mBodyText = (EditText) findViewById(R.id.note_edit_body);
		mRatingbar = (RatingBar) findViewById(R.id.note_edit_ratingbar);

		Button confirmButton = (Button) findViewById(R.id.note_edit_confirm);
		Button cancelButton = (Button) findViewById(R.id.note_edit_cancel);

		mRowId = (savedInstanceState == null) ? null
				: (Long) savedInstanceState
						.getSerializable(NotesDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
					: null;
		}

		populateFields();

		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString(NotesDbAdapter.KEY_TITLE, mTitleText.getText()
						.toString());
				bundle.putString(NotesDbAdapter.KEY_BODY, mBodyText.getText()
						.toString());
				bundle.putString(NotesDbAdapter.KEY_STATE, String.valueOf(mRating));
				bundle.putString(NotesDbAdapter.KEY_TIME, mTime);
				if (mRowId != null) {
					bundle.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
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
		
		mRating = (float) 0.0;
		mRatingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
		    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		    	mRating = rating;
		    }
		});
		
		Calendar c = Calendar.getInstance();
		Date d = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sdf.format(d);
		mTime = now;
	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor Note = mDbHelper.fetchNote(mRowId);
			startManagingCursor(Note);
			mTitleText.setText(Note.getString(Note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
			mBodyText.setText(Note.getString(Note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
			mRatingbar.setRating(Float.parseFloat(Note.getString(Note
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_STATE))));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
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
//		Calendar c = Calendar.getInstance();
//		Date d = c.getTime();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String now = sdf.format(d);
		String time = String.valueOf(mRating);

		if (mRowId == null) {
			long id = mDbHelper.createNote(title, body, time, mTime);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateNote(mRowId, title, body, time, mTime);
		}
	}
}
