package com.hackingtrace.vancexu;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import android.widget.ViewFlipper;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.hackingtrace.vancexu.aim.AimToAimActivity;
import com.hackingtrace.vancexu.chart.ChartActivity;
import com.hackingtrace.vancexu.task.AimToTaskActivity;

public class AimToActivity extends TabActivity {
	private TabHost mTabHost;
	private ViewFlipper mFlipper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mFlipper = ((ViewFlipper) this.findViewById(R.id.head_flipper));
		mFlipper.startFlipping();
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		Intent i;
		i = new Intent().setClass(this, AimToAimActivity.class);
		setupTab(new TextView(this), "Aim", i);
		i = new Intent().setClass(this, AimToTaskActivity.class);
		setupTab(new TextView(this), "Task", i);
		i = new Intent().setClass(this, ChartActivity.class);
		setupTab(new TextView(this), "Chart", i);
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
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
}