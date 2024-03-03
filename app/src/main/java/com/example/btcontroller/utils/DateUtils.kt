package com.example.btcontroller.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    var cal = Calendar.getInstance()

    //获取年份
    @JvmStatic
    val year: Int
        get() = cal[Calendar.YEAR] //获取年份

    //获取月份
    @JvmStatic
    val month: Int
        get() = cal[Calendar.MONTH] + 1 //获取月份

    //获取日
    @JvmStatic
    val day: Int
        get() = cal[Calendar.DATE] //获取日

    //  return  cal.get(Calendar.HOUR);//获取小时,12小时制
    // 24小时制
    val hour: Int
        get() =//  return  cal.get(Calendar.HOUR);//获取小时,12小时制
            cal[Calendar.HOUR_OF_DAY] // 24小时制

    //获取分
    val minute: Int
        get() = cal[Calendar.MINUTE] //获取分

    //获取秒
    val second: Int
        get() = cal[Calendar.SECOND] //获取秒
    val week: String
        get() {
            val weeks = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
            val cal = Calendar.getInstance()
            cal.time = Date()
            var week_index = cal[Calendar.DAY_OF_WEEK] - 1
            if (week_index < 0) {
                week_index = 0
            }
            return weeks[week_index]
        }

    fun getWeek(date: Long): String {
        val weeks = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        var week_index = cal[Calendar.DAY_OF_WEEK] - 1
        if (week_index < 0) {
            week_index = 0
        }
        return weeks[week_index]
    }

    /**
     * 实现给定某日期，判断是星期几
     *
     * @param date 必须yyyy-MM-dd
     * @return
     */
    fun getWeekday(date: String?): String {
        val sd = SimpleDateFormat("yyyy-MM-dd")
        val sdw = SimpleDateFormat("E")
        var d: Date? = null
        try {
            d = sd.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return sdw.format(d)
    }

    /**
     * @param sdf
     * @return
     */
    fun getTime(sdf: SimpleDateFormat): String {
        //"yyyy-MM-dd HH:mm:ss aa",最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
        return sdf.format(Date())
    }// return time / 1000;

    /**
     * 获取当前时间戳
     *
     * @return
     */
    val currentTimeMillis: Long
        get() =// return time / 1000;
            System.currentTimeMillis()

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str   字符串日期 “2016-02-26 12:00:00”
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun date2TimeStamp(date_str: String?, format: String?): Long {
        try {
            val sdf = SimpleDateFormat(format)
            //  return sdf.parse(date_str).getTime() / 1000;
            return sdf.parse(date_str).time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 时间戳字符串转换成日期格式
     *
     * @param seconds 精确到毫秒
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun timeStamp2Date(seconds: Long, format: String?): String {
        var format = format
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss"
        }
        val sdf = SimpleDateFormat(format)
        // return sdf.format(new Date(Long.valueOf(seconds+"000")));
        return sdf.format(Date(seconds))
    }

    /**
     * @param dateStr 2008-08-08 12:10:12
     * @param format  yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun getFormatDate(dateStr: String?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        try {
            return sdf.format(sdf.parse(dateStr))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 判断是否为今天或者今天以后
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    fun isAfterToday(year: Int, month: Int, day: Int): Boolean {
        return if (year > DateUtils.year) {
            true
        } else if (year == DateUtils.year) {
            if (month > DateUtils.month) {
                true
            } else if (month == DateUtils.month) {
                day >= DateUtils.day
            } else {
                false
            }
        } else {
            false
        }
    }

    /**
     * 判断是否为当月或者当月以后
     *
     * @param year
     * @param month
     * @return
     */
    fun isAfterThisMonth(year: Int, month: Int): Boolean {
        return when {
            year > DateUtils.year -> true
            year == DateUtils.year -> month >= DateUtils.month
            else -> false
        }
    }

    fun getConvertMonth(numberStr: String?): String {
        val months = arrayOf("一月", "二月", "三月", "四月", "五月",
                "六月", "七月", "八月", "九月", "十月",
                "十一月", "十二月")
        try {
            val month = Integer.valueOf(numberStr!!)
            return months[month - 1]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}