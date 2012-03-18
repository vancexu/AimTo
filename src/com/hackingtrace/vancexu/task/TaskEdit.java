package com.hackingtrace.vancexu.task;


import com.hackingtrace.vancexu.R;

import android.app.Activity;
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
        Button cancelButton = (Button) findViewById(R.id.cancel);

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
        
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //setResult(RESULT_CANCELED);
                finish();
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
}
