package com.example.btcontroller.adapter

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.btcontroller.R
import com.example.btcontroller.entity.CalendarDate
import com.example.btcontroller.entity.Lunar
import java.util.*

class CalendarGridViewAdapter(mListData: List<CalendarDate>) : BaseAdapter() {
    var listData: List<CalendarDate> = ArrayList()
    private val i = 0
    private val view: View? = null
    private val viewGroup: ViewGroup? = null
    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertview: View?, parent: ViewGroup): View? {
        var convertView = convertview
        var viewHolder: ViewHolder? = null
        val calendarDate = listData[position]
        if (convertView == null) {
            val inflater = parent.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.item_calendar, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.tv_day.text = calendarDate.solar.solarDay.toString()
        val str: String?
        str = if (!TextUtils.isEmpty(calendarDate.solar.solar24Term)) {
            calendarDate.solar.solar24Term
        } else if (!TextUtils.isEmpty(calendarDate.solar.solarFestivalName)) {
            calendarDate.solar.solarFestivalName
        } else {
            Lunar.getChinaDayString(listData[position].lunar!!.lunarDay)
        }
        viewHolder.tv_lunar_day.text = str
        if (listData[position].isInThisMonth) {
            viewHolder.tv_day.setTextColor(Color.parseColor("#000000"))
        } else {
            viewHolder.tv_day.setTextColor(Color.parseColor("#D7D7D7"))
            viewHolder.tv_lunar_day.setTextColor(Color.parseColor("#D7D7D7"))
        }
        return convertView
    }

    class ViewHolder(itemView: View) {
        val tv_day: TextView
        val tv_lunar_day: TextView

        init {
            tv_day = itemView.findViewById<View>(R.id.tv_day) as TextView
            tv_lunar_day = itemView.findViewById<View>(R.id.tv_lunar_day) as TextView
        }
    }

    init {
        listData = mListData
    }
}