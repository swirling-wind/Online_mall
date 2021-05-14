package com.Lmall.dao;

import com.Lmall.entity.MallShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(MallShoppingCartItem record);

    int insertSelective(MallShoppingCartItem record);

    MallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    MallShoppingCartItem selectByUserIdAndGoodsId(@Param("mallUserId") Long mallUserId, @Param("goodsId") Long goodsId);

    List<MallShoppingCartItem> selectByUserId(@Param("mallUserId") Long mallUserId, @Param("number") int number);

    List<MallShoppingCartItem> selectByUserIdAndCartItemIds(@Param("mallUserId") Long mallUserId, @Param("cartItemIds") List<Long> cartItemIds);

    int selectCountByUserId(Long mallUserId);

    int updateByPrimaryKeySelective(MallShoppingCartItem record);

    int updateByPrimaryKey(MallShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}