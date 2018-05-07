package com.cqu.expstuer.ui.userinfo;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.User;
import com.cqu.expstuer.listener.OnDialogListener;
import com.cqu.expstuer.listener.OnRequestListener;
import com.cqu.expstuer.listener.OnSexDialogListener;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.utils.DataMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

public class MyInfoActivity extends TitleBarActivity implements OnSexDialogListener, OnDialogListener, SwipeRefreshLayout.OnRefreshListener {

    User mUser;
    SexChooseDialog mSexDialog;
    PicChooseDialog mPicDialog;
    TwodcodeDialog mQrcodeDialog;
    SwipeRefreshLayout srl;

    public final static String TYPE_HEAD_IMG = "TYPE_HEAD_IMG", TYPE_BG_IMG = "TYPE_BG_IMG";
    String picChooseType;

    @Override
    protected int getContentView() {
        return R.layout.activity_myinfo;
    }

    @Override
    public String getUITitle() {
        return "我的信息";
    }

    @Override
    public void init() {

        setUserInfo();

        mSexDialog = new SexChooseDialog(this, R.style.Translucent_NoTitle);
        mPicDialog = new PicChooseDialog(this, R.style.Translucent_NoTitle);
        mQrcodeDialog = new TwodcodeDialog(this, R.style.Translucent_NoTitle);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);

        initEvent();
    }

    private void initEvent() {

        registerClickListener(R.id.rl_choose_headpic);
        registerClickListener(R.id.rl_bgimg);
        registerClickListener(R.id.rl_sex);
        registerClickListener(R.id.rl_twodcode);
        registerClickListener(R.id.iv_headimg);
        registerClickListener(R.id.iv_bgimg);
        registerClickListener(R.id.rl_autograph);
        registerClickListener(R.id.rl_userid);
        registerClickListener(R.id.rl_nickname);

        mSexDialog.setOnSexDialogListener(this);
        mPicDialog.setOnDialogListener(this);

        srl.setOnRefreshListener(this);
    }

    private void setUserInfo() {

        mUser = myTool.getUserInfo();
        if (mUser == null) return;

        setImg(R.id.iv_headimg, mUser.getImageUrl());
        setImg(R.id.iv_bgimg, mUser.getBgUrl());

        setText(R.id.tv_nickname, mUser.getNickName());
        setText(R.id.tv_userid, mUser.getSeId());
        setText(R.id.tv_sex, mUser.getSex());
        setText(R.id.tv_signature, mUser.getSignature());

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.rl_choose_headpic:
                picChooseType = TYPE_HEAD_IMG;
                mPicDialog.show();
                break;

            case R.id.rl_bgimg:
                picChooseType = TYPE_BG_IMG;
                mPicDialog.show();
                break;

            case R.id.rl_sex:
                mSexDialog.show();
                break;

            case R.id.rl_twodcode:
                mQrcodeDialog.show();
                break;

            case R.id.iv_headimg:
                if (mUser != null)
                    myTool.previewImg((ImageView) findViewById(R.id.iv_headimg), mUser.getImageUrl());
                else myTool.showInfo("请先更新用户信息！");
                break;

            case R.id.iv_bgimg:
                if (mUser != null)
                    myTool.previewImg((ImageView) findViewById(R.id.iv_bgimg), mUser.getBgUrl());
                else myTool.showInfo("请先更新用户信息！");
                break;

            case R.id.rl_autograph:
                myTool.startActivity(SignatureActivity.class);
                break;

            case R.id.rl_nickname:
                myTool.startActivity(NickNameActivity.class);
                break;
        }
    }

    @Override
    public void sexChangedSuccess() {
        setUserInfo();
    }

    @Override
    public void dialogResult(Dialog dialog, String from) {
        switch (picChooseType) {
            case TYPE_HEAD_IMG:
                getPic(1, 1, from, myTool.getUserHeadFileUri());
                break;
            case TYPE_BG_IMG:
                getPic(16, 10, from, myTool.getUserBgFileUri());
                break;
        }
    }

    @Override
    protected void takeSuccess(String path, final Bitmap bm, File picFile) {

        //上传头像至服务器
        String fileName = myTool.getUserId() + "_head.png";
        switch (picChooseType) {
            case TYPE_HEAD_IMG:
                setVisible(R.id.pb_uploadhead, true);
                fileName = "consumer_" + myTool.getUserId() + "_head.png";
                break;
            case TYPE_BG_IMG:
                setVisible(R.id.pb_uploadbg, true);
                fileName = "consumer_" + myTool.getUserId() + "_background.png";
                break;
        }
        //上传用户头像
        OkHttpUtils.post().url(myTool.getServAdd() + "imageUpload.do")
//                .addParams("userId", myTool.getUserId())
//                .addParams("flag", flag)//flag=0表示上次头像,1表示背景图像
                .addFile("mFile", fileName, picFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        setPbGone();
                        myTool.log("Upload img failed err = " + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        myTool.log("Upload img info = " + response);
                        setPbGone();
                        try {
                            JSONObject object = new JSONObject(response);

                            String msg = object.getString("message");

                            switch (msg) {
                                case "success":
                                case "文件上传成功！":
                                    switch (picChooseType) {
                                        case TYPE_HEAD_IMG:
                                            setImg(R.id.iv_headimg, bm);
                                            break;
                                        case TYPE_BG_IMG:
                                            setImg(R.id.iv_bgimg, bm);
                                            break;
                                    }

                                    myTool.showInfo("上传成功！");
                                    DataMaker.updateUserInfo(MyInfoActivity.this);
                                    break;
                                case "error":
                                    myTool.showInfo("上传失败，请稍后再试！");
                                    break;
                                case "overSize":
                                    myTool.showInfo("上传失败，图像超过最大限制了！");
                                    break;
                            }
                        } catch (JSONException e) {
                            myTool.log(e.getMessage());
                            myTool.showInfo("上传异常，请稍后再试！");
                        }
                    }
                });
    }

    private void setPbGone() {
        setVisible(R.id.pb_uploadhead, false);
        setVisible(R.id.pb_uploadbg, false);
    }

    @Override
    public void onRefresh() {
        DataMaker.updateUserInfo(this, new OnRequestListener() {
            @Override
            public void onError(Exception e) {
                srl.setRefreshing(false);
                myTool.showInfo("网络请求异常，请稍后再试！");
            }

            @Override
            public void onResponse(String response) {
                srl.setRefreshing(false);
                setUserInfo();
                myTool.showInfo("用户信息更新成功！");
            }
        });
    }
}
