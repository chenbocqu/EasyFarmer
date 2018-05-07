package com.cqu.expstuer.ui.plan;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.cqu.expstuer.R;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.utils.TextUtils;
import com.cqu.expstuer.utils.TimePickerUtil;

import java.util.Date;


public class TimeLocationChooseActivity extends TitleBarActivity {

    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_LOC = "KEY_LOC";

    int tvItemIds[] = {
            R.id.tv_item1,
            R.id.tv_item2,
            R.id.tv_item3,
            R.id.tv_item4,
            R.id.tv_item5,
            R.id.tv_item6,
    };

    String times[] = {
            "9:00", "11:00", "13:00", "15:00", "17:00", "19:00"
    };

    String time, startTime, loc;
    Date mStartDate;
    EditText edtLoc;

    @Override
    public String getUITitle() {
        return "选择地点和时间";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_time_location;
    }

    @Override
    public void init() {

        registerClickListener(R.id.btn_ok);
        registerClickListener(R.id.tv_start_time);
        registerClickListener(R.id.tv_end_time);

        for (int i = 0; i < tvItemIds.length; i++) {
            registerClickListener(tvItemIds[i]);
            setText(tvItemIds[i], i < times.length ? times[i] : "null");
        }

        edtLoc = (EditText) findViewById(R.id.edt_location);
        TextUtils
                .with(this)
                .restrictTextLenth(edtLoc, 6, "地点名称不超过6", (TextView) findViewById(R.id.tv_cnt));
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.btn_ok:
                save();
                break;

            case R.id.tv_start_time:

                time = null;
                resetView();

                TimePickerUtil.pickTimeDialog(this, "起始时间", mStartDate, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        mStartDate = date;
                        startTime = mStartDate.getHours() + ":" +
                                (mStartDate.getMinutes() > 9 ? mStartDate.getMinutes() : ("0" + mStartDate.getMinutes()));

                        setText(R.id.tv_start_time, startTime);
                    }
                });
                break;
        }

        for (int i = 0; i < tvItemIds.length; i++) {
            if (v.getId() == tvItemIds[i])
                choose(i);
        }
    }

    private void save() {

        if (startTime != null)
            time = startTime;

        if (time == null) {
            myTool.showInfo("请先选择时间。");
            return;
        }
        if (TextUtils.isEmpty(edtLoc)) {
            TextUtils.with(this).hintEdt(edtLoc, "请先输入一个地点！");
            return;
        }

        loc = edtLoc.getText().toString().trim();

        Intent intent = new Intent();
        intent.putExtra(KEY_TIME, time);
        intent.putExtra(KEY_LOC, loc);

        setResult(RESULT_OK, intent);

        finish();
    }

    private void choose(int i) {
        resetView();
        // select one
        selected(i);
    }

    // unselect all
    void resetView() {
        for (int index = 0; index < tvItemIds.length; index++)
            unSelectView(index);
    }

    void selected(int i) {

        TextView tv = (TextView) findViewById(tvItemIds[i]);

        if (tv == null) return;

        tv.setBackgroundResource(R.drawable.item_selected);
        tv.setTextColor(Color.parseColor("#019978"));
        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(true);

        // 选择类型
        time = times[i];
    }

    void unSelectView(int i) {

        TextView tv = (TextView) findViewById(tvItemIds[i]);
        if (tv == null) return;

        tv.setBackgroundResource(R.drawable.item_unselected);
        tv.setTextColor(Color.parseColor("#888888"));
        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(false);
    }
}
