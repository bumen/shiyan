package com.bmn.sdk.weixin;

import com.bmn.sdk.weixin.bean.UserBean;
import com.bmn.util.JsonUtils;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinServices {
    // 微信RedisKey
    public final static String WeixinTokenRedisKey = "weixin_access_token";

    // 微信RedisKey
    public final static String WeixinTicketRedisKey = "weixin_ticket";

    // 应用AppId
    private final static String AppId = "";
    // 用户认证
    private final static String AppSecret = "";

    // 日志输入
    private Logger log = LoggerFactory.getLogger("wx");

    private static WeixinServices instance = null;

    public static WeixinServices getInstance() {
        if (instance == null) {
            instance = new WeixinServices();
        }

        return instance;
    }

    private WeixinServices() {
    }

    /**
     * 获取token
     * 
     * @return
     */
    public String getToken() {
        String access_token = null;
        // 先从redis去Token
        byte[] tmp = {};//ByteRedis.get(WeixinTokenRedisKey);
        if (tmp != null) {
            access_token = new String(tmp);

            log.debug(String.format("从Redis获取用户Token成功: [%s]", access_token));
            return access_token;
        } else {// 没有就从微信取
            access_token = this.getTokenFromWeixin();

            log.debug(String.format("从微信获取用户Token成功: [%s]", access_token));
            return access_token;
        }
    }

    /**
     * 从微信平台获取token
     * 
     * 文档地址：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183
     * 
     * @return
     */
    private String getTokenFromWeixin() {
        String URL = String.format(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                AppId,
                AppSecret);

        String data = "";//HttpsUtils.getRequest(URL, null);

        Map<String, String> params = null;//JsonUtils.toMap(data, String.class, String.class);

        if (!(params.containsKey("access_token") && params.containsKey("expires_in"))) {
            return null;
        }

        // 微信的access_token
        String access_token = params.get("access_token");
        // 超时时间
        int expires_in = (int) Double.parseDouble(String.valueOf(params.get("expires_in")));

        // 保存到Redis
        //ByteRedis.set(WeixinTokenRedisKey, access_token.getBytes());
        //ByteRedis.expire(WeixinTokenRedisKey, expires_in);

        return access_token;
    }

    /**
     * 获取ticket
     * 
     * @return
     */
    public String getTicket() {
        String ticket = null;
        // 先从redis去Token
        byte[] tmp = {};//ByteRedis.get(WeixinTicketRedisKey);
        if (tmp != null) {
            ticket = new String(tmp);

            log.debug(String.format("从Redis获取用户Ticket成功: [%s]", ticket));
            return ticket;
        } else {// 没有就从微信取
            ticket = this.getTicketFromWeixin();

            log.debug(String.format("从微信获取用户Ticket成功: [%s]", ticket));
            return ticket;
        }
    }

    /**
     * 获取ticket剩下的过期时间
     * 
     * @return
     */
    public int getTicketExpires() {
        int expires = (int) 0;//ByteRedis.expireRemain(WeixinTicketRedisKey);
        return expires;
    }

    /**
     * 从微信获取获取ticket剩下的过期时间
     * 
     * 文档地址：https://mp.weixin.qq.com/wiki?action=doc&id=mp1421141115&t=0.9202249578938705#wxkq1
     * 
     * @return
     */
    private String getTicketFromWeixin() {
        String token = this.getToken();

        if (token == null) {
            log.error("Cannot find token!");
            return null;
        }

        String URL = String.format(
                "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi",
                token);

        String data = "";//HttpsUtils.getRequest(URL, null);

        Map<String, String> params = null;//JsonUtils.toMap(data, String.class, String.class);

        if (!"ok".equalsIgnoreCase(params.get("errmsg"))) {
            return null;
        }

        if (!(params.containsKey("ticket") && params.containsKey("expires_in"))) {
            return null;
        }

        // 微信的ticket
        String ticket = (String) params.get("ticket");
        // 超时时间
        int expires_in = (int) Double.parseDouble(String.valueOf(params.get("expires_in")));

        // 保存到Redis
        //ByteRedis.set(WeixinTicketRedisKey, ticket.getBytes());
        //ByteRedis.expire(WeixinTicketRedisKey, expires_in);

        return ticket;
    }

    /*public List<BaseMsg> doGetWeixinTicket(CG_GetWeixinTicketMsg msg) {
        List<BaseMsg> back = new ArrayList<BaseMsg>();

        GC_GetWeixinTicketMsg getWeixinTicketMsg = new GC_GetWeixinTicketMsg();
        back.add(getWeixinTicketMsg);

        String ticket = this.getTicket();
        if (ticket == null) {
            return back;
        } else {
            int expires = this.getTicketExpires();

            getWeixinTicketMsg.setResponseCode(1);
            getWeixinTicketMsg.setExpires(expires);
            getWeixinTicketMsg.setTicket(ticket);

            return back;
        }
    }*/

    /**
     * 获取用户信息
     * 
     * 文档地址：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140840
     * 
     * @param openId
     * @return
     */
    public UserBean getUserInfoFromWeixin(String openId) {
        String token = this.getToken();
        if (token == null) {
            log.error("Cannot find token!");
            return null;
        }

        String URL = String.format(
                "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN ",
                token, openId);

        String data = "";//HttpsUtils.getRequest(URL, null);

        UserBean userBean = JsonUtils.fromJson(data, UserBean.class);

        log.debug(String.format("从微信获取用户数据获取成功:[%s]", data));

        return userBean;
    }

    /**
     * 微信授权后调整，用来获取用户的openId
     * 
     * 文档地址：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
     */
    public String getOpenId(String code) {

        String URL = String.format(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                AppId, AppSecret, code);

        String data = "";//HttpsUtils.getRequest(URL, null);

        Map<String, String> params = JsonUtils.toMap(data, String.class, String.class);

        if (!params.containsKey("openid")) {
            log.error("没有openid，认证失败");
            return null;
        }

        // 用户的openId
        String openId = params.get("openid");
        log.debug(String.format("从微信获取用户OpenId成功:[%s]", data));

        return openId;
    }

    /**
     * 执行登录
     * 
     * @param code
     */
    /*public SDKBean doWeixinLogin(String code) {
        // 获取用户唯一Id
        String openId = this.getOpenId(code);

        UserBean userBean = this.getUserInfoFromWeixin(openId);

        // 创建UUID
        String uuid = String.format("weixin_%s", openId);

        SDKBean bean = new SDKBean();

        bean.setUuid(uuid);

        // 获取NickName
        bean.setNickName(userBean.getNickname());

        // 头像
        bean.setHeandUrl(userBean.getHeadimgurl());

        return bean;
    }*/


}
