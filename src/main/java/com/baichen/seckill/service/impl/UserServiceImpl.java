package com.baichen.seckill.service.impl;

import com.baichen.seckill.dao.UserMapper;
import com.baichen.seckill.dao.UserPasswordMapper;
import com.baichen.seckill.pojo.User;
import com.baichen.seckill.pojo.UserPassword;
import com.baichen.seckill.service.UserService;
import com.baichen.seckill.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Date: 2019-10-06 18:00
 * @Author: baichen
 * @Description
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPasswordMapper userPasswordMapper;


    //为什么不直接返回
    @Override
    public UserModel getUserById(Integer id) {
        //调用userMapper获取到pojo
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return null;
        }
        //通过用户id获取用户加密密码信息
        UserPassword userPassword = userPasswordMapper.selectByUserId(user.getId());
        return convertFromDataObject(user, userPassword);
    }

    private UserModel convertFromDataObject(User user, UserPassword userPassword) {
        if (user == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);  // 必须确保两个值中字段名和类型一直
        if (userPassword != null) {
            userModel.setEncrptPassword(userPassword.getEncrptPassword());
        }
        return userModel;
    }
}