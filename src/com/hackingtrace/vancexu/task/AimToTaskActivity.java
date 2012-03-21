package com.hackingtrace.vancexu.task;



import com.hackingtrace.vancexu.R;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class AimToTaskActivity extends ListActivity {
	private static final String TAG = "AimToTask";
	
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
	
    private TasksDbAdapter mDbHelper;
    private Cursor mTasksCursor;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        mDbHelper = new TasksDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
        
        final Button addbutton = (Button) findViewById(R.id.task_addbutton);
        final EditText edittext = (EditText) findViewById(R.id.task_edittext);
        
        addbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	String title = edittext.getText().toString();
            	String body = "";
            	mDbHelper.createTask(title, body, "false");
            	fillData();
            	edittext.setText("");
            }
        });
    }
    
    private void fillData() {
        // Get all of the rows from the database and create the item list
        mTasksCursor = mDbHelper.fetchAllTasks();
        startManagingCursor(mTasksCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{TasksDbAdapter.KEY_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.task_text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter tasks = 
            new SimpleCursorAdapter(this, R.layout.task_row, mTasksCursor, from, to);
        
        final SimpleCursorAdapter.ViewBinder mViewBinder = 
        		new SimpleCursorAdapter.ViewBinder() {
        	public boolean setViewValue(
        			final View view, 
        			final Cursor cursor, 
        			final int columnIndex) {

                             final int checkedIndex =
                                 cursor.getColumnIndexOrThrow(
                                         TasksDbAdapter.KEY_STATE);
                             String state = cursor.getString(checkedIndex);
                             if (state.equals("true")) {
                            	 CheckedTextView ctw = (CheckedTextView)view;
                            	 ctw.setChecked(true);
                            	 ctw.setTextColor(Color.GRAY);
                            	 //Log.d("ISCHECKED","?true");
                             }

                             //Log.d("LIST","VIEW: "+view+" NAME: "+cursor.getString(columnIndex)+" "+cursor.getString(checkedIndex));

                            return false;
                        }
                    };
        tasks.setViewBinder(mViewBinder);
        
        setListAdapter(tasks);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.task_menu_insert);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createTask();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.task_menu_delete);
        menu.add(0, EDIT_ID, 0, R.string.task_menu_edit);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteTask(info.id);
                fillData();
                return true;
            case EDIT_ID:
            	AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item.getMenuInfo();
            	Intent i = new Intent(this, TaskEdit.class);
            	i.putExtra(TasksDbAdapter.KEY_ROWID, info1.id);
            	startActivityForResult(i, ACTIVITY_EDIT);
            	return true;
        }
        return super.onContextItemSelected(item);
    }
    
    private void createTask() {
        Intent i = new Intent(this, TaskEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        String state;
        CheckedTextView ctv = (CheckedTextView)v;
        ctv.toggle();
        if(ctv.isChecked()){
        	ctv.setTextColor(getResources().getColor(R.color.task_list_done));
        	state = "true";	
        }else {
        	ctv.setTextColor(getResources().getColor(R.color.task_list_normal));
        	state = "false";
        }
        Long mRowId = id;
        //Log.d(TAG,state+' '+mRowId);
    	if (mRowId != null) {
    		mDbHelper.updateState(mRowId, state);
    	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}