package com.example.btcontroller.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.btcontroller.R
import com.example.btcontroller.utils.IntentActions
import com.example.btcontroller.utils.Preferences
import com.qx.mylibrary.OneNetApi

class Login2Activity : AppCompatActivity() {
    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        //后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/5409118
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        val mPreferences: Preferences = Preferences.getInstance(this)!!
        val apiKey = "NaLTVN1Hp9K2MReFBuyNO=x7yQE=" //"m9m=KnDpUQ7duNsTwh5dQUL3P4A="
        OneNetApi.setAppKey(apiKey)
        mPreferences.putString(Preferences.API_KEY, apiKey)
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(IntentActions.ACTION_UPDATE_APIKEY))
        handler = Handler()
        handler!!.postDelayed({
            startActivity(Intent(this@Login2Activity, LoginActivity::class.java))
            // startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out)
            finish()
        }, 500)
    }
}