package cn.handyplus.afdian.pay.command.admin;

import cn.handyplus.afdian.pay.AfDianPay;
import cn.handyplus.afdian.pay.util.AfDianUtil;
import cn.handyplus.afdian.pay.util.ConfigUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.BaseUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 重载插件
 *
 * @author handy
 */
public class ReloadCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "reload";
    }

    @Override
    public String permission() {
        return "afDianPay.reload";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ConfigUtil.init();
                // 判断是否激活爱发电
                AfDianUtil.pingResult();
                MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("reloadMsg"));
            }
        }.runTaskAsynchronously(AfDianPay.getInstance());
    }

}