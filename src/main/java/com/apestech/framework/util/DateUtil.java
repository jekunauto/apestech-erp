package com.apestech.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 功能：日期相关工具类
 *
 * @author xul
 * @create 2017-12-02 16:08
 */
public class DateUtil {


    /**
     *
     * @param str String 时间格式字串
     * @return String 带“-”分隔符的格式化时间字串
     */
    public static String strToDateStr(String str) throws Exception {
        String strDes = "", strTime = "";
        if (str.indexOf("-") == -1) {

            for (int i = 0; i <= str.length() - 1; i++) {
                if (((i % 4 == 0) || (i % 6 == 0)) && (i != 0) && (i <= 6)) {
                    strDes += "-";
                }
                if (i == 8) {
                    strDes += " ";
                } else {
                    strDes += str.charAt(i);
                }
            }

            if (strDes.length() > 19) {
                System.out.println(strDes.substring(0, 19));
                strTime += strDes.substring(0, 19);
                strDes = "";
                strDes += strTime;
            }
            return strDes;
        } else
            return str;
    }

    public static java.sql.Date parseDate(String sdate) {

        Calendar calendar = Calendar.getInstance();
        try {
            sdate = strToDateStr(sdate);
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(sdate));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    public static java.sql.Timestamp parseTimestamp(String sdate) {

        Calendar calendar = Calendar.getInstance();
        try {
            sdate = strToDateStr(sdate);
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdate));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new java.sql.Timestamp(calendar.getTimeInMillis());
    }

    public static String formatTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.valueOf(sdf.format(new Date()));
    }
}
