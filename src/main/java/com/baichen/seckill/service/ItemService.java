package com.baichen.seckill.service;

import com.baichen.seckill.error.BusinessException;
import com.baichen.seckill.service.model.ItemModel;

import java.util.List;

/**
 * @Date: 2019-10-07 16:36
 * @Author: baichen
 * @Description
 */
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();

    //商品详细浏览
    ItemModel getItemById(Integer id);

    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount);

    //商品下单后对应销量增加
    void increaseSales(Integer itemId,Integer amount) throws BusinessException;
}
