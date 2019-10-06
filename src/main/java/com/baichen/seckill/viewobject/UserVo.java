package com.baichen.seckill.viewobject;

import lombok.Data;

/**
 * @Date: 2019-10-06 18:13
 * @Author: baichen
 * @Description User值对象类，返回给前端的字段
 */
@Data
public class UserVo {
    private Integer id;
    private String name;
    private Byte gender;
    private Integer age;
    private String telphone;
}
