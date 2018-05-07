package com.cqu.expstuer.ui.setting;

import android.view.View;

import com.cqu.expstuer.R;
import com.cqu.expstuer.ui.base.TitleBarActivity;


public class SettingActivity extends TitleBarActivity {

    @Override
    public String getUITitle() {
        return "设置";
    }

    @Override
    public void init() {
        registerClickListener(R.id.rl_ipaddress);
        registerClickListener(R.id.rl_about);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_ipaddress:
                myTool.modifyIp();
                break;
            case R.id.rl_about:
                myTool.startActivity(AboutActivity.class);
                break;
        }
    }
}
