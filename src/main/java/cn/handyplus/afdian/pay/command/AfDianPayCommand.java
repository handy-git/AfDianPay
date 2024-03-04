package cn.handyplus.afdian.pay.command;

import cn.handyplus.afdian.pay.constants.TabListEnum;
import cn.handyplus.afdian.pay.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyCommand;
import cn.handyplus.lib.command.HandyCommandWrapper;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 命令
 *
 * @author handy
 */
@HandyCommand(name = "afDianPay")
public class AfDianPayCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // 判断指令是否正确
        if (args.length < 1) {
            return sendHelp(sender);
        }
        boolean rst = HandyCommandWrapper.onCommand(sender, cmd, label, args, BaseUtil.getMsgNotColor("noPermission"));
        if (!rst) {
            return sendHelp(sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (sender.hasPermission("afDianPay.reload")) {
            commands = TabListEnum.returnList(args, args.length);
        }
        if (commands == null) {
            return null;
        }
        StringUtil.copyPartialMatches(args[args.length - 1].toLowerCase(), commands, completions);
        Collections.sort(completions);
        return completions;
    }

    /**
     * 发送帮助
     *
     * @param sender 发送人
     * @return 消息
     */
    private Boolean sendHelp(CommandSender sender) {
        if (!sender.hasPermission("afDianPay.reload")) {
            List<String> playerHelps = ConfigUtil.LANG_CONFIG.getStringList("playerHelps");
            for (String help : playerHelps) {
                MessageUtil.sendMessage(sender, help);
            }
            return true;
        }

        List<String> helps = ConfigUtil.LANG_CONFIG.getStringList("helps");
        for (String help : helps) {
            MessageUtil.sendMessage(sender, help);
        }
        return true;
    }

}
