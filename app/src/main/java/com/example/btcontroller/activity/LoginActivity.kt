package com.example.btcontroller.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.btcontroller.R
import com.example.btcontroller.fragment.LoginFragment
import com.example.btcontroller.fragment.RegisterFragment
import com.example.btcontroller.model.User
import com.google.android.material.tabs.TabLayout
import java.util.*
import org.litepal.LitePal.where
import java.lang.Exception

class LoginActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    var viewPager: ViewPager? = null
    var sharedPreferences: SharedPreferences? = null
    private var isLogin = false
    var tablayout: TabLayout? = null
    var Tag = 0
    private val titleList: MutableList<String> = ArrayList()
    private val fragmentlist: MutableList<Fragment?> = ArrayList()
    private val adapter: myFragmentAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        isLogin = sharedPreferences!!.getBoolean("isLogin", false)
        if (isLogin) {
            var loggedInUser : User? = null
            val username = sharedPreferences!!.getString("username", null)
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            if (username != null) {
                try {
                    val list = where("username like ?", username).find(User::class.java)
                    if (list.size > 0) {
                        val user = list[0]
                        if (user.username?.compareTo(username) == 0) {
                            loggedInUser = user
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            i.putExtra("user", loggedInUser)
            startActivity(i)
            finish()
        } else {
            setContentView(R.layout.activity_login1)
            viewPager = findViewById(R.id.view_pager)
            tablayout = findViewById(R.id.tablayout)
            initFragmentList()
        }
    }

    private fun initFragmentList() {
        titleList.clear()
        fragmentlist.clear()
        titleList.add("注册")
        titleList.add("登录")
        fragmentlist.add(RegisterFragment.newInstance())
        fragmentlist.add(LoginFragment.newInstance())
        viewPager!!.addOnPageChangeListener(this)
        viewPager!!.adapter = myFragmentAdapter(supportFragmentManager, fragmentlist as List<Fragment>)
        viewPager!!.offscreenPageLimit = 2
        viewPager!!.currentItem = 0
        tablayout!!.tabMode = TabLayout.MODE_FIXED
        tablayout!!.setupWithViewPager(viewPager) //将TabLayout和ViewPager关联起来。
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        when (position) {
            0 -> viewPager!!.currentItem = 0
            1 -> viewPager!!.currentItem = 1
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}
    inner class myFragmentAdapter(fm: FragmentManager?, var list: List<Fragment>) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return list.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }
    }
}