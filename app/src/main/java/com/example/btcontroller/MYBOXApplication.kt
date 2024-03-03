package com.example.btcontroller

import android.content.Context
import com.example.btcontroller.model.User
import com.example.btcontroller.utils.Preferences
import com.qx.mylibrary.OneNetApi
import com.qx.mylibrary.http.Config
import com.uuzuche.lib_zxing.activity.ZXingLibrary
import org.litepal.LitePalApplication
import java.util.concurrent.TimeUnit

class MYBOXApplication : LitePalApplication() {
    override fun onCreate() {
        super.onCreate()
        // 初始化SDK（必须）
        mContext = this
        ZXingLibrary.initDisplayOpinion(this)
        val config = Config.newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .retryCount(2)
                .build()
        OneNetApi.init(this, true, config)
        val savedApiKey: String? = Preferences.getInstance(this)!!.getString(Preferences.API_KEY, null)
        if (savedApiKey != null && savedApiKey.isNotEmpty()) {
            OneNetApi.setAppKey(savedApiKey)
        }
    }

    companion object {
        var mContext: Context? = null
        var loggedInUser: User? = null
    }
}