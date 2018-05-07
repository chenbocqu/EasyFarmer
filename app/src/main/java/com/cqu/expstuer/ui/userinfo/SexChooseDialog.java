/**
 * 二维码显示框
 */
package com.cqu.expstuer.ui.userinfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cqu.expstuer.R;
import com.cqu.expstuer.listener.OnSexDialogListener;
import com.cqu.expstuer.utils.CommonTools;
import com.cqu.expstuer.utils.DataMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


public class SexChooseDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private CommonTools myTool;
    private RelativeLayout rlFemale, rlMale;
    private ImageView ivFemaleSelected, ivMaleSelected;
    private OnSexDialogListener l;
    private ProgressBar pbChangeSex;

    public SexChooseDialog(Context context) {
        this(context, R.style.Translucent_NoTitle);
    }

    public SexChooseDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sexchoose);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        rlMale.setOnClickListener(this);
        rlFemale.setOnClickListener(this);
    }

    private void initData() {
        showSex(myTool.getUserSex());
    }

    private void showSex(String userSex) {
        resetSex();
        switch (userSex) {
            case "":
                break;
            case "男":
                ivMaleSelected.setVisibility(View.VISIBLE);
                break;
            case "女":
                ivFemaleSelected.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void resetSex() {
        ivMaleSelected.setVisibility(View.GONE);
        ivFemaleSelected.setVisibility(View.GONE);
    }

    private void initView() {
        rlMale = (RelativeLayout) findViewById(R.id.rl_male);
        rlFemale = (RelativeLayout) findViewById(R.id.rl_female);

        ivMaleSelected = (ImageView) findViewById(R.id.iv_male_selected);
        ivFemaleSelected = (ImageView) findViewById(R.id.iv_female_selected);

        pbChangeSex = (ProgressBar) findViewById(R.id.pb_changesex);

        myTool = new CommonTools(mContext);
    }


    @Override
    public void show() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.show();
            } catch (Exception e) {
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
            case R.id.rl_male:
                toSelectSex("男");//选择男性
                break;
            case R.id.rl_female:
                toSelectSex("女");//选择女性
                break;
        }
    }

    private void toSelectSex(final String sex) {
        showSex(sex);
        pbChangeSex.setVisibility(View.VISIBLE);
        //更改服务器信息
        String url = myTool.getServAdd() + "updateUser.do";
        //修改性别
        OkHttpUtils.post().url(url)
                .addParams("exuserID", myTool.getUserId())
                .addParams("item", "userSex")
                .addParams("value", sex)
                .build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e, int id) {

                                 myTool.log("Update sex err = " + e.getMessage());

                                 myTool.showInfo("修改失败，稍后重试");
                                 pbChangeSex.setVisibility(View.GONE);
                                 showSex(myTool.getUserSex());
                             }

                             @Override
                             public void onResponse(String response, int id) {
                                 myTool.log("Update sex response = " + response);
                                 try {
                                     JSONObject obj = new JSONObject(response);
                                     String state = obj.getString("state");

                                     switch (state) {
                                         case "success":
                                             myTool.setUserSex(sex);
                                             pbChangeSex.setVisibility(View.GONE);
                                             DataMaker.updateUserInfo(mContext);
                                             if (l != null) {
                                                 l.sexChangedSuccess();
                                             }
                                             new CountDownTimer(1000, 1000) {
                                                 @Override
                                                 public void onTick(long millisUntilFinished) {
                                                 }

                                                 @Override
                                                 public void onFinish() {
                                                     dismiss();
                                                 }
                                             }.start();
                                             break;
                                         case "error":
                                             pbChangeSex.setVisibility(View.GONE);
                                             myTool.showInfo("修改失败，请稍后重试！");
                                             showSex(myTool.getUserSex());
                                             break;
                                     }
                                 } catch (JSONException e) {
                                     pbChangeSex.setVisibility(View.GONE);
                                     myTool.showInfo("修改失败，请稍后重试！");
                                     showSex(myTool.getUserSex());
                                 }
                             }
                         }
                );
    }

    public void setOnSexDialogListener(OnSexDialogListener l) {
        this.l = l;
    }

}
