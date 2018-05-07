package com.cqu.expstuer.ui.plan;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.utils.TextUtils;


public class PlanTypeChooseActivity extends TitleBarActivity {

    public static final String KEY_TYPE = "KEY_TYPE";
    int tvItemIds[] = {
            R.id.tv_item1,
            R.id.tv_item2,
            R.id.tv_item3,
            R.id.tv_item4,
            R.id.tv_item5,
            R.id.tv_item6
    };

    String types[] = {
            "食堂买饭", "超市购物", "投递报销", "图书馆", "校医院", "教学楼"
    };

    String type;
    boolean isEdit = false;
    EditText edtType;

    @Override
    public String getUITitle() {
        return "其他计划";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_plan_type_choose;
    }

    @Override
    public void init() {

        registerClickListener(R.id.btn_ok);
        registerClickListener(R.id.edt_price);
        registerClickListener(R.id.edt_type);

        for (int i = 0; i < tvItemIds.length; i++) {
            registerClickListener(tvItemIds[i]);
            setText(tvItemIds[i], i < types.length ? types[i] : "null");
        }

        edtType = (EditText) findViewById(R.id.edt_type);
        TextUtils.with(this).restrictTextLenth(edtType, 5, "计划最多不超过5位。", (TextView) findViewById(R.id.tv_cnt));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.btn_ok:
                save();
                break;


            case R.id.edt_type:

                type = null;
                resetView();
                isEdit = true;

                break;
        }

        for (int i = 0; i < tvItemIds.length; i++) {
            if (v.getId() == tvItemIds[i])
                choose(i);
        }
    }

    private void save() {

        if (isEdit)
            if (TextUtils.isEmpty(edtType)) {
                TextUtils.with(this).hintEdt(edtType, "请描述您的计划！");
                return;
            } else
                type = edtType.getText().toString().trim();

        if (type == null) {
            myTool.showInfo("请先选择类型。");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(KEY_TYPE, type);

        setResult(RESULT_OK, intent);

        finish();
    }

    private void choose(int i) {

        isEdit = false;

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
        type = types[i];
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
