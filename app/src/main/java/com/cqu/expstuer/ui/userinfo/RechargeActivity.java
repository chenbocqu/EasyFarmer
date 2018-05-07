package com.cqu.expstuer.ui.userinfo;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.text.TextPaint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.User;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.utils.DataMaker;
import com.cqu.expstuer.utils.TextUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;


public class RechargeActivity extends TitleBarActivity {

    SweetAlertDialog mDialog;
    User mUser;
    double updateVal = 0;

    int tvItemIds[] = {
            R.id.tv_item1,
            R.id.tv_item2,
            R.id.tv_item3,
            R.id.tv_item4,
            R.id.tv_item5
    };

    double prices[] = {
            2, 5, 10, 20, 50
    };

    double price = 2;
    EditText edtPrice;
    boolean isCustom = false;  // 自定义价格

    @Override
    public String getUITitle() {
        return "充值";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_recharge;
    }

    @Override
    public void init() {

        mUser = myTool.getUserInfo();

        edtPrice = (EditText) findViewById(R.id.edt_price);
        edtPrice.clearFocus();

        registerClickListener(R.id.btn_charge);
        registerClickListener(R.id.edt_price);

        for (int i = 0; i < tvItemIds.length; i++)
            registerClickListener(tvItemIds[i]);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.btn_charge:

                charge();

                break;

            case R.id.edt_price:

                isCustom = true;
                resetView();

                break;
        }

        for (int i = 0; i < tvItemIds.length; i++) {
            if (v.getId() == tvItemIds[i])
                choose(i);
        }
    }

    private void charge() {

        if (isCustom) {
            if (TextUtils.with(this).isEmpty(edtPrice)) {
                myTool.showInfo("请输入或选择要充值的金额！");
                return;
            }
            price = Double.parseDouble(edtPrice.getText().toString().trim());
        }

        // 模拟支付
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在充值...");
        mDialog.show();
        if (mUser == null) {
            myTool.showInfo("No User is avalible!");
            return;
        }

        updateVal = mUser.getBalance() + price;

        OkHttpUtils.post().url(myTool.getServAdd() + "updateUser.do")
                .addParams("exuserID", myTool.getUserId())
                .addParams("item", "remainingSum")
                .addParams("value", updateVal + "")
                .build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 myTool.log("Update sex err = " + e.getMessage());
                                 showError(e.getMessage());
                             }

                             @Override
                             public void onResponse(String response, int id) {

                                 myTool.log("Update sex response = " + response);

                                 try {
                                     JSONObject obj = new JSONObject(response);
                                     String state = obj.getString("state");

                                     switch (state) {
                                         case "success":
                                             showSuccess();
                                             break;
                                         case "error":
                                             showError("修改失败，请稍后重试！");
                                             break;
                                     }
                                 } catch (JSONException e) {
                                     showError(e.getMessage());
                                 }
                             }
                         }
                );
    }

    private void choose(@IdRes int i) {

        resetView();
        edtPrice.clearFocus();

        // select one
        selected(i);

        isCustom = false;
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

        // 选择价格
        price = prices[i];
    }

    void unSelectView(int i) {

        TextView tv = (TextView) findViewById(tvItemIds[i]);
        if (tv == null) return;

        tv.setBackgroundResource(R.drawable.item_unselected);
        tv.setTextColor(Color.parseColor("#888888"));
        TextPaint tp1 = tv.getPaint();
        tp1.setFakeBoldText(false);
    }


    private void showSuccess() {

        DataMaker.updateUserInfo(this);

        mDialog
                .setTitleText("充值成功")
                .setConfirmText("确定并返回")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mDialog.dismissWithAnimation();
                        new CountDownTimer(500, 500) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                finish();
                            }
                        }.start();
                    }
                })
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

    }

    private void showError(String info) {
        mDialog
                .setTitleText("充值失败")
                .setContentText(info)
                .setConfirmText("重试")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mDialog.dismissWithAnimation();
                        charge();
                    }
                })
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }
}
