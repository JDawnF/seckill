package com.baichen.seckill.service.impl;

import com.baichen.seckill.dao.UserMapper;
import com.baichen.seckill.dao.UserPasswordMapper;
import com.baichen.seckill.error.BusinessException;
import com.baichen.seckill.error.EmBusinessError;
import com.baichen.seckill.pojo.User;
import com.baichen.seckill.pojo.UserPassword;
import com.baichen.seckill.service.UserService;
import com.baichen.seckill.service.model.UserModel;
import com.baichen.seckill.valiator.ValidationResult;
import com.baichen.seckill.valiator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ValidatorImpl validator;


    @Transactional
    @Override
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null)
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);

        ValidationResult result = validator.validate(userModel);
        System.out.println(result.getErrMsg()+"result");
        if (result.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //实现model->dataobject方法
        User user = convertFromModel(userModel);
        //使用insertSelective而不是insert,insertSelective会将对象为null的字段不进行插入，使这个字段依赖数据库的默认值。
        try {
            userMapper.insertSelective(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已被注册！");
        }
        userModel.setId(user.getId());

        UserPassword userPassword = convertPasswordFromModel(userModel);
        userPasswordMapper.insertSelective(userPassword);
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户的手机获取用户信息
        User user = userMapper.selectByTelphone(telphone);
        if(user == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPassword userPassword = userPasswordMapper.selectByUserId(user.getId());
        UserModel userModel = convertFromDataObject(user,userPassword);
        //拿到用户信息内加密的密码是否和传输的是否相匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

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

    /**
     * 以下3个都是将DTO转为pojo
     */
    private UserPassword convertPasswordFromModel(UserModel userModel){
        if(userModel == null) {
            return null;
        }
        UserPassword userPassword = new UserPassword();
        userPassword.setEncrptPassword(userModel.getEncrptPassword());
        userPassword.setUserId(userModel.getId());
        return userPassword;
    }
    private User convertFromModel(UserModel userModel){
        if(userModel == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userModel,user);
        return user;
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