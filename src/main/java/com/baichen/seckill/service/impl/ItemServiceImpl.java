package com.baichen.seckill.service.impl;

import com.baichen.seckill.dao.ItemMapper;
import com.baichen.seckill.dao.ItemStockMapper;
import com.baichen.seckill.error.BusinessException;
import com.baichen.seckill.error.EmBusinessError;
import com.baichen.seckill.pojo.Item;
import com.baichen.seckill.pojo.ItemStock;
import com.baichen.seckill.service.ItemService;
import com.baichen.seckill.service.PromoService;
import com.baichen.seckill.service.model.ItemModel;
import com.baichen.seckill.service.model.PromoModel;
import com.baichen.seckill.valiator.ValidationResult;
import com.baichen.seckill.valiator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Beans;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date: 2019-10-07 16:41
 * @Author: baichen
 * @Description
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemStockMapper itemStockMapper;
    @Autowired
    private PromoService promoService;

    private Item convertItemFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemModel, item);
        return item;
    }

    private ItemStock convertItemStockFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        itemStock.setItemId(itemModel.getId());
        itemStock.setStock(itemModel.getStock());
        return itemStock;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        //转化itemMode-》dataobject
        Item item = this.convertItemFromItemModel(itemModel);
        //写入数据库
        itemMapper.insertSelective(item);
        itemModel.setId(item.getId());
        ItemStock itemStock = this.convertItemStockFromItemModel(itemModel);
        itemStockMapper.insertSelective(itemStock);
        //返回创建完成的对象，这里要返回存入数据库的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<Item> itemList = itemMapper.listItem(); // 降序排序
        List<ItemModel> itemModelList = itemList.stream().map(item -> {
            ItemStock itemStock = itemStockMapper.selectByItemId(item.getId());
            ItemModel itemModel = this.convertModelFromDataObject(item, itemStock);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        Item item = itemMapper.selectByPrimaryKey(id);
        if (item == null) {
            return null;
        }
        //操作获得库存数量
        ItemStock itemStock = itemStockMapper.selectByItemId(item.getId());
        //将dataobject转换成model
        ItemModel itemModel = this.convertModelFromDataObject(item, itemStock);
        //获取商品秒杀活动信息
        PromoModel promoModel = promoService.getPromoById(itemModel.getId());
        if (promoModel != null && promoModel.getStatus().intValue() != 3) {
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int affectedRow = itemStockMapper.decreaseStock(itemId, amount);    //受影响条数
        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject(Item item, ItemStock itemStock) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(item, itemModel);
        itemModel.setStock(itemStock.getStock());
        return itemModel;
    }
}
