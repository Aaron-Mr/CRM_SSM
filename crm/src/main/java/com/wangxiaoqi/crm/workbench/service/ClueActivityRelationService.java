package com.wangxiaoqi.crm.workbench.service;

import com.wangxiaoqi.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationService {
    int insertRelation(List<ClueActivityRelation> clueActivityRelationList);
    int unbund(Map<String,String> map);
}
