package com.wangxiaoqi.crm.workbench.service;

import com.wangxiaoqi.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {

    int addActivityRemark(ActivityRemark activityRemark);

    List<ActivityRemark> selectActivityRemarkByActivityId(String id);

    int editActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

}
