package com.bmn.sdk.weixin.bean;

import java.awt.font.ShapeGraphicAttribute;

public class OrderBean {

    private String orderId;

    private int returnCode;
    private int totalFee;
    private long uid;
    private int money;
    private String feeType;

    public String getFeeType() {
        return null;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int getMoney() {
        return money;
    }
}
