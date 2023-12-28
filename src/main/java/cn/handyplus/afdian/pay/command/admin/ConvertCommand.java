package cn.handyplus.afdian.pay.command.admin;

import cn.handyplus.afdian.pay.entity.AfDianOrder;
import cn.handyplus.afdian.pay.service.AfDianOrderService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.db.Db;
import cn.handyplus.lib.db.SqlManagerUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.HandyConfigUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * 转换数据-从mysql转换到sqlite，或者反之
 *
 * @author handy
 */
public class ConvertCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "convert";
    }

    @Override
    public String permission() {
        return "afDianPay.convert";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, sender, BaseUtil.getMsgNotColor("paramFailureMsg"));
        String storageMethod = args[1];
        if (!BaseConstants.MYSQL.equalsIgnoreCase(storageMethod) && !BaseConstants.SQLITE.equalsIgnoreCase(storageMethod)) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("paramFailureMsg"));
            return;
        }
        if (storageMethod.equalsIgnoreCase(BaseConstants.STORAGE_CONFIG.getString(BaseConstants.STORAGE_METHOD))) {
            MessageUtil.sendMessage(sender, "&4禁止转换！原因，您当前使用的存储方式已经为：" + storageMethod);
            return;
        }
        // 查询当前全部数据
        List<AfDianOrder> all = AfDianOrderService.getInstance().findAll();

        // 修改链接方式
        HandyConfigUtil.setPath(BaseConstants.STORAGE_CONFIG, "storage-method", storageMethod, Collections.singletonList("存储方法(MySQL,SQLite)请复制括号内的类型,不要自己写"), "storage.yml");
        // 加载新连接
        SqlManagerUtil.enableSql();

        // 新连接创建表
        Db.use(AfDianOrder.class).execution().create();
        // 插入数据
        Db.use(AfDianOrder.class).execution().insertBatch(all);
        MessageUtil.sendMessage(sender, "&4转换数据完成，请务必重启服务器，不然有可能会出现未知bug");
    }

}