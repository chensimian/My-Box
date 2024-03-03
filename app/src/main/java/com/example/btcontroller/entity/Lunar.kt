package com.example.btcontroller.entity

import java.io.Serializable

class Lunar : Serializable {
    var isLeap = false
    var lunarDay = 0
    var lunarMonth = 0
    var lunarYear = 0
    var isLFestival = false
    var lunarFestivalName //农历节日
            : String? = null

    /*
    override fun toString(): String {
        return ("Lunar [isleap=" + isLeap + ", lunarDay=" + lunarDay
                + ", lunarMonth=" + lunarMonth + ", lunarYear=" + lunarYear
                + ", isLFestival=" + isLFestival + ", lunarFestivalName="
                + lunarFestivalName + "]")
    }

     */

    companion object {
        private val chineseNumber = arrayOf("一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二")
        fun getChinaDayString(day: Int): String {
            val chineseTen = arrayOf("初", "十", "廿", "卅")
            val n = if (day % 10 == 0) 9 else day % 10 - 1
            if (day > 30) return ""
            return if (day == 10) "初十" else chineseTen[day / 10] + chineseNumber[n]
        }
    }
}