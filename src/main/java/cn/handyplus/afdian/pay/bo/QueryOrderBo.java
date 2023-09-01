package cn.handyplus.afdian.pay.bo;

import lombok.Data;

/**
 * @author handy
 */
@Data
public class QueryOrderBo {
    private Integer ec;
    private String em;
    private QueryOrderData data;
}
