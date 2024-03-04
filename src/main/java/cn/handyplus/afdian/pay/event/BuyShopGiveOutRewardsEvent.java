package cn.handyplus.afdian.pay.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 购买商品发奖励事件
 *
 * @author handy
 */
@Getter
public class BuyShopGiveOutRewardsEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Integer afDianOrderId;

    @NotNull
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


}