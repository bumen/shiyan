
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.sdk.weixin.pay;

import com.bmn.sdk.weixin.bean.PayCallbackBean;
import java.util.EventListener;

/**
 * 
 *
 * @date 2018-06-10
 * @author
 */
public interface PayCallbackEventListener extends EventListener {

    boolean support(PayCallbackBean bean);

    void callback(String mchId, PayCallbackBean bean);
}
