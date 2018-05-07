package com.cqu.expstuer.bean;

import java.io.Serializable;

public class ContactAddress implements Serializable {


    Contact contact;

    // 联系人姓名和电话
    String contactName;
    String phone;

    // 省市区
    String province; // 省
    String city; // 市
    String area; //区县

    String detialAddress;

    public ContactAddress() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetialAddress() {
        return detialAddress;
    }

    public String getAddressDesc() {
        return getProvince() + " " + getCity() + " " + getArea() + " " + getDetialAddress();
    }

    public void setDetialAddress(String detialAddress) {
        this.detialAddress = detialAddress;
    }

    @Override
    public String toString() {
        return "ContactAddress{" +
                "contactName='" + contactName + '\'' +
                ", phone='" + phone + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", detialAddress='" + detialAddress + '\'' +
                '}';
    }
}
