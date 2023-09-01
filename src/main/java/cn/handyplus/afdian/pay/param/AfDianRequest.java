package cn.handyplus.afdian.pay.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 爱发电参数
 *
 * @author handy
 */
@Data
public class AfDianRequest implements Serializable {

    /**
     * user_id
     */
    private String user_id;

    /**
     * 参数
     */
    private String params;

    /**
     * 时间戳
     */
    private Long ts;

    /**
     * 签名
     */
    private String sign;
}