package com.cqu.expstuer.bean;

import java.io.Serializable;


public class Contact implements Serializable {

    String name;
    String phone;
    String country; // 国家代码


    public Contact() {
    }

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactName='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
