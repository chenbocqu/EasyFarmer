package com.cqu.expstuer.ui.homepage;

import android.view.View;

import com.cqu.expstuer.R;
import com.cqu.expstuer.listener.PopwindowListener;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.ui.homepage.exptake.Dialog4SelectPrice;

public class OtherTaskActivity extends TitleBarActivity implements PopwindowListener<Object, Object> {

    Dialog4SelectPrice dialog4SelectPrice;
    int mPrice = 3;

    @Override
    public String getUITitle() {
        return "其他悬赏";
    }

    @Override
    public void init() {

        dialog4SelectPrice = new Dialog4SelectPrice(this, mPrice);

        initEvent();
    }

    private void initEvent() {
        registerClickListener(R.id.rl_price);
        dialog4SelectPrice.setOnPowwinListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_other_task;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_price:
                myTool.showPopFormBottom(dialog4SelectPrice, findViewById(R.id.ll_main));
                break;
        }
    }

    @Override
    public void infoChanged(Object w, Object obj) {
        if ("Dialog4SelectPrice".equals(w.getClass().getSimpleName())) {
            mPrice = (int) obj;

            setText(R.id.tv_price, mPrice + "元");
        }
    }
}
