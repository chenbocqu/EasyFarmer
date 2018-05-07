package com.cqu.expstuer.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.cqu.expstuer.R;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.ui.login.LoginActivity;
import com.cqu.expstuer.ui.plan.PlanPublishActivity;
import com.cqu.expstuer.ui.setting.SettingActivity;
import com.cqu.expstuer.ui.userinfo.MyInfoActivity;


public class HomeActivity extends TitleBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout srl;

    @Override
    protected void onResume() {
        super.onResume();
        setVisible(R.id.ll_login, !myTool.isLogin());
        setVisible(R.id.ll_msg, myTool.isLogin());
    }

    @Override
    public void init() {
        registerClickListener(R.id.ll_publish_plan);
        registerClickListener(R.id.ll_hall);
        registerClickListener(R.id.rl_userinfo);
        registerClickListener(R.id.ll_userinfo);
        registerClickListener(R.id.ll_setting);
        registerClickListener(R.id.ll_login);
        registerClickListener(R.id.ll_msg);

        srl = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_homepage;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_publish_plan:
                myTool.startActivity(PlanPublishActivity.class);
                break;
            case R.id.ll_hall:
                myTool.showInfo("悬赏大厅");
                break;
            case R.id.ll_userinfo:
            case R.id.rl_userinfo:
                myTool.startActivity(MyInfoActivity.class);
                break;
            case R.id.ll_setting:
                myTool.startActivity(SettingActivity.class);
                break;
            case R.id.ll_login:
                myTool.startActivity(LoginActivity.class);
                break;

        }
    }

    @Override
    public void onRefresh() {
        srl.postDelayed(new Runnable() {
            @Override
            public void run() {
                myTool.showInfo("信息已更新");
                srl.setRefreshing(false);
            }
        }, 1500);
    }
}
