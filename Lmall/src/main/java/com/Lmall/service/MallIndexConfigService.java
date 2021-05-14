package com.Lmall.service;

import com.Lmall.controller.vo.MallIndexConfigGoodsVO;
import com.Lmall.entity.IndexConfig;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;

import java.util.List;

public interface MallIndexConfigService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<MallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
