package cn.handyplus.afdian.pay.listener;

import cn.handyplus.afdian.pay.entity.AfDianOrder;
import cn.handyplus.afdian.pay.event.BuyShopGiveOutRewardsEvent;
import cn.handyplus.afdian.pay.hook.PlaceholderApiUtil;
import cn.handyplus.afdian.pay.hook.PlayerPointsUtil;
import cn.handyplus.afdian.pay.service.AfDianOrderService;
import cn.handyplus.afdian.pay.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;

/**
 * 玩家购买发奖励事件
 *
 * @author handy
 */
@HandyListener
public class BuyShopGiveOutRewardsEventListener implements Listener {

    /**
     * 处理
     *
     * @param event 事件
     */
    @EventHandler
    public void onBuyShopGiveOutRewards(BuyShopGiveOutRewardsEvent event) {
        giveOutRewards(event.getAfDianOrderId());
    }

    /**
     * 发奖励
     *
     * @param id 订单id
     */
    public static synchronized void giveOutRewards(Integer id) {
        Optional<AfDianOrder> afDianOrderOptional = AfDianOrderService.getInstance().findById(id);
        // 是否为空
        if (!afDianOrderOptional.isPresent()) {
            MessageUtil.sendConsoleMessage(BaseUtil.getMsgNotColor("notOrder"));
            return;
        }
        AfDianOrder afDianOrder = afDianOrderOptional.get();
        Player player = check(afDianOrder);
        if (player == null) {
            return;
        }
        String shopName = afDianOrder.getShopName();
        int count = afDianOrder.getCount() != null ? afDianOrder.getCount() : 1;
        // 点券处理
        int point = ConfigUtil.SHOP_CONFIG.getInt(shopName + ".point", 0) * count;
        PlayerPointsUtil.give(player, point);
        // 命令处理
        for (int i = 0; i < count; i++) {
            executeCommand(player, shopName);
        }
        // 发送消息
        sendMsg(player, shopName);
        // 订单状态处理
        AfDianOrderService.getInstance().updateResult(point, true, null, id);
    }

    /**
     * 校验订单信息
     *
     * @param afDianOrder 订单
     * @return 玩家
     */
    private static Player check(AfDianOrder afDianOrder) {
        // 是否错误数据
        if (afDianOrder.getResult() && StrUtil.isNotEmpty(afDianOrder.getErrorMsg())) {
            MessageUtil.sendConsoleMessage(afDianOrder.getErrorMsg());
            return null;
        }
        // 是否发过奖励
        if (afDianOrder.getResult() && StrUtil.isEmpty(afDianOrder.getErrorMsg())) {
            MessageUtil.sendConsoleMessage(BaseUtil.getMsgNotColor("notReward").replace("${order}", afDianOrder.getOutTradeNo()));
            return null;
        }
        // 玩家是否存在
        Player player = Bukkit.getPlayerExact(afDianOrder.getPlayerName());
        if (player == null) {
            MessageUtil.sendConsoleMessage(BaseUtil.getMsgNotColor("noPlayer").replace("${player}", afDianOrder.getPlayerName()));
            return null;
        }
        // 玩家是否在线
        if (!player.isOnline()) {
            MessageUtil.sendConsoleMessage(BaseUtil.getMsgNotColor("onlinePlayer").replace("${player}", afDianOrder.getPlayerName()));
            return null;
        }
        return player;
    }

    /**
     * 执行命令
     *
     * @param player   玩家
     * @param shopName 商品
     */
    private static void executeCommand(Player player, String shopName) {
        List<String> commandList = ConfigUtil.SHOP_CONFIG.getStringList(shopName + ".command");
        if (CollUtil.isEmpty(commandList)) {
            return;
        }
        for (String command : commandList) {
            if (StrUtil.isEmpty(command)) {
                continue;
            }
            command = PlaceholderApiUtil.set(player, command);
            command = command.replace("${player}", player.getName()).replace("${shop}", shopName);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.trim());
        }
    }

    /**
     * 发送提醒消息
     *
     * @param player   玩家
     * @param shopName 商品
     */
    private static void sendMsg(Player player, String shopName) {
        // 提醒消息处理
        List<String> messageList = ConfigUtil.SHOP_CONFIG.getStringList(shopName + ".message");
        if (CollUtil.isEmpty(messageList)) {
            return;
        }
        for (String message : messageList) {
            if (StrUtil.isEmpty(message)) {
                continue;
            }
            message = PlaceholderApiUtil.set(player, message);
            message = message.replace("${player}", player.getName()).replace("${shop}", shopName);
            if (message.contains("[message]")) {
                String trimMessage = message.replace("[message]", "").trim();
                MessageUtil.sendMessage(player, trimMessage);
                continue;
            }
            if (message.contains("[allMessage]")) {
                String trimMessage = message.replace("[allMessage]", "").trim();
                MessageUtil.sendAllMessage(trimMessage);
                continue;
            }
            if (message.contains("[title]")) {
                String trimMessage = message.replace("[title]", "").trim();
                String[] split = trimMessage.split(":");
                String title = "";
                String subTitle = "";
                title = split[0];
                if (split.length > 1) {
                    subTitle = split[1];
                }
                MessageUtil.sendTitle(player, title, subTitle);
                continue;
            }
            if (message.contains("[allTitle]")) {
                String trimMessage = message.replace("[allTitle]", "").trim();
                String[] split = trimMessage.split(":");
                String title = "";
                String subTitle = "";
                title = split[0];
                if (split.length > 1) {
                    subTitle = split[1];
                }
                MessageUtil.sendAllTitle(title, subTitle);
                continue;
            }
            if (message.contains("[actionbar]")) {
                String trimMessage = message.replace("[actionbar]", "").trim();
                MessageUtil.sendActionbar(player, trimMessage);
                continue;
            }
            if (message.contains("[allActionbar]")) {
                String trimMessage = message.replace("[allActionbar]", "").trim();
                MessageUtil.sendAllActionbar(trimMessage);
            }
        }
    }

}