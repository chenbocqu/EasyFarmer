package com.cqu.expstuer.ui.plan;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.utils.TextUtils;

public class PlanPublishActivity extends TitleBarActivity {

    public static final int REQUEST_PLAN_TYPE = 1;
    private static final int REQUEST_FROM_TIME_LOC = 2;
    private static final int REQUEST_TO_TIME_LOC = 3;
    int llItemIds[] = {
            R.id.ll_express_take,
            R.id.ll_express_send,
            R.id.ll_book_return,
            R.id.ll_other_task,
    };

    String types[] = {
            "取快递", "寄快递", "还书", "其他"
    };

    String type = types[0];

    @Override
    public String getUITitle() {
        return "计划任务";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_plan_publish;
    }

    EditText edtMark;
    TextView tvCnt;
    int maxLen = 50;

    @Override
    public void init() {
        // 注册点击事件
        for (int llItemId : llItemIds) registerClickListener(llItemId);

        registerClickListener(R.id.rl_from);
        registerClickListener(R.id.rl_to);
        edtMark = (EditText) findViewById(R.id.edt_mark);
        tvCnt = (TextView) findViewById(R.id.tv_cnt);

        TextUtils.with(this).restrictTextLenth(edtMark, maxLen, "最长不超过" + maxLen + "个字。", tvCnt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_PLAN_TYPE:
                if (resultCode == RESULT_OK) {
                    String s = data.getStringExtra(PlanTypeChooseActivity.KEY_TYPE);
                    if (s == null) return;

                    setText(R.id.tv_other, s);
                    type = s;

                    // 选中最后一个
                    choose(llItemIds.length - 1);
                }
                break;

            case REQUEST_FROM_TIME_LOC:

                if (resultCode == RESULT_OK) {

                    String time = data.getStringExtra(TimeLocationChooseActivity.KEY_TIME);
                    String loc = data.getStringExtra(TimeLocationChooseActivity.KEY_LOC);
                    if (time == null || loc == null) return;
                    setText(R.id.tv_start_time, time);
                    setText(R.id.tv_start_loc, loc);

                }
                break;
            case REQUEST_TO_TIME_LOC:

                if (resultCode == RESULT_OK) {

                    String time = data.getStringExtra(TimeLocationChooseActivity.KEY_TIME);
                    String loc = data.getStringExtra(TimeLocationChooseActivity.KEY_LOC);
                    if (time == null || loc == null) return;
                    setText(R.id.tv_to_time, time);
                    setText(R.id.tv_to_loc, loc);

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        for (int i = 0; i < llItemIds.length; i++) {
            if (v.getId() == llItemIds[i])
                if (i == llItemIds.length - 1) {
                    myTool.startActivityForResult(PlanTypeChooseActivity.class, REQUEST_PLAN_TYPE);
                } else {
                    setText(R.id.tv_other, "其他");
                    choose(i);
                }
        }

        switch (v.getId()) {
            case R.id.rl_from:
                myTool.startActivityForResult(TimeLocationChooseActivity.class, REQUEST_FROM_TIME_LOC);
                break;
            case R.id.rl_to:
                myTool.startActivityForResult(TimeLocationChooseActivity.class, REQUEST_TO_TIME_LOC);
                break;
        }
    }

    private void choose(int index) {
        resetView();
        selected(index);
    }


    // unselect all
    void resetView() {
        for (int index = 0; index < llItemIds.length; index++)
            unSelectView(index);
    }

    void selected(int i) {

        View v = findViewById(llItemIds[i]);

        if (v == null) return;

        v.setBackgroundResource(R.drawable.item_selected);

        // 选择类型
        type = types[i];
    }

    void unSelectView(int i) {
        View v = findViewById(llItemIds[i]);
        if (v == null) return;
        v.setBackgroundResource(R.drawable.item_unselected);
    }
}
