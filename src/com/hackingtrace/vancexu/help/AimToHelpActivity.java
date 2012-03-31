package com.hackingtrace.vancexu.help;

import com.hackingtrace.vancexu.R;
import com.hackingtrace.vancexu.help.HelpScrollLayout;
import com.hackingtrace.vancexu.help.OnViewChangeListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AimToHelpActivity extends Activity implements OnViewChangeListener, OnClickListener{
    /** Called when the activity is first created. */

	private HelpScrollLayout mScrollLayout;	
	private ImageView[] mImageViews;	
	private int mViewCount;	
	private int mCurSel;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        init();
        Log.v("@@@@@@", "this is in  SwitchViewDemoActivity onClick()");
    }
    
    private void init()
    {
    	mScrollLayout = (HelpScrollLayout) findViewById(R.id.ScrollLayout); 	
    	LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llayout);   	
    	mViewCount = mScrollLayout.getChildCount();
    	mImageViews = new ImageView[mViewCount];   	
    	for(int i = 0; i < mViewCount; i++)    	{
    		mImageViews[i] = (ImageView) linearLayout.getChildAt(i);
    		mImageViews[i].setEnabled(true);
    		mImageViews[i].setOnClickListener(this);
    		mImageViews[i].setTag(i);
    	}    	
    	mCurSel = 0;
    	mImageViews[mCurSel].setEnabled(false);    	
    	mScrollLayout.SetOnViewChangeListener(this);
    	Log.v("@@@@@@", "this is in  SwitchViewDemoActivity init()");
    }

    private void setCurPoint(int index)
    {
    	if (index < 0 || index > mViewCount - 1 || mCurSel == index)    	{
    		return ;
    	}    	
    	mImageViews[mCurSel].setEnabled(true);
    	mImageViews[index].setEnabled(false);    	
    	mCurSel = index;
    }

    public void OnViewChange(int view) {
		// TODO Auto-generated method stub
		setCurPoint(view);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = (Integer)(v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}
}