package com.cqu.expstuer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqu.expstuer.listener.OnFragmentListener;
import com.cqu.expstuer.ui.base.FragmentWithOnResume;
import com.cqu.expstuer.ui.homepage.HomepageFragment;
import com.cqu.expstuer.ui.login.LoginActivity;
import com.cqu.expstuer.ui.nearby.NearbyFragment;
import com.cqu.expstuer.ui.parcel.ParcelFragment;
import com.cqu.expstuer.ui.qrcode.ScanActivity;
import com.cqu.expstuer.ui.userinfo.UserInfoFragment;
import com.cqu.expstuer.utils.CommonTools;
import com.cqu.expstuer.view.IndicatorIcon;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, OnFragmentListener {

    @ViewById
    LinearLayout llBack;

    @ViewById(R.id.tv_title)
    TextView mTvTitle;

    @ViewById(R.id.tv_menu)
    TextView tvMenu;

    @ViewById(R.id.id_viewpager)
    ViewPager mViewPager;

    private List<FragmentWithOnResume> mTabs = new ArrayList<>();
    private String[] mTitles = new String[]{"首页", "附近", "包裹", "个人信息"};
    private FragmentPagerAdapter mAdapter;

    @ViewById(R.id.ll_menu)
    LinearLayout llLogin;

    @ViewById(R.id.ll_icon_menu)
    LinearLayout llMsg;

    CommonTools myTool;
    int curIndex = 0, pageIndex = 0;

    private List<IndicatorIcon> mTabIndicators = new ArrayList<IndicatorIcon>();

    @AfterViews
    void doSomeThing() {
        initView();
        initDatas();
        setFragemnts(); //加载页面及数据
        initEvent();
    }

    /**
     * 初始化所有事件
     */
    private void initEvent() {
        llLogin.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
    }

    private void setFragemnts() {
        // 载入主UI的几个页面
        loadFragments();

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

        };
        mViewPager.setAdapter(mAdapter);
    }

    /**
     * 加载主界面的几个子UI
     */
    private void loadFragments() {
        // 首页
        mTabs.add(new HomepageFragment());

        // 附近的小学递
        mTabs.add(new NearbyFragment());

        // 包裹数据
        mTabs.add(new ParcelFragment());

        // 用户的个人信息
        mTabs.add(new UserInfoFragment());
    }

    private void initView() {

        myTool = new CommonTools(this);

        // 拿到底部的四个自定义View
        mTabIndicators.add((IndicatorIcon) findViewById(R.id.id_indicator_one));
        mTabIndicators.add((IndicatorIcon) findViewById(R.id.id_indicator_two));
        mTabIndicators.add((IndicatorIcon) findViewById(R.id.id_indicator_three));
        mTabIndicators.add((IndicatorIcon) findViewById(R.id.id_indicator_four));

        //指示器设置监听
        for (IndicatorIcon indicator : mTabIndicators)
            indicator.setOnClickListener(this);

        //默认是首页设置为选中状态，默认首页title
        ((IndicatorIcon) findViewById(R.id.id_indicator_one)).setIconAlpha(1.0f);

        llBack.setVisibility(View.GONE);
        setUITitle(0);
    }

    /**
     * 设置导航顶部的显示文字
     *
     * @param position
     */
    private void setUITitle(int position) {
        mTvTitle.setText(mTitles[position]);
    }

    @Override
    public void onClick(View v) {
        setIndicators(v);
        doBtnClick(v);
    }

    /**
     * 处理按钮的点击事件
     *
     * @param v
     */
    private void doBtnClick(View v) {
        switch (v.getId()) {
            case R.id.ll_menu:
                myTool.startActivity(LoginActivity.class);
                break;
        }
    }


    /**
     * 设置底部指示器的点击及指示效果
     *
     * @param v
     */
    private void setIndicators(View v) {
        // 根据按键切换ViewPager
        switch (v.getId()) {
            case R.id.id_indicator_one:
                setItemByIndex(0);
                break;
            case R.id.id_indicator_two:
                setItemByIndex(1);
                break;
            case R.id.id_indicator_three:
                setItemByIndex(2);
                break;
            case R.id.id_indicator_four:
                setItemByIndex(3);
                break;
        }
    }

    private void setItemByIndex(int index) {
        //首先把其他Tab清除
        resetOtherTabs();
        mTabIndicators.get(index).setIconAlpha(1.0f);
        mViewPager.setCurrentItem(index, false);
        refreshCurPage(); // 刷新页面
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // 设置指示器，渐变效果
        if (positionOffset > 0) {
            IndicatorIcon left = mTabIndicators.get(position);
            IndicatorIcon right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        // 设置title
        setUITitle(position);
        curIndex = position;
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        // 0是停完全,分发update函数
        if (state == 0)
            refreshCurPage(); // 刷新
    }

    private void refreshCurPage() {

        if (pageIndex == curIndex) return;

        pageIndex = curIndex;
        mTabs.get(pageIndex).update();
    }

    /**
     * MainActivity重新获得焦点是读取登录状态并设置属性
     */
    @Override
    protected void onResume() {
        super.onResume();

        llBack.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mTabs.isEmpty())
                    mTabs.get(curIndex).update();
            }
        }, 500);

        initDatas();
    }

    private void initDatas() {

        tvMenu.setText("登录");

        //设置见或者不见
        llLogin.setVisibility(myTool.isLogin() ? View.GONE : View.VISIBLE);
        llMsg.setVisibility(myTool.isLogin() ? View.VISIBLE : View.GONE);
    }

    @Click(R.id.ll_icon_menu)
    void llMsg() {
//        myTool.startActivityForResult("notFirstIn", IntroActivity.class, 0);
        myTool.startActivity(ScanActivity.class);
    }

    /**
     * 子页面发过来的消息
     */
    @Override
    public void onMessage(Fragment fg, Object obj) {
        if (fg.getClass().equals(UserInfoFragment.class)) {
            switch ((String) obj) {
                // 注销后重新刷新页面
                case "logout":
                    initDatas();
                    setItemByIndex(0);
                    break;
            }
        }
    }
}

