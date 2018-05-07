package com.cqu.expstuer.ui.homepage;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.ContactAddress;
import com.cqu.expstuer.bean.TakeMsg;
import com.cqu.expstuer.listener.PopwindowListener;
import com.cqu.expstuer.ui.address.AddressManageActivity;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.ui.homepage.exptake.Dialog4Mark;
import com.cqu.expstuer.ui.homepage.exptake.Dialog4SelectPrice;

/**
 * Created by Chenbo on 2017/11/14.
 * 帮我还书页面
 */

public class BookReturnActivity extends TitleBarActivity implements PopwindowListener<Object, Object> {

    public static final int REQUEST_RECEIVE_ADDRESS = 1;
    public static final int REQUEST_TAKE_MSG = 2;// 请求姓名，手机，短消息

    Dialog4SelectPrice dialog4SelectPrice;
    Dialog4Mark dialog4Remark;

    int mPrice = 3;
    String remark;
    TextView tvPrice, tvRemark;

    private WindowManager.LayoutParams params;
    ContactAddress mReceiveAdd;

    TakeMsg mTakeMsg;

    @Override
    protected int getContentView() {
        return R.layout.activity_book_return;
    }

    @Override
    public String getUITitle() {
        return "帮我还";
    }

    @Override
    public void init() {

        mTakeMsg = new TakeMsg();

        viewById();
        initEvents();
    }

    private void initEvents() {

        registerClickListener(R.id.rl_contact_info);
        registerClickListener(R.id.btn_publish);
        registerClickListener(R.id.rl_remark);
        registerClickListener(R.id.rl_price);

        dialog4Remark.setOnPowwinListener(this);
        dialog4SelectPrice.setOnPowwinListener(this);
    }

    private void viewById() {

        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvRemark = (TextView) findViewById(R.id.tv_remark);

        dialog4SelectPrice = new Dialog4SelectPrice(this, mPrice);
        dialog4Remark = new Dialog4Mark(this, R.style.Translucent_NoTitle);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 联系方式选择
            case R.id.rl_contact_info:
//                myTool.startActivityForResult(mTakeMsg, SmsSelectedActivity.class, REQUEST_TAKE_MSG);
                break;

            case R.id.rl_receive_address:
                myTool.startActivityForResult(AddressManageActivity.class, REQUEST_RECEIVE_ADDRESS);
                break;

            case R.id.rl_price:
                myTool.showPopFormBottom(dialog4SelectPrice, findViewById(R.id.ll_main));
//                showPopFormBottom();
                break;
            case R.id.rl_remark:
                dialog4Remark.show();
                break;

            case R.id.btn_publish:
                myTool.showInfo("发布成功！");
                finish();
                break;
        }
    }

    @Override
    public void infoChanged(Object w, Object obj) {
        if ("Dialog4Mark".equals(w.getClass().getSimpleName())) {
            remark = (String) obj;
            tvRemark.setText(remark);
        }
        if ("Dialog4SelectPrice".equals(w.getClass().getSimpleName())) {
            mPrice = (int) obj;
            tvPrice.setText(mPrice + "元");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 请求收货地址
        switch (requestCode) {
            // 请求收货地址
            case REQUEST_RECEIVE_ADDRESS:

                if (resultCode == RESULT_OK) {
                    mReceiveAdd = (ContactAddress) data.getSerializableExtra(AddressManageActivity.ADDRESS);

                    setText(R.id.tv_receive_name_phone, mReceiveAdd.getContactName() + " " + mReceiveAdd.getPhone());
                    setText(R.id.tv_receive_address, mReceiveAdd.getAddressDesc());
                }
                break;
        }
    }
}
