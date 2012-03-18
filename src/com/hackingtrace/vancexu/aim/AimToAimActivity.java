package com.hackingtrace.vancexu.aim;


import com.hackingtrace.vancexu.R;

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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AimToAimActivity extends ListActivity {
	private static final String TAG = "AimToAim";

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int EDIT_ID = Menu.FIRST + 2;
	private static final int WATCH_ID = Menu.FIRST + 3;

	private AimsDbAdapter mDbHelper;
	private Cursor mAimsCursor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aim_list);
		mDbHelper = new AimsDbAdapter(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
//		Log.d(TAG, "on create");
	}

	private void fillData() {
		// Get all of the rows from the database and create the item list
		mAimsCursor = mDbHelper.fetchAllAims();
		startManagingCursor(mAimsCursor);
		
		CursorAdapter adapter = new AimCustomizedCursorAdapter(this, mAimsCursor);
		this.setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.aim_menu_insert);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			createAim();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.aim_menu_delete);
		menu.add(0, EDIT_ID, 0, R.string.aim_menu_edit);
		menu.add(0, WATCH_ID, 0, R.string.aim_menu_watch);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			mDbHelper.deleteAim(info.id);
			fillData();
			return true;
		case EDIT_ID:
			AdapterContextMenuInfo info1 = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Intent i1 = new Intent(this, AimEdit.class);
			i1.putExtra(AimsDbAdapter.KEY_ROWID, info1.id);
			startActivityForResult(i1, ACTIVITY_EDIT);
			return true;
		case WATCH_ID:
			AdapterContextMenuInfo info2 = (AdapterContextMenuInfo) item
			.getMenuInfo();
			Intent i2 = new Intent(this, AimWatch.class);
			i2.putExtra(AimsDbAdapter.KEY_ROWID, info2.id);
			startActivityForResult(i2, ACTIVITY_EDIT);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void createAim() {
		Intent i = new Intent(this, AimEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String state;
		CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.aim_chktext1);
		ctv.toggle();
		if (ctv.isChecked()) {
			ctv.setTextColor(Color.GRAY);
			state = "true";
		} else {
			ctv.setTextColor(Color.WHITE);
			state = "false";
		}
		Long mRowId = id;
		// Log.d(TAG,state+' '+mRowId);
		if (mRowId != null) {
			mDbHelper.updateState(mRowId, state);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}
}