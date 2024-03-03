package com.example.btcontroller.entity

import java.io.Serializable

class Solar : Serializable {
    @JvmField
    var solarDay = 0
    @JvmField
    var solarMonth = 0
    @JvmField
    var solarYear = 0
    var isSFestival = false
    var solarFestivalName //公历节日
            : String? = null
    var solar24Term //24节气
            : String? = null

    /*
    override fun toString(): String {
        return ("Solar [solarDay=$solarDay, solarMonth=$solarMonth, solarYear=$solarYear, isSFestival=$isSFestival, solarFestivalName=$solarFestivalName, solar24Term=$solar24Term]")
    }

     */
}