package com.hackingtrace.vancexu.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_STATE = "state"; // 1-5
	public static final String KEY_TIME = "time"; // record time
	public static final String KEY_ROWID = "_id";

	private static final String TAG = "NotesDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE = "create table notes (_id integer primary key autoincrement, "
			+ "title text not null, body text not null, state text not null, time text not null);";

	private static final String DATABASE_NAME = "note_data";
	private static final String DATABASE_TABLE = "notes";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	public NotesDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public NotesDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createNote(String title, String body, String state) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		initialValues.put(KEY_STATE, state);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public long createNote(String title, String body, String state, String time) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		initialValues.put(KEY_STATE, state);
		initialValues.put(KEY_TIME, time);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteNote(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllNotes() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_STATE, KEY_TIME }, null, null, null, null, null);
	}

	public Cursor fetchAllNotesTime() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_TIME }, null, null,
				null, null, null);
	}
	
	public Cursor fetchAllNotesState() {
		
		return mDb.query(DATABASE_TABLE, new String[] { KEY_STATE }, null, null,
				null, null, null);
	}

	public Cursor fetchNote(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_STATE, KEY_TIME }, KEY_ROWID + "=" + rowId, null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updateNote(long rowId, String title, String body,
			String state) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		args.put(KEY_STATE, state);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateNote(long rowId, String title, String body,
			String state, String time) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		args.put(KEY_STATE, state);
		args.put(KEY_TIME, time);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateState(long rowId, String state) {
		ContentValues args = new ContentValues();
		args.put(KEY_STATE, state);
		// Log.d(TAG,"In update");
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateTime(long rowId, String time) {
		ContentValues args = new ContentValues();
		args.put(KEY_TIME, time);
		// Log.d(TAG,"In update");
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
