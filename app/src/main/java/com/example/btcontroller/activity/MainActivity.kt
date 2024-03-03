package com.example.btcontroller.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.btcontroller.entity.CalendarDate
import com.example.btcontroller.R
import com.example.btcontroller.fragment.CalendarViewFragment.OnDateCancelListener
import com.example.btcontroller.fragment.CalendarViewFragment.OnDateClickListener
import com.example.btcontroller.fragment.CalendarViewPagerFragment
import com.example.btcontroller.fragment.DoseRecordFragment
import com.example.btcontroller.fragment.DoseNotifyFragment
import com.example.btcontroller.fragment.MoreFragment
import com.example.btcontroller.model.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.uuzuche.lib_zxing.activity.CodeUtils
import java.util.*

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, OnDateClickListener, CalendarViewPagerFragment.OnPageChangeListener, OnDateCancelListener {
    var viewPager: ViewPager? = null
    private val mListDate: MutableList<CalendarDate> = ArrayList()
    var tablayout: TabLayout? = null
    var Tag = 0
    private val titleList: MutableList<String> = ArrayList()
    private val fragmentlist: MutableList<Fragment?> = ArrayList()
    private val adapter: MyFragmentAdapter? = null
    private var pageTag = 0
    private val IconImg = intArrayOf(
            R.mipmap.record,
            R.mipmap.notify,
            R.mipmap.more)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (lacksPermission(this, permissionsREAD)) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), 200)
        }
        val user = intent.getSerializableExtra("user") as User?
        if (user != null) {
            // username.setText(user.getUsername());
            Companion.user = user
        }
        viewPager = findViewById(R.id.view_pager)
        tablayout = findViewById(R.id.tablayout)
        initFragmentList()
        // initWidgets();
    }

    private fun initFragmentList() {
        titleList.clear()
        fragmentlist.clear()
        titleList.add("服药记录")
        titleList.add("服药提醒")
        titleList.add("更多")
        fragmentlist.add(DoseRecordFragment.newInstance())
        fragmentlist.add(DoseNotifyFragment.newInstance())
        fragmentlist.add(MoreFragment.newInstance())
        viewPager!!.adapter = MyFragmentAdapter(supportFragmentManager, fragmentlist as List<Fragment>)
        viewPager!!.offscreenPageLimit = 3
        viewPager!!.currentItem = 0
        forIconAndText()
    }

    /**
     * 文字图片都有的情况
     */
    private fun forIconAndText() {
        tablayout!!.setupWithViewPager(viewPager) //将TabLayout和ViewPager关联起来。

        //setupWithViewPager里面有removeAllTabs，所以关联以后在添加
        tablayout!!.removeAllTabs()
        for (i in titleList.indices) {
            tablayout!!.addTab(tablayout!!.newTab().setIcon(IconImg[i]).setText(titleList[i]))
        }
        tablayout!!.getTabAt(0)!!.icon?.setTint(Color.RED)
        tablayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val i = tablayout!!.selectedTabPosition
                Tag = i
                Log.e("onTabSelected", i.toString() + "")
                //tab.setIcon(IconImg_p[i])
                tab.icon?.setTint(Color.RED)
                viewPager!!.currentItem = i
                //tablayout!!.setTabTextColors(Color.BLACK, Color.RED)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val i = tablayout!!.selectedTabPosition
                //tab.setIcon(IconImg[Tag])
                tab.icon?.setTint(Color.BLACK)
                //tablayout!!.setTabTextColors(Color.BLACK, Color.BLACK)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun initWidgets() {
//        adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentlist);
//
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(this);
//        viewPager.setOffscreenPageLimit(2);
        tablayout!!.tabMode = TabLayout.MODE_FIXED
        tablayout!!.setupWithViewPager(viewPager)

        //viewPager.setCurrentItem(0);
        tablayout!!.addTab(tablayout!!.newTab().setIcon(IconImg[0]).setText(titleList[0]), true)
        tablayout!!.addTab(tablayout!!.newTab().setIcon(IconImg[1]).setText(titleList[1]), false)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> {
                viewPager!!.currentItem = 0
                pageTag = 0
            }
            1 -> {
                viewPager!!.currentItem = 1
                pageTag = 1
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}
    override fun OnDateClick(calendarDate: CalendarDate) {
        val year = calendarDate.solar.solarYear
        val month = calendarDate.solar.solarMonth
        val day = calendarDate.solar.solarDay
        if (DoseRecordFragment.isChoiceModelSingle) {
            DoseRecordFragment.tv_date!!.setText("$year-$month-$day")
        } else {
            //System.out.println(calendarDate.getSolar().solarDay);
            mListDate.add(calendarDate)
            DoseRecordFragment.tv_date!!.setText(DoseRecordFragment.listToString(mListDate))
        }
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
        DoseRecordFragment.tv_date!!.text = DoseRecordFragment.listToString(mListDate)
    }

    override fun OnPageChange(year: Int, month: Int) {
        DoseRecordFragment.tv_date!!.text = "$year-$month"
        mListDate.clear()
    }

    inner class MyFragmentAdapter(fm: FragmentManager?, var list: List<Fragment>) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return list.size
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                val bundle = data.extras ?: return
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    Toast.makeText(this, "解析结果:$result", Toast.LENGTH_LONG).show()
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        /**
         * 读写权限 自己可以添加需要判断的权限
         */
        var permissionsREAD = Manifest.permission.CAMERA

        /**
         * 判断是否缺少权限
         */
        private fun lacksPermission(mContexts: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(mContexts, permission) ==
                    PackageManager.PERMISSION_DENIED
        }

        var user: User? = null
    }
}