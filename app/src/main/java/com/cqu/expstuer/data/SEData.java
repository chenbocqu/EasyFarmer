package com.cqu.expstuer.data;

import com.cqu.expstuer.bean.ContactAddress;
import com.cqu.expstuer.bean.ExpressStu;

import java.util.ArrayList;
import java.util.List;

public class SEData {

    public static List<String> getImageUrls() {
        List<String> images = new ArrayList<>();
        images.add("http://graduate.cqu.edu.cn/images/slide1.jpg");
        images.add("http://graduate.cqu.edu.cn/images/slide3.jpg");
        images.add("http://graduate.cqu.edu.cn/images/slide2.jpg");
        return images;
    }

    public static List<String> getImageTitles() {
        List<String> titles = new ArrayList<>();
        titles.add("一滴水也可以看到一个大世界");
        titles.add("生气勃勃的五教");
        titles.add("最美不过校园一角");
        return titles;
    }

    public static List<ExpressStu> getStus() {
        List<ExpressStu> stus = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            stus.add(ExpressStu.getInstance());
        return stus;
    }

    public static List<ContactAddress> getAddress() {

        List<ContactAddress> addresses = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            ContactAddress address = new ContactAddress();
            address.setContactName("用户" + i);
            address.setPhone("1502531689" + (i - 1));
            address.setProvince("重庆市");
            address.setCity("重庆市");
            address.setArea("沙坪坝区");
            address.setDetialAddress("重庆大学A区6舍70" + (i + 2));
            addresses.add(address);
        }

        return addresses;
    }

}
