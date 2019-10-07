package com.baichen.seckill.service.impl;

import com.baichen.seckill.dao.PromoMapper;
import com.baichen.seckill.pojo.Promo;
import com.baichen.seckill.error.BusinessException;
import com.baichen.seckill.service.PromoService;
import com.baichen.seckill.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @Date: 2019-10-07 16:47
 * @Author: baichen
 * @Description
 */
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoMapper promoDOMapper;

    @Override
    public PromoModel getPromoById(Integer itemId) {
        //获取对应商品的秒杀信息
        Promo promoDO = promoDOMapper.selectByItemId(itemId);
        PromoModel promoModel = convertFromDataObject(promoDO);
        if(promoModel == null) {
            return null;
        }

        //判断秒杀活动的当前状态,1表示还未开始，2表示进行中，3表示已结束
        DateTime now = new DateTime();
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        } else if(promoModel.getEndDate().isBeforeNow()) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }
        return promoModel;
    }

    private PromoModel convertFromDataObject(Promo promoDO){
        if(promoDO == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}
