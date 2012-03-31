package com.hackingtrace.vancexu.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hackingtrace.vancexu.R;
import com.hackingtrace.vancexu.note.NotesDbAdapter;

public class ChartActivity extends Activity {
	private static final String TAG = "CHART";
	private NotesDbAdapter mDbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();

		Cursor cTimes = mDbHelper.fetchAllNotesTime();
		startManagingCursor(cTimes);
		ArrayList<String> timeList = new ArrayList<String>();
		for (cTimes.moveToFirst(); !cTimes.isAfterLast(); cTimes.moveToNext()) {
			timeList.add(cTimes.getString(cTimes
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_TIME)));
		}

		Cursor cStates = mDbHelper.fetchAllNotesState();
		startManagingCursor(cStates);
		ArrayList<String> stateList = new ArrayList<String>();
		for (cStates.moveToFirst(); !cStates.isAfterLast(); cStates
				.moveToNext()) {
			stateList.add(cStates.getString(cStates
					.getColumnIndexOrThrow(NotesDbAdapter.KEY_STATE)));
		}

		String[] titles = new String[] { "STATE", };
		String[] time = new String[] { "2011/1/1", "2011/1/2", "2011/1/3",
				"2011/1/4", "2011/1/5", "2011/1/6", "2011/1/7", "2011/1/8" };
		int timeSize = timeList.size();

		List x = new ArrayList();
		List y = new ArrayList();
		double[] xD = new double[timeSize];
		double[] yD = new double[timeSize];
		// String[] time = new String[timeSize];
		// String[] state = new String[timeSize];
		for (int i = 0; i < timeSize; ++i) {
			time[i] = timeList.get(i);
			xD[i] = (double) (2 * i + 1); // distance
			yD[i] = (double) (Double.parseDouble(stateList.get(i)));
		}
		x.add(xD);
		y.add(yD);
		// Log.d(TAG, time[0]+time[1]);

		// 这里是x轴的数据；
		// Double[] xLine = new Double[] {};
		// x.add(new double[] { 1, 3, 5, 7, 9, 11 ,13 ,15,17,19,21} );
		// x.add(new double[] { 1, 3, 5, 7, 9, 11,13 ,15,17,19,21} );

		// 这里的y轴数据和x轴数据一一对应。
		// y.add(new double[] { 1, 3, 5, 2, 2, 4, 3, 2,3,4,4});
		// y.add(new double[] { 1, 5, 3, 3, 4, 3, 2, 1,3,4,4});

		// 这里的参数是曲线名数组，x,y轴数组；
		XYMultipleSeriesDataset dataset = buildDataset(titles, x, y);

		int[] colors = new int[] { Color.BLUE };

		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };

		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles, true);

		renderer.setXLabels(0);
		renderer.setPanEnabled(true, false);
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);

		renderer.setMargins(new int[] { 50, 50, 50, 50 }); // 设置4边留白
		renderer.setDisplayChartValues(true);
		renderer.setBackgroundColor(Color.WHITE);// 设置背景色透明
		renderer.setApplyBackgroundColor(true);// 使背景颜色生效
		// renderer.setMarginsColor(Color.WHITE);
		renderer.setMarginsColor(getResources().getColor(R.color.pink));
		renderer.setXLabelsAngle(-20);

		int i = 1;
		for (int p = 0; p < time.length; p++) {

			renderer.addTextLabel(i, time[p]);
			i = i + 2;
		}

		setChartSettings(renderer, "Line Chart Demo", "时间", "状态", 0, 12, 0, 5,
				Color.BLACK, Color.BLACK);

		View chart = ChartFactory.getLineChartView(this, dataset, renderer);

		setContentView(chart);

	}

	protected XYMultipleSeriesDataset buildDataset(String[] titles,
			List xValues, List yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		int length = titles.length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i]);
			double[] xV = (double[]) xValues.get(i);
			double[] yV = (double[]) yValues.get(i);
			int seriesLength = xV.length;

			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}

			dataset.addSeries(series);
		}

		return dataset;
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			r.setFillPoints(fill);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {

		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setApplyBackgroundColor(true);

	}
}