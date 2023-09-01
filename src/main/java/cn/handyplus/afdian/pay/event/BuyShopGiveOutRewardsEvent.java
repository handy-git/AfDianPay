package cn.handyplus.afdian.pay.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 购买商品发奖励事件
 *
 * @author handy
 */
public class BuyShopGiveOutRewardsEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Integer afDianOrderId;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public BuyShopGiveOutRewardsEvent(Integer afDianOrderId) {
        this.afDianOrderId = afDianOrderId;
    }

    /**
     * 获取订单id
     *
     * @return id
     */
    public Integer getAfDianOrderId() {
        return this.afDianOrderId;
    }


}