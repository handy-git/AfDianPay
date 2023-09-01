package cn.handyplus.afdian.pay.service;

import cn.handyplus.afdian.pay.entity.AfDianOrder;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.db.Db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 爱发电订单
 *
 * @author handy
 */
public class AfDianOrderService {

    private AfDianOrderService() {
    }

    private static class SingletonHolder {
        private static final AfDianOrderService INSTANCE = new AfDianOrderService();
    }

    public static AfDianOrderService getInstance() {
        return AfDianOrderService.SingletonHolder.INSTANCE;
    }

    /**
     * 新增
     *
     * @param afDianOrder 入参
     * @return 成功
     */
    public int add(AfDianOrder afDianOrder) {
        return Db.use(AfDianOrder.class).execution().insert(afDianOrder);
    }

    /**
     * 新增
     *
     * @param afDianOrderList 入参
     */
    public void addBatch(List<AfDianOrder> afDianOrderList) {
        List<String> outTradeNoList = afDianOrderList.stream().map(AfDianOrder::getOutTradeNo).collect(Collectors.toList());
        List<AfDianOrder> whitelistAfDianOrders = this.listByOutTradeNoList(outTradeNoList);
        if (CollUtil.isNotEmpty(whitelistAfDianOrders)) {
            List<String> whitelistAfDianOrderList = whitelistAfDianOrders.stream().map(AfDianOrder::getOutTradeNo).collect(Collectors.toList());
            afDianOrderList = afDianOrderList.stream().filter(s -> !whitelistAfDianOrderList.contains(s.getOutTradeNo())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(afDianOrderList)) {
            return;
        }
        Db.use(AfDianOrder.class).execution().insertBatch(afDianOrderList);
    }

    /**
     * 根据id更新结果
     *
     * @param point    点券
     * @param errorMsg 错误消息
     * @param id       id
     * @return 成功
     */
    public boolean updateResult(Integer point, Boolean result, String errorMsg, Integer id) {
        Db<AfDianOrder> db = Db.use(AfDianOrder.class);
        db.update().set(point != null, AfDianOrder::getPoint, point)
                .set(AfDianOrder::getResult, result)
                .set(StrUtil.isNotEmpty(errorMsg), AfDianOrder::getErrorMsg, errorMsg);
        return db.execution().updateById(id) > 0;
    }

    /**
     * 查询全部
     *
     * @return list
     */
    public List<AfDianOrder> findAll() {
        return Db.use(AfDianOrder.class).execution().list();
    }

    /**
     * 分批查询已存在订单
     *
     * @param outTradeNoList 订单号
     * @return AfDianOrder
     */
    public List<AfDianOrder> listByOutTradeNoList(List<String> outTradeNoList) {
        List<List<String>> lists = CollUtil.splitList(outTradeNoList, 300);
        List<AfDianOrder> orderList = new ArrayList<>();
        for (List<String> list : lists) {
            Db<AfDianOrder> db = Db.use(AfDianOrder.class);
            db.where().in(AfDianOrder::getOutTradeNo, list);
            orderList.addAll(db.execution().list());
        }
        return orderList;
    }

    /**
     * 查询未处理的记录
     *
     * @return WhitelistAfDianOrder
     */
    public List<AfDianOrder> list() {
        Db<AfDianOrder> db = Db.use(AfDianOrder.class);
        db.where().eq(AfDianOrder::getResult, false)
                .ne(AfDianOrder::getPlayerName, "")
                .isNotNull(AfDianOrder::getPlayerName);
        return db.execution().list();
    }

    /**
     * 查询最大订单号
     *
     * @return Max 订单号
     */
    public String findMaxOrder() {
        Db<AfDianOrder> db = Db.use(AfDianOrder.class);
        db.where().orderByDesc(AfDianOrder::getOutTradeNo).limit(1, 1);
        Optional<AfDianOrder> afDianOrderOptional = db.execution().selectOne();
        return afDianOrderOptional.map(AfDianOrder::getOutTradeNo).orElse(null);
    }

    /**
     * 查询订单号
     *
     * @param orderNumber 订单号
     * @return AfDianOrder
     */
    public Optional<AfDianOrder> findByOrder(String orderNumber) {
        Db<AfDianOrder> db = Db.use(AfDianOrder.class);
        db.where().eq(AfDianOrder::getOutTradeNo, orderNumber);
        return db.execution().selectOne();
    }

    /**
     * 根据玩家名查询
     *
     * @param playerName 玩家名
     * @return AfDianOrder
     */
    public Integer findByPlayerName(String playerName) {
        Db<AfDianOrder> db = Db.use(AfDianOrder.class);
        db.where().eq(AfDianOrder::getPlayerName, playerName);
        return db.execution().count();
    }

    /**
     * 根据玩家名查询
     *
     * @param playerName 玩家名
     * @return 赞助的点券数量
     */
    public Integer findPointByPlayerName(String playerName) {
        Db<AfDianOrder> db = Db.use(AfDianOrder.class);
        db.where().eq(AfDianOrder::getPlayerName, playerName);
        List<AfDianOrder> list = db.execution().list();
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        return list.stream().mapToInt(AfDianOrder::getPoint).sum();
    }

    /**
     * 查询订单
     *
     * @param id 订单号
     * @return AfDianOrder
     */
    public Optional<AfDianOrder> findById(Integer id) {
        return Db.use(AfDianOrder.class).execution().selectById(id);
    }

}