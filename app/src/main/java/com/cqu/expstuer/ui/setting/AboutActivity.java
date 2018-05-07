package com.cqu.expstuer.ui.setting;

import android.view.View;

import com.cqu.expstuer.R;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.ui.intro.IntroActivity;


public class AboutActivity extends TitleBarActivity {

    @Override
    public String getUITitle() {
        return "关于";
    }

    @Override
    public void init() {

        registerClickListener(R.id.rl_evaluate);
        registerClickListener(R.id.rl_intro);
        registerClickListener(R.id.rl_msg);
        registerClickListener(R.id.rl_upgrade);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.rl_upgrade:
            case R.id.rl_msg:
            case R.id.rl_evaluate:
                myTool.toHint();
                break;

            case R.id.rl_intro:
                myTool.startActivityForResult("notFirstIn", IntroActivity.class, 0);
                break;

        }
    }
}
