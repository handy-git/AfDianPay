package cn.handyplus.afdian.pay.entity;

import cn.handyplus.lib.annotation.TableField;
import cn.handyplus.lib.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 爱发电订单
 *
 * @author handy
 */
@Data
@TableName(value = "pay_afdian_order", comment = "爱发电订单")
public class AfDianOrder {

    @TableField(value = "id", comment = "ID")
    private Integer id;

    @TableField(value = "shop_name", comment = "商品名称", length = 255)
    private String shopName;

    @TableField(value = "count", comment = "购买数量", fieldDefault = "1")
    private Integer count;

    @TableField(value = "player_name", comment = "玩家名称", length = 255)
    private String playerName;

    @TableField(value = "out_trade_no", comment = "订单号", length = 255)
    private String outTradeNo;

    @TableField(value = "point", comment = "本次处理的点券数量")
    private Integer point;

    @TableField(value = "result", comment = "是否处理")
    private Boolean result;

    @TableField(value = "error_msg", comment = "错误消息")
    private String errorMsg;

    @TableField(value = "param", comment = "爱发电参数", length = 20000)
    private String param;

    @TableField(value = "create_time", comment = "创建时间")
    private Date createTime;
}
