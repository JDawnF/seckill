package com.baichen.seckill.dao;

import com.baichen.seckill.pojo.Sequence;

public interface SequenceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int deleteByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int insert(Sequence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int insertSelective(Sequence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    Sequence selectByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int updateByPrimaryKeySelective(Sequence record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbggenerated Sun Oct 06 22:43:35 CST 2019
     */
    int updateByPrimaryKey(Sequence record);
}