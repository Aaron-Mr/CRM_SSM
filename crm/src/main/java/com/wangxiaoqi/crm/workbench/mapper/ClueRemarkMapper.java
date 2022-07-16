package com.wangxiaoqi.crm.workbench.mapper;

import com.wangxiaoqi.crm.workbench.domain.ClueRemark;

import java.util.List;
import java.util.Map;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sun Jul 10 20:56:44 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sun Jul 10 20:56:44 CST 2022
     */
    int insert(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sun Jul 10 20:56:44 CST 2022
     */
    int insertSelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sun Jul 10 20:56:44 CST 2022
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sun Jul 10 20:56:44 CST 2022
     */
    int updateByPrimaryKeySelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sun Jul 10 20:56:44 CST 2022
     */
    int updateByPrimaryKey(ClueRemark record);

    int createClueRemark(ClueRemark clueRemark);

    List<ClueRemark> selectRemarkByClueId(String clueId);

    int updateClue(ClueRemark clueRemark);

    int deleteRemarkById(String id);


}