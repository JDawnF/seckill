package com.baichen.seckill.service;

import com.baichen.seckill.error.BusinessException;
import com.baichen.seckill.service.model.OrderModel;

/**
 * @Date: 2019-10-07 16:45
 * @Author: baichen
 * @Description
 */
public interface OrderService {

    /**
     * @param userId    用户id
     * @param itemId    商品id
     * @param promoId   秒杀活动id
     * @param amount    数量
     * @return  商品订单模型
     * @throws BusinessException
     */
    //1.通过前端url上传过来秒杀活动id，然后
    //2.直接在下单接口内判断对应商品是否在秒杀
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}
