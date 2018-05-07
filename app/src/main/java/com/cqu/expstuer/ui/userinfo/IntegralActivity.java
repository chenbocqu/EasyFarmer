package com.cqu.expstuer.ui.userinfo;

import android.os.CountDownTimer;
import android.view.View;

import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.User;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.utils.DataMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;


public class IntegralActivity extends TitleBarActivity {

    SweetAlertDialog mDialog;
    User mUser;
    int updateVal = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral;
    }

    @Override
    public String getUITitle() {
        return "积分";
    }

    @Override
    public void init() {
        registerClickListener(R.id.btn_sign);

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
        setText(R.id.tv_integral, mUser.getIntegral() + "");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_sign:
                toSignIn();
                break;
        }
    }

    private void toSignIn() {

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在签到...");
        mDialog.show();

        //今天是否签过到
        if (isSignedToday()) {

            mDialog.setTitleText("已签到")
                    .setContentText("您今天已经签过到了，少侠明日再来吧！")
                    .setConfirmText("确定")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            mDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .changeAlertType(SweetAlertDialog.WARNING_TYPE);
        } else
            //没有签过到则增加10个积分
            addIntegration();
    }

    private void addIntegration() {

        if (mUser == null) {
            myTool.showInfo("No User is avalible!");
            return;
        }

        updateVal = mUser.getIntegral() + 10;

        OkHttpUtils.post().url(myTool.getServAdd() + "updateUser.do")
                .addParams("exuserID", myTool.getUserId())
                .addParams("item", "userIntegration")
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

    private boolean isSignedToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = simpleDateFormat.format(new Date()); //获取当前时间
//        myTool.showInfo("上次签到时间：" + myTool.getSignTime());
        if (myTool.getSignTime().equals(currentTime)) {
            return true;
        } else {
            return false;
        }
    }

    private void showSuccess() {

        DataMaker.updateUserInfo(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = simpleDateFormat.format(new Date()); //获取当前时间作为新的签到时间
        myTool.setSignTime(currentTime);

        setText(R.id.tv_integral, updateVal + "");

        mDialog
                .setTitleText("签到成功")
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
                .setTitleText("签到失败")
                .setContentText(info)
                .setConfirmText("重试")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mDialog.dismissWithAnimation();
                        toSignIn();
                    }
                })
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }
}
