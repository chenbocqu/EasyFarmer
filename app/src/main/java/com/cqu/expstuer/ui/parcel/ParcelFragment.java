package com.cqu.expstuer.ui.parcel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqu.expstuer.R;
import com.cqu.expstuer.ui.base.FragmentWithOnResume;
import com.cqu.expstuer.utils.CommonTools;
import com.cqu.expstuer.utils.TimeUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ParcelFragment extends FragmentWithOnResume implements OnChartValueSelectedListener {

    private View view;
    private CommonTools myTool;

    LineChart mChart;
    XAxis xAxis;

    ArrayList<Entry> yVals1;
    ArrayList<String> xTimes;

    LineDataSet set;
    Legend l;
    int durationMillis = 1000;
    float axisMargin = 20f;

    int curPos = 0;
    String unit = "%";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parcel, container, false);
        initView();
        initDatas();
        initEvent();
        return view;
    }

    @Override
    public void update() {

    }

    private void initEvent() {
        mChart.setOnChartValueSelectedListener(this);
    }


    private void initDatas() {

        initLineStyle();
    }

    private void initLineStyle() {

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // add data
        setData(10, 150);
        mChart.animateY(durationMillis);

        mChart.getLegend().setEnabled(false);

        xAxis = mChart.getXAxis();
        xAxis.setTextSize(15f);
        xAxis.setTextColor(getResources().getColor(R.color.text_color));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
    }

    private void getDataByIndex(final int index) {
    }

    private void invalidateLine() {
        set.setValues(yVals1);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= 0 && value < xTimes.size())
                    return TimeUtils.getTime(xTimes.get((int) value));
                else
                    return "";
            }
        };

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.animateY(durationMillis);
    }


    private void initView() {
        myTool = new CommonTools(getActivity());
        mChart = (LineChart) view.findViewById(R.id.chart);

        myTool.setHeightByWindow(mChart, 3 / 4f);
    }

    private void setData(int count, float range) {

        yVals1 = new ArrayList<Entry>();
        xTimes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float mult = range / 2f;
            float val = (float) (Math.random() * mult) + 50;
            yVals1.add(new Entry(i, (int) val));
        }

        if (mChart.getData() != null) {
            set = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set = new LineDataSet(yVals1, "模拟数据");

            set.setAxisDependency(YAxis.AxisDependency.LEFT);//依靠左边的Y轴
            set.setColor(ColorTemplate.getHoloBlue());
            set.setCircleColor(getResources().getColor(R.color.main_color));
            set.setLineWidth(1f);
            set.setCircleRadius(5f);
            set.setFillAlpha(55);
            set.setFillColor(getResources().getColor(R.color.main_color));
            set.setHighLightColor(getResources().getColor(R.color.main_color));
            set.enableDashedLine(10f, 10f, 0f);//将折线设置为曲线

            set.setDrawFilled(true);
            set.setFillFormatter(new DefaultFillFormatter());
            //set.setVisible(false);
            set.setDrawCircleHole(true);
            set.setCircleHoleRadius(4f);
            set.setCircleColorHole(getResources().getColor(R.color.main_color));
            set.setDrawHorizontalHighlightIndicator(true);
            set.setCubicIntensity(23f);

            // create a data object with the datasets
            LineData data = new LineData(set);
            data.setValueTextColor(Color.GRAY);
            data.setValueTextSize(16f);

            // set data
            mChart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        //动画滚动到当前位置
        mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
    }

    @Override
    public void onNothingSelected() {

    }
}
