package com.example.btcontroller.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.btcontroller.entity.CalendarDate
import com.example.btcontroller.adapter.CalendarGridViewAdapter
import com.example.btcontroller.utils.DateUtils.day
import com.example.btcontroller.utils.DateUtils.month
import com.example.btcontroller.utils.DateUtils.year
import com.example.btcontroller.R
import com.example.btcontroller.entity.CalendarDate.Companion.getCalendarDate

/**
 * A simple [Fragment] subclass.
 */
class CalendarViewFragment : Fragment() {
    private var isChoiceModelSingle = false
    private var mYear = 0
    private var mMonth = 0
    private var mGridView: GridView? = null
    private var onDateClickListener: OnDateClickListener? = null
    private var onDateCancelListener: OnDateCancelListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDateClickListener = context as OnDateClickListener
            if (!isChoiceModelSingle) {
                //多选
                onDateCancelListener = context as OnDateCancelListener
            }
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement OnDateClickListener or OnDateCancelListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mYear = requireArguments().getInt(YEAR)
            mMonth = requireArguments().getInt(MONTH)
            isChoiceModelSingle = requireArguments().getBoolean(CHOICE_MODE_SINGLE, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        mGridView = view.findViewById<View>(R.id.gv_calendar) as GridView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mListDataCalendar: List<CalendarDate> = getCalendarDate(mYear, mMonth) //日历数据
        mGridView!!.adapter = CalendarGridViewAdapter(mListDataCalendar)
        if (isChoiceModelSingle) {
            mGridView!!.choiceMode = GridView.CHOICE_MODE_SINGLE
        } else {
            mGridView!!.choiceMode = GridView.CHOICE_MODE_MULTIPLE
        }
        mGridView!!.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val calendarDate = (mGridView!!.adapter as CalendarGridViewAdapter).listData[position]
            if (isChoiceModelSingle) {
                //单选
                if (mListDataCalendar[position].isInThisMonth) {
                    onDateClickListener!!.OnDateClick(calendarDate)
                } else {
                    mGridView!!.setItemChecked(position, false)
                }
            } else {
                //多选
                if (mListDataCalendar[position].isInThisMonth) {
                    // mGridView.getCheckedItemIds()
                    if (!mGridView!!.isItemChecked(position)) {
                        onDateCancelListener!!.OnDateCancel(calendarDate)
                    } else {
                        onDateClickListener!!.OnDateClick(calendarDate)
                    }
                } else {
                    mGridView!!.setItemChecked(position, false)
                }
            }
        }
        mGridView!!.post {
            //需要默认选中当天
            val mListData = (mGridView!!.adapter as CalendarGridViewAdapter).listData
            val count = mListData.size
            for (i in 0 until count) {
                if (mListData[i].solar.solarDay == day && mListData[i].solar.solarMonth == month && mListData[i].solar.solarYear == year) {
                    if (null != mGridView!!.getChildAt(i) && mListData[i].isInThisMonth) {
                        // mListData.get(i).setIsSelect(true);
                        onDateClickListener!!.OnDateClick(mListData[i])
                        mGridView!!.setItemChecked(i, true)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser) {
            if (null != mGridView) {
                // mGridView.setItemChecked(mCurrentPosition, false);
                mGridView!!.clearChoices()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    interface OnDateClickListener {
        fun OnDateClick(calendarDate: CalendarDate)
    }

    interface OnDateCancelListener {
        fun OnDateCancel(calendarDate: CalendarDate)
    }

    companion object {
        private const val YEAR = "year"
        private const val MONTH = "month"
        private const val CHOICE_MODE_SINGLE = "choice_mode_single"
        fun newInstance(year: Int, month: Int): CalendarViewFragment {
            val fragment = CalendarViewFragment()
            val args = Bundle()
            args.putInt(YEAR, year)
            args.putInt(MONTH, month)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(year: Int, month: Int, isChoiceModelSingle: Boolean): CalendarViewFragment {
            val fragment = CalendarViewFragment()
            val args = Bundle()
            args.putInt(YEAR, year)
            args.putInt(MONTH, month)
            args.putBoolean(CHOICE_MODE_SINGLE, isChoiceModelSingle)
            fragment.arguments = args
            return fragment
        }
    }
}