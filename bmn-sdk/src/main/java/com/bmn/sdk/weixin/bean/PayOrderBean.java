
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.sdk.weixin.bean;

/**
 * 
 *
 * @date 2018-06-08
 * @author
 */
public class PayOrderBean {

    // 商品描述（必填）
    private String body;
    // 商品详情（可选）
    private String detail;
    // 附加数据（可选）
    private String attach;

    // 商品订单id
    private String orderId;
    // 单位：元
    private int money;

    private String clientIp;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

}
