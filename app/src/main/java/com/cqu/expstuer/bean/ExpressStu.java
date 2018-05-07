/**
 * 用于展示在附近的小学递ListView上的数据
 */
package com.cqu.expstuer.bean;

import java.io.Serializable;

public class ExpressStu implements Serializable {
    private String imageUrl;//头像URL
    private String nickName; //昵称
    private String signature;//签名
    private String sex;//性别
    private int orderNum;//接单量
    private String startAddress;
    private String endAddress;

    private String startTime;
    private String endTime;

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    private String trip;

    public boolean isNewTrip() {
        return isNewTrip;
    }

    public void setNewTrip(boolean newTrip) {
        isNewTrip = newTrip;
    }

    private boolean isNewTrip;

    public static ExpressStu getInstance() {
        return new ExpressStu();
    }

    public ExpressStu() {

    }

    public ExpressStu(String imageUrl, String nickName, String signature, String sex, int orderNum) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
        this.signature = signature;
        this.sex = sex;
        this.orderNum = orderNum;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
