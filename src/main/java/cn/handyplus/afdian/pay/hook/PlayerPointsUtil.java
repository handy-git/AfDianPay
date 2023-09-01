package cn.handyplus.afdian.pay.hook;

import cn.handyplus.afdian.pay.AfDianPay;
import org.bukkit.entity.Player;

/**
 * 点券奖励
 *
 * @author handy
 */
public class PlayerPointsUtil {

    /**
     * 发奖励
     *
     * @param player 玩家
     * @param amount 数量
     */
    public static boolean give(Player player, Integer amount) {
        if (AfDianPay.getPlayerPoints() == null) {
            return false;
        }
        if (player == null || amount == 0) {
            return false;
        }
        return AfDianPay.getPlayerPoints().getAPI().give(player.getUniqueId(), amount);
    }

}