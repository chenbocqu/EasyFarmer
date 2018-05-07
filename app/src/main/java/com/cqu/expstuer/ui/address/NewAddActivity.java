package com.cqu.expstuer.ui.address;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cqu.expstuer.R;
import com.cqu.expstuer.bean.ContactAddress;
import com.cqu.expstuer.bean.JsonBean;
import com.cqu.expstuer.ui.base.TitleBarActivity;
import com.cqu.expstuer.utils.GetJsonDataUtil;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class NewAddActivity extends TitleBarActivity {

    public static final String NEW_ADDRESS = "NEW_ADDRESS";
    EditText edtName, edtPhone, edtDetialAdd;
    TextView tvCity;
    ContactAddress mAddress;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;
    SweetAlertDialog mDialog;

    @Override
    public String getUITitle() {
        return "新建联系人";
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_new_address;
    }

    @Override
    public void init() {

        mAddress = new ContactAddress();

        // 加载城市数据
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        setRightMenu("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress(); //保存地址信息
            }
        });
        initView();
    }

    /**
     * 保存所新建的联系人地址信息
     */
    private void saveAddress() {

        if (isEmpty(edtName, "姓名不能为空！")) return;
        if (isEmpty(edtPhone, "电话不能为空！")) return;

        int len = edtPhone.getText().toString().trim().length();

        if (len == 11 || len == 8) ;
        else {
            hintEdt(edtPhone, "电话格式有误！");
            return;
        }

        if (mAddress.getProvince() == null) {
            myTool.showInfo("请选择区域！");
            return;
        }

        if (isEmpty(edtDetialAdd, "详细地址不能为空！")) return;

        mAddress.setContactName(edtName.getText().toString());
        mAddress.setPhone(edtPhone.getText().toString().trim());
        mAddress.setDetialAddress(edtDetialAdd.getText().toString());


        // 开始添加信息
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("正在保存...");
        mDialog.show();

        Map map = new HashMap();

        map.put("userID", myTool.getUserId());
        map.put("receiveName", mAddress.getContactName());
        map.put("receiveTel", mAddress.getPhone());

        map.put("receiveCampus", "重庆大学");
        map.put("receiveBuilding", "12栋");
        map.put("receiveBuildno", "706");

        map.put("receiveContent", mAddress.getDetialAddress());

        // 添加地址
        OkHttpUtils.get().url(myTool.getServAdd() + "addReceiveAddress.do")
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myTool.log(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        myTool.log(response);
                    }
                });

        tvCity.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.setTitleText("保存成功")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                mDialog.dismissWithAnimation();

                                Intent intent = new Intent();
                                intent.putExtra(NEW_ADDRESS, mAddress);
                                setResult(RESULT_OK, intent);

                                finish();
                            }
                        })
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            }
        }, 1500);

//        OkHttpUtils.get().url(myTool.getServAdd() + "addinfo")
//                .addParams("", "")
//                .addParams("", "")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        mDialog.setTitleText("失败")
//                                .setContentText(e.getMessage())
//                                .setConfirmText("稍后重试")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        sweetAlertDialog.dismissWithAnimation();
//                                    }
//                                })
//                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//
//                        mDialog.setTitleText("保存成功")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        finish();
//                                    }
//                                })
//                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                    }
//                });
    }

    private boolean isEmpty(final EditText edt, final String info) {

        if ("".equals(edt.getText().toString().trim())) {
            hintEdt(edt, info);
            return true;
        }
        return false;
    }

    private void hintEdt(final EditText edt, final String info) {

        YoYo.with(Techniques.Shake)
                .duration(800)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        edt.requestFocus();
                        edt.setError(info);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(edt);
    }

    void initView() {
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        tvCity = (TextView) findViewById(R.id.tv_city);
        edtDetialAdd = (EditText) findViewById(R.id.edt_detial_address);
    }

    @Override
    protected void initEventForTitleBar() {
        super.initEventForTitleBar();
        restrictTextLenth(edtName, 5); // 最长不超过5
        restrictTextLenth(edtPhone, 11); // 最长不超过11
        findViewById(R.id.iv_phone).setOnClickListener(this);
        findViewById(R.id.tv_city).setOnClickListener(this);
    }

    // 为EditText添加一个最大长度
    private void restrictTextLenth(final EditText edt, final int maxLen) {

        if (edt == null) return;

        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String input = s.toString();

                int len = input.length();
                // 最长maxLen个字符
                if (len > maxLen) {
                    input = input.substring(0, maxLen);
                    edt.setText(input);
                    edt.setSelection(input.length());//将光标移至文字末尾
                    myTool.showInfo("最大长度不超过" + maxLen + "！");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_phone:

                myTool.showInfo("暂无开放！");

                break;

            case R.id.tv_city:
                if (isLoaded) {
                    ShowPickerView();
                } else {
                    myTool.showInfo("数据加载中，请稍后...");
                }

                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

//                        Toast.makeText(JsonDataActivity.this,"Begin Parse Data",Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//                    Toast.makeText(this,"Parse Succeed",Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
//                    Toast.makeText(JsonDataActivity.this,"Parse Failed",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };


    private void ShowPickerView() {// 弹出选择器

        // 如果软键盘打开，则关掉键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            imm.hideSoftInputFromWindow(tvCity.getWindowToken(), 0); //强制隐藏键盘
        }

        // 显示三级菜单
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + " " +
                        options2Items.get(options1).get(options2) + " " +
                        options3Items.get(options1).get(options2).get(options3);

                tvCity.setText(tx);

                mAddress.setProvince(options1Items.get(options1).getPickerViewText());
                mAddress.setCity(options2Items.get(options1).get(options2));
                mAddress.setArea(options3Items.get(options1).get(options2).get(options3));
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }
}
