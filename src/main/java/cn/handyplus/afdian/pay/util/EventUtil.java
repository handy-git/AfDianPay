package cn.handyplus.afdian.pay.util;

import cn.handyplus.afdian.pay.entity.AfDianOrder;
import cn.handyplus.afdian.pay.event.BuyShopGiveOutRewardsEvent;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import org.bukkit.Bukkit;

import java.util.List;

/**
 * @author handy
 */
public class EventUtil {

    /**
     * 玩家购买发奖励事件
     *
     * @param list 订单
     */
    public static void callBuyShopGiveOutRewardsEvent(List<AfDianOrder> list) {
        HandySchedulerUtil.runTask(() -> {
            for (AfDianOrder afDianOrder : list) {
                Bukkit.getServer().getPluginManager().callEvent(new BuyShopGiveOutRewardsEvent(afDianOrder.getId()));
            }
        });
    }

}