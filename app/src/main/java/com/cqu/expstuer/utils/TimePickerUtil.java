package com.cqu.expstuer.utils;

import android.content.Context;

import com.bigkoo.pickerview.TimePickerView;

import java.util.Calendar;
import java.util.Date;

public class TimePickerUtil {

    public static void pickTimeDialog(Context context, TimePickerView.OnTimeSelectListener l) {
        pickTimeDialog(context, "时间选择", null, l);
    }

    public static void pickTimeDialog(Context context, String title, Date date, TimePickerView.OnTimeSelectListener l) {

        Calendar selectedDate = Calendar.getInstance();

        if (date != null)
            selectedDate.setTime(date);

        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 5, 1); // 起始时间

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.getInstance().get(Calendar.YEAR) + 10, 1, 1);

        boolean[] type = new boolean[]{false, false, false, true, true, false};//显示类型 默认全部显示

        TimePickerView pvTime = new TimePickerView.Builder(context, l)
                .setType(type)//月日时分
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(0xff636363)//标题文字颜色
                .setSubmitColor(0xff00abff)//确定按钮文字颜色
                .setCancelColor(0xff636363)//取消按钮文字颜色
                .setTitleBgColor(0xffe7e7e7)//标题背景颜色 Night mode
                .setBgColor(0xffffffff)//滚轮背景颜色 Night mode
//                .setRange(Calendar.getInstance().get(Calendar.YEAR) - 10, Calendar.getInstance().get(Calendar.YEAR) + 10)//默认是1900-2100年
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvTime.show();
    }
}
