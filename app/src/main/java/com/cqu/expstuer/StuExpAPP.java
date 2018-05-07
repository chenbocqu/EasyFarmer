package com.cqu.expstuer;

import android.app.Application;

import com.cqu.expstuer.utils.PreviewImageLoader;
import com.mob.MobSDK;
import com.previewlibrary.ZoomMediaLoader;


public class StuExpAPP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        ZoomMediaLoader.getInstance().init(new PreviewImageLoader());
    }
}
