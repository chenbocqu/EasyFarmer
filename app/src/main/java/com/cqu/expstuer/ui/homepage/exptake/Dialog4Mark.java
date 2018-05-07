/**
 * 留言备注
 */
package com.cqu.expstuer.ui.homepage.exptake;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.listener.PopwindowListener;
import com.cqu.expstuer.utils.CommonTools;


public class Dialog4Mark extends Dialog implements View.OnClickListener {
    PopwindowListener<Object, Object> mCallBack;

    private TextView tvA, tvB, tvC, tvD, tvE, tvF, tvLenth;
    private ImageView ivCancel;

    private Button btnOK;
    EditText etRemark;
    String remark = "";
    Context mContext;

    CommonTools myTool;

    public Dialog4Mark(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public Dialog4Mark(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remark);
        this.setCanceledOnTouchOutside(false);

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        //取消按钮
        btnOK.setOnClickListener(this);

        tvA.setOnClickListener(this);
        tvB.setOnClickListener(this);
        tvC.setOnClickListener(this);
        tvD.setOnClickListener(this);
        tvE.setOnClickListener(this);
        tvF.setOnClickListener(this);

        ivCancel.setOnClickListener(this);

        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                remark = s.toString();

                int len = remark.length();
                // 最长20个字符
                if (len > 20) {
                    remark = remark.substring(0, 20);
                    etRemark.setText(remark);
                    etRemark.setSelection(remark.length());//将光标移至文字末尾
                    myTool.showInfo("文本最大长度不超过20！");
                    return;
                }
                tvLenth.setText("(" + len + "/20)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {
    }

    private void initView() {
        myTool = new CommonTools(mContext);
        ivCancel = (ImageView) findViewById(R.id.iv_cancel);//取消按钮

        btnOK = (Button) findViewById(R.id.btn_ok);

        tvA = (TextView) findViewById(R.id.tv_A);
        tvB = (TextView) findViewById(R.id.tv_B);
        tvC = (TextView) findViewById(R.id.tv_C);
        tvD = (TextView) findViewById(R.id.tv_D);
        tvE = (TextView) findViewById(R.id.tv_E);
        tvF = (TextView) findViewById(R.id.tv_F);

        etRemark = (EditText) findViewById(R.id.et_remark);
        tvLenth = (TextView) findViewById(R.id.tv_lenth);
    }

    @Override
    public void show() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.show();
                initData();
            } catch (Exception e) {
                Log.i("DEBUG", "show: " + e.getMessage());
            }
        }
    }

    @Override
    public void dismiss() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void cancel() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.cancel();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_A:
                addText2Remark(tvA);
                break;
            case R.id.tv_B:
                addText2Remark(tvB);
                break;
            case R.id.tv_C:
                addText2Remark(tvC);
                break;
            case R.id.tv_D:
                addText2Remark(tvD);
                break;
            case R.id.tv_E:
                addText2Remark(tvE);
                break;
            case R.id.tv_F:
                addText2Remark(tvF);
                break;

            case R.id.iv_cancel:
                dismiss();
                break;

            case R.id.btn_ok:
                if (mCallBack != null) mCallBack.infoChanged(this, remark);
                dismiss();
                break;
        }
    }

    private void addText2Remark(TextView tv) {

        String word = tv.getText().toString().trim();
        if ((remark + "，" + word).length() > 20) {
            myTool.showInfo("文本最大长度不超过20！");
            return;
        }

        if ("".equals(remark))
            remark = word;
        else
            remark = remark + "，" + word;

        etRemark.setText(remark);
        etRemark.setSelection(remark.length());//将光标移至文字末尾
    }

    public void setOnPowwinListener(PopwindowListener<Object, Object> callBack) {
        this.mCallBack = callBack;
    }
}
