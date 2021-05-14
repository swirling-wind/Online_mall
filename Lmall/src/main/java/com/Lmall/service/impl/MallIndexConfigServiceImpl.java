package com.Lmall.service.impl;

import com.Lmall.common.ServiceResultEnum;
import com.Lmall.controller.vo.MallIndexConfigGoodsVO;
import com.Lmall.dao.IndexConfigMapper;
import com.Lmall.dao.MallGoodsMapper;
import com.Lmall.entity.IndexConfig;
import com.Lmall.entity.MallGoods;
import com.Lmall.service.MallIndexConfigService;
import com.Lmall.util.BeanUtil;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallIndexConfigServiceImpl implements MallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private MallGoodsMapper goodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<MallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<MallIndexConfigGoodsVO> mallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<MallGoods> mallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            mallIndexConfigGoodsVOS = BeanUtil.copyList(mallGoods, MallIndexConfigGoodsVO.class);
            for (MallIndexConfigGoodsVO mallIndexConfigGoodsVO : mallIndexConfigGoodsVOS) {
                String goodsName = mallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = mallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    mallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    mallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return mallIndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
