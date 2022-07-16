package com.wangxiaoqi.crm.settings.service.impl;

import com.wangxiaoqi.crm.settings.domain.DicValue;
import com.wangxiaoqi.crm.settings.mapper.DicValueMapper;
import com.wangxiaoqi.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DicValueServiceImpl implements DicValueService {

    @Autowired
    DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> selectDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
