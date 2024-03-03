package com.example.btcontroller.utils

import com.example.btcontroller.entity.Lunar
import com.example.btcontroller.entity.Solar
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object LunarSolarConverter {
    /*
     * |----4位闰月|-------------13位1为30天，0为29天|
     */
    private val lunar_month_days = intArrayOf(1887, 0x1694, 0x16aa, 0x4ad5,
            0xab6, 0xc4b7, 0x4ae, 0xa56, 0xb52a, 0x1d2a, 0xd54, 0x75aa, 0x156a,
            0x1096d, 0x95c, 0x14ae, 0xaa4d, 0x1a4c, 0x1b2a, 0x8d55, 0xad4,
            0x135a, 0x495d, 0x95c, 0xd49b, 0x149a, 0x1a4a, 0xbaa5, 0x16a8,
            0x1ad4, 0x52da, 0x12b6, 0xe937, 0x92e, 0x1496, 0xb64b, 0xd4a,
            0xda8, 0x95b5, 0x56c, 0x12ae, 0x492f, 0x92e, 0xcc96, 0x1a94,
            0x1d4a, 0xada9, 0xb5a, 0x56c, 0x726e, 0x125c, 0xf92d, 0x192a,
            0x1a94, 0xdb4a, 0x16aa, 0xad4, 0x955b, 0x4ba, 0x125a, 0x592b,
            0x152a, 0xf695, 0xd94, 0x16aa, 0xaab5, 0x9b4, 0x14b6, 0x6a57,
            0xa56, 0x1152a, 0x1d2a, 0xd54, 0xd5aa, 0x156a, 0x96c, 0x94ae,
            0x14ae, 0xa4c, 0x7d26, 0x1b2a, 0xeb55, 0xad4, 0x12da, 0xa95d,
            0x95a, 0x149a, 0x9a4d, 0x1a4a, 0x11aa5, 0x16a8, 0x16d4, 0xd2da,
            0x12b6, 0x936, 0x9497, 0x1496, 0x1564b, 0xd4a, 0xda8, 0xd5b4,
            0x156c, 0x12ae, 0xa92f, 0x92e, 0xc96, 0x6d4a, 0x1d4a, 0x10d65,
            0xb58, 0x156c, 0xb26d, 0x125c, 0x192c, 0x9a95, 0x1a94, 0x1b4a,
            0x4b55, 0xad4, 0xf55b, 0x4ba, 0x125a, 0xb92b, 0x152a, 0x1694,
            0x96aa, 0x15aa, 0x12ab5, 0x974, 0x14b6, 0xca57, 0xa56, 0x1526,
            0x8e95, 0xd54, 0x15aa, 0x49b5, 0x96c, 0xd4ae, 0x149c, 0x1a4c,
            0xbd26, 0x1aa6, 0xb54, 0x6d6a, 0x12da, 0x1695d, 0x95a, 0x149a,
            0xda4b, 0x1a4a, 0x1aa4, 0xbb54, 0x16b4, 0xada, 0x495b, 0x936,
            0xf497, 0x1496, 0x154a, 0xb6a5, 0xda4, 0x15b4, 0x6ab6, 0x126e,
            0x1092f, 0x92e, 0xc96, 0xcd4a, 0x1d4a, 0xd64, 0x956c, 0x155c,
            0x125c, 0x792e, 0x192c, 0xfa95, 0x1a94, 0x1b4a, 0xab55, 0xad4,
            0x14da, 0x8a5d, 0xa5a, 0x1152b, 0x152a, 0x1694, 0xd6aa, 0x15aa,
            0xab4, 0x94ba, 0x14b6, 0xa56, 0x7527, 0xd26, 0xee53, 0xd54, 0x15aa,
            0xa9b5, 0x96c, 0x14ae, 0x8a4e, 0x1a4c, 0x11d26, 0x1aa4, 0x1b54,
            0xcd6a, 0xada, 0x95c, 0x949d, 0x149a, 0x1a2a, 0x5b25, 0x1aa4,
            0xfb52, 0x16b4, 0xaba, 0xa95b, 0x936, 0x1496, 0x9a4b, 0x154a,
            0x136a5, 0xda4, 0x15ac)
    private val solar_1_1 = intArrayOf(1887, 0xec04c, 0xec23f, 0xec435, 0xec649,
            0xec83e, 0xeca51, 0xecc46, 0xece3a, 0xed04d, 0xed242, 0xed436,
            0xed64a, 0xed83f, 0xeda53, 0xedc48, 0xede3d, 0xee050, 0xee244,
            0xee439, 0xee64d, 0xee842, 0xeea36, 0xeec4a, 0xeee3e, 0xef052,
            0xef246, 0xef43a, 0xef64e, 0xef843, 0xefa37, 0xefc4b, 0xefe41,
            0xf0054, 0xf0248, 0xf043c, 0xf0650, 0xf0845, 0xf0a38, 0xf0c4d,
            0xf0e42, 0xf1037, 0xf124a, 0xf143e, 0xf1651, 0xf1846, 0xf1a3a,
            0xf1c4e, 0xf1e44, 0xf2038, 0xf224b, 0xf243f, 0xf2653, 0xf2848,
            0xf2a3b, 0xf2c4f, 0xf2e45, 0xf3039, 0xf324d, 0xf3442, 0xf3636,
            0xf384a, 0xf3a3d, 0xf3c51, 0xf3e46, 0xf403b, 0xf424e, 0xf4443,
            0xf4638, 0xf484c, 0xf4a3f, 0xf4c52, 0xf4e48, 0xf503c, 0xf524f,
            0xf5445, 0xf5639, 0xf584d, 0xf5a42, 0xf5c35, 0xf5e49, 0xf603e,
            0xf6251, 0xf6446, 0xf663b, 0xf684f, 0xf6a43, 0xf6c37, 0xf6e4b,
            0xf703f, 0xf7252, 0xf7447, 0xf763c, 0xf7850, 0xf7a45, 0xf7c39,
            0xf7e4d, 0xf8042, 0xf8254, 0xf8449, 0xf863d, 0xf8851, 0xf8a46,
            0xf8c3b, 0xf8e4f, 0xf9044, 0xf9237, 0xf944a, 0xf963f, 0xf9853,
            0xf9a47, 0xf9c3c, 0xf9e50, 0xfa045, 0xfa238, 0xfa44c, 0xfa641,
            0xfa836, 0xfaa49, 0xfac3d, 0xfae52, 0xfb047, 0xfb23a, 0xfb44e,
            0xfb643, 0xfb837, 0xfba4a, 0xfbc3f, 0xfbe53, 0xfc048, 0xfc23c,
            0xfc450, 0xfc645, 0xfc839, 0xfca4c, 0xfcc41, 0xfce36, 0xfd04a,
            0xfd23d, 0xfd451, 0xfd646, 0xfd83a, 0xfda4d, 0xfdc43, 0xfde37,
            0xfe04b, 0xfe23f, 0xfe453, 0xfe648, 0xfe83c, 0xfea4f, 0xfec44,
            0xfee38, 0xff04c, 0xff241, 0xff436, 0xff64a, 0xff83e, 0xffa51,
            0xffc46, 0xffe3a, 0x10004e, 0x100242, 0x100437, 0x10064b, 0x100841,
            0x100a53, 0x100c48, 0x100e3c, 0x10104f, 0x101244, 0x101438,
            0x10164c, 0x101842, 0x101a35, 0x101c49, 0x101e3d, 0x102051,
            0x102245, 0x10243a, 0x10264e, 0x102843, 0x102a37, 0x102c4b,
            0x102e3f, 0x103053, 0x103247, 0x10343b, 0x10364f, 0x103845,
            0x103a38, 0x103c4c, 0x103e42, 0x104036, 0x104249, 0x10443d,
            0x104651, 0x104846, 0x104a3a, 0x104c4e, 0x104e43, 0x105038,
            0x10524a, 0x10543e, 0x105652, 0x105847, 0x105a3b, 0x105c4f,
            0x105e45, 0x106039, 0x10624c, 0x106441, 0x106635, 0x106849,
            0x106a3d, 0x106c51, 0x106e47, 0x10703c, 0x10724f, 0x107444,
            0x107638, 0x10784c, 0x107a3f, 0x107c53, 0x107e48)

    /**
     * 国历节日 *表示放假日
     */
    private val sFtv = arrayOf(
            "0101*元旦", "0214 情人节", "0308 妇女节", "0312 植树节",
            "0315 消费者权益日", "0401 愚人节", "0501*劳动节", "0504 青年节",
            "0509 郝维节", "0512 护士节", "0601 儿童节", "0701 建党节",
            "0801 建军节", "0808 父亲节", "0816 燕衔泥节",
            "0910 教师节", "0928 孔子诞辰", "1001*国庆节", "1006 老人节",
            "1024 联合国日", "1111 光棍节",
            "1225 圣诞节")

    /**
     * 农历节日 *表示放假日
     */
    private val lFtv = arrayOf(
            "0101*春节、弥勒佛诞", "0106 定光佛诞", "0115 元宵节",
            "0208 释迦牟尼佛出家", "0215 释迦牟尼佛涅槃", "0209 海空上师诞",
            "0219 观世音菩萨诞", "0221 普贤菩萨诞", "0316 准提菩萨诞",
            "0404 文殊菩萨诞", "0408 释迦牟尼佛诞", "0415 佛吉祥日",
            "0505*端午节", "0513 伽蓝菩萨诞", "0603 护法韦驮尊天菩萨诞",
            "0619 观世音菩萨成道——此日放生、念佛，功德殊胜",
            "0707 七夕情人节", "0713 大势至菩萨诞", "0715 中元节",
            "0724 龙树菩萨诞", "0730 地藏菩萨诞", "0815*中秋节",
            "0822 燃灯佛诞", "0909 重阳节", "0919 观世音菩萨出家纪念日",
            "0930 药师琉璃光如来诞", "1005 达摩祖师诞", "1107 阿弥陀佛诞",
            "1208 释迦如来成道日，腊八节", "1224 小年",
            "1229 华严菩萨诞", "0100*除夕"
    )
    private val solarTerm = arrayOf(
            "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
            "清明", "谷雨", "立夏", "小满", "芒种", "夏至",
            "小暑", "大暑", "立秋", "处暑", "白露", "秋分",
            "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
    )
    private val solarTermInfo = intArrayOf(
            0, 21208, 42467, 63836, 85337, 107014, 128867, 150921,
            173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033,
            353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758
    )
    private val sFreg = Pattern.compile("^(\\d{2})(\\d{2})([\\s\\*])(.+)$")
    private val wFreg = Pattern.compile("^(\\d{2})(\\d)(\\d)([\\s\\*])(.+)$")
    private var utcCal: GregorianCalendar? = null
    fun toInt(str: String): Int {
        return try {
            str.toInt()
        } catch (e: Exception) {
            -1
        }
    }

    /**
     * 返回公历日期的节气字符串
     *
     * @return 二十四节气字符串, 若不是节气日, 返回空串(例:冬至)
     */
    fun getTermString(solar: Solar): String {
        //  System.out.println(solar.solarYear + "-" + solar.solarMonth + "-" + solar.solarDay);
        // 二十四节气
        val convertMonth = solar.solarMonth - 1
        var termString = ""
        if (getSolarTermDay(solar.solarYear, convertMonth * 2) == solar.solarDay) {
            termString = solarTerm[convertMonth * 2]
        } else if (getSolarTermDay(solar.solarYear, convertMonth * 2 + 1) == solar.solarDay) {
            termString = solarTerm[convertMonth * 2 + 1]
        }
        return termString
    }

    /**
     * 返回公历年节气的日期
     *
     * @param solarYear 指定公历年份(数字)
     * @param index     指定节气序号(数字,0从小寒算起)
     * @return 日期(数字, 所在月份的第几天)
     */
    private fun getSolarTermDay(solarYear: Int, index: Int): Int {
        var l = 31556925974.7.toLong() * (solarYear - 1900) + solarTermInfo[index] * 60000L
        l = l + UTC(1900, 0, 6, 2, 5, 0)
        return getUTCDay(Date(l))
    }

    /**
     * 返回全球标准时间 (UTC) (或 GMT) 的 1970 年 1 月 1 日到所指定日期之间所间隔的毫秒数。
     *
     * @param y   指定年份
     * @param m   指定月份
     * @param d   指定日期
     * @param h   指定小时
     * @param min 指定分钟
     * @param sec 指定秒数
     * @return 全球标准时间 (UTC) (或 GMT) 的 1970 年 1 月 1 日到所指定日期之间所间隔的毫秒数
     */
    @Synchronized
    fun UTC(y: Int, m: Int, d: Int, h: Int, min: Int, sec: Int): Long {
        makeUTCCalendar()
        synchronized(utcCal!!) {
            utcCal!!.clear()
            utcCal!![y, m, d, h, min] = sec
            return utcCal!!.timeInMillis
        }
    }

    @Synchronized
    private fun makeUTCCalendar() {
        if (utcCal == null) {
            utcCal = GregorianCalendar(TimeZone.getTimeZone("UTC"))
        }
    }

    /**
     * 取 Date 对象中用全球标准时间 (UTC) 表示的日期
     *
     * @param date 指定日期
     * @return UTC 全球标准时间 (UTC) 表示的日期
     */
    @Synchronized
    fun getUTCDay(date: Date): Int {
        makeUTCCalendar()
        synchronized(utcCal!!) {
            utcCal!!.clear()
            utcCal!!.timeInMillis = date.time
            return utcCal!![Calendar.DAY_OF_MONTH]
        }
    }

    @Synchronized
    fun findFestival(solar: Solar, lunar: Lunar) {
        var m: Matcher
        for (i in sFtv.indices) {
            m = sFreg.matcher(sFtv[i])
            if (m.find()) {
                if (solar.solarMonth == toInt(m.group(1)) && solar.solarDay == toInt(m.group(2))) {
                    solar.isSFestival = true
                    solar.solarFestivalName = m.group(4)
                    //if ("*".equals(m.group(3))) this.isHoliday = true;
                    break
                }
            }
        }
        for (i in lFtv.indices) {
            m = sFreg.matcher(lFtv[i])
            if (m.find()) {
                if (lunar.lunarMonth == toInt(m.group(1)) && lunar.lunarDay == toInt(m.group(2))) {
                    lunar.isLFestival = true
                    lunar.lunarFestivalName = m.group(4)
                    //					if ("*".equals(m.group(3))) this.isHoliday = true;
                    break
                }
            }
        }
    }

    private fun GetBitInt(data: Int, length: Int, shift: Int): Int {
        return data and ((1 shl length) - 1 shl shift) shr shift
    }

    // WARNING: Dates before Oct. 1582 are inaccurate
    private fun SolarToInt(y: Int, m: Int, d: Int): Long {
        var y = y
        var m = m
        m = (m + 9) % 12
        y = y - m / 10
        return (365 * y + y / 4 - y / 100 + y / 400 + (m * 306 + 5) / 10 + (d - 1)).toLong()
    }

    /**
     *
     * @param lunarYear
     * @return String of : 甲子年
     */
    fun lunarYearToGanZhi(lunarYear: Int): String {
        val tianGan = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
        val diZhi = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
        return tianGan[(lunarYear - 4) % 10] + diZhi[(lunarYear - 4) % 12] + "年"
    }

    private fun SolarFromInt(g: Long): Solar {
        var y = (10000 * g + 14780) / 3652425
        var ddd = g - (365 * y + y / 4 - y / 100 + y / 400)
        if (ddd < 0) {
            y--
            ddd = g - (365 * y + y / 4 - y / 100 + y / 400)
        }
        val mi = (100 * ddd + 52) / 3060
        val mm = (mi + 2) % 12 + 1
        y = y + (mi + 2) / 12
        val dd = ddd - (mi * 306 + 5) / 10 + 1
        val solar = Solar()
        solar.solarYear = y.toInt()
        solar.solarMonth = mm.toInt()
        solar.solarDay = dd.toInt()
        return solar
    }

    fun LunarToSolar(lunar: Lunar): Solar {
        val days = lunar_month_days[lunar.lunarYear - lunar_month_days[0]]
        val leap = GetBitInt(days, 4, 13)
        var offset = 0
        var loopend = leap
        if (!lunar.isLeap) {
            loopend = if (lunar.lunarMonth <= leap || leap == 0) {
                lunar.lunarMonth - 1
            } else {
                lunar.lunarMonth
            }
        }
        for (i in 0 until loopend) {
            offset += if (GetBitInt(days, 1, 12 - i) == 1) 30 else 29
        }
        offset += lunar.lunarDay
        val solar11 = solar_1_1[lunar.lunarYear - solar_1_1[0]]
        val y = GetBitInt(solar11, 12, 9)
        val m = GetBitInt(solar11, 4, 5)
        val d = GetBitInt(solar11, 5, 0)
        return SolarFromInt(SolarToInt(y, m, d) + offset - 1)
    }

    fun SolarToLunar(solar: Solar): Lunar {
        val lunar = Lunar()
        var index = solar.solarYear - solar_1_1[0]
        val data = (solar.solarYear shl 9 or (solar.solarMonth shl 5)
                or solar.solarDay)
        var solar11 = 0
        if (solar_1_1[index] > data) {
            index--
        }
        solar11 = solar_1_1[index]
        val y = GetBitInt(solar11, 12, 9)
        val m = GetBitInt(solar11, 4, 5)
        val d = GetBitInt(solar11, 5, 0)
        var offset = SolarToInt(solar.solarYear, solar.solarMonth,
                solar.solarDay) - SolarToInt(y, m, d)
        val days = lunar_month_days[index]
        val leap = GetBitInt(days, 4, 13)
        val lunarY = index + solar_1_1[0]
        var lunarM = 1
        var lunarD = 1
        offset += 1
        for (i in 0..12) {
            val dm = if (GetBitInt(days, 1, 12 - i) == 1) 30 else 29
            offset -= if (offset > dm) {
                lunarM++
                dm.toLong()
            } else {
                break
            }
        }
        lunarD = offset.toInt()
        lunar.lunarYear = lunarY
        lunar.lunarMonth = lunarM
        lunar.isLeap = false
        if (leap != 0 && lunarM > leap) {
            lunar.lunarMonth = lunarM - 1
            if (lunarM == leap + 1) {
                lunar.isLeap = true
            }
        }
        lunar.lunarDay = lunarD
        solar.solar24Term = getTermString(solar)
        findFestival(solar, lunar)
        return lunar
    }
}