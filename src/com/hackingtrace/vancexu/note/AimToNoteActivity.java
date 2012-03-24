package com.hackingtrace.vancexu.note;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.hackingtrace.vancexu.R;
import com.hackingtrace.vancexu.aim.AimCustomizedCursorAdapter;

public class AimToNoteActivity extends ListActivity{
	private static final String TAG = "AimToNote";
	
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
	
    private NotesDbAdapter mDbHelper;
    private Cursor mNotesCursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
        
    }
    
    private void fillData() {
        mNotesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(mNotesCursor);

        CursorAdapter adapter = new NoteCustomizedCursorAdapter(this, mNotesCursor);
		this.setListAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.note_menu_insert);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.note_menu_delete);
        menu.add(0, EDIT_ID, 0, R.string.note_menu_edit);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
            case EDIT_ID:
            	AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item.getMenuInfo();
            	Intent i = new Intent(this, NoteEdit.class);
            	i.putExtra(NotesDbAdapter.KEY_ROWID, info1.id);
            	startActivityForResult(i, ACTIVITY_EDIT);
            	return true;
        }
        return super.onContextItemSelected(item);
    }
    
    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Intent i = new Intent(this, NoteWatch.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
    	startActivity(i);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        if (requestCode == ACTIVITY_CREATE) {
        	if(resultCode == RESULT_OK) {
        		Bundle extras = intent.getExtras();
        		String title = extras.getString(NotesDbAdapter.KEY_TITLE);
            	String body = extras.getString(NotesDbAdapter.KEY_BODY);
            	String time = extras.getString(NotesDbAdapter.KEY_TIME);
            	String state = extras.getString(NotesDbAdapter.KEY_STATE);
            	mDbHelper.createNote(title, body, state, time);
        	}
        	if(resultCode == RESULT_CANCELED){
        		
        	}
        }
        if (requestCode == ACTIVITY_EDIT) {
        	if(resultCode == RESULT_OK) {
        		Bundle extras = intent.getExtras();
	        	Long mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
	        	if (mRowId != null) {
	        		String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
	        		String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
	        		String editTime = extras.getString(NotesDbAdapter.KEY_TIME);
	        		String editState = extras.getString(NotesDbAdapter.KEY_STATE);
	        		mDbHelper.updateNote(mRowId, editTitle, editBody, editState, editTime);
	        	}
        	}
        }
        fillData();	
    }
}