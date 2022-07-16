package com.wangxiaoqi.crm.workbench.web.controller;

import com.wangxiaoqi.crm.commons.contants.Contants;
import com.wangxiaoqi.crm.commons.domain.ReturnObj;
import com.wangxiaoqi.crm.commons.utils.DateTimeUtil;
import com.wangxiaoqi.crm.commons.utils.UUIDUtil;
import com.wangxiaoqi.crm.settings.domain.DicValue;
import com.wangxiaoqi.crm.settings.domain.User;
import com.wangxiaoqi.crm.settings.service.DicValueService;
import com.wangxiaoqi.crm.settings.service.UserService;
import com.wangxiaoqi.crm.workbench.domain.Activity;
import com.wangxiaoqi.crm.workbench.domain.Clue;
import com.wangxiaoqi.crm.workbench.domain.ClueActivityRelation;
import com.wangxiaoqi.crm.workbench.domain.ClueRemark;
import com.wangxiaoqi.crm.workbench.service.ClueActivityRelationService;
import com.wangxiaoqi.crm.workbench.service.ClueRemarkService;
import com.wangxiaoqi.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {
    @Autowired
    DicValueService dicValueService;

    @Autowired
    UserService userService;

    @Autowired
    ClueService clueService;

    @Autowired
    ClueRemarkService clueRemarkService;

    @Autowired
    ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request,HttpSession session){
        //获取动态数据
        List<User> userList = userService.selectAllUser();
        List<DicValue> appellationList = dicValueService.selectDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.selectDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");

        //将数据保存到request中
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        session.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);

        //跳转到index页面
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveClue.do")
    @ResponseBody
    public Object saveClue(Clue clue, HttpSession session){
        //封装参数
        clue.setId(UUIDUtil.getUUID());
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateTimeUtil.getSysTime());

        ReturnObj returnObj = new ReturnObj();

        int count = clueService.saveClue(clue);

        System.out.println("-------------------------------------------");
        System.out.println("state:"+clue.getState());
        if (count==1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/clue/selectClueByConditionForPage.do")
    @ResponseBody
    public Object selectClueByConditionForPage(String fullname, String company, String phone, String owner, String mphone, String source, String state, Integer pageSize, Integer pageNo){
        Map<String,Object> map = new HashMap<>();
        System.out.println("--------------------clue分页查询-------------------");
        //计算页码
        int beginNo = (pageNo-1)*pageSize;

        //封装参数
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("state",state);
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);

        //获取clueList
        List<Clue> clueList = clueService.selectClueByConditionForPage(map);
        //获取总条数
        int count = clueService.selectClueByCondition(map);

        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("totalRows",count);

        return retMap;
    }

    @RequestMapping("/workbench/clue/toDetail.do")
    public String toDetail(String id, HttpSession session){
        Clue clue = clueService.selectClueById(id);
        session.setAttribute("clue",clue);
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/getClueById.do")
    @ResponseBody
    public Object getClueById(String id){
        Clue clue = clueService.selectOneClueById(id);
        System.out.println("---123---");
        return clue;
    }

    @RequestMapping("/workbench/clue/editClue.do")
    @ResponseBody
    public Object editClue(Clue clue,HttpSession session){
        //封装参数
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        clue.setEditBy(user.getId());
        clue.setEditTime(DateTimeUtil.getSysTime());

        int count = clueService.editClue(clue);

        ReturnObj returnObj = new ReturnObj();

        if (count == 1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id){

        int count = clueService.deleteClueByIds(id);

        ReturnObj returnObj = new ReturnObj();

        if (count>0){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/clue/createClueRemark.do")
    @ResponseBody
    public Object createClueRemark(String noteContent,HttpSession session){
        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setNoteContent(noteContent);
        clueRemark.setId(UUIDUtil.getUUID());
        Clue clue = (Clue) session.getAttribute("clue");
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clueRemark.setClueId(clue.getId());
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateTimeUtil.getSysTime());
        clueRemark.setEditFlag("0");

        ReturnObj returnObj = new ReturnObj();

        int count = clueRemarkService.createClueRemark(clueRemark);

        if (count==1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/clue/selectRemarkByClueId.do")
    @ResponseBody
    public Object selectRemarkByClueId(String clueId){
        List<ClueRemark> clueRemarkList = clueRemarkService.selectRemarkByClueId(clueId);
        return clueRemarkList;
    }

    @RequestMapping("/workbench/clue/updateClue.do")
    @ResponseBody
    public Object updateClue(ClueRemark clueRemark, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clueRemark.setEditBy(user.getId());
        clueRemark.setEditTime(DateTimeUtil.getSysTime());
        clueRemark.setEditFlag("1");
        int count = clueRemarkService.updateClue(clueRemark);
        ReturnObj returnObj = new ReturnObj();

        if (count==1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/clue/deleteRemarkById.do")
    @ResponseBody
    public Object deleteRemarkById(String id){
        ReturnObj returnObj = new ReturnObj();

        int count = clueRemarkService.deleteRemarkById(id);

        if (count == 1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return  returnObj;
    }

    @RequestMapping("/workbench/clue/getActivityByName.do")
    @ResponseBody
    public Object getActivityByName(String clueId, String name){
        Map<String,String> map = new HashMap();
        map.put("clueId",clueId);
        map.put("name",name);

        List<Activity> activityList = clueService.getActivityByName(map);

        return activityList;
    }

    @RequestMapping("/workbench/clue/insertClueActivityRelation.do")
    @ResponseBody
    public Object insertClueActivityRelation(String clueId, String[] activityId){

        ReturnObj returnObj = new ReturnObj();

        List<ClueActivityRelation> clueActivityRelationList = new ArrayList<>();

        //封装参数
        for (int i=0; i<activityId.length; ++i){
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setActivityId(activityId[i]);
            clueActivityRelationList.add(clueActivityRelation);
        }

        int count = clueActivityRelationService.insertRelation(clueActivityRelationList);

        if (count == activityId.length){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/clue/getActivityListByClueId.do")
    @ResponseBody
    public Object getActivityListByClueId(String clueId){
        List<Activity> activityList = clueService.getActivityListByClueId(clueId);
        return activityList;
    }

    @RequestMapping("/workbench/clue/unbund.do")
    @ResponseBody
    public Object unbund(String clueId, String activityId){
        Map<String,String> map = new HashMap<>();

        map.put("clueId",clueId);
        map.put("activityId",activityId);

        int count = clueActivityRelationService.unbund(map);
        System.out.println("---"+count+"---");
        ReturnObj returnObj = new ReturnObj();
        if (count==1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/clue/toConvert.do")
    public String convert(){
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        System.out.println("---进入转换操作---");
        //封装参数
        Map<String,Object> map = new HashMap<>();

        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));

        ReturnObj returnObj = new ReturnObj();

        try {
            //调用service层方法
            clueService.saveConvert(map);
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
            returnObj.setMessage("转换失败！");
        }

        return returnObj;
    }

}
