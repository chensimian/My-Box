package com.example.btcontroller.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.btcontroller.entity.CalendarDate
import com.example.btcontroller.R
import com.example.btcontroller.activity.ShujuActivity
import com.example.btcontroller.fragment.CalendarViewFragment.OnDateCancelListener
import com.example.btcontroller.fragment.CalendarViewFragment.OnDateClickListener
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DoseRecordFragment : Fragment(), OnDateClickListener, CalendarViewPagerFragment.OnPageChangeListener, OnDateCancelListener {

    private lateinit var iv_analyze: ImageView
    private lateinit var iv_search: ImageView
    private var v: View? = null
    private val mListDate: MutableList<CalendarDate> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fu_yao_ji_lu, container, false)
        iv_analyze =v!!.findViewById(R.id.iv_analyze)  //
        iv_search = v!!.findViewById(R.id.iv_search)   //
        tv_date = v!!.findViewById(R.id.tv_date)


        //分析点击按钮
        (iv_analyze.parent as View).setOnClickListener {
            val i = Intent(activity, ShujuActivity::class.java)
             startActivity(i)
        }
        //搜索按钮
        (iv_search.parent as View).setOnClickListener {
            //val i = Intent(activity, EditActivity::class.java)
           // startActivity(i)
        }

        initFragment()
        return v
    }

    private fun initFragment() {
        val fm = childFragmentManager
        val tx = fm.beginTransaction()
        //Fragment fragment = new CalendarViewPagerFragment();
        val fragment: Fragment = CalendarViewPagerFragment.Companion.newInstance(isChoiceModelSingle)
        tx.replace(R.id.fl_content, fragment, null)
        tx.commit()
    }

    override fun OnDateCancel(calendarDate: CalendarDate) {
        val count = mListDate.size
        for (i in 0 until count) {
            val date = mListDate[i]
            if (date.solar.solarDay == calendarDate.solar.solarDay) {
                mListDate.removeAt(i)
                break
            }
        }
        tv_date!!.text = listToString(mListDate)
    }

    override fun OnDateClick(calendarDate: CalendarDate) {
        val year = calendarDate.solar.solarYear
        val month = calendarDate.solar.solarMonth
        val day = calendarDate.solar.solarDay
        if (isChoiceModelSingle) {
            tv_date!!.text = "$year-$month-$day"
        } else {
            //System.out.println(calendarDate.getSolar().solarDay);
            mListDate.add(calendarDate)
            tv_date!!.text = listToString(mListDate)
        }
    }

    override fun OnPageChange(year: Int, month: Int) {
        tv_date!!.text = "$year-$month"
        mListDate.clear()
    }

    companion object {
        var doseRecordFragment: DoseRecordFragment? = null
        var isChoiceModelSingle = true
        var tv_date: TextView? = null
        fun newInstance(): DoseRecordFragment? {
            if (doseRecordFragment == null) {
                doseRecordFragment = DoseRecordFragment()
            }
            return doseRecordFragment
        }

        fun listToString(list: List<CalendarDate>): String {
            var result : String = ""
            for (date in list) {
                result += "${date.solar.solarYear}-${date.solar.solarMonth}=${date.solar.solarDay} "
            }
            return result
        }
    }
}