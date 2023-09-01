package cn.handyplus.afdian.pay.api;

import cn.handyplus.afdian.pay.service.AfDianOrderService;
import org.bukkit.entity.Player;

/**
 * 爱发电支付API
 *
 * @author handy
 */
public class AfDianPayApi {

    private AfDianPayApi() {
    }

    private static class SingletonHolder {
        private static final AfDianPayApi INSTANCE = new AfDianPayApi();
    }

    public static AfDianPayApi getInstance() {
        return AfDianPayApi.SingletonHolder.INSTANCE;
    }

    /**
     * 查询玩家赞助的点券数量
     *
     * @param playerName 玩家名
     * @since 1.0.1
     */
    public Integer findPointByPlayerName(String playerName) {
        return AfDianOrderService.getInstance().findPointByPlayerName(playerName);
    }

    /**
     * 查询玩家赞助的点券数量
     *
     * @param player 玩家
     * @since 1.0.1
     */
    public Integer findPointByPlayerName(Player player) {
        return this.findPointByPlayerName(player.getName());
    }

}