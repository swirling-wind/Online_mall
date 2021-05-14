$(function () {

    //选择框点击事件处理
    $(".tbody_checkbox>.cbx_select").click(function () {
        var cbxArray = $(".tbody_checkbox>.cbx_select");
        //是否全部选择
        var allChecked = false;
        //是否全部未选择
        var noChecked = false;
        var checkedCount = 0;
        for (var cbx = 0; cbx < cbxArray.length; cbx++) {
            if (cbxArray[cbx].checked) {
                checkedCount = checkedCount + 1;
            }
        }
        if (checkedCount == cbxArray.length) {
            allChecked = true;
        }
        if (checkedCount == 0) {
            noChecked = true;
        }
        var itemDivs = $("tr.cart_item");
        //处理全选
        if (allChecked) {
            $("#select_all_cbx").prop("checked", true);
            itemDivs.addClass("item_selected");
            $("#settleBtn").addClass("selected");
            summary();
        }
        //处理全不选
        else if (noChecked) {
            $("#select_all_cbx").prop("checked", false);
            itemDivs.removeClass("item_selected");
            $("#settleBtn").removeClass("selected");
            summary();
        }
        //处理部分选择
        else {
            $("#select_all_cbx").prop("checked", false);
            itemDivs.removeClass("item_selected");
            $("#settleBtn").addClass("selected");
            summary();
        }
    });

    //全选按钮处理逻辑
    $("#select_all_cbx").click(function () {
        var allChecked = $(this).prop("checked");
        var obj = $("tr.cart_item");
        if (!allChecked) {
            $(".tbody_checkbox>.cbx_select").prop("checked", false);
            summary();
            obj.removeClass("item_selected");
            $("#settleBtn").removeClass("selected");
        } else {
            $(".tbody_checkbox>.cbx_select").prop("checked", true);
            summary();
            obj.addClass("item_selected");
            $("#settleBtn").addClass("selected");
        }
    });

});

//数量减
function itemDecrease(minusButtonObj) {
    minusButtonObj = $(minusButtonObj);
    var number = minusButtonObj.next("label");
    var value = parseInt(number.html());
    //调用修改数量的接口
    updateItem(minusButtonObj, value - 1);
    if (value > 1) {
        minusButtonObj.removeClass("no_minus");
    } else {
        minusButtonObj.addClass("no_minus");
    }
    if (minusButtonObj.hasClass("no_minus")) {
        return true;
    } else {
        if (isNaN(number.html()) || $.trim(number.html()) === "" || parseInt(number.html()) <= 1) {
            number.html("1");
            minusButtonObj.addClass("no_minus");
            return true;
        }
        value--;
        if (value < 10) {
            number.next("a").removeClass("no_minus");
        } else {
            number.next("a").addClass("no_minus");
        }
        var price = minusButtonObj.parents("tr").find(".goods_selling_price").text();
        price = price.substring(1);
        var price_sum = price * value;
        minusButtonObj.parents("tr").find(".goods_summary_price").text("￥" + price_sum);
        number.html(value);
        if (value === 1) {
            minusButtonObj.addClass("no_minus");
        }
        summary();
    }
}

//数量加
function itemIncrease(plusButtonObj) {
    plusButtonObj = $(plusButtonObj);
    var number = plusButtonObj.prev("label");
    var value = parseInt(number.html());
    //调用修改数量的接口
    updateItem(plusButtonObj, value + 1);
    if (value < 10) {
        plusButtonObj.removeClass("no_minus");
    } else {
        plusButtonObj.addClass("no_minus");
    }
    if (plusButtonObj.hasClass("no_minus")) {
        return true;
    } else {
        if (isNaN(number.html()) || $.trim(number.html()) === "" || parseInt(number.html()) < 1) {
            number.html("1");
            return true;
        }
        plusButtonObj.prevAll(".minus_btn").removeClass("no_minus");
        value++;
        var price = plusButtonObj.parents("tr").find(".goods_selling_price").text();
        price = price.substring(1);
        var price_sum = price * value;
        plusButtonObj.parents("tr").find(".goods_summary_price").text("￥" + price_sum);
        number.html(value);
        summary();
    }
}

//汇总数据
function summary() {
    var price_sum = 0.00;
    var obj = $("input.cbx_select:checked").parents("tr.cart_item");
    obj.each(function () {
        price_sum += parseFloat($(this).find(".goods_summary_price").text().substring(1));
    });
    $(".total_value").text(price_sum.toFixed(2));
    $("#item_size").text(obj.length);
}


/**
 *更新购物项
 */
function updateItem(btnObj, goodsCount) {
    var itemId = btnObj.parents("tr").find("#cartItemId").val();
    if (goodsCount > 10) {
        swal("单个商品最多可购买10个", {
            icon: "error",
        });
        return;
    }
    if (goodsCount < 1) {
        swal("数量异常", {
            icon: "error",
        });
        return;
    }
    //按钮设置为不可点击
    btnObj.addClass("no_minus");
    var data = {
        "cartItemId": itemId,
        "goodsCount": goodsCount
    };
    $.ajax({
        type: 'PUT',
        url: '/shop-cart',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            //按钮设置为可点击
            btnObj.removeClass("no_minus");
            if (result.resultCode == 200) {
                swal("修改数量成功", {
                    icon: "success",
                });
            } else {
                swal(result.message, {
                    icon: "error",
                });
            }
        },
        error: function () {
            //按钮设置为可点击
            btnObj.removeClass("no_minus");
            swal("操作失败", {
                icon: "error",
            });
        }
    });
}

/**
 * * 删除购物项
 * @param id
 */
function deleteItem(id) {
    swal({
        title: "确认弹框",
        text: "确认要删除数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: 'DELETE',
                    url: '/shop-cart/' + id,
                    success: function (result) {
                        if (result.resultCode == 200) {
                            window.location.reload();
                        } else {
                            swal("操作失败", {
                                icon: "error",
                            });
                        }
                    },
                    error: function () {
                        swal("操作失败", {
                            icon: "error",
                        });
                    }
                });
            }
        }
    )
    ;
}

function settle(obj) {
    obj = $(obj);
    if (!obj.hasClass("selected")) {
        return true;
    }
    var cartItemIds = [];
    var cartItemObjs = $("input.cbx_select:checked").parents("tr.cart_item");
    cartItemObjs.each(function () {
        var id = $(this).find("#cartItemId").val();
        cartItemIds.push(id);
    });
    window.location.href = '/shop-cart/settle?cartItemIds=' + cartItemIds;
}