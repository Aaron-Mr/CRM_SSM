package com.wangxiaoqi.crm.workbench.service.impl;

import com.wangxiaoqi.crm.commons.contants.Contants;
import com.wangxiaoqi.crm.commons.utils.DateTimeUtil;
import com.wangxiaoqi.crm.commons.utils.UUIDUtil;
import com.wangxiaoqi.crm.settings.domain.User;
import com.wangxiaoqi.crm.workbench.domain.Activity;
import com.wangxiaoqi.crm.workbench.domain.Clue;
import com.wangxiaoqi.crm.workbench.domain.Customer;
import com.wangxiaoqi.crm.workbench.mapper.ClueMapper;
import com.wangxiaoqi.crm.workbench.mapper.CustomerMapper;
import com.wangxiaoqi.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    ClueMapper clueMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public int saveClue(Clue clue) {
        return clueMapper.saveClue(clue);
    }

    @Override
    public List<Clue> selectClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int selectClueByCondition(Map<String, Object> map) {
        return clueMapper.selectClueByCondition(map);
    }

    @Override
    public Clue selectClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public Clue selectOneClueById(String id) {
        return clueMapper.selectOneClueById(id);
    }

    @Override
    public int editClue(Clue clue) {
        return clueMapper.editClue(clue);
    }

    @Override
    public int deleteClueByIds(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }

    @Override
    public List<Activity> getActivityByName(Map<String,String> map) {
        return clueMapper.getActivityByName(map);
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        return clueMapper.getActivityListByClueId(clueId);
    }


    @Override
    public void saveConvert(Map<String, Object> map) {
        String clueId = (String) map.get("clueId");

        //获取当前用户
        User user = (User) map.get(Contants.SESSION_USER);

        //获取是否创建交易按钮状态
        String isCreateTran = (String) map.get("isCreateTran");

        //根据id查询线索
        Clue clue = clueMapper.selectClue(clueId);
        //把线索中有关公司的信息转换到客户表中
        Customer customer = new Customer();
        customer.setId(UUIDUtil.getUUID());
        customer.setOwner(user.getId());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setName(clue.getCompany());
        customer.setDescription(clue.getDescription());
        customer.setCreateTime(DateTimeUtil.getSysTime());
        customer.setCreateBy(user.getId());
        customer.setContactSummary(clue.getContactSummary());
        customer.setAddress(clue.getAddress());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());

        customerMapper.insertCustomer(customer);

    }
}
