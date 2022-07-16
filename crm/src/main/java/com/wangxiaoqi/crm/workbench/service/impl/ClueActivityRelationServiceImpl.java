package com.wangxiaoqi.crm.workbench.service.impl;

import com.wangxiaoqi.crm.workbench.domain.ClueActivityRelation;
import com.wangxiaoqi.crm.workbench.mapper.ClueActivityRelationMapper;
import com.wangxiaoqi.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {

    @Autowired
    ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int insertRelation(List<ClueActivityRelation> clueActivityRelationList) {
        return clueActivityRelationMapper.insertRelation(clueActivityRelationList);
    }

    @Override
    public int unbund(Map<String, String> map) {
        return clueActivityRelationMapper.unbund(map);
    }
}
