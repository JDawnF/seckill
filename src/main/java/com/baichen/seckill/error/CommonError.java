package com.baichen.seckill.error;

/**
 * @Date: 2019-10-06 18:09
 * @Author: baichen
 * @Description
 */
public interface CommonError {
    int getErrCode();

    String getErrMsg();

    CommonError setErrMsg(String errMsg);
}
