package com.wangxiaoqi.crm.commons.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将Date日期类转换成String的工具类
 */
public class DateUtils {
    /**
     * 将指定的Date类型对象转换成String：yyyy-MM-dd HH:mm:ss
     * @param date 要转换的Date数据
     * @return 转换后的String对象
     */
    public static String formateDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
}
