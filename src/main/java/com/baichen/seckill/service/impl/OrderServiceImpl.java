package com.baichen.seckill.service.impl;

import com.baichen.seckill.dao.OrderMapper;
import com.baichen.seckill.dao.SequenceMapper;
import com.baichen.seckill.pojo.Order;
import com.baichen.seckill.pojo.Sequence;
import com.baichen.seckill.error.BusinessException;
import com.baichen.seckill.error.EmBusinessError;
import com.baichen.seckill.service.ItemService;
import com.baichen.seckill.service.OrderService;
import com.baichen.seckill.service.UserService;
import com.baichen.seckill.service.model.ItemModel;
import com.baichen.seckill.service.model.OrderModel;
import com.baichen.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Date: 2019-10-07 16:46
 * @Author: baichen
 * @Description
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderMapper orderDOMapper;
    @Autowired
    private SequenceMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        //1.校验下单状态（用户是否合法，下单商品是否存在，购买数量是否正确）
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException((EmBusinessError.PARAMETER_VALIDATION_ERROR), "商品不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null || userId == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }

        // 校验秒杀活动信息
        if (promoId != null) {
            // 1. 校验对应活动是否存在对应商品
            if (promoId.intValue() != itemModel.getPromoModel().getId()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            } else if (itemModel.getPromoModel().getStatus().intValue() != 2) {
                // 2.校验活动是否在进行中
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动未开始");
            }
        }

        //2.落单减库存(支付减库存,这种无法确定支付后一定能买到商品，所以还是选择落单减库存)
        boolean result = itemService.decreaseStock(itemId, amount); //减库存，要保持事务一致性
        if (!result) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null) {
            // 秒杀获得的商品价格
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        // 订单总价
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(BigDecimal.valueOf(amount)));

        //生成交易流水号，订单号
        orderModel.setId(generatorOrderNo());
        Order orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        //商品销量增加
        itemService.increaseSales(itemId, amount);
        //4.返回前端
        return orderModel;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW) // 保证事务回滚后序列也是新的
    String generatorOrderNo() {
        //1.订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        //2.中间六位为自增序列
        //获取当前sequence，sequence_info中要设定最大值，防止超过6位，类似oracle中的序列
        int sequence = 0;
        Sequence sequenceDO = sequenceDOMapper.selectByPrimaryKey("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        //凑足六位
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0); // 补0
        }
        stringBuilder.append(sequenceStr);
        //3.最后两位分库分表位
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private Order convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        Order orderDO = new Order();
        BeanUtils.copyProperties(orderModel, orderDO);
        return orderDO;
    }
}

