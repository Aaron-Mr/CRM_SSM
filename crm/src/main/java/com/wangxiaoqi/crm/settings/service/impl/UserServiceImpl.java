package com.wangxiaoqi.crm.settings.service.impl;

import com.wangxiaoqi.crm.settings.domain.User;
import com.wangxiaoqi.crm.settings.mapper.UserMapper;
import com.wangxiaoqi.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User selectUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }
}
