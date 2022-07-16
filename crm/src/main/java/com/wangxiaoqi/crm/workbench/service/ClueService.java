package com.wangxiaoqi.crm.workbench.service;

import com.wangxiaoqi.crm.workbench.domain.Activity;
import com.wangxiaoqi.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveClue(Clue clue);
    List<Clue> selectClueByConditionForPage(Map<String,Object> map);
    int selectClueByCondition(Map<String,Object> map);
    Clue selectClueById(String id);
    Clue selectOneClueById(String id);
    int editClue(Clue clue);
    int deleteClueByIds(String[] ids);
    List<Activity> getActivityByName(Map<String,String> map);
    List<Activity> getActivityListByClueId(String clueId);

    void saveConvert(Map<String,Object> map);

}
