package com.baichen.seckill.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @Date: 2019-10-07 16:44
 * @Author: baichen
 * @Descriptionv 秒杀模型
 */
@Data
public class PromoModel {
    private Integer id;

    //秒杀活动状态，1表示还未开始，2表示进行中，3表示已结束
    private Integer status;

    //秒杀活动名称
    private String promoName;

    //秒杀开始时间
    private DateTime startDate;

    private DateTime endDate;

    //秒杀活动使用商品
    private Integer itemId;

    //秒杀获得的商品价格
    private BigDecimal promoItemPrice;
}
