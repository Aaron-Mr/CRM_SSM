package com.wangxiaoqi.crm.settings.mapper;

import com.wangxiaoqi.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Tue Jun 07 23:18:51 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Tue Jun 07 23:18:51 CST 2022
     */
    int insert(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Tue Jun 07 23:18:51 CST 2022
     */
    int insertSelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Tue Jun 07 23:18:51 CST 2022
     */
    User selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Tue Jun 07 23:18:51 CST 2022
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Tue Jun 07 23:18:51 CST 2022
     */
    int updateByPrimaryKey(User record);

    /**
     * 验证用户名和密码
     * @param map 前端传过来的用户名和密码
     * @return 将查询结果封装成User对象返回
     */
    User selectUserByLoginActAndPwd(Map<String,Object> map);

    /**
     * 查询用户列表
     * @return 返回用户列表集合
     */
    List<User> selectAllUser();


}