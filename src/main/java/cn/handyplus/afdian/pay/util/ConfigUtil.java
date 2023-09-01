package cn.handyplus.afdian.pay.util;

import cn.handyplus.afdian.pay.constants.AfDianPayConstants;
import cn.handyplus.lib.util.HandyConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

/**
 * 配置
 *
 * @author handy
 */
public class ConfigUtil {
    public static FileConfiguration CONFIG, LANG_CONFIG, SHOP_CONFIG;

    /**
     * 初始化加载文件
     */
    public static void init() {
        // 加载config
        CONFIG = HandyConfigUtil.loadConfig();
        LANG_CONFIG = HandyConfigUtil.loadLangConfig(CONFIG.getString("language"));
        SHOP_CONFIG = HandyConfigUtil.load("shop.yml");
        // 处理配置
        AfDianPayConstants.AFDIAN_TOKEN = CONFIG.getString("afDianToken");
        AfDianPayConstants.AFDIAN_USER_ID = CONFIG.getString("afDianUserId");
        upConfig();
    }

    /**
     * 升级节点处理
     *
     * @since 1.0.4
     */
    private static void upConfig() {
        // 1.0.4 添加拉取控制
        HandyConfigUtil.setPathIsNotContains(CONFIG, "onlyReward", false, Arrays.asList("是否只发奖励", "多子服连接同一个mysql的时候", "一个主服设置为false表示拉订单并发奖励(这个主服例如登陆服，玩家都必须进去过)", "其他子服都设置为true表示只发奖励不拉订单，避免重复拉取"), "config.yml");
        CONFIG = HandyConfigUtil.load("config.yml");
    }

}