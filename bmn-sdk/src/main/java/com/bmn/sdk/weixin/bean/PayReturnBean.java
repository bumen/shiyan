
/**
* Copyright (c) 2018 bumen.All rights reserved.
*/

package com.bmn.sdk.weixin.bean;

/**
 * 
 *
 * @date 2018-06-10
 * @author
 */
public class PayReturnBean {

    private String returnCode;

    private String returnMsg;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<return_code><![CDATA[").append(this.returnCode).append("]]></return_code>");
        if (returnMsg != null && !returnMsg.equals("")) {
            sb.append("<return_msg><![CDATA[").append(this.returnMsg).append("]]></return_msg>");
        }
        sb.append("</xml>");
        return sb.toString();
    }

}
