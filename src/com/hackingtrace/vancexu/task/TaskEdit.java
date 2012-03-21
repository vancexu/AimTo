package com.hackingtrace.vancexu.task;


import com.hackingtrace.vancexu.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TaskEdit extends Activity {
	private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private TasksDbAdapter mDbHelper;
    
    private static final int ACTIVITY_EDIT_TIME=0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);
        setTitle(R.string.edit_task);
        
        mDbHelper = new TasksDbAdapter(this);
        mDbHelper.open();

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        Button setTimeButton = (Button) findViewById(R.id.task_edit_set_time);

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(TasksDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(TasksDbAdapter.KEY_ROWID)
                                    : null;
        }
        
        populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
        
        /*cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //setResult(RESULT_CANCELED);
                finish();
            }

        });*/
        setTimeButton.setOnClickListener(new View.OnClickListener() {
        	
        	public void onClick(View view) {
        		Intent i = new Intent(view.getContext(), TaskTimeSet.class);
            	i.putExtra(TasksDbAdapter.KEY_ROWID, mRowId);
            	i.putExtra("date", "2012-03-21");
            	startActivityForResult(i, ACTIVITY_EDIT_TIME);
        	}
        	
        });
    }
    
    private void populateFields() {
        if (mRowId != null) {
            Cursor Task = mDbHelper.fetchTask(mRowId);
            startManagingCursor(Task);
            mTitleText.setText(Task.getString(
                        Task.getColumnIndexOrThrow(TasksDbAdapter.KEY_TITLE)));
            mBodyText.setText(Task.getString(
                    Task.getColumnIndexOrThrow(TasksDbAdapter.KEY_BODY)));
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(TasksDbAdapter.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    
    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (mRowId == null) {
            long id = mDbHelper.createTask(title, body, "false");
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateTask(mRowId, title, body, "false");
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
