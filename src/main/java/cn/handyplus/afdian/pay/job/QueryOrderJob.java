package cn.handyplus.afdian.pay.job;

import cn.handyplus.afdian.pay.AfDianPay;
import cn.handyplus.afdian.pay.bo.QueryOrderBo;
import cn.handyplus.afdian.pay.bo.QueryOrderData;
import cn.handyplus.afdian.pay.bo.QueryOrderDataList;
import cn.handyplus.afdian.pay.constants.AfDianPayConstants;
import cn.handyplus.afdian.pay.entity.AfDianOrder;
import cn.handyplus.afdian.pay.service.AfDianOrderService;
import cn.handyplus.afdian.pay.util.AfDianUtil;
import cn.handyplus.afdian.pay.util.ConfigUtil;
import cn.handyplus.afdian.pay.util.EventUtil;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * 定时任务
 *
 * @author handy
 */
public class QueryOrderJob {

    private static final Semaphore ORDER_TASK_LOCK = new Semaphore(1);

    /**
     * 初始化
     */
    public static void init() {
        // 判断是否激活爱发电
        AfDianUtil.pingResult();
        // 定时任务
        new BukkitRunnable() {
            @Override
            public void run() {
                // 判断是否可以ping成功
                if (!AfDianPayConstants.PING_RESULT) {
                    return;
                }
                getOrder();
            }
        }.runTaskTimerAsynchronously(AfDianPay.getInstance(), 60, 20L * ConfigUtil.CONFIG.getInt("jobTime", 60));
    }

    /**
     * 拉订单
     */
    private static void getOrder() {
        if (!ORDER_TASK_LOCK.tryAcquire()) {
            return;
        }
        try {
            long millis = System.currentTimeMillis();
            // 判断是否拉订单
            if (!ConfigUtil.CONFIG.getBoolean("onlyReward", false)) {
                sendMsg("定时开始获取爱发电数据");
                // 最大订单
                String maxOrder = AfDianOrderService.getInstance().findMaxOrder();
                List<AfDianOrder> afDianOrderList = queryOrder(1, maxOrder);
                if (CollUtil.isEmpty(afDianOrderList)) {
                    sendMsg("没有获取到爱发电数据,耗时:" + (System.currentTimeMillis() - millis) / 1000 + "秒");
                    return;
                }
                // 保存
                sendMsg("开始保存爱发电订单");
                AfDianOrderService.getInstance().addBatch(afDianOrderList);
                sendMsg("保存爱发电订单结束");
            }
            // 需发送奖励的列表
            List<AfDianOrder> list = AfDianOrderService.getInstance().list();
            sendMsg("发送奖励,本次需要操作" + list.size() + "条");
            if (CollUtil.isEmpty(list)) {
                sendMsg("获取爱发电数据结束,耗时:" + (System.currentTimeMillis() - millis) / 1000 + "秒");
                return;
            }
            // 发送奖励 事件
            EventUtil.callBuyShopGiveOutRewardsEvent(list);
            sendMsg("获取爱发电数据结束,耗时:" + (System.currentTimeMillis() - millis) / 1000 + "秒");
        } finally {
            ORDER_TASK_LOCK.release();
        }
    }

    /**
     * 轮训查询订单
     *
     * @param page     页数
     * @param maxOrder 最大订单号
     * @return 订单信息
     */
    private static List<AfDianOrder> queryOrder(Integer page, String maxOrder) {
        List<AfDianOrder> afDianOrderList = new ArrayList<>();
        // 查询订单
        QueryOrderBo queryOrderBo = AfDianUtil.queryOrderByPage(page);
        QueryOrderData queryOrderBoData = queryOrderBo.getData();
        int totalPage = queryOrderBoData.getTotal_page();
        List<QueryOrderDataList> queryOrderDataList = queryOrderBoData.getList();
        if (CollUtil.isNotEmpty(queryOrderDataList)) {
            for (QueryOrderDataList orderDataList : queryOrderDataList) {
                String name = orderDataList.getPlan_title();
                Integer count = orderDataList.getMonth();
                if (CollUtil.isNotEmpty(orderDataList.getSku_detail())) {
                    name = orderDataList.getSku_detail().get(0).getName();
                    count = orderDataList.getSku_detail().get(0).getCount();
                }
                AfDianOrder afDianOrder = new AfDianOrder();
                afDianOrder.setShopName(name);
                afDianOrder.setCount(count);
                afDianOrder.setPlayerName(StrUtil.isNotEmpty(orderDataList.getRemark()) ? orderDataList.getRemark().trim() : "");
                afDianOrder.setOutTradeNo(orderDataList.getOut_trade_no());
                afDianOrder.setResult(false);
                afDianOrder.setErrorMsg(null);
                afDianOrder.setParam(JsonUtil.toJson(orderDataList));
                afDianOrder.setCreateTime(new Date());
                // 异常订单数据处理
                if (StrUtil.isEmpty(afDianOrder.getPlayerName())) {
                    afDianOrder.setResult(true);
                    afDianOrder.setErrorMsg(BaseUtil.getLangMsg("notPlayerName"));
                } else {
                    OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(afDianOrder.getPlayerName());
                    if (!offlinePlayer.hasPlayedBefore()) {
                        afDianOrder.setResult(true);
                        afDianOrder.setErrorMsg(BaseUtil.getLangMsg("noPlayer").replace("${player}", afDianOrder.getPlayerName()));
                    }
                }
                // 异常商品名称处理
                if (StrUtil.isEmpty(afDianOrder.getShopName())) {
                    afDianOrder.setResult(true);
                    afDianOrder.setErrorMsg(BaseUtil.getLangMsg("notShopName"));
                }
                afDianOrderList.add(afDianOrder);
            }
            sendMsg("获取爱发电数据: 页数" + page + ",条数:" + afDianOrderList.size());
        }
        // 判断当页记录是否存在了
        List<String> outTradeNoList = afDianOrderList.stream().map(AfDianOrder::getOutTradeNo).collect(Collectors.toList());
        if (page < totalPage && !outTradeNoList.contains(maxOrder)) {
            afDianOrderList.addAll(queryOrder(++page, maxOrder));
        }
        return afDianOrderList;
    }

    /**
     * 发送提醒消息
     *
     * @param msg 消息内容
     */
    private static void sendMsg(String msg) {
        boolean jobMsg = ConfigUtil.CONFIG.getBoolean("jobMsg");
        if (!jobMsg) {
            return;
        }
        MessageUtil.sendConsoleMessage(msg);
    }

}