/**
 * 发现
 */
package com.cqu.expstuer.ui.homepage;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cqu.expstuer.R;
import com.cqu.expstuer.data.SEData;
import com.cqu.expstuer.ui.base.FragmentWithOnResume;
import com.cqu.expstuer.ui.homepage.exptake.ExpressSendActivity;
import com.cqu.expstuer.utils.CommonTools;
import com.cqu.expstuer.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.List;

public class HomepageFragment extends FragmentWithOnResume implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private CommonTools myTool;
    private Banner mBanner;
    // 服务器获取
    List<String> images;
    List<String> titles;

    SwipeRefreshLayout refreshLayout;
    ExpressInfoFragment fgExpressInfo;

    LinearLayout llExpressTake;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);

        initView(view);
        initData();
        initBanner();
        initEvent();

        return view;
    }

    // 更新
    @Override
    public void update() {

    }

    private void initData() {

        if (fgExpressInfo == null)
            fgExpressInfo = new ExpressInfoFragment();

        getChildFragmentManager().beginTransaction().replace(R.id.fg_notice, fgExpressInfo).commit();
    }

    private void initBanner() {

        images = SEData.getImageUrls();
        titles = SEData.getImageTitles();

        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .setImageLoader(new GlideImageLoader())
                .setImages(images)
                .setBannerAnimation(Transformer.ZoomOut)
                .setBannerTitles(titles)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .start();
    }

    void setClickListenerOnThis(@IdRes int id) {
        View v = view.findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
    }

    private void initEvent() {
        refreshLayout.setOnRefreshListener(this);
        llExpressTake.setOnClickListener(this);
        setClickListenerOnThis(R.id.ll_other_task);
        setClickListenerOnThis(R.id.ll_send);
        setClickListenerOnThis(R.id.ll_return);
    }

    private void initView(View v) {
        myTool = new CommonTools(getActivity());
        mBanner = (Banner) v.findViewById(R.id.banner);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        llExpressTake = (LinearLayout) v.findViewById(R.id.ll_express_take);

        myTool.setHeightByWindow(mBanner);

//        fgExpressInfo = new ExpressInfoFragment();
//        FragmentTransaction t = getChildFragmentManager().beginTransaction();
//        t.replace(R.id.fg_notice, fgExpressInfo).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_express_take:
                myTool.startActivity(ExpressTakeActivity.class);
                break;
            case R.id.ll_other_task:
                myTool.startActivity(OtherTaskActivity.class);
                break;
            case R.id.ll_send:
                myTool.startActivity(ExpressSendActivity.class);
                break;
            case R.id.ll_return:
                myTool.startActivity(BookReturnActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1500);
    }
}
