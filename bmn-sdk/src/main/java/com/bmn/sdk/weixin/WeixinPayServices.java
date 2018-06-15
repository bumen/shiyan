
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.sdk.weixin;

import com.bmn.sdk.weixin.bean.PayCallbackBean;
import com.bmn.sdk.weixin.bean.PayOrderBean;
import com.bmn.sdk.weixin.bean.PayOrderParamBean;
import com.bmn.sdk.weixin.bean.PayOrderQueryReturnBean;
import com.bmn.sdk.weixin.bean.PayOrderReturnBean;
import com.bmn.sdk.weixin.bean.PayReturnBean;
import com.bmn.sdk.weixin.pay.PayCallbackEventListener;
import com.bmn.sdk.weixin.pay.UserWXPayConfigImpl;
import com.bmn.sdk.weixin.pay.WXPay;
import com.bmn.sdk.weixin.pay.WXPayConfig;
import com.bmn.sdk.weixin.pay.WXPayConstants;
import com.bmn.sdk.weixin.pay.WXPayUtil;
import com.bmn.util.JsonUtils;
import com.bmn.util.StringUtils;
import com.bmn.util.TimeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.xml.sax.InputSource;

/**
 * 
 *
 * @date 2018-06-08
 * @author
 */
public class WeixinPayServices {
    private static final WeixinPayServices instance = new WeixinPayServices();

    private WeixinPayServices() {
        config = UserWXPayConfigImpl.getInstance();
        try {
            wxpay = new WXPay(config, ORDER_CALLBACK_URL, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WeixinPayServices getInstance() {
        return instance;
    }

    private WXPay wxpay;
    private WXPayConfig config;

    private static final String ORDER_CALLBACK_URL = "http://.iask.in:9000/fans/payback";

    private static final String DEVICE_INFO = "FANS";
    private static final String TRADE = "JSAPI";

    private final List<PayCallbackEventListener> listeners = new ArrayList<>();

    public void addListener(PayCallbackEventListener listener) {
        this.listeners.add(listener);
    }

    private static final Logger logger = WXPayUtil.getLogger();

    /**
     * 下单
     */
    public PayOrderReturnBean order(String openId, PayOrderBean order) {
        if (StringUtils.isEmpty(openId)) {
            logger.debug("统一下单: openId为空");
            return null;
        }

        if (!checkOrder(order)) {
            logger.debug(String.format("统一下单: order异常: %s", JsonUtils.toJson(order)));
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("统一下单: order: %s", JsonUtils.toJson(order)));
        }

        // 钱由元转为分
        int total_fee = order.getMoney();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("body", order.getBody());
        data.put("out_trade_no", order.getOrderId());
        data.put("device_info", DEVICE_INFO);
        data.put("fee_type", "CNY");
        data.put("total_fee", Integer.toString(total_fee));
        data.put("spbill_create_ip", order.getClientIp());
        data.put("trade_type", TRADE);
        data.put("openid", openId);

        if (!StringUtils.isEmpty(order.getDetail())) {
            data.put("detail", order.getDetail());
        }

        if (!StringUtils.isEmpty(order.getAttach())) {
            data.put("attach", order.getAttach());
        }

        try {
            Map<String, String> r = wxpay.unifiedOrder(data);

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("统一下单: order: %s, 成功", order.getOrderId()));
            }

            PayOrderReturnBean bean = new PayOrderReturnBean();
            bean.setReturnCode(r.get("return_code"));
            if (bean.getReturnCode().equals(WXPayConstants.SUCCESS)) {
                bean.setResultCode(r.get("result_code"));
                if (bean.getResultCode().equals(WXPayConstants.SUCCESS)) {

                    Map<String, String> paybackParam = new HashMap<>();
                    paybackParam.put("timeStamp", Integer.toString(TimeUtils.nowSeconds()));
                    paybackParam.put("package", "prepay_id=" + r.get("prepay_id"));

                    wxpay.fillOrderCallback(paybackParam);

                    PayOrderParamBean paramBean = new PayOrderParamBean();
                    paramBean.setAppId(paybackParam.get("appId"));
                    paramBean.setNonceStr(paybackParam.get("nonceStr"));
                    paramBean.setPackageStr(paybackParam.get("package"));
                    paramBean.setTimestamp(paybackParam.get("timeStamp"));
                    paramBean.setSignType(paybackParam.get("signType"));
                    paramBean.setPaySign(paybackParam.get("paySign"));

                    bean.setParam(paramBean);
                } else {
                    bean.setErrCode(r.get("err_code"));
                    bean.setErrCodeDes(r.get("err_code_des"));
                }

            } else {
                bean.setReturnMsg(r.get("return_msg"));
            }

            return bean;
        } catch (Exception e) {
            logger.error(String.format("统一下单: order: %s, 失败", order.getOrderId()), e);
        }

        return null;
    }

    /**
     * 用户支付结果通知
     * 
     * @throws Exception
     */
    public String payCallback(InputSource inputSource) {
        logger.info("收到-支付回调通知");
        PayReturnBean returnBean = new PayReturnBean();

        Map<String, String> r = null;
        try {
            r = WXPayUtil.inputSourceToMap(inputSource);
        } catch (Exception e) {
            logger.error("支付回调通知，数据解析异常", e);

            returnBean.setReturnCode(WXPayConstants.FAIL);
            returnBean.setReturnMsg("数据解析异常");
            return returnBean.toString();
        }

        PayCallbackBean bean = new PayCallbackBean();
        bean.setReturnCode(r.get("return_code"));
        if (bean.getReturnCode().equals(WXPayConstants.SUCCESS)) {
            bean.setResultCode(r.get("result_code"));
            if (bean.getResultCode().equals(WXPayConstants.SUCCESS)) {
                bean.setOpenId(r.get("openid"));
                bean.setTotalFee(Integer.parseInt(r.get("total_fee")));
                bean.setCashFee(Integer.parseInt(r.get("cash_fee")));
                bean.setTransactionId(r.get("transaction_id"));
                bean.setOutTradeNo(r.get("out_trade_no"));
                bean.setAttach(r.get("attach"));
                bean.setFeeType(r.get("fee_type"));
                bean.setMchId(r.get("mch_id"));

                boolean valid = true;
                try {
                    valid = wxpay.isResponseSignatureValid(r);
                } catch (Exception e) {
                    valid = false;
                    logger.error("支付回调通知，数据签名验证异常", e);
                }

                bean.setSignValid(valid);

            } else {
                bean.setErrCode(r.get("err_code"));
                bean.setErrCodeDes(r.get("err_code_des"));
            }
        } else {
            bean.setReturnMsg(r.get("return_msg"));
        }

        for (PayCallbackEventListener listener : listeners) {
            if (listener.support(bean)) {
                listener.callback(config.getMchID(), bean);
                break;
            }
        }

        returnBean.setReturnCode(WXPayConstants.SUCCESS);

        logger.info("支付回调通知-成功");

        return returnBean.toString();
    }

    /**
     * 查询订单状态
     */
    public PayOrderQueryReturnBean orderQuery(String orderId) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("查询订单接口: orderId: %s", orderId));
        }
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", orderId);
        try {
            Map<String, String> r = wxpay.orderQuery(data);

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("查询订单接口: orderId: %s, 返回成功", orderId));
            }

            PayOrderQueryReturnBean bean = new PayOrderQueryReturnBean();
            bean.setReturnCode(r.get("return_code"));
            if (bean.getReturnCode().equals(WXPayConstants.SUCCESS)) {
                bean.setResultCode(r.get("result_code"));
                if (bean.getResultCode().equals(WXPayConstants.SUCCESS)) {
                    bean.setTradeState(r.get("trade_state"));
                    bean.setOutTradeNo(r.get("out_trade_no"));
                    bean.setAttach(r.get("attach"));
                    if (bean.getTradeState().equals(WXPayConstants.SUCCESS)) {
                        bean.setTotalFee(r.get("total_fee"));
                        bean.setCashFee(r.get("cash_fee"));
                        bean.setTransactionId(r.get("transaction_id"));
                    }
                } else {
                    bean.setErrCode(r.get("err_code"));
                    bean.setErrCodeDes(r.get("err_code_des"));
                }
            } else {
                bean.setReturnMsg(r.get("return_msg"));
            }

            return bean;
        } catch (Exception e) {
            logger.error(String.format("查询订单接口: orderId: %s", orderId), e);
        }

        return null;
    }

    private boolean checkOrder(PayOrderBean order) {
        if (order == null) {
            return false;
        }
        if (StringUtils.isEmpty(order.getBody())) {
            return false;
        }

        if (StringUtils.isEmpty(order.getOrderId())) {
            return false;
        }

        if (order.getMoney() <= 0) {
            return false;
        }

        return true;
    }

}
