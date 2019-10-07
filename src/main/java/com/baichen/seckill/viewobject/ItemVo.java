package com.baichen.seckill.viewobject;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Date: 2019-10-07 16:36
 * @Author: baichen
 * @Description 商品值对象,与前端交互
 */
@Data
public class ItemVo {

    private Integer id;
    private String title;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String sales;
    private String imgUrl;

    //记录商品是否在秒杀活动中，以及对应的status = 0 没有秒杀活动，为1 待抢购，为2 进行中
    private Integer promoStatus;

    //秒杀活动价格
    private BigDecimal promoPrice;
    //秒杀活动ID
    private Integer promoId;

    private String startDate;
}
