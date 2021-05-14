package com.Lmall.controller.Lmall;

import com.Lmall.common.Constants;
import com.Lmall.common.MallException;
import com.Lmall.common.MallOrderStatusEnum;
import com.Lmall.common.ServiceResultEnum;
import com.Lmall.controller.vo.MallShoppingCartItemVO;
import com.Lmall.controller.vo.MallUserVO;
import com.Lmall.entity.MallOrder;
import com.Lmall.service.MallOrderService;
import com.Lmall.service.MallShoppingCartService;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.Result;
import com.Lmall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class LmallOrderController {

    @Resource
    private MallShoppingCartService mallShoppingCartService;
    @Resource
    private MallOrderService mallOrderService;

    @PostMapping("/saveOrder")
    @ResponseBody
    public Result settlePage(Long[] cartItemIds, HttpSession httpSession) {
        int priceTotal = 0;
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (cartItemIds.length < 1) {
            MallException.fail("参数异常");
        }
        List<MallShoppingCartItemVO> itemsForSave = mallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), user.getUserId());
        if (CollectionUtils.isEmpty(itemsForSave)) {
            //无数据
            MallException.fail("参数异常");
        } else {
            //总价
            for (MallShoppingCartItemVO mallShoppingCartItemVO : itemsForSave) {
                priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                MallException.fail("价格异常");
            }
            //保存订单并返回订单号
            String saveOrderResult = mallOrderService.saveOrder(user, itemsForSave);
            Result result = ResultGenerator.genSuccessResult();
            result.setData(saveOrderResult);
            return result;
        }
        return ResultGenerator.genFailResult("生成订单失败");
    }


    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallOrder mallOrder = mallOrderService.getMallOrderByOrderNo(orderNo);
        if (mallOrder.getOrderStatus().intValue() != MallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
            MallException.fail("非待支付状态下的订单无法支付");
        }
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", mallOrder.getTotalPrice());
        return "Lmall/pay-select";
    }

    @GetMapping("/payPage")
    public String payPage(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallOrder mallOrder = mallOrderService.getMallOrderByOrderNo(orderNo);
        if (mallOrder.getOrderStatus().intValue() != MallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
            MallException.fail("非待支付状态下的订单无法支付");
        }
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", mallOrder.getTotalPrice());
        if (payType == 1) {
            return "Lmall/alipay";
        } else {
            return "Lmall/wxpay";
        }
    }

    @GetMapping("/paySuccess")
    @ResponseBody
    public Result paySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType) {
        String payResult = mallOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }


    @GetMapping("/successPage/{orderNo}")
    public String successPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        MallOrder mallOrder = mallOrderService.getMallOrderByOrderNo(orderNo);
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("userAddress", mallOrder.getUserAddress());
        request.setAttribute("totalPrice", mallOrder.getTotalPrice());
        return "Lmall/success";
    }

    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", mallOrderService.getMyOrders(pageUtil));
        request.setAttribute("path", "orders");
        return "Lmall/my-orders";
    }


    @PutMapping("/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancelOrderResult = mallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        MallUserVO user = (MallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = mallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

}
