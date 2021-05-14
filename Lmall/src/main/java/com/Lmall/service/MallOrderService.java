package com.Lmall.service;

import com.Lmall.controller.vo.MallOrderItemVO;
import com.Lmall.controller.vo.MallShoppingCartItemVO;
import com.Lmall.controller.vo.MallUserVO;
import com.Lmall.entity.MallOrder;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;

import java.util.List;

public interface MallOrderService {

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(MallUserVO user, List<MallShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    MallOrder getMallOrderByOrderNo(String orderNo);

    /**
     * 支付成功
     *
     * @param orderNo
     * @param payType
     * @return
     */
    String paySuccess(String orderNo, int payType);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);


    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param mallOrder
     * @return
     */
    String updateOrderInfo(MallOrder mallOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    List<MallOrderItemVO> getOrderItems(Long id);
}
