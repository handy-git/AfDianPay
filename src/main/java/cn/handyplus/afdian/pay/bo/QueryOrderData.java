package cn.handyplus.afdian.pay.bo;

import lombok.Data;

import java.util.List;

/**
 * @author handy
 */
@Data
public class QueryOrderData {

    private List<QueryOrderDataList> list;
    private int total_count;
    private int total_page;

}
