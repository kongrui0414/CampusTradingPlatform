package com.example.campustradingplatform.UtilTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {



    public static String getCurrentTime(){
        // 基于静态函数创建
        TimeZone time = TimeZone.getTimeZone("Etc/GMT-8");  //转换为中国时区
        TimeZone.setDefault(time);


        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param  pTime     设置的需要判断的时间  //格式如2012-09-08
     *

     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

//  String pTime = "2012-03-12";
    public String getWeek(String pTime) {


        String Week = "";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        return Week;
    }

    public static Date strToDate(String s){
        if(null == s|| "".equals(s)) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }

    public static String getCurrentTimeString(){
        return getOutputFormat().format(getCurrentDate());
    }

    public static SimpleDateFormat getOutputFormat(){
//        TimeZone timeZoneSH = TimeZone.getTimeZone("Asia/Shanghai");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
//        outputFormat.setTimeZone(timeZoneSH);
        return outputFormat;
    }

    public static int getPartCurrentTime(String part){
//        TimeZone timeZoneSH = TimeZone.getTimeZone("Asia/Shanghai");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
//        outputFormat.setTimeZone(timeZoneSH);
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (part){
            case "year":
                int year=calendar.get(Calendar.YEAR);
                return year;
            case "month":
                int month=calendar.get(Calendar.MONTH);
                return month;
            case "day":
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                return day;
            default:
                break;
        }
        return -1;
    }

    public static long getTimeMillis(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnMillis;
    }
    public static String getTimeExpend(String startTime, String endTime){

        //传入字串类型 2016/06/28 08:30

        long longStart = getTimeMillis(startTime); //获取开始时间毫秒数
        long longEnd = getTimeMillis(endTime);  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差


        long days = longExpend/(60 * 60 * 1000*24);
        long longHours = (longExpend-days*(60 * 60 * 1000*24) )/ (60 * 60 * 1000); //根据时间差来计算小时数
        long longMinutes = ((longExpend - days*(60 * 60 * 1000*24)-longHours * (60 * 60 * 1000)) )/ (60 * 1000);   //根据时间差来计算分钟数
        return days+"天"+longHours + "小时" + longMinutes+"分";
    }

    public static long getTimeExpendNum(String startTime, String endTime){

        //传入字串类型 2016/06/28 08:30

        long longStart = getTimeMillis(startTime); //获取开始时间毫秒数
        long longEnd = getTimeMillis(endTime);  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差


        return longExpend;
    }
    public static boolean compare(String time1, String time2) throws ParseException
    {
        //如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
        //将字符串形式的时间转化为Date类型的时间
        Date a=sdf.parse(time1);
        Date b=sdf.parse(time2);
        //Date类的一个方法，如果a早于b返回true，否则返回false
        if(a.before(b))
            return true;
        else
            return false;
		/*
		 * 如果你不喜欢用上面这个太流氓的方法，也可以根据将Date转换成毫秒
		if(a.getTime()-b.getTime()<0)
			return true;
		else
			return false;
		*/
    }
    public static boolean compare(Date a,Date b){
        if(a.before(b))
            return true;
        else
            return false;
    }

}
