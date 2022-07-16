package com.wangxiaoqi.crm.settings.service;

import com.wangxiaoqi.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //登录验证
    User selectUserByLoginActAndPwd(Map<String,Object> map);

    //查询所有用户
    List<User> selectAllUser();

}
