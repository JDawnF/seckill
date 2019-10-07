package com.baichen.seckill.service.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Date: 2019-10-07 16:45
 * @Author: baichen
 * @Description 交易模型
 */
@Data
public class OrderModel {
    //要用string，表示订单号
    private String id;

    private Integer userId;

    private Integer itemId;

    private BigDecimal itemPrice;

    private Integer amount;

    private BigDecimal orderPrice;

    //若非空，表示以秒杀方式下单
    private Integer promoId;
}
