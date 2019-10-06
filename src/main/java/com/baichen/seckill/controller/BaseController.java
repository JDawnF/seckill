package com.baichen.seckill.controller;

import com.baichen.seckill.error.BusinessException;
import com.baichen.seckill.error.EmBusinessError;
import com.baichen.seckill.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2019-10-06 18:19
 * @Author: baichen
 * @Description controller基类，定义了统一异常处理的方法
 */
public class BaseController {

//    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";
    //定义ExceptionHandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody       // 返回具体的异常信息
    public Object handlerException(HttpServletRequest request, Exception ex) {
        //必须自己封装data对象，否则data为exception反序列化的对象
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {  // 判断异常类型
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            responseData.put("errCode", EmBusinessError.UN_KNOW_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UN_KNOW_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData, "fail");
    }
}
