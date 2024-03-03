package com.example.btcontroller.activity


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.btcontroller.R

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        (findViewById<View>(R.id.btn_back).parent as View).setOnClickListener { finish() }
        findViewById<TextView>(R.id.bar_title).text = "个人信息"
        findViewById<TextView>(R.id.textview_username).text = MainActivity.user?.username  //把注册的username放进来
        findViewById<TextView>(R.id.textview_phone01).text = MainActivity.user?.phone  //把注册的手机号放进来
    }
}
