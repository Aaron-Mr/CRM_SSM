package com.wangxiaoqi.crm.workbench.service.impl;

import com.wangxiaoqi.crm.workbench.domain.ClueRemark;
import com.wangxiaoqi.crm.workbench.mapper.ClueRemarkMapper;
import com.wangxiaoqi.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Autowired
    ClueRemarkMapper clueRemarkMapper;

    @Override
    public int createClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.createClueRemark(clueRemark);
    }

    @Override
    public List<ClueRemark> selectRemarkByClueId(String clueId) {
        return clueRemarkMapper.selectRemarkByClueId(clueId);
    }

    @Override
    public int updateClue(ClueRemark clueRemark) {
        return clueRemarkMapper.updateClue(clueRemark);
    }

    @Override
    public int deleteRemarkById(String id) {
        return clueRemarkMapper.deleteRemarkById(id);
    }
}
