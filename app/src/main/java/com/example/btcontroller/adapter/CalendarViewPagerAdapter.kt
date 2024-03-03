package com.example.btcontroller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.btcontroller.fragment.CalendarViewFragment
import com.example.btcontroller.utils.DateUtils

class CalendarViewPagerAdapter(fm: FragmentManager?, private val isChoiceModelSingle: Boolean) : FragmentStatePagerAdapter(fm!!) {
    private val mThisMonthPosition = DateUtils.year * 12 + DateUtils.month - 1 //---100 -position
    private val number = mThisMonthPosition - NUM_ITEMS_CURRENT
    override fun getItem(position: Int): Fragment {
        val year = getYearByPosition(position)
        val month = getMonthByPosition(position)
        return CalendarViewFragment.newInstance(year, month, isChoiceModelSingle)
    }

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    fun getYearByPosition(position: Int): Int {
        return (position + number) / 12
    }

    fun getMonthByPosition(position: Int): Int {
        return (position + number) % 12 + 1
    }

    companion object {
        const val NUM_ITEMS = 200
        const val NUM_ITEMS_CURRENT = NUM_ITEMS / 2
    }
}