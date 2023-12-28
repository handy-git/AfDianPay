package cn.handyplus.afdian.pay.command.admin;

import cn.handyplus.afdian.pay.entity.AfDianOrder;
import cn.handyplus.afdian.pay.service.AfDianOrderService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * 查看订单详情
 *
 * @author handy
 */
public class LookCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "look";
    }

    @Override
    public String permission() {
        return "afDianPay.look";
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
        AfDianOrder afDianOrder = afDianOrderOptional.get();
        String titleMsg = ChatColor.GRAY + "_________________/ &e" + afDianOrder.getShopName() + ChatColor.GRAY + " \\_________________\n";
        String oneMsg = "&7| " + "&a商品名称" + ": &7" + afDianOrder.getShopName() + "\n";
        String countMsg = "&7| " + "&a购买数量" + ": &7" + afDianOrder.getCount() + "\n";
        String twoMsg = "&7| " + "&a玩家名称" + ": &7" + afDianOrder.getPlayerName() + "\n";
        String threeMsg = "&7| " + "&a订单号" + ": &7" + afDianOrder.getOutTradeNo() + "\n";
        String fourMsg = "&7| " + "&a是否处理" + ": &7" + (afDianOrder.getResult() ? "&a已处理" : "&7未处理") + "\n";
        String sixMsg = "&7| " + "&a创建时间" + ": &7" + DateUtil.format(afDianOrder.getCreateTime(), DateUtil.YYYY_HH) + "\n";
        String msg = ChatColor.GRAY + "----------------------------------------";
        MessageUtil.sendMessage(sender, titleMsg);
        MessageUtil.sendMessage(sender, oneMsg);
        MessageUtil.sendMessage(sender, countMsg);
        MessageUtil.sendMessage(sender, twoMsg);
        MessageUtil.sendMessage(sender, threeMsg);
        MessageUtil.sendMessage(sender, fourMsg);
        if (StrUtil.isNotEmpty(afDianOrder.getErrorMsg())) {
            String fiveMsg = "&7| " + "&a错误消息" + ": &7" + afDianOrder.getErrorMsg() + "\n";
            MessageUtil.sendMessage(sender, fiveMsg);
        }
        MessageUtil.sendMessage(sender, sixMsg);
        MessageUtil.sendMessage(sender, msg);
    }

}