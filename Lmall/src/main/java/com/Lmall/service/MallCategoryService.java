package com.Lmall.service;

import com.Lmall.controller.vo.MallIndexCategoryVO;
import com.Lmall.controller.vo.SearchPageCategoryVO;
import com.Lmall.entity.GoodsCategory;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;

import java.util.List;

public interface MallCategoryService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCategorisPage(PageQueryUtil pageUtil);

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 根据parentId和level获取分类列表
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<MallIndexCategoryVO> getCategoriesForIndex();

    /**
     * 返回分类数据(搜索页调用)
     *
     * @param categoryId
     * @return
     */
    SearchPageCategoryVO getCategoriesForSearch(Long categoryId);
}
