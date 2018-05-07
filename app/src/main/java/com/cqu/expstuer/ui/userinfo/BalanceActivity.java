package com.cqu.expstuer.ui.userinfo;

import android.view.View;

import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.User;
import com.cqu.expstuer.ui.base.TitleBarActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class BalanceActivity extends TitleBarActivity {

    SweetAlertDialog mDialog;
    User mUser;
    int updateVal = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_balance;
    }

    @Override
    public String getUITitle() {
        return "积分";
    }

    @Override
    public void init() {

        registerClickListener(R.id.btn_charge);
        setRightMenu("积分明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = myTool.getUserInfo();
        setText(R.id.tv_balance, "¥" + mUser.getBalance());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_charge:
                myTool.startActivity(RechargeActivity.class);
                break;
        }
    }
}
