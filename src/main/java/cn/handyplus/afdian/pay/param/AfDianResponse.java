package cn.handyplus.afdian.pay.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 爱发电响应
 *
 * @author handy
 */
@Data
public class AfDianResponse implements Serializable {
    /**
     * 状态码 200成功
     */
    private String ec;
    /**
     * 消息
     */
    private String em;
    /**
     * 参数
     */
    private Map<String, Object> data;
}