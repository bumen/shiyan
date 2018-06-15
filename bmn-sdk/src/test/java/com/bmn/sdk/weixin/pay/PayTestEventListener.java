
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.sdk.weixin.pay;

import com.bmn.sdk.weixin.bean.PayCallbackBean;

/**
 * 
 *
 * @date 2018-06-11
 * @author
 */
public class PayTestEventListener implements PayCallbackEventListener {

    @Override
    public void callback(String mchId, PayCallbackBean bean) {
        PayTestServices.getInstance().paycallback(mchId, bean);
    }

    @Override
    public boolean support(PayCallbackBean bean) {
        return "Test".equals(bean.getAttach());
    }

}
