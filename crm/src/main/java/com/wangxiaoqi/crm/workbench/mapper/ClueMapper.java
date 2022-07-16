package com.wangxiaoqi.crm.workbench.mapper;

import com.wangxiaoqi.crm.workbench.domain.Activity;
import com.wangxiaoqi.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Jul 07 23:57:17 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Jul 07 23:57:17 CST 2022
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Jul 07 23:57:17 CST 2022
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Jul 07 23:57:17 CST 2022
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Jul 07 23:57:17 CST 2022
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Jul 07 23:57:17 CST 2022
     */
    int updateByPrimaryKey(Clue record);

    int saveClue(Clue clue);

    List<Clue> selectClueByConditionForPage(Map<String,Object> map);

    int selectClueByCondition(Map<String,Object> map);

    Clue selectClueById(String id);

    Clue selectOneClueById(String id);

    int editClue(Clue clue);

    int deleteClueByIds(String[] ids);

    List<Activity> getActivityByName(Map<String,String> map);

    List<Activity> getActivityListByClueId(String clueId);

    Clue selectClue(String id);

}