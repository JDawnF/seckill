package com.baichen.seckill.service;

import com.baichen.seckill.service.model.UserModel;

/**
 * @Date: 2019-10-06 17:59
 * @Author: baichen
 * @Description
 */
public interface UserService {
    //通过对象id获取对象
    UserModel getUserById(Integer id);
    //注册
//    void register(UserModel userModel) throws BusinessException;

    /**
     * 登陆验证
     * @param telphone 用户注册的手机
     * @param encrptPassword 用户加密后的密码
     */
//    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;

}
