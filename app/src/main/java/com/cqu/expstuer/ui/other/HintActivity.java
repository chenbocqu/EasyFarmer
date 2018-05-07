package com.cqu.expstuer.ui.other;

import com.cqu.expstuer.R;
import com.cqu.expstuer.ui.base.TitleBarActivity;

public class HintActivity extends TitleBarActivity {


    @Override
    public String getUITitle() {
        return "提示";
    }

    @Override
    public void init() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hint;
    }
}
