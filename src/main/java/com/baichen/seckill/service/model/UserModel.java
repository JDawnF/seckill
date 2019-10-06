package com.baichen.seckill.service.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Date: 2019-10-06 18:01
 * @Author: baichen
 * @Description User数据模型，定义前端返回的数据类型,其实就是DTO
 */
@Data
public class UserModel {

    private Integer id;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "姓名不能为空")
    private Byte gender;

    @NotNull(message = "年龄不能为空")
    @Min(value = 0,message = "年龄不能小于0")
    @Max(value = 150,message = "年龄不能大于150")
    private Integer age;

    @NotBlank(message = "手机号不能为空")
    private String telphone;

    private String registerMode;
    private String thirdPartyId;

    @NotBlank(message = "密码不能为空")
    private String encrptPassword;

}
