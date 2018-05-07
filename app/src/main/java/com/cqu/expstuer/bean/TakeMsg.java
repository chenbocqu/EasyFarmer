package com.cqu.expstuer.bean;


import java.io.Serializable;

/**
 * 取件信息，取件人姓名/手机，取件用短信，期望派送时间，类型与重量，留言与备注，赏金金额
 */

public class TakeMsg implements Serializable {

    // 取件人手机信息
    String name;
    String phone;

    // 取件短信
    Sms sms;

    String deliveryTime;
    String type;

    String weight; // kg

    String remark; // 备注

    int reward; // 悬赏金额


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

    public Sms getSms() {
        return sms;
    }

    public void setSms(Sms sms) {
        this.sms = sms;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "TakeMsg{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sms=" + sms.toString() +
                '}';
    }
}
