package com.example.btcontroller.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {
    /**
     * 获取上一月最后一天的日期数
     *
     * @param calendar
     * @return
     */
    fun getDayOfLastMonth(calendar: Calendar): Int {
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.add(Calendar.DATE, -1)
        //        System.out.println(calendar.get(Calendar.YEAR));
//        System.out.println(calendar.get(Calendar.MONTH)+1);
//        System.out.println(calendar.get(Calendar.DATE));
        val result = calendar[Calendar.DATE]
        calendar.add(Calendar.DATE, +1) //重新加一天，恢复
        return result
    }

    /**
     * 获取上一月月份数
     *
     * @param calendar
     * @return
     */
    fun getMonthOfLastMonth(calendar: Calendar): Int {
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.add(Calendar.DATE, -1)
        val result = calendar[Calendar.MONTH] + 1
        calendar.add(Calendar.DATE, +1) //重新加一天，恢复
        return result
    }

    /**
     * 获取上一月年份数
     *
     * @param calendar
     * @return
     */
    fun getYearOfLastMonth(calendar: Calendar): Int {
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.add(Calendar.DATE, -1)
        //        System.out.println(calendar.get(Calendar.YEAR));
//        System.out.println(calendar.get(Calendar.MONTH)+1);
//        System.out.println(calendar.get(Calendar.DATE));
        val result = calendar[Calendar.YEAR]
        calendar.add(Calendar.DATE, +1) //重新加一天，恢复
        return result
    }

    /**
     * 获取某年某月的天数
     *
     * @param year
     * @param month
     * @return
     */
    fun getdataCount(year: Int, month: Int): Int {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month - 1
        return cal.getActualMaximum(Calendar.DATE)
    }

    /**
     * 根据年月生成当月的日历中的日期
     *
     * @param year
     * @param month
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun getEverydayOfMonth(year: Int, month: Int): List<CalendarSimpleDate> {
        val list: MutableList<CalendarSimpleDate> = ArrayList()
        val count = getdataCount(year, month) //获取当月的天数
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        cal.time = sdf.parse("$year-$month-1")
        cal[Calendar.DAY_OF_MONTH] = 1
        var begin = cal[Calendar.DAY_OF_WEEK] - 1 //获取每月号星期几
        if (begin == 0) {
            begin = 7
        }
        //重置
        cal.time = sdf.parse("$year-$month-1")
        // int first = cal.get(Calendar.DAY_OF_WEEK);
        val dayOfLastMonth = getDayOfLastMonth(cal)
        val monthOfLastMonth = getMonthOfLastMonth(cal)
        val yearOfLastMonth = getYearOfLastMonth(cal)
        //填补上月的数据
        for (i in 0 until begin - 1) {
            //每月第一天为星期一则不添加
            if (begin != 1) {
                val calendarDate = CalendarSimpleDate(yearOfLastMonth, monthOfLastMonth, dayOfLastMonth - begin + i + 2)
                list.add(calendarDate)
            }
        }
        //填补本月的数据
        for (i in 1..count) {
            val calendarDate = CalendarSimpleDate(year, month, i)
            list.add(calendarDate)
            cal.time = sdf.parse("$year-$month-$i")
        }
        //填补下月的数据
        for (i in 1..7 - (begin - 1 + count) % 7) {
            if (7 - (begin - 1 + count) % 7 != 0 && 7 - (begin - 1 + count) % 7 != 7) {
                //                int nextMonth = (month + 1) % 12;
//                if (nextMonth == 1) {
//                    nextYear = nextYear + 1;
//                }
                var nextMonth = month + 1
                if (nextMonth == 13) {
                    nextMonth = 1
                }
                val calendarDate = CalendarSimpleDate(year, nextMonth, i)
                list.add(calendarDate)
            }
        }
        return list
    }

    class CalendarSimpleDate(var year: Int, var month: Int, var day: Int)
}