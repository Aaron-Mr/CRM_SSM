package com.wangxiaoqi.crm.workbench.web.controller;

import com.wangxiaoqi.crm.commons.contants.Contants;
import com.wangxiaoqi.crm.commons.domain.ReturnObj;
import com.wangxiaoqi.crm.commons.utils.DateTimeUtil;
import com.wangxiaoqi.crm.commons.utils.HSSFUtils;
import com.wangxiaoqi.crm.commons.utils.UUIDUtil;
import com.wangxiaoqi.crm.settings.domain.User;
import com.wangxiaoqi.crm.settings.service.UserService;
import com.wangxiaoqi.crm.workbench.domain.Activity;
import com.wangxiaoqi.crm.workbench.domain.ActivityRemark;
import com.wangxiaoqi.crm.workbench.service.ActivityRemarkService;
import com.wangxiaoqi.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){

        List<User> userList = userService.selectAllUser();

        request.setAttribute("userList",userList);

        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveActivity.do")
    @ResponseBody
    public Object saveActivity(Activity activity, HttpSession session){
        activity.setId(UUIDUtil.getUUID());
        System.out.println(activity.getId());
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateTimeUtil.getSysTime());

        ReturnObj returnObj = new ReturnObj();

        try {

            int count = activityService.saveActivity(activity);
            if (count == 1){
                returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
            }else {
                returnObj.setCode(Contants.RETURN_OBJ_FAIL);
                returnObj.setMessage("??????????????????????????????");
            }

        }catch (Exception e){
            e.printStackTrace();
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
            returnObj.setMessage("??????????????????????????????");
        }

        return returnObj;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageSize,int pageNo){

        Map<String,Object> map = new HashMap<>();

        //??????????????????
        int beginNo = (pageNo-1)*pageSize;

        //????????????
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageSize",pageSize);
        map.put("beginNo",beginNo);

        List<Activity> activityList = activityService.selectActivityByConditionForPage(map);
        int count = activityService.selectCountOfActivityByCondition(map);

        //???????????????????????????map???????????????
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",count);

        System.out.println("---------------------");
        System.out.println("totalRows???"+count);
        System.out.println("activityList???"+activityList.size());

        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        ReturnObj returnObj = new ReturnObj();
        int count = activityService.deleteActivityByIds(id);

        if (count > 0){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/activity/selectActivityById.do")
    @ResponseBody
    public Object selectActivityById(String id){

        //??????????????????
        List<User> userList = userService.selectAllUser();

        System.out.println("-------------");
        System.out.println("id="+id);

        //??????activity??????
        Activity activity = activityService.selectActivityById(id);

        Map<String,Object> map = new HashMap<>();

        map.put("userList",userList);
        map.put("activity",activity);

        return map;
    }


    @RequestMapping("/workbench/activity/updateActivity.do")
    @ResponseBody
    public Object updateActivity(Activity activity, HttpSession session){
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)session.getAttribute("sessionUser")).getId();

        activity.setEditBy(editBy);
        activity.setEditTime(editTime);

        int count = activityService.updateActivity(activity);
        ReturnObj returnObj = new ReturnObj();
        if (count == 1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/activity/detail.do")
    public String detail(String id, HttpSession session){
        //??????id???????????????
        Activity activity = activityService.selectActivity(id);
        session.setAttribute("activity",activity);
        return "workbench/activity/detail";
    }

    @RequestMapping("/workbench/activity/addActivityRemark.do")
    @ResponseBody
    public Object addActivityRemark(String activityId, String noteContent, HttpSession session){

        ActivityRemark activityRemark = new ActivityRemark();

        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setNoteContent(noteContent);
        activityRemark.setActivityId(activityId);
        activityRemark.setCreateBy(((User)session.getAttribute("sessionUser")).getId());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("0");

        int count = activityRemarkService.addActivityRemark(activityRemark);
        ReturnObj returnObj = new ReturnObj();
        if (count == 1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/activity/selectActivityRemark.do")
    @ResponseBody
    public Object selectActivityRemark(String id){

        List<ActivityRemark> activityRemarkList = activityRemarkService.selectActivityRemarkByActivityId(id);

        return activityRemarkList;
    }


    @RequestMapping("/workbench/activity/editActivityRemark.do")
    @ResponseBody
    public Object editActivityRemark(ActivityRemark activityRemark, HttpSession session){
        activityRemark.setEditFlag("1");
        activityRemark.setEditBy(((User)session.getAttribute("sessionUser")).getId());
        activityRemark.setEditTime(DateTimeUtil.getSysTime());

        int count = activityRemarkService.editActivityRemark(activityRemark);
        ReturnObj returnObj = new ReturnObj();

        if (count==1){
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        int count = activityRemarkService.deleteActivityRemarkById(id);

        ReturnObj returnObj = new ReturnObj();

        if (count == 1) {
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
        }else {
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
        }

        return returnObj;
    }

    @RequestMapping("/testDownload.do")
    public void testDownload(HttpServletResponse response) throws IOException {
        //1?????????????????????
        response.setContentType("application/octet-stream;charset=UTF-8");

        //2??????????????????
        OutputStream outputStream = response.getOutputStream();

        //??????????????????????????????????????????????????????????????????????????????????????????
        response.addHeader("Content-Disposition","attachment;filename=MyStudentList.xls");

        //??????excel???????????????????????????
        InputStream inputStream = new FileInputStream("D:\\student.xls");

        byte[] bytes = new byte[256];
        int len = 0;

        while ((len = inputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
        }

        inputStream.close();
        outputStream.flush();

    }

    @RequestMapping("/workbench/activity/exportActivity.do")
    public void exportActivity(HttpServletResponse response) throws IOException {

        List<Activity> activityList = activityService.selectAllActivity();

        //??????excel??????
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("??????????????????");

        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("?????????");
        cell = row.createCell(2);
        cell.setCellValue("??????");
        cell = row.createCell(3);
        cell.setCellValue("????????????");
        cell = row.createCell(4);
        cell.setCellValue("????????????");
        cell = row.createCell(5);
        cell.setCellValue("??????");
        cell = row.createCell(6);
        cell.setCellValue("??????");
        cell = row.createCell(7);
        cell.setCellValue("????????????");
        cell = row.createCell(8);
        cell.setCellValue("?????????");
        cell = row.createCell(9);
        cell.setCellValue("????????????");
        cell = row.createCell(10);
        cell.setCellValue("?????????");

        //?????????????????????list????????????excel???
        Activity activity = null;
        for (int i = 0; i<activityList.size(); ++i){
            activity = activityList.get(i);

            row = sheet.createRow(i+1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellValue(activity.getEditBy());
        }

        //??????????????????
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=MyStudentList.xls");
        //???????????????
        OutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);

        workbook.close();
        outputStream.flush();

    }

    @RequestMapping("/workbench/activity/exportActivityByIds.do")
    public void exportActivityByIds(String[] id, HttpServletResponse response) throws IOException {

        List<Activity> activityList = activityService.selectActivityByIds(id);

        //??????excel??????
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("??????????????????");

        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("?????????");
        cell = row.createCell(2);
        cell.setCellValue("??????");
        cell = row.createCell(3);
        cell.setCellValue("????????????");
        cell = row.createCell(4);
        cell.setCellValue("????????????");
        cell = row.createCell(5);
        cell.setCellValue("??????");
        cell = row.createCell(6);
        cell.setCellValue("??????");
        cell = row.createCell(7);
        cell.setCellValue("????????????");
        cell = row.createCell(8);
        cell.setCellValue("?????????");
        cell = row.createCell(9);
        cell.setCellValue("????????????");
        cell = row.createCell(10);
        cell.setCellValue("?????????");

        //?????????????????????list????????????excel???
        Activity activity = null;
        for (int i = 0; i<activityList.size(); ++i){
            activity = activityList.get(i);

            row = sheet.createRow(i+1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellValue(activity.getEditBy());
        }

        //??????????????????
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=MyStudentList.xls");
        //???????????????
        OutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);

        workbook.close();
        outputStream.flush();

    }

    @RequestMapping("/workbench/activity/fileUpload.do")
    @ResponseBody
    public Object fileUpload(String myName, MultipartFile myFile){
        System.out.println("myName="+myName);
        System.out.println("------");
        String originalFilename = myFile.getOriginalFilename();
        System.out.println(originalFilename);
        File file = new File("D:\\JavaProject\\CRM_SSM\\serverDir\\",originalFilename);
        ReturnObj returnObj = new ReturnObj();
        try {
            myFile.transferTo(file);
            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
            returnObj.setMessage("????????????");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnObj;
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session){

        System.out.println("---?????????????????????---");

        List<Activity> activityList = new ArrayList<>();
        ReturnObj returnObj = new ReturnObj();
        Activity activity = null;

        try {
            InputStream inputStream = activityFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            HSSFCell cell = null;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);

                activity = new Activity();
                activity.setCreateBy(((User)session.getAttribute(Contants.SESSION_USER)).getId());
                activity.setOwner(((User)session.getAttribute(Contants.SESSION_USER)).getId());
                activity.setCreateTime(DateTimeUtil.getSysTime());
                activity.setId(UUIDUtil.getUUID());

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String str = HSSFUtils.getCellValueForStr(cell);
                    System.out.println(str);
                    if (j==0){
                        activity.setName(str);
                    }else if (j==1){
                        activity.setStartDate(str);
                    }else if (j==2){
                        activity.setEndDate(str);
                    }else if (j==3){
                        activity.setCost(str);
                    }else if (j==4){
                        activity.setDescription(str);
                    }
                }

                activityList.add(activity);

            }

            int count = activityService.insertActivityByList(activityList);

            returnObj.setCode(Contants.RETURN_OBJ_SUCCESS);
            returnObj.setRetDate(count);


        } catch (IOException e) {
            e.printStackTrace();
            returnObj.setCode(Contants.RETURN_OBJ_FAIL);
            returnObj.setMessage("???????????????");
        }

        return returnObj;
    }

}
