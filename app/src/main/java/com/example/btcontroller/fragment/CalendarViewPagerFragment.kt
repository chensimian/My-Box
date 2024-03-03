package com.example.btcontroller.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.btcontroller.entity.CalendarDate
import com.example.btcontroller.adapter.CalendarViewPagerAdapter
import com.example.btcontroller.R
import com.example.btcontroller.fragment.CalendarViewFragment.OnDateClickListener

/**
 * A simple [Fragment] subclass.
 */
class CalendarViewPagerFragment : Fragment(), OnDateClickListener {
    private var isChoiceModelSingle = false
    private var viewPager: ViewPager? = null
    private var onPageChangeListener: OnPageChangeListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onPageChangeListener = try {
            context as OnPageChangeListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement OnDateClickListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isChoiceModelSingle = arguments!!.getBoolean(CHOICE_MODE_SINGLE, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_viewpager, container, false)
        initViewPager(view)
        return view
    }

    private fun initViewPager(view: View) {
        viewPager = view.findViewById<View>(R.id.viewpager) as ViewPager
        viewPager!!.offscreenPageLimit = 2
        val myAdapter = CalendarViewPagerAdapter(childFragmentManager, isChoiceModelSingle)
        viewPager!!.adapter = myAdapter
        viewPager!!.currentItem = CalendarViewPagerAdapter.NUM_ITEMS_CURRENT
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                val year = myAdapter.getYearByPosition(position)
                val month = myAdapter.getMonthByPosition(position)
                // tv_date.setText(year+"-"+month+"");
                onPageChangeListener!!.OnPageChange(year, month)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun OnDateClick(calendarDate: CalendarDate) {}
    interface OnPageChangeListener {
        fun OnPageChange(year: Int, month: Int)
    }

    companion object {
        private const val CHOICE_MODE_SINGLE = "choice_mode_single"
        fun newInstance(isChoiceModelSingle: Boolean): CalendarViewPagerFragment {
            val fragment = CalendarViewPagerFragment()
            val args = Bundle()
            args.putBoolean(CHOICE_MODE_SINGLE, isChoiceModelSingle)
            fragment.arguments = args
            return fragment
        }
    }
}