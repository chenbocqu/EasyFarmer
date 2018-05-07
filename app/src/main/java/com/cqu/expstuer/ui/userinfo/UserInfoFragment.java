/**
 * 发现
 */
package com.cqu.expstuer.ui.userinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.User;
import com.cqu.expstuer.listener.OnFragmentListener;
import com.cqu.expstuer.listener.OnRequestListener;
import com.cqu.expstuer.ui.address.AddressManageActivity;
import com.cqu.expstuer.ui.base.FragmentWithOnResume;
import com.cqu.expstuer.ui.setting.SettingActivity;
import com.cqu.expstuer.utils.CommonTools;
import com.cqu.expstuer.utils.DataMaker;

public class UserInfoFragment extends FragmentWithOnResume implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private CommonTools myTool;
    SwipeRefreshLayout refreshLayout;
    OnFragmentListener l;
    User user;
    ImageView ivSex, ivHeadImg, ivBgImg;

    @Override
    public void update() {
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_userinfo, container, false);
        initView(view);

        initData();
        initEvent();

        return view;
    }

    private void initData() {

        if (myTool == null) return;

        user = myTool.getUserInfo();

        if (user == null) return;

        if (user.getNickName() != null)
            setText(R.id.tv_nickname, user.getNickName());
        else setText(R.id.tv_nickname, user.getSeId());

        if (user.getSignature() != null)
            setText(R.id.tv_signature, user.getSignature());
        else setText(R.id.tv_signature, "暂无签名！");

        setText(R.id.tv_balance, "¥" + user.getBalance());
        setText(R.id.tv_integral, user.getIntegral() + "");
        setImg(R.id.iv_headimg, user.getImageUrl());
        setImg(R.id.iv_bgimg, user.getBgUrl());

        myTool.showSex(user.getSex(), ivSex);
    }


    private void initEvent() {
        refreshLayout.setOnRefreshListener(this);

        setClickListenerOnThis(R.id.rl_join);
        setClickListenerOnThis(R.id.rl_manage_address);
        setClickListenerOnThis(R.id.rl_myinfo);
        setClickListenerOnThis(R.id.rl_logout);
        setClickListenerOnThis(R.id.rl_setting);
        setClickListenerOnThis(R.id.iv_headimg);
        setClickListenerOnThis(R.id.iv_bgimg);
        setClickListenerOnThis(R.id.rl_integral);
        setClickListenerOnThis(R.id.rl_balance);
    }

    private void initView(View v) {

        myTool = new CommonTools(this);

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        ivSex = (ImageView) v.findViewById(R.id.iv_usersex);
        ivHeadImg = (ImageView) v.findViewById(R.id.iv_headimg);
        ivBgImg = (ImageView) v.findViewById(R.id.iv_bgimg);

        myTool.setHeightByWindow(v.findViewById(R.id.rl_head_view), 10.5f / 16f);
    }

    void setClickListenerOnThis(@IdRes int id) {
        View v = view.findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_manage_address:
                myTool.startActivity(AddressManageActivity.class);
                break;

            // 成为小学递
            case R.id.rl_join:
                myTool.toHint();
                break;

            case R.id.rl_myinfo:
                myTool.startActivity(MyInfoActivity.class);
                break;

            case R.id.rl_logout:

                if (myTool.isLogin()) {
                    myTool.setLoginFlag(false);
                    if (l != null) l.onMessage(this, "logout");
                } else myTool.showInfo("暂未登录账号！");

                break;

            case R.id.rl_setting:
                myTool.startActivity(SettingActivity.class);
                break;

            case R.id.iv_headimg:
                myTool.previewImg(ivHeadImg, user.getImageUrl());
                break;
            case R.id.iv_bgimg:
                myTool.previewImg(ivBgImg, user.getBgUrl());
                break;

            case R.id.rl_integral:
                myTool.startActivity(IntegralActivity.class);
                break;
            case R.id.rl_balance:
                myTool.startActivity(BalanceActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh() {

        DataMaker.updateUserInfo(getActivity(), new OnRequestListener() {
            @Override
            public void onError(Exception e) {
                refreshLayout.setRefreshing(false);
                myTool.showInfo("网络请求异常，请稍后再试！");
            }

            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                initData();
                myTool.showInfo("用户信息更新成功！");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (l == null) l = (OnFragmentListener) activity;
    }
}
