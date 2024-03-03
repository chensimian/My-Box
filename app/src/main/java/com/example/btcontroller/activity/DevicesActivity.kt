package com.example.btcontroller.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btcontroller.R
import com.example.btcontroller.adapter.DeviceListViewAdapter
import com.example.btcontroller.entity.ClientMQTT
import com.example.btcontroller.model.DeviceItem
import com.example.btcontroller.utils.DeviceItemDeserializer
import com.example.btcontroller.utils.Preferences
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.jude.easyrecyclerview.EasyRecyclerView
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.qx.mylibrary.OneNetApi
import com.qx.mylibrary.OneNetApiCallback
import java9.util.concurrent.CompletableFuture
import java.util.*
import kotlin.concurrent.thread


class DevicesActivity : AppCompatActivity(), RecyclerArrayAdapter.OnItemClickListener {
    var mRecyclerView: EasyRecyclerView? = null


    private var mTotalCount = 0
    private val mDeviceItems: MutableList<DeviceItem> = ArrayList()
    private var adapter: DeviceListViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imei)
        mRecyclerView = findViewById<View>(R.id.recyler_view) as EasyRecyclerView  //弹出框
        (findViewById<View>(R.id.btn_back).parent as View).setOnClickListener { finish() }
        findViewById<TextView>(R.id.bar_title).text = "连接设备"
        //添加设备按钮
        findViewById<View>(R.id.add_constraintLayout).setOnClickListener {
            thread {
                sendData()  //发数据
                //finish()
            }

        }
        initView()
    }


    private fun sendData() {
        val topic = Preferences.getInstance(this@DevicesActivity)!!.getString("topic", "kai")
        val deviceWaiter = CompletableFuture<Boolean>()
        OneNetApi.sendCmdByTopic(topic,"1", OneNetApiBooleanFuture(deviceWaiter)) //MQTT的下发命令
    }

    private inner class OneNetApiBooleanFuture(val future: CompletableFuture<Boolean>) : OneNetApiCallback {
        override fun onSuccess(response: String) {
            Log.i(DevicesActivity.Tag, "success =$response")
            future.complete(true)
        }

        override fun onFailed(e: Exception) {
            Log.e(DevicesActivity.Tag, "onFailed =${e.message}")
            future.complete(false)
        }
    }
    companion object {
        private const val Tag = "DevicesActivity"
    }

    private fun initView() {
        adapter = DeviceListViewAdapter(this)
        mRecyclerView!!.adapter = adapter
        adapter!!.setOnItemClickListener(this)
        val linearLayoutManager = LinearLayoutManager(this)
        mRecyclerView!!.setLayoutManager(linearLayoutManager)
        devices
    }

    private val devices: Unit
        get() {
            val urlParams: MutableMap<String, String> = HashMap()
            urlParams["page"] = "1"
            urlParams["per_page"] = "100"
            OneNetApi.fuzzyQueryDevices(urlParams, object : OneNetApiCallback {
                override fun onSuccess(response: String) {
                    val resp = JsonParser().parse(response).asJsonObject
                    val errno = resp["errno"].asInt
                    if (0 == errno) {
                        parseData(resp["data"].asJsonObject)
                    } else {
                        val error = resp["error"].asString
                        Toast.makeText(this@DevicesActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailed(e: Exception) {
                    e.printStackTrace()
                }
            })
        }

    private fun parseData(data: JsonObject?) {
        if (null == data) {
            return
        }
        mTotalCount = data["total_count"].asInt
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(DeviceItem::class.java, DeviceItemDeserializer())
        val gson = gsonBuilder.create()
        val jsonArray = data["devices"].asJsonArray
        val devices: MutableList<DeviceItem> = ArrayList()
        for (element in jsonArray) {
            devices.add(gson.fromJson(element, DeviceItem::class.java))
        }
        mDeviceItems.addAll(devices)
        adapter!!.addAll(mDeviceItems)
    }

    override fun onItemClick(position: Int) {

        val contentView = View.inflate(this, R.layout.dialog_text, null)
        val tv = contentView.findViewById<TextView>(R.id.med_name)
        tv.text = "确认连接该设备？"
        AlertDialog.Builder(this)
                .setView(contentView)
                .setPositiveButton(R.string.action_ok) { _, _ ->
                    Preferences.Companion.getInstance(this@DevicesActivity)!!.putString("devices_id", adapter!!.allData[position]!!.id)
                    Toast.makeText(this@DevicesActivity, "连接成功", Toast.LENGTH_LONG).show()
                    val client = ClientMQTT()
                    client.start()
                }
                .setNegativeButton("取消", null)
                .show()
    }


}


