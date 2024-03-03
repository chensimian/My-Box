package com.example.btcontroller.activity

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.btcontroller.R
import com.example.btcontroller.utils.Preferences
import com.qx.mylibrary.OneNetApi
import com.qx.mylibrary.OneNetApiCallback
import java9.util.concurrent.CompletableFuture
import kotlin.concurrent.thread

class AddDeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)
        (findViewById<View>(R.id.btn_back).parent as View).setOnClickListener { finish() }
        findViewById<TextView>(R.id.bar_title).text = "开门"

        //添加设备按钮
        findViewById<View>(R.id.add_constraintLayout).setOnClickListener {
            thread {
                sendData()  //发数据
                finish()
            }

        }

    }
    

    private fun sendData() {
        val topic = Preferences.getInstance(this@AddDeviceActivity)!!.getString("topic", "kai")
        val deviceWaiter = CompletableFuture<Boolean>()
        OneNetApi.sendCmdByTopic(topic,"1", OneNetApiBooleanFuture(deviceWaiter)) //MQTT的下发命令
    }

    private inner class OneNetApiBooleanFuture(val future: CompletableFuture<Boolean>) : OneNetApiCallback {
        override fun onSuccess(response: String) {
            Log.i(Tag, "success =$response")
            future.complete(true)
        }

        override fun onFailed(e: Exception) {
            Log.e(Tag, "onFailed =${e.message}")
            future.complete(false)
        }
    }

    companion object {
        private const val Tag = "AddDeviceActivity"
    }

}



