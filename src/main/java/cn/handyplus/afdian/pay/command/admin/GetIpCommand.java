package cn.handyplus.afdian.pay.command.admin;

import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.constants.VersionCheckEnum;
import cn.handyplus.lib.core.NetUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.lib.util.TextUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 获取地址
 *
 * @author handy
 */
public class GetIpCommand implements IHandyCommandEvent {
    @Override
    public String command() {
        return "getIp";
    }

    @Override
    public String permission() {
        return "afDianPay.getIp";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        String mac = NetUtil.getLocalMacAddress();
        // 玩家执行并且是高版本
        if (BaseUtil.isPlayer(sender) && BaseConstants.VERSION_ID >= VersionCheckEnum.V_1_15.getVersionId()) {
            TextComponent textComponent = TextUtil.getInstance().init(mac).build();
            textComponent.addExtra(TextUtil.getInstance().init(BaseUtil.getMsgNotColor("copy", "&r   &8[&a点击复制&8]")).addClickCopyToClipboard(mac).build());
            MessageUtil.sendMessage((Player) sender, textComponent);
            return;
        }
        MessageUtil.sendMessage(sender, mac);
    }

}