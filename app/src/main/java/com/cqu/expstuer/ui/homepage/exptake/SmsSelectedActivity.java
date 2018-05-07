package com.cqu.expstuer.ui.homepage.exptake;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.Sms;
import com.cqu.expstuer.bean.TakeMsg;
import com.cqu.expstuer.ui.base.ContListActivity;
import com.cqu.expstuer.utils.SMSUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SmsSelectedActivity extends ContListActivity<Sms> {

    public static final String NEW_ADDRESS = "NEW_ADDRESS";
    private static final int LOAD_SMS = 1;
    EditText edtName, edtPhone;
    SweetAlertDialog mDialog;

    TakeMsg mTakeMsg;
    List<Sms> mDatas;

    @Override
    public String getUITitle() {
        return "选择取件信息";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_sms_phone;
    }

    @Override
    public void init() {

        mTakeMsg = (TakeMsg) myTool.getParam(TakeMsg.class);

        setRightMenu("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSms(); // 选择短消息
            }
        });

        mDatas = new ArrayList<>();

        initUI();

        new Thread() {
            @Override
            public void run() {
                super.run();
                mDatas = SMSUtils.getSmsInPhone(SmsSelectedActivity.this);
                mHandler.sendEmptyMessage(LOAD_SMS);
            }
        }.start();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            myTool.log("handleMessage !");

            switch (msg.what) {
                case LOAD_SMS:
                    myTool.log("mDatas size : " + mDatas.size());
                    addAll(mDatas);
                    break;
            }

        }
    };


    private void initUI() {

        if (mTakeMsg == null || mTakeMsg.getName() == null || mTakeMsg.getSms() == null) return;

        setText(R.id.edt_name, mTakeMsg.getName());
        setText(R.id.edt_phone, mTakeMsg.getPhone());
        setText(R.id.tv_msg_content, mTakeMsg.getSms().getContent());

    }

    @Override
    public CommonAdapter<Sms> getAdapter() {
        return new CommonAdapter<Sms>(this, R.layout.item_sms, mDatas) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, final Sms item, int position) {

                helper.setBackgroundRes(R.id.ll_item, R.drawable.item_selected);

                helper
                        .setText(R.id.tv_name_phone, (item.getName() == null || "null".equals(item.getName())
                                ? "" : (item.getName()) + " ") + item.getPhone())
                        .setText(R.id.tv_content, item.getContent());

                helper.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setText(R.id.tv_msg_content, item.getContent());
                    }
                });
            }
        };
    }

    /**
     * 保存所新建的联系人地址信息
     */
    private void chooseSms() {

        if (isEmpty(edtName, "姓名不能为空！")) return;
        if (isEmpty(edtPhone, "电话不能为空！")) return;

        int len = edtPhone.getText().toString().trim().length();

        if (len == 11 || len == 8) ;
        else {
            hintEdt(edtPhone, "电话格式有误！");
            return;
        }

        // 开始添加信息
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在保存...");
        mDialog.show();

        edtName.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.setTitleText("保存成功")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                mDialog.dismissWithAnimation();

                                Intent intent = new Intent();
                                intent.putExtra(NEW_ADDRESS, mTakeMsg);
                                setResult(RESULT_OK, intent);

                                finish();
                            }
                        })
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            }
        }, 1500);
    }

    private boolean isEmpty(final EditText edt, final String info) {

        if ("".equals(edt.getText().toString().trim())) {
            hintEdt(edt, info);
            return true;
        }
        return false;
    }

    private void hintEdt(final EditText edt, final String info) {

        YoYo.with(Techniques.Shake)
                .duration(800)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        edt.requestFocus();
                        edt.setError(info);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(edt);
    }

    protected void initView() {
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
    }

    protected void initEvent() {
        restrictTextLenth(edtName, 5); // 最长不超过5
        restrictTextLenth(edtPhone, 11); // 最长不超过11
        findViewById(R.id.iv_phone).setOnClickListener(this);
        findViewById(R.id.tv_city).setOnClickListener(this);
    }

    // 为EditText添加一个最大长度
    private void restrictTextLenth(final EditText edt, final int maxLen) {
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String input = s.toString();

                int len = input.length();
                // 最长maxLen个字符
                if (len > maxLen) {
                    input = input.substring(0, maxLen);
                    edt.setText(input);
                    edt.setSelection(input.length());//将光标移至文字末尾
                    myTool.showInfo("最大长度不超过" + maxLen + "！");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_phone:
                myTool.showInfo("暂无开放！");
                break;
        }
    }
}
