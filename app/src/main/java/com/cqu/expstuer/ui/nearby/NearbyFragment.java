/**
 * 通知
 */
package com.cqu.expstuer.ui.nearby;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.adapter.StuAdapter;
import com.cqu.expstuer.bean.ExpressStu;
import com.cqu.expstuer.data.SEData;
import com.cqu.expstuer.ui.base.FragmentWithOnResume;
import com.cqu.expstuer.utils.CommonTools;
import com.mingle.widget.LoadingView;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;

import java.util.List;

public class NearbyFragment extends FragmentWithOnResume implements View.OnClickListener {
    private View view;
    private CommonTools myTool;
    ListView mListView;
    StuAdapter mAdapter;
    List<ExpressStu> mStus;
    View vNodata;
    LoadingView loadView;
    SHSwipeRefreshLayout mRefreshLayout;

    @Override
    public void update() {
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nearby, container, false);

        initView(view);
        initEvent();
        initData();

        return view;
    }

    private void initData() {
        if (vNodata == null) return;
        vNodata.setVisibility(View.GONE);
        vNodata.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadView.setVisibility(View.GONE);
                mStus = SEData.getStus();
                mAdapter = new StuAdapter(getActivity(), R.layout.item_express_stu, mStus);
                mListView.setAdapter(mAdapter);
            }
        }, 3000);
    }

    private void initEvent() {
        mRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onLoading() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishLoadmore();
                    }
                }, 1500);
            }

            @Override
            public void onRefreshPulStateChange(float v, int i) {

            }

            @Override
            public void onLoadmorePullStateChange(float v, int i) {

            }
        });
    }

    private void initView(View v) {
        myTool = new CommonTools(getActivity());
        mListView = (ListView) v.findViewById(R.id.listView);
        vNodata = v.findViewById(R.id.rl_nodata);
        loadView = (LoadingView) v.findViewById(R.id.loadView);
        mRefreshLayout = (SHSwipeRefreshLayout) v.findViewById(R.id.refreshLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
