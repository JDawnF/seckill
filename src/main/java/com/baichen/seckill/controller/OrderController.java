package com.baichen.seckill.controller;

import com.baichen.seckill.error.BusinessException;
import com.baichen.seckill.error.EmBusinessError;
import com.baichen.seckill.response.CommonReturnType;
import com.baichen.seckill.service.OrderService;
import com.baichen.seckill.service.model.OrderModel;
import com.baichen.seckill.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
/**
 * @Date: 2019-10-07 16:48
 * @Author: baichen
 * @Description
 */
@Controller("/order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="promoId",required = false)Integer promoId,
                                        @RequestParam(name="amount")Integer amount) throws BusinessException {

        //获取登录信息（Boolean)
        Boolean isLogin = (Boolean) this.httpServletRequest.getSession().getAttribute("IS_LOGIN");
//        System.out.println(isLogin.booleanValue());
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登陆，不能下单");
        }
//        Boolean isLogin = (Boolean) this.httpServletRequest.getSession().getAttribute("IS_LOGIN");
//        if (isLogin == null || !isLogin.booleanValue()) {
//            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户还未登录，不能下单");
//        }
        // 获取在UserController中存入session的登录状态
        UserModel userModel = (UserModel) this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel = orderService.createOrder(userModel.getId(),itemId,promoId,amount);
        return CommonReturnType.create(null);
    }
}
