package cn.handyplus.afdian.pay.hook;

import cn.handyplus.afdian.pay.AfDianPay;
import cn.handyplus.afdian.pay.api.AfDianPayApi;
import cn.handyplus.afdian.pay.service.AfDianOrderService;
import cn.handyplus.lib.util.BaseUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

/**
 * 变量扩展
 *
 * @author handy
 */
public class PlaceholderUtil extends PlaceholderExpansion {
    private final AfDianPay plugin;

    public PlaceholderUtil(AfDianPay plugin) {
        this.plugin = plugin;
    }

    /**
     * 变量前缀
     *
     * @return 结果
     */
    @Override
    public String getIdentifier() {
        return "afDianPay";
    }

    /**
     * 注册变量
     *
     * @param player     玩家
     * @param identifier 变量
     * @return 结果
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null) {
            return null;
        }
        // %afDianPay_exist%
        if ("exist".equals(identifier)) {
            Integer count = AfDianOrderService.getInstance().findByPlayerName(player.getName());
            return plugin.getConfig().getString(identifier, BaseUtil.getLangMsg(count > 0 ? "exist" : "notExist"));
        }
        // %afDianPay_number%
        if ("number".equals(identifier)) {
            Integer count = AfDianOrderService.getInstance().findByPlayerName(player.getName());
            return plugin.getConfig().getString(identifier, count + "");
        }
        // %afDianPay_point%
        if ("point".equals(identifier)) {
            return plugin.getConfig().getString(identifier, AfDianPayApi.getInstance().findPointByPlayerName(player.getName()).toString());
        }
        return null;
    }

    /**
     * 因为这是一个内部类，
     * 你必须重写这个方法，让PlaceholderAPI知道不要注销你的扩展类
     *
     * @return 结果
     */
    @Override
    public boolean persist() {
        return true;
    }

    /**
     * 因为这是一个内部类，所以不需要进行这种检查
     * 我们可以简单地返回{@code true}
     *
     * @return 结果
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * 作者
     *
     * @return 结果
     */
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * 版本
     *
     * @return 结果
     */
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
