package com.cqu.expstuer.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cqu.expstuer.utils.CommonTools;

public class FragmentWithOnResume extends Fragment {

    protected CommonTools comTools;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        comTools = new CommonTools(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // 使用此方法更新UI
    public void update() {

    }

    // 设置文本，此类方法都在update()中调用
    protected void setText(@IdRes int id, String text) {

        if (getView() == null) return;

        TextView tv = (TextView) getView().findViewById(id);

        if (tv != null && text != null)
            tv.setText(text);
    }

    protected void setImg(@IdRes int id, String url) {
        if (getView() == null) return;

        ImageView iv = (ImageView) getView().findViewById(id);

        if (iv != null && url != null)
            Glide.with(getActivity()).load(url).centerCrop().into(iv);

    }
}
