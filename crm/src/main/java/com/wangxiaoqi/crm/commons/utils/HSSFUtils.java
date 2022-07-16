package com.wangxiaoqi.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFUtils {
    public static String getCellValueForStr(HSSFCell cell){
        String value = "";

        if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            value = cell.getStringCellValue();
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            value = cell.getNumericCellValue()+"";
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            value = cell.getBooleanCellValue()+"";
        }else if (cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            value = cell.getCellFormula();
        }

        return value;
    }
}
