package com.hackingtrace.vancexu.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TasksDbAdapter {
	public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_STATE = "state";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "TasksDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_CREATE =
            "create table tasks (_id integer primary key autoincrement, "
            + "title text not null, body text not null, state text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "tasks";
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
            db.execSQL("DROP TABLE IF EXISTS tasks");
            onCreate(db);
        }
    }
    
    public TasksDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    public TasksDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }
    
    public long createTask(String title, String body, String state) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_STATE, state);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean deleteTask(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public Cursor fetchAllTasks() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_STATE}, null, null, null, null, null);
    }
    
    public Cursor fetchTask(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_TITLE, KEY_BODY, KEY_STATE}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public boolean updateTask(long rowId, String title, String body, String state) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(KEY_STATE, state);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean updateState(long rowId, String state) {
    	ContentValues args = new ContentValues();
        args.put(KEY_STATE, state);
        //Log.d(TAG,"In update");
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;        
    }
}
