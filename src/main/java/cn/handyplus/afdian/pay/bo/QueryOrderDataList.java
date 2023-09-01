package cn.handyplus.afdian.pay.bo;

import lombok.Data;

import java.util.List;

/**
 * @author handy
 */
@Data
public class QueryOrderDataList {

    private String out_trade_no;
    private String user_id;
    private String user_private_id;
    private String plan_id;
    private String plan_title;
    private Integer month;
    private String total_amount;
    private String show_amount;
    private Integer status;
    private String remark;
    private String redeem_id;
    private Integer product_type;
    private String discount;
    private List<QueryOrderDataListSkuDetail> sku_detail;
    private String address_person;
    private String address_phone;
    private String address_address;

}
