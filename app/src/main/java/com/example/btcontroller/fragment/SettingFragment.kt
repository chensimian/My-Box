package com.example.btcontroller.fragment

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.btcontroller.R
import com.example.btcontroller.activity.*


class SettingFragment : PreferenceFragmentCompat() {
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preferences, rootKey)  //把preference放进来
        sharedPreferences = requireActivity().getSharedPreferences("data", Context.MODE_PRIVATE) //这是顶部的个人信息

        val debug = findPreference<Preference>("wifi_debug")
        debug!!.setOnPreferenceClickListener {
            val i = Intent(activity, DevicesActivity::class.java)
            startActivity(i)
            true
        }

        //启动热点界面
        val redian = findPreference<Preference>("redian_debug")
        redian!!.setOnPreferenceClickListener {
            val intent = Intent()
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.action = "android.intent.action.MAIN"
            val cn = ComponentName("com.android.settings", "com.android.settings.Settings\$TetherSettingsActivity")
            intent.component = cn
            startActivity(intent)
            true
        }

        val account = findPreference<Preference>("account")
        account!!.setOnPreferenceClickListener {
            val j = Intent(activity, AccountActivity::class.java)
            startActivity(j)
            true
        }

        val neabypharm = findPreference<Preference>("nearbypharm")
        neabypharm!!.setOnPreferenceClickListener {
            val k = Intent(activity, NearbyPharmActivity::class.java)
            startActivity(k)
            true
        }

        val purchasenotice = findPreference<Preference>("notify")
        purchasenotice!!.setOnPreferenceClickListener {
            val l = Intent(activity, Purchase_noticeActivity::class.java)
            startActivity(l)
            true
        }


        val fenxi = findPreference<Preference>("analyze")
        fenxi!!.setOnPreferenceClickListener {
            val m = Intent(activity, fenxiActivity::class.java)
            startActivity(m)
            true
        }

        val yuyin = findPreference<Preference>("voice")
        yuyin!!.setOnPreferenceClickListener {
            val n = Intent(activity, yuyinActivity::class.java)
            startActivity(n)
            true
        }

        val addbox = findPreference<Preference>("add_box")
        addbox!!.setOnPreferenceClickListener {
            val o = Intent(activity, AddDeviceActivity::class.java)
            startActivity(o)
            true
        }

        val logout = findPreference<Preference>("logout")
        logout!!.setOnPreferenceClickListener {
            AlertDialog.Builder(context)
                    .setTitle("退出当前账号")
                    .setMessage("您确定要退出当前账户吗？")
                    .setPositiveButton("确定") { _, _ ->
                        val intent = Intent(activity, LoginActivity::class.java)
                        val editor = sharedPreferences!!.edit()
                        //步骤3：将获取过来的值放入文件
                        editor.putBoolean("isLogin", false)
                        //步骤4：提交
                        editor.apply()
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    .setNegativeButton("取消") { _, _ -> }
                    .show()
            true
        }
    }
}