package com.wangxiaoqi.crm.workbench.service.impl;

import com.wangxiaoqi.crm.workbench.domain.ActivityRemark;
import com.wangxiaoqi.crm.workbench.mapper.ActivityRemarkMapper;
import com.wangxiaoqi.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Autowired
    ActivityRemarkMapper activityRemarkMapper;

    @Override
    public int addActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.addActivityRemark(activityRemark);
    }

    @Override
    public List<ActivityRemark> selectActivityRemarkByActivityId(String id) {
        return activityRemarkMapper.selectActivityRemarkByActivityId(id);
    }

    @Override
    public int editActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.editActivityRemark(activityRemark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }
}
