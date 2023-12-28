package cn.handyplus.afdian.pay.util;

import cn.handyplus.afdian.pay.bo.QueryOrderBo;
import cn.handyplus.afdian.pay.constants.AfDianPayConstants;
import cn.handyplus.afdian.pay.param.AfDianRequest;
import cn.handyplus.afdian.pay.param.AfDianResponse;
import cn.handyplus.lib.core.HttpUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.SecureUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * 爱发电API
 *
 * @author handy
 */
public class AfDianUtil {

    private final static String PING_URL = "https://afdian.net/api/open/ping";
    private final static String QUERY_ORDER_URL = "https://afdian.net/api/open/query-order";
    private final static String QUERY_SPONSOR_URL = "https://afdian.net/api/open/query-sponsor";

    /**
     * 获取ping结果
     */
    public static void pingResult() {
        AfDianResponse ping = AfDianUtil.ping();
        // 判断是否激活爱发电
        if (!"200".equals(ping.getEc())) {
            MessageUtil.sendConsoleMessage(BaseUtil.getMsgNotColor("noToken"));
            AfDianPayConstants.PING_RESULT = false;
        } else {
            AfDianPayConstants.PING_RESULT = true;
        }
    }

    /**
     * ping
     */
    private static AfDianResponse ping() {
        Map<String, Object> params = new HashMap<>();
        params.put("ping", "123456");
        return JsonUtil.toBean(sendPost(PING_URL, params), AfDianResponse.class);
    }

    /**
     * 查订单
     *
     * @param outTradeNo 订单号
     * @return QueryOrderBo 订单信息
     */
    public static QueryOrderBo queryOrderByOutTradeNo(String outTradeNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", outTradeNo);
        return queryOrder(params);
    }

    /**
     * 查订单
     *
     * @param page 页数
     * @return QueryOrderBo 订单信息
     */
    public static QueryOrderBo queryOrderByPage(Integer page) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        return queryOrder(params);
    }

    /**
     * 查订单
     *
     * @param params 参数
     * @return QueryOrderBo 订单信息
     */
    public static QueryOrderBo queryOrder(Map<String, Object> params) {
        return JsonUtil.toBean(sendPost(QUERY_ORDER_URL, params), QueryOrderBo.class);
    }

    /**
     * 查赞助者
     *
     * @param params 参数
     */
    public static AfDianResponse querySponsor(Map<String, Object> params) {
        return JsonUtil.toBean(sendPost(QUERY_SPONSOR_URL, params), AfDianResponse.class);
    }

    /**
     * 发送请求
     *
     * @param url    url
     * @param params 参数
     * @return response
     */
    @SneakyThrows
    private static String sendPost(String url, Map<String, Object> params) {
        long ts = System.currentTimeMillis() / 1000;
        String paramsJson = JsonUtil.toJson(params);
        String sign = AfDianPayConstants.AFDIAN_TOKEN + "params" + paramsJson + "ts" + ts + "user_id" + AfDianPayConstants.AFDIAN_USER_ID;
        AfDianRequest afDianRequest = new AfDianRequest();
        afDianRequest.setUser_id(AfDianPayConstants.AFDIAN_USER_ID);
        afDianRequest.setParams(paramsJson);
        afDianRequest.setTs(ts);
        afDianRequest.setSign(SecureUtil.md5(sign).orElse(null));
        return HttpUtil.post(url, JsonUtil.toJson(afDianRequest));
    }

}