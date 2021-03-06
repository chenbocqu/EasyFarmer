package com.cqu.expstuer.ui.qrcode;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cqu.expstuer.R;
import com.cqu.expstuer.utils.CommonTools;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends Activity implements QRCodeView.Delegate, View.OnClickListener {

    QRCodeView mQRCodeView;
    CommonTools myTool;
    ImageView ivBack, ivAlbum, ivFlashLight;
    boolean isFlashLightOn = false;
    private int REQUEST_CODE_CAMERA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        viewById();
        initEvent();
    }

    private void initEvent() {
        mQRCodeView.setDelegate(this);
        ivBack.setOnClickListener(this);
        ivAlbum.setOnClickListener(this);
        ivFlashLight.setOnClickListener(this);
    }

    private void viewById() {
        myTool = new CommonTools(this);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivAlbum = (ImageView) findViewById(R.id.iv_album);
        ivFlashLight = (ImageView) findViewById(R.id.iv_flash_light);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {

        vibrate();//震动
        mQRCodeView.stopSpot();

        if (!TextUtils.isEmpty(result)) {

            myTool.showInfo(result);
            mQRCodeView.startSpot(); // 重新扫描

//            mQRCodeView.stopCamera();
//            mQRCodeView.onDestroy();
//            Intent intent = new Intent(ScanActivity.this, ServiceManagerDianZhuNewActivity.class);
//            intent.putExtra("url", result);
//            //intent.setData(Uri.parse(result));
//            startActivity(intent);
//            finish();
        } else {
            Toast.makeText(this, "链接无效,请重新扫描", Toast.LENGTH_SHORT).show();
            mQRCodeView.startSpot();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
//        myTool.showInfo("无权限，申请权限！");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.FLASHLIGHT}, REQUEST_CODE_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            mQRCodeView.startCamera();
            mQRCodeView.startSpot();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mQRCodeView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mQRCodeView.startCamera();//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                mQRCodeView.startSpot();
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQRCodeView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mQRCodeView.stopCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            // 相册
            case R.id.iv_album:
                break;
            // 闪光灯
            case R.id.iv_flash_light:

                if (isFlashLightOn)
                    mQRCodeView.closeFlashlight();
                else
                    mQRCodeView.openFlashlight();

                isFlashLightOn = !isFlashLightOn;
                myTool.log("QRcode isFlashLightOn : " + isFlashLightOn);
                break;
        }
    }

    //震动器
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }
}
