
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.sdk.weixin.pay;

import com.bmn.sdk.weixin.WeixinPayServices;
import com.bmn.sdk.weixin.bean.CG_PayOrderMsg;
import com.bmn.sdk.weixin.bean.CG_PayOrderQueryMsg;
import com.bmn.sdk.weixin.bean.OrderBean;
import com.bmn.sdk.weixin.bean.PayCallbackBean;
import com.bmn.sdk.weixin.bean.PayOrderBean;
import com.bmn.sdk.weixin.bean.PayOrderParamBean;
import com.bmn.sdk.weixin.bean.PayOrderQueryReturnBean;
import com.bmn.sdk.weixin.bean.PayOrderReturnBean;
import com.bmn.util.JsonUtils;
import com.bmn.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 * @date 2018-06-11
 * @author
 */
public class PayTestServices {

    private static final PayTestServices instance = new PayTestServices();

    private PayTestServices() {
        WeixinPayServices.getInstance().addListener(new PayTestEventListener());
    }

    public static PayTestServices getInstance() {
        return instance;
    }

    private Map<Long, OrderBean> orderMap = new HashMap<>(8);

    /*
     * 支付成功回调
     */
    public void paycallback(String mchId, PayCallbackBean bean) {
        log("收到微信支付回调：" + JsonUtils.toJson(bean));

        if (bean.getReturnCode().equals(WXPayConstants.FAIL)) {
            log("支付失败");
            return;
        }

        if (bean.getResultCode().equals(WXPayConstants.FAIL)) {
            log("支付失败");
            return;
        }

        if (!bean.getMchId().equals(mchId)) {
            log("商家不一致");
            return;
        }

        boolean signValid = bean.getSignValid();
        if (!signValid) {
            log("支付通知，签名失败");
            return;
        }

        long orderId = Long.parseLong(bean.getOutTradeNo());
        if (!orderMap.containsKey(orderId)) {
            log("收到微信支付回调：订单不存在：" + orderId + "--" + orderMap);
        }

        OrderBean order = orderMap.get(orderId);

        if (!bean.getFeeType().equals(order.getFeeType())) {
            log("货币类型不一致");
            return;
        }

        int orderCode = order.getReturnCode();
        if (orderCode == 1) {
            log("收到重复通知");
            return;
        }

        if (order.getTotalFee() != bean.getTotalFee()) {
            log("订单总金额不一致");
            return;
        }

        order.setReturnCode(1);

    }


    private static long ORDER_ID = 36900005012l;

    /**
     * 统一下单接口
     */
    public void pay(CG_PayOrderMsg msg) throws Exception {
        long uid = msg.getUid();

        String openId = msg.getOpenId();

        // 223.104.3.206
        String clientIp = msg.getIp();


        ORDER_ID++;
        long orderId = ORDER_ID;
        long applyId = msg.getActivityId();
        int money = 100;
        if (applyId == 1) {
            money += 1;
        }
        if (applyId == 2) {
            money += 2;
        }
        if (applyId == 3) {
            money += 30;
        }
        if (applyId == 4) {
            money += 31;
        }
        if (applyId == 5) {
            money += 32;
        }
        if (applyId == 6) {
            money += 33;
        }
        if (applyId == 7) {
            money += 34;
        }

        OrderBean order = new OrderBean();
        order.setOrderId(Long.toString(orderId));
        order.setUid(uid);
        order.setMoney(money);
        order.setTotalFee(money);
        order.setFeeType("CNY");

        orderMap.put(orderId, order);

        payTest(openId, clientIp, order);
    }

    private void payTest(String openId, String clientIp, OrderBean userOrder)
            throws Exception {

        String body = "Itimi-活动套餐";

        PayOrderBean order = new PayOrderBean();
        order.setOrderId(userOrder.getOrderId());
        order.setBody(body);
        order.setMoney(userOrder.getMoney());
        order.setAttach("Test");
        order.setClientIp(clientIp);

        PayOrderReturnBean resultBean = WeixinPayServices.getInstance().order(openId, order);
        if (resultBean == null) {
            return;
        }

        if (resultBean.getReturnCode().equals(WXPayConstants.FAIL)) {
            return;
        }

        if (resultBean.getResultCode().equals(WXPayConstants.FAIL)) {
            return;
        }

        PayOrderParamBean param = resultBean.getParam();

    }

    private void log(String msg) {
        System.out.println(msg);
    }

    public void payQuery(CG_PayOrderQueryMsg msg) {

        String orderId = msg.getOrderId();
        if (StringUtils.isEmpty(orderId)) {
            return;
        }

        long orderID = Long.parseLong(orderId);

        OrderBean payEntity = orderMap.get(orderID);
        if (payEntity == null) {
            return;
        }

        PayOrderQueryReturnBean orderQueryBean = WeixinPayServices.getInstance()
                .orderQuery(orderId);

        if (orderQueryBean == null) {
            return;
        }

        if (orderQueryBean.getResultCode().equals(WXPayConstants.FAIL)) {
            return;
        }

        if (!orderQueryBean.getTradeState().equals(WXPayConstants.SUCCESS)) {
            return;
        }

        if (orderQueryBean.getTotalFee() != payEntity.getTotalFee()) {
            return;
        }

    }
}
