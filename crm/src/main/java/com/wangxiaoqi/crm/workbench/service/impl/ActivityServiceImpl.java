package com.wangxiaoqi.crm.workbench.service.impl;

import com.wangxiaoqi.crm.workbench.domain.Activity;
import com.wangxiaoqi.crm.workbench.mapper.ActivityMapper;
import com.wangxiaoqi.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveActivity(Activity activity) {
        return activityMapper.saveActivity(activity);
    }

    @Override
    public List<Activity> selectActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int selectCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int updateActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public Activity selectActivity(String id) {
        return activityMapper.selectActivity(id);
    }

    @Override
    public List<Activity> selectAllActivity() {

        return activityMapper.selectAllActivity();
    }

    @Override
    public List<Activity> selectActivityByIds(String[] ids) {
        return activityMapper.selectActivityByIds(ids);
    }

    @Override
    public int insertActivityByList(List<Activity> activityList) {

        return activityMapper.insertActivityByList(activityList);
    }
}
