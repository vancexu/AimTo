package com.hackingtrace.vancexu;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hackingtrace.vancexu.aim.AimToAimActivity;
import com.hackingtrace.vancexu.chart.ChartActivity;
import com.hackingtrace.vancexu.help.AimToHelpActivity;
import com.hackingtrace.vancexu.note.AimToNoteActivity;
import com.hackingtrace.vancexu.task.AimToTaskActivity;

public class AimToActivity extends TabActivity {
	private static final String TAG = "AimTo";
	
	private TabHost mTabHost;
	private ViewFlipper mFlipper; // slogan animation
	
	private ImageButton helpBtn;
	private ImageButton exitBtn;
	
	private static final int SWIPE_MIN_DISTANCE = 180; // conflict with chart
														// gesture
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipperBody; // the tabhost body ViewFlipper

	int currentView = 0;
	int currentPosition = 0; // mark the previous position
	private static int maxTabIndex = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* slogan begin */
		mFlipper = ((ViewFlipper) this.findViewById(R.id.head_flipper));
		mFlipper.startFlipping();
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		/* slogan done */

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		Intent i;
		i = new Intent().setClass(this, AimToAimActivity.class);
		setupTab(new TextView(this), "Aim", i);
		i = new Intent().setClass(this, AimToTaskActivity.class);
		setupTab(new TextView(this), "Task", i);
		i = new Intent().setClass(this, AimToNoteActivity.class);
		setupTab(new TextView(this), "Note", i);
		i = new Intent().setClass(this, ChartActivity.class);
		setupTab(new TextView(this), "Chart", i);

		/* body animation */
		viewFlipperBody = (ViewFlipper) mTabHost.getTabContentView();

		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils
				.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils
				.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_right_out);

		gestureDetector = new GestureDetector(new MyGestureDetector());

		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};

//		currentPosition = mTabHost.getCurrentTab();

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
//				viewFlipperBody = (ViewFlipper) mTabHost.getTabContentView();
//				int a = viewFlipperBody.getChildCount();
//				Log.d(TAG, ""+a);
				currentView = mTabHost.getCurrentTab();
//				Log.d("Tab position", "" + currentView + " " + currentPosition);
				viewFlipperBody.setDisplayedChild(currentView);
				int times = currentView - currentPosition;
				if (times > 0) {
					viewFlipperBody.setInAnimation(slideLeftIn);
					viewFlipperBody.setOutAnimation(slideLeftOut);
					viewFlipperBody.setDisplayedChild(currentView);
					 
				}
				if (times < 0) {
					viewFlipperBody.setInAnimation(slideRightIn);
					viewFlipperBody.setOutAnimation(slideRightOut);
					viewFlipperBody.setDisplayedChild(currentView);
				}
				currentPosition = currentView;
			}
		});
		
		helpBtn = (ImageButton) findViewById(R.id.help);
		exitBtn = (ImageButton) findViewById(R.id.exit);

		helpBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(v.getContext(), AimToHelpActivity.class);
				startActivity(intent);
			}
		});
		
		exitBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setupTab(final View view, final String tag, final Intent i) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(i);
		/*
		 * TabSpec setContent =
		 * mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(new
		 * TabContentFactory() { public View createTabContent(String tag)
		 * {return view;} });
		 */
		mTabHost.addTab(setContent);
		// viewFlipper.addView(tabview);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			TabHost tabHost = getTabHost();
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe, tab change to right
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i("Gesture ", "right");
					if (currentView == maxTabIndex) {
						currentView = 0;
					} else {
						currentView++;
					}
					viewFlipperBody.setInAnimation(slideLeftIn);
					viewFlipperBody.setOutAnimation(slideLeftOut);
					viewFlipperBody.setDisplayedChild(currentView);

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i("Gesture ", "left");
					if (currentView == 0) {
						currentView = maxTabIndex;
					} else {
						currentView--;
					}
					viewFlipperBody.setInAnimation(slideRightIn);
					viewFlipperBody.setOutAnimation(slideRightOut);
					viewFlipperBody.setDisplayedChild(currentView);
				}
			} catch (Exception e) {
				// nothing
				e.printStackTrace();
			}
			tabHost.setCurrentTab(currentView);
			return false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}
}