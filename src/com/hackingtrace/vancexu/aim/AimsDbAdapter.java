package com.hackingtrace.vancexu.aim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AimsDbAdapter {
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_STATE = "state";
	public static final String KEY_DATE = "date"; //eg:2012-1-1
	public static final String KEY_ROWID = "_id";

	private static final String TAG = "AimsDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE = "create table aims (_id integer primary key autoincrement, "
			+ "title text not null, body text not null, state text not null, date text not null);";

	private static final String DATABASE_NAME = "aim_data";
	private static final String DATABASE_TABLE = "aims";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "I am create");
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS Aims");
			onCreate(db);
		}
	}

	public AimsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public AimsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createAim(String title, String body, String state, String date) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		initialValues.put(KEY_STATE, state);
		initialValues.put(KEY_DATE, date);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteAim(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllAims() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_BODY, KEY_STATE, KEY_DATE }, null, null, null, null, null);
	}

	public Cursor fetchAim(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_STATE, KEY_DATE },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updateAim(long rowId, String title, String body,
			String state, String date) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		args.put(KEY_STATE, state);
		args.put(KEY_DATE, date);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateState(long rowId, String state) {
		ContentValues args = new ContentValues();
		args.put(KEY_STATE, state);
		// Log.d(TAG,"In update");
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateDate(long rowId, String date) {
		ContentValues args = new ContentValues();
		args.put(KEY_DATE, date);
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
