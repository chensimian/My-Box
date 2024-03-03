package com.example.btcontroller.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.btcontroller.R

class LoadingActivity : Activity() {
    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isTaskRoot) {
            finish()
            return
        }
        setContentView(R.layout.activity_loading)
        //后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        handler = Handler()
        handler!!.postDelayed({
            startActivity(Intent(this@LoadingActivity, LoginActivity::class.java))
            // startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out)
            finish()
        }, 500)
    }
}