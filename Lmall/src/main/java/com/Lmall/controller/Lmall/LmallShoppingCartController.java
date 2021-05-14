package com.Lmall.controller.Lmall;

import com.Lmall.common.Constants;
import com.Lmall.common.MallException;
import com.Lmall.common.ServiceResultEnum;
import com.Lmall.controller.vo.MallShoppingCartItemVO;
import com.Lmall.controller.vo.MallUserVO;
import com.Lmall.entity.MallShoppingCartItem;
import com.Lmall.service.MallShoppingCartService;
import com.Lmall.util.Result;
import com.Lmall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
public class LmallShoppingCartController {

    @Resource
    private MallShoppingCartService mallShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemSize = 0;
        int priceTotal = 0;
        List<MallShoppingCartItemVO> myShoppingCartItems = mallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            int itemsTotal = myShoppingCartItems.stream().mapToInt(MallShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {//小于1说明数据异常
                return "error/error_5xx";
            }
            itemSize = myShoppingCartItems.size();
            //总价
            for (MallShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemSize", itemSize);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "Lmall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveMallShoppingCartItem(@RequestBody MallShoppingCartItem mallShoppingCartItem,
                                           HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        mallShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = mallShoppingCartService.saveMallCartItem(mallShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateMallShoppingCartItem(@RequestBody MallShoppingCartItem mallShoppingCartItem,
                                             HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        mallShoppingCartItem.setUserId(user.getUserId());
        String updateResult = mallShoppingCartService.updateMallCartItem(mallShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{mallShoppingCartItemId}")
    @ResponseBody
    public Result updateMallShoppingCartItem(@PathVariable("mallShoppingCartItemId") Long mallShoppingCartItemId,
                                             HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = mallShoppingCartService.deleteById(mallShoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(Long[] cartItemIds, HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (cartItemIds.length < 1) {
            MallException.fail("参数异常");
        }
        List<MallShoppingCartItemVO> itemsForSettle = mallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), user.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //无数据则不跳转至结算页
            MallException.fail("参数异常");
        } else {
            //总价
            for (MallShoppingCartItemVO mallShoppingCartItemVO : itemsForSettle) {
                priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                MallException.fail("价格异常");
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("cartItems", itemsForSettle);
        request.setAttribute("cartItemIds", Arrays.toString(cartItemIds).replace("[", "").replace("]", ""));
        return "Lmall/order-settle";
    }
}
