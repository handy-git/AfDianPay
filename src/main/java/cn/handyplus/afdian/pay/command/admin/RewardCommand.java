package cn.handyplus.afdian.pay.command.admin;

import cn.handyplus.afdian.pay.entity.AfDianOrder;
import cn.handyplus.afdian.pay.service.AfDianOrderService;
import cn.handyplus.afdian.pay.util.EventUtil;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Optional;

/**
 * 手动发奖励
 *
 * @author handy
 */
public class RewardCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "reward";
    }

    @Override
    public String permission() {
        return "afDianPay.reward";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, sender, BaseUtil.getMsgNotColor("paramFailureMsg"));
        Optional<AfDianOrder> afDianOrderOptional = AfDianOrderService.getInstance().findByOrder(args[1]);
        if (!afDianOrderOptional.isPresent()) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("noOrder").replace("${order}", args[1]));
            return;
        }
        // 发送奖励 事件
        EventUtil.callBuyShopGiveOutRewardsEvent(Collections.singletonList(afDianOrderOptional.get()));
        MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("opReward").replace("${order}", args[1]));
    }

}