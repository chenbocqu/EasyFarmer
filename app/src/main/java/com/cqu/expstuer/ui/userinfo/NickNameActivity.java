package com.cqu.expstuer.ui.userinfo;


import android.os.CountDownTimer;
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

public class NickNameActivity extends TitleBarActivity {

    User mUser;
    String nickName = "";
    EditText edt;
    SweetAlertDialog mDialog;
    TextView tvLen;
    int maxLen = 10;

    @Override
    protected int getContentView() {
        return R.layout.activity_nickname;
    }

    @Override
    public String getUITitle() {
        return "昵称";
    }

    @Override
    public void init() {

        setRightMenu("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNickName();
            }
        });

        mUser = myTool.getUserInfo();

        if (mUser != null) {

            setEditText(R.id.edt_nickname, mUser.getNickName());

            if (mUser.getNickName() != null)
                nickName = mUser.getNickName();
        }

        edt = (EditText) findViewById(R.id.edt_nickname);
        tvLen = (TextView) findViewById(R.id.tv_lenth);

        tvLen.setText(edt.getText().toString().trim().length() + "/" + maxLen);

        TextUtils.with(this).restrictTextLenth(edt, maxLen, "长度最多不超过" + maxLen + "！", tvLen);
    }

    // 保存签名
    private void saveNickName() {

        String s = edt.getText().toString().trim();

        if ("".equals(s)) {
            TextUtils.with(this).hintEdt(edt, "请输入昵称再保存。");
            return;
        }

        if (nickName.equals(s)) {
            TextUtils.with(this).hintEdt(edt, "修改的昵称与原昵称相同。");
            return;
        }

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在更新昵称...");
        mDialog.show();

        OkHttpUtils.post().url(myTool.getServAdd() + "updateUser.do")
                .addParams("exuserID", myTool.getUserId())
                .addParams("item", "userNickname")
                .addParams("value", s)
                .build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 myTool.log("Update nickname err = " + e.getMessage());
                                 showError(e.getMessage());
                             }

                             @Override
                             public void onResponse(String response, int id) {

                                 myTool.log("Update nickname response = " + response);

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

    private void showSuccess() {

        mDialog
                .setTitleText("修改成功")
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

        // 更新用户信息
        DataMaker.updateUserInfo(this);
    }

    private void showError(String info) {
        mDialog
                .setTitleText("修改失败")
                .setContentText(info)
                .setConfirmText("重试")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mDialog.dismissWithAnimation();
                        saveNickName();
                    }
                })
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }
}
