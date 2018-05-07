/**
 * 带有titlebar的通用窗口必须包含titlebar.xml的布局
 */
package com.cqu.expstuer.ui.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.utils.CommonTools;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragmentActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import java.io.File;

public abstract class TitleBarActivity extends TakePhotoFragmentActivity implements View.OnClickListener {

    protected CommonTools myTool;

    private View.OnClickListener menuListener, iconListener;
    private String menuText;
    private int iconResId = 0;
    protected TakePhoto takePhoto;

    public static final String FORM_CAMERA = "FORM_CAMERA";
    public static final String FROM_FILE = "FROM_FILE";

    CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(200 * 1024).setMaxPixel(800).create();//最大200K

    protected void getPic(int aspectX, int aspectY, String from, Uri dest) {

        if (from == null) return;

        CropOptions mCropOptions = new CropOptions
                .Builder()
                .setAspectX(aspectX)
                .setAspectY(aspectY)
                .setWithOwnCrop(true)
                .create();

        takePhoto.onEnableCompress(compressConfig, false);

        switch (from) {
            case FORM_CAMERA:
                takePhoto.onPickFromCaptureWithCrop(dest, mCropOptions);
                break;

            case FROM_FILE:
                takePhoto.onPickFromDocumentsWithCrop(dest, mCropOptions);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        takePhoto = getTakePhoto();

        initDataForTitleBar();
        initEventForTitleBar();
    }

    protected void initEventForTitleBar() {
        registerClickListener(R.id.llBack);
        registerClickListener(R.id.ll_menu);
        registerClickListener(R.id.ll_icon_menu);
    }

    private void initDataForTitleBar() {

        myTool = new CommonTools(this);

        init();
        initTitleBar();
    }

    protected void setVisible(@IdRes int id, boolean isVisible) {
        View v = findViewById(id);
        if (v != null) v.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    protected void setText(@IdRes int id, String text) {
        TextView tv = (TextView) findViewById(id);
        if (tv != null && text != null)
            tv.setText(text);
    }

    protected void setEditText(@IdRes int id, String text) {
        EditText tv = (EditText) findViewById(id);
        if (tv != null && text != null)
            tv.setText(text);
    }

    protected void setImg(@IdRes int id, Bitmap bm) {

        ImageView iv = (ImageView) findViewById(id);

        if (iv != null)
            iv.setImageBitmap(bm);
    }

    protected void setImg(@IdRes int id, String url) {

        ImageView iv = (ImageView) findViewById(id);
        myTool.showImage(url, iv);
    }


    // 注册点击事件
    protected void registerClickListener(@IdRes int id) {

        View v = findViewById(id);

        if (v != null) v.setOnClickListener(this);
    }

    protected void setTitle(String title) {
        setText(R.id.tv_title, title);
    }

    private void initTitleBar() {
        setText(R.id.tv_title, getUITitle());

        if (menuListener != null || menuText != null) {
            setVisible(R.id.ll_menu, true);
            setText(R.id.tv_menu, menuText);
        }

        if (iconListener != null || iconResId != 0) {
            setVisible(R.id.ll_icon_menu, true);
            setImg(R.id.iv_icon_menu, BitmapFactory.decodeResource(getResources(), iconResId));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                this.finish();
                break;
            case R.id.ll_menu:
                if (menuListener != null)
                    menuListener.onClick(findViewById(R.id.ll_icon_menu)); // 文字菜单点击事件接口回调
                break;
            case R.id.ll_icon_menu:
                if (iconListener != null) iconListener.onClick(findViewById(R.id.ll_icon_menu));
                break;
        }
    }

    public String getUITitle() {
        return "";
    }

    public abstract void init();

    //设置布局文件
    protected abstract int getContentView();

    public void setRightMenu(String menuText, View.OnClickListener menuListener) {
        this.menuText = menuText;
        this.menuListener = menuListener;
    }

    public void setIconMenu(@DrawableRes int resId, View.OnClickListener iconListener) {
        this.iconResId = resId;
        this.iconListener = iconListener;
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        myTool.showInfo("选择已取消！");
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        myTool.showInfo("选择失败，请稍后重试！");
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        // 一张图片的时候，解析
        String path = result.getImage().getPath();
        File file = new File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(result.getImage().getPath());

        takeSuccess(path, bitmap, file);
    }

    // 多种形式直接返回
    protected void takeSuccess(String path, Bitmap bm, File file) {

    }

}
