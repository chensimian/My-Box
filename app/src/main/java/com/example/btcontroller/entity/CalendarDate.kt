package com.example.btcontroller.entity

import androidx.core.net.ParseException
import com.example.btcontroller.entity.Lunar
import com.example.btcontroller.entity.Solar
import com.example.btcontroller.utils.CalendarUtils
import com.example.btcontroller.utils.LunarSolarConverter

class CalendarDate {
    var lunar: Lunar? = Lunar() //农历
    var solar = Solar() //公历
    var isInThisMonth //是否在当月
            : Boolean
    var isSelect //是否被选中
            : Boolean

    constructor(year: Int, month: Int, day: Int, isInThisMonth: Boolean, isSelect: Boolean, lunar: Lunar?) {
        this.isInThisMonth = isInThisMonth
        this.isSelect = isSelect
        this.lunar = lunar
    }

    constructor(isInThisMonth: Boolean, isSelect: Boolean, solar: Solar, lunar: Lunar?) {
        this.isInThisMonth = isInThisMonth
        this.isSelect = isSelect
        this.solar = solar
        this.lunar = lunar
    }

    companion object {
        fun getCalendarDate(year: Int, month: Int): List<CalendarDate> {
            val mListDate: MutableList<CalendarDate> = ArrayList()
            var list: List<CalendarUtils.CalendarSimpleDate?>? = null
            try {
                list = CalendarUtils.getEverydayOfMonth(year, month)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (list == null) {
                return emptyList()
            }
            val count = list.size
            for (i in 0 until count) {
                val solar = Solar()
                solar.solarYear = list[i]!!.year
                solar.solarMonth = list[i]!!.month
                solar.solarDay = list[i]!!.day
                val lunar = LunarSolarConverter.SolarToLunar(solar)
                mListDate.add(CalendarDate(month == list[i]!!.month, false, solar, lunar))
            }
            return mListDate
        }
    }

}