package com.Lmall.service;

import com.Lmall.controller.vo.MallShoppingCartItemVO;
import com.Lmall.entity.MallShoppingCartItem;

import java.util.List;

public interface MallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param mallShoppingCartItem
     * @return
     */
    String saveMallCartItem(MallShoppingCartItem mallShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param mallShoppingCartItem
     * @return
     */
    String updateMallCartItem(MallShoppingCartItem mallShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param mallShoppingCartItemId
     * @return
     */
    MallShoppingCartItem getMallCartItemById(Long mallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param mallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long mallShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param mallUserId
     * @return
     */
    List<MallShoppingCartItemVO> getMyShoppingCartItems(Long mallUserId);

    /**
     * 根据userId和cartItemIds获取对应的购物项记录
     *
     * @param cartItemIds
     * @param mallUserId
     * @return
     */
    List<MallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long mallUserId);
}
