package com.baichen.seckill.dao;

import com.baichen.seckill.pojo.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int insert(Item record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int insertSelective(Item record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    Item selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int updateByPrimaryKeySelective(Item record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int updateByPrimaryKey(Item record);

    /**
     *  根据销量降序
     * @return
     */
    List<Item> listItem();

    int increaseSales(@Param("id")Integer id, @Param("amount")Integer amount);
}