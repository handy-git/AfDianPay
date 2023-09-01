package cn.handyplus.afdian.pay.bo;

import lombok.Data;

/**
 * @author handy
 */
@Data
public class QueryOrderDataListSkuDetail {
    private String sku_id;
    private Integer count;
    private String name;
    private String album_id;
    private String pic;
}
