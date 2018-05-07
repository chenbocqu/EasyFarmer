/**
 * 通用工具类
 *
 * @时间：2016-7-6
 */
package com.cqu.expstuer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.Img;
import com.cqu.expstuer.bean.User;
import com.cqu.expstuer.disk.DiskLruCacheHelper;
import com.cqu.expstuer.ui.login.IpModifyDialog;
import com.cqu.expstuer.ui.other.HintActivity;
import com.previewlibrary.GPreviewBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CommonTools {

    public static final String PACKAGE_NAME = "com.cqu.stuexpress";

    public static final String USER_FILE_PATH = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME; //用户文件
    private static final String KEY_FIRSTIN = "KEY_FIRSTIN";

    private Context mContext;
    private Fragment mFragment;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    DiskLruCacheHelper cacheHelper = null;

    public CommonTools(Context context) {
        this.mContext = context;

        init();
    }

    public CommonTools(Fragment fragment) {
        this.mFragment = fragment;
        this.mContext = fragment.getActivity();

        init();
    }

    private void init() {
        sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE);
        editor = sp.edit();

        try {
            cacheHelper = new DiskLruCacheHelper(mContext);
        } catch (IOException e) {
            showExceptionInfo(e);
        }
    }

    public void showExceptionInfo(Exception e) {
        showExceptionInfo(e.getMessage());
    }

    public void showExceptionInfo(String info) {
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("异常！")
                .setContentText("未处置的异常，异常信息为[e:" + info + "]")
                .setConfirmText("确  定!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }


    /**
     * Toast消息
     *
     * @param msg
     * @param duration
     */
    private void showInfo(String msg, int duration) {
        Toast.makeText(mContext, msg, duration).show();
    }

    public void showInfo(String msg) {
        showInfo(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 从网络平滑加载图片到指定ImageView上
     */
    public CommonTools showImage(String url, ImageView imageView) {
        return showImage(url, imageView, R.drawable.img_holder);
    }

    public CommonTools showImage(String url, ImageView imageView, @DrawableRes int errRes) {

        if (imageView == null || url == null || "".equals(url)) return this;

        Glide.with(mContext).load(url).error(errRes)
                .centerCrop().crossFade().into(imageView);

        return this;
    }

    /**
     * 设置第一次登陆
     *
     * @param firstIn
     * @return
     */
    public CommonTools setFirstIn(boolean firstIn) {
        editor.putBoolean(KEY_FIRSTIN, firstIn);
        editor.commit();
        return this;
    }

    public boolean isFirstIn() {
        return sp.getBoolean(KEY_FIRSTIN, true);
    }


    /**
     * 启动activity
     *
     * @param activity
     */
    public void startActivity(Class<?> activity) {
        try {

            if (mFragment != null)
                mFragment.startActivity(new Intent(mContext, activity));
            else
                mContext.startActivity(new Intent(mContext, activity));

        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    /**
     * 带参数的启动
     *
     * @param o        传递参数
     * @param activity 要启动的avtivity
     * @return 当前实例
     */
    public CommonTools startActivity(Serializable o, Class<?> activity) {
        try {
            //跳转到详细信息界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(o.getClass().getName(), o);
            Intent intent = new Intent(mContext, activity);
            intent.putExtras(bundle);

            mContext.startActivity(intent);
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return this;
    }

    /**
     * 得到上一页面传递过来的参数
     *
     * @param o 已经实例化的类型传递参数
     * @return 上一页面传递过来的参数
     */
    public Serializable getParam(@NonNull Serializable o) {
        Bundle bundle = ((Activity) mContext).getIntent().getExtras();
        if (bundle == null)
            return "";
        return (Serializable) bundle.get(o.getClass().getName());
    }

    /**
     * 得到上一页面传递过来的参数
     *
     * @param c 要接收的类型
     * @return 上一页面传递过来的参数
     */
    public Serializable getParam(@NonNull Class c) {
        Bundle bundle = ((Activity) mContext).getIntent().getExtras();
        if (bundle == null)
            return "";
        return (Serializable) bundle.get(c.getName());
    }

    /**
     * 启动activity,得到返回结果
     *
     * @param activity 要启动的activity
     */
    public void startActivityForResult(Class<?> activity, int requestCode) {
        try {
            ((Activity) mContext).startActivityForResult(new Intent(mContext, activity), requestCode);
        } catch (Exception e) {
            startException(e);
        }
    }

    public CommonTools startActivityForResult(Serializable o, Class<?> activity, int requestCode) {
        try {
            //跳转到详细信息界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(o.getClass().getName(), o);
            Intent intent = new Intent(mContext, activity);
            intent.putExtras(bundle);

            ((Activity) mContext).startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            startException(e);
        }
        return this;
    }

    private void startException(Exception e) {
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("异常！")
                .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                .setConfirmText("确  定!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    public void log(String info) {

        if (!debugable()) return;

        if (mFragment != null)
            Log.i("DEBUG_INFO_" + mFragment.getClass().getSimpleName(), info);
        else
            Log.i("DEBUG_INFO_" + mContext.getClass().getSimpleName(), info);
    }

    //可否调试
    private boolean debugable() {
        return true;
    }

    public void setIP(String ip) {
        editor.putString(StuExpValues.IP_ADDRESS, ip);
        editor.commit();
    }

    // 获得服务器ip地址
    public String getIP() {
        return sp.getString(StuExpValues.IP_ADDRESS, StuExpValues.IP);
    }

    // 获取服务器数据地址
    public String getServAdd() {
        return "http://" + getIP() + ":" + StuExpValues.PORT + "//studentExpress/";
    }

    public String getSocketAdd() {
        return "http://" + getIP() + ":" + StuExpValues.PORT + "//studentExpress/Receiveorder";
    }


    public int getScreenWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        return dpMetrics.widthPixels;
    }

    /**
     * 设置空间布局为屏幕宽度的ratio倍，如1/3
     *
     * @param ratio
     */
    public void setHeightByWindow(View v, float ratio) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = (int) (getScreenWidth() * ratio);
        v.setLayoutParams(lp);
    }

    public void setHeightByWindow(View v) {
        setHeightByWindow(v, 9 / 16f);
    }


    public void showPopFormBottom(PopupWindow win, View mainView, final PopupWindow.OnDismissListener l) {
        final WindowManager.LayoutParams[] params = {((Activity) mContext).getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        ((Activity) mContext).getWindow().setAttributes(params[0]);

        // 设置Popupwindow显示位置（从底部弹出）
        win.showAtLocation(mainView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        win.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = ((Activity) mContext).getWindow().getAttributes();
                params[0].alpha = 1f;
                ((Activity) mContext).getWindow().setAttributes(params[0]);
                if (l != null) l.onDismiss();
            }
        });
    }

    public void showPopFormBottom(PopupWindow win, View mainView) {
        showPopFormBottom(win, mainView, null);
    }

    public void toHint() {
        startActivity(HintActivity.class);
    }

    public CommonTools setUserName(String userName) {
        editor.putString(StuExpValues.USER_NAME, userName);
        editor.commit();
        return this;
    }

    /**
     * 得到token
     *
     * @return token信息
     */
    public String getUserId() {
        return sp.getString(StuExpValues.USER_NAME, "001");
    }

    public CommonTools setToken(String token) {
        editor.putString(StuExpValues.KEY_TOKEN, token);
        editor.commit();
        return this;
    }

    /**
     * 得到token
     *
     * @return token信息
     */
    public String getToken() {
        return sp.getString(StuExpValues.KEY_TOKEN, "");
    }

    // 环信账户是否创建
    public CommonTools setEMCountExist(boolean isExist) {
        editor.putBoolean(StuExpValues.KEY_EMCOUNT_EXIST, isExist);
        editor.commit();
        return this;
    }

    public boolean isEMCountExist() {
        return sp.getBoolean(StuExpValues.KEY_EMCOUNT_EXIST, false);
    }

    // 登录标志
    public CommonTools setLoginFlag(boolean isLogin) {
        editor.putBoolean(StuExpValues.LOGIN_FLAG, isLogin);
        editor.commit();
        return this;
    }

    public boolean isLogin() {
        return sp.getBoolean(StuExpValues.LOGIN_FLAG, false);
    }


    // ORM存储方案
    public User getUserInfo() {
        User user = new User();
        if (cacheHelper != null)
            user = cacheHelper.getAsSerializable(StuExpValues.USER_INFO + getUserId());
        return user;
    }

    /**
     * 通过用户Id存储用户信息到本地
     *
     * @param userInfo 要保存用户信息
     * @return 当前实例
     */
    public CommonTools setUserInfo(User userInfo) {
        cacheHelper.put(StuExpValues.USER_INFO + getUserId(), userInfo);
        return this;
    }


    // 默认性别为男同志
    public String getUserSex() {
        User user = getUserInfo();

        if (user != null && user.getSex() != null)
            return user.getSex();

        return "男";
    }

    // 存放性别
    public void setUserSex(String sex) {
        User user = getUserInfo();

        if (user == null) user = new User();

        user.setSex(sex);

        setUserInfo(user);
    }

    public Uri getUserHeadFileUri() {
        File file = new File(USER_FILE_PATH + "/" + getUserId(), "head.jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    public Uri getUserBgFileUri() {
        File file = new File(USER_FILE_PATH + "/" + getUserId(), "bg.jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    /**
     * 设置性别图标
     */
    public CommonTools showSex(String sex, ImageView imageView) {
        if (sex == null) {//为空就直接返回了避免错误
            return this;
        }
        Bitmap female = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.female);
        Bitmap male = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.male);
        //设置性别图标
        switch (sex) {
            case "男":
                imageView.setImageBitmap(male);
                break;
            case "女":
                imageView.setImageBitmap(female);
                break;
            default:
                log("显示性别不成功:sex[" + sex + "],imageView[" + imageView.toString() + "]");
                break;
        }
        return this;
    }

    // 配置IP
    public void modifyIp() {
        IpModifyDialog ipModifyDialog;
        ipModifyDialog = new IpModifyDialog(mContext, R.style.Translucent_NoTitle);
        ipModifyDialog.show();
    }

    public void previewImg(ImageView iv, String url) {

        if (iv == null || url == null) return;

        //在你点击时，调用computeBoundsBackward（）方法
        Rect bounds = new Rect();
        iv.getGlobalVisibleRect(bounds);
        Img img = new Img(url);
        img.setBounds(bounds);

        List<Img> list = new ArrayList<>();
        list.add(img);

        GPreviewBuilder.from((Activity) mContext)
                .setData(list)
                .setCurrentIndex(0)
                .setType(GPreviewBuilder.IndicatorType.Number)
                .start();
    }

    public String getSignTime() {
        return sp.getString(StuExpValues.KEY_SIGN_TIME, "");
    }

    public CommonTools setSignTime(String time) {
        editor.putString(StuExpValues.KEY_SIGN_TIME, time);
        editor.commit();
        return this;
    }
}
