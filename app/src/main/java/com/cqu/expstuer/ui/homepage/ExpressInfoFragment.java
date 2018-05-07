/**
 * 通知
 */
package com.cqu.expstuer.ui.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cqu.expstuer.R;
import com.cqu.expstuer.utils.CommonTools;

public class ExpressInfoFragment extends Fragment implements View.OnClickListener {
    private View view;
    private CommonTools myTool;
    ImageView ivImg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_express_info, container, false);
        initView(view);

        initData();
        initEvent();

        return view;
    }

    private void initData() {
        Glide.with(getActivity()).load("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3066196835,2948820611&fm=23&gp=0.jpg").into(ivImg);
    }

    private void initEvent() {
    }

    private void initView(View v) {
        myTool = new CommonTools(getActivity());
        ivImg = (ImageView) v.findViewById(R.id.iv_img);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
