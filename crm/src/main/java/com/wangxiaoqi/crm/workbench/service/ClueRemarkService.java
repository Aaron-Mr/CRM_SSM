package com.wangxiaoqi.crm.workbench.service;

import com.wangxiaoqi.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    int createClueRemark(ClueRemark clueRemark);
    List<ClueRemark> selectRemarkByClueId(String clueId);
    int updateClue(ClueRemark clueRemark);
    int deleteRemarkById(String id);
}
