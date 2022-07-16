package com.wangxiaoqi.crm.workbench.service;

import com.wangxiaoqi.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveActivity(Activity activity);

    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    int selectCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityByIds(String[] ids);

    Activity selectActivityById(String id);

    int updateActivity(Activity activity);

    Activity selectActivity(String id);

    List<Activity> selectAllActivity();

    List<Activity> selectActivityByIds(String[] ids);

    int insertActivityByList(List<Activity> activityList);

}
