package com.example.btcontroller.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btcontroller.adapter.MedicineRecyclerViewAdapter
import com.example.btcontroller.R
import com.example.btcontroller.api.BarcodeApi
import com.example.btcontroller.entity.Medicine
import com.example.btcontroller.utils.Preferences
import com.example.btcontroller.view.EmptySupportRecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.qx.mylibrary.OneNetApi
import com.qx.mylibrary.OneNetApiCallback
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import java9.util.concurrent.CompletableFuture
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class EditActivity : AppCompatActivity() {
    private lateinit var add_constraintLayout: ConstraintLayout
    var mProgressDialog: ProgressDialog? = null
    lateinit var mTimepicker: TimePicker
    private var time: String = "8:10"
    private lateinit var imageView5: ImageView
    private lateinit var medsRv: EmptySupportRecyclerView
    private var mMedicines : ArrayList<Medicine> = ArrayList()
    private var addDialog : AlertDialog? = null
    private lateinit var btnBack : ImageView
    private lateinit var btnConfirm : ImageView

    private lateinit var adapter : MedicineRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        findViewById<View>(R.id.add_constraintLayout).setOnClickListener {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setMessage("Sending request...")
            showInputDataStreamIdDialog()
        }
        findViewById<TextView>(R.id.bar_title).text = "添加药品"
        btnBack = findViewById(R.id.btn_back)  //放回按钮
        btnConfirm = findViewById(R.id.btn_ok) //点OK发送
        (btnBack.parent as View).setOnClickListener { finish() }
        (btnConfirm.parent as View).setOnClickListener {
            thread {
                sendData()
                finish()
            }
        }

        mTimepicker = findViewById(R.id.timepicker)
        mTimepicker.descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS //设置点击事件不弹键盘
        mTimepicker.setIs24HourView(true) //设置时间显示为24小时
        mTimepicker.currentHour = 8 //设置当前小时
        mTimepicker.currentMinute = 10 //设置当前分（0-59）
        mTimepicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            //获取当前选择的时间
            time = "$hourOfDay:$minute"
        }

        medsRv = findViewById(R.id.meds_rv)
        medsRv.emptyView = findViewById<TextView>(R.id.emptyText)
        medsRv.setIsEmpty(true)
        adapter = object : MedicineRecyclerViewAdapter(this, mMedicines) {
            override fun callOnClick(position: Int, medicine: Medicine) {
                showInputDataStreamIdDialog(position)
            }
        }
        medsRv.adapter = adapter
        medsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val animator = DefaultItemAnimator()
        with (animator) {
            addDuration = 300
            removeDuration = 300
        }
        medsRv.itemAnimator = animator
    }


    //下发信息
    private fun sendData() {
        for (med in mMedicines) {
            val deviceId = Preferences.getInstance(this@EditActivity)!!.getString("devices_id", "688993156\t\n") //我的的设备ID
            val topic = Preferences.getInstance(this@EditActivity)!!.getString("topic", "yaohe")//MQTT 订阅  编辑服药信息信息下发订阅名yaohe
            val dataStreamWaiter = CompletableFuture<Boolean>()
            val dataPointWaiter = CompletableFuture<Boolean>()
            val deviceWaiter = CompletableFuture<Boolean>()
            val dataStream = JSONObject()
            dataStream.put("id", med.id)
            dataStream.put("unit", "na")
            dataStream.put("unit_symbol", "na")
            OneNetApi.addDataStream(deviceId, dataStream.toString(), OneNetApiBooleanFuture(dataStreamWaiter))
            val dataPoint = JSONObject()
            dataPoint.put("datastreams", JSONArray().put(
                    JSONObject().put("id", med.id)
                            .put("datapoints", JSONArray().put(
                                    JSONObject().put("value", med.dumpObject())
                            ))
            ))
            OneNetApi.addDataPoints(deviceId, dataPoint.toString(), OneNetApiBooleanFuture(dataPointWaiter))
            val deviceData = JSONObject()
            deviceData.put("cmd", "addMedicine")
            deviceData.put("data", med.dumpObject())
            OneNetApi.sendCmdByTopic(topic,deviceData.toString(), OneNetApiBooleanFuture(deviceWaiter)) //MQTT的下发命令

/*
            public static void sendCmdToDevice(String deviceId, String requestBodyString, OneNetApiCallback callback) {
                post(Command.urlForSending(deviceId), requestBodyString, callback);
            }

               public static void sendCmdByTopic(String topic, String requestBodyString, OneNetApiCallback callback) {
                post(Mqtt.urlForSendingCmdByTopic(topic), requestBodyString, callback);
    }

  */


           // OneNetApi.sendCmdToDevice(deviceId, deviceData.toString(), OneNetApiBooleanFuture(deviceWaiter))
            if (!dataStreamWaiter.get() || !dataPointWaiter.get() || !deviceWaiter.get())
                runOnUiThread { Toast.makeText(this@EditActivity, "${med.name} 添加失败", Toast.LENGTH_LONG).show() }
            else
                runOnUiThread { Toast.makeText(this@EditActivity, "${med.name} 添加成功", Toast.LENGTH_LONG).show() }
        }
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

    private fun getInfo(code: String?): String? {
        val info = BarcodeApi(this).getBarcodeInfo(code)
        if (info == null) Toast.makeText(this@EditActivity, "条形码未识别", Toast.LENGTH_LONG).show()
        return info
    }

    private fun showInputDataStreamIdDialog(originalId: Int = -1) {
        val contentView = View.inflate(this, R.layout.dialog_add_medicine, null)
        val medicineName : TextInputEditText = contentView.findViewById(R.id.med_name)
        val medicineRepeat : TextInputEditText = contentView.findViewById(R.id.med_repeat)
        val medicineCount : TextInputEditText = contentView.findViewById(R.id.med_count)
        var id : String? = null
        if (originalId != -1) {
            val med = adapter.getMedicine(originalId)
            medicineName.setText(med.name)
            medicineRepeat.setText(med.repeat)
            medicineCount.setText(med.count)
            id = med.id
        }
        (contentView.findViewById<ImageView>(R.id.btn_scan).parent as View).setOnClickListener {
            val intent = Intent(this, CaptureActivity::class.java)
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (null != it.data) {
                    val bundle = it.data!!.extras
                    if (bundle?.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        val result = bundle.getString(CodeUtils.RESULT_STRING)
                        thread {
                            val info = getInfo(result)
                            if (addDialog != null && info != null) {
                                addDialog!!.findViewById<TextInputEditText>(R.id.med_name)!!.setText(info)
                            }
                        }
                    } else if (bundle?.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(this, "解析条形码失败", Toast.LENGTH_LONG).show()
                    }
                }
            }.launch(intent)
        }
        addDialog = AlertDialog.Builder(this)
                .setView(contentView)
                .setPositiveButton(R.string.action_ok) { _, _ ->
                    val name = medicineName.text.toString().trim { it <= ' ' }
                    val repeat = medicineRepeat.text.toString().trim { it <= ' ' }
                    val count = medicineCount.text.toString().trim { it <= ' ' }
                    if (name.isNotEmpty() && repeat.isNotEmpty() && count.isNotEmpty()) {
                        val med = Medicine(name, repeat, count, time, id)
                        if (originalId != -1) adapter.setMedicine(originalId, med)
                        else adapter.addMedicine(med)
                        medsRv.checkEmptyView()
                    }
                }
                .setNegativeButton("取消") { _, _ -> addDialog = null }
                .setOnDismissListener {
                    addDialog = null
                }
                .show()
    }

    companion object {
        private const val Tag = "EditActivity"
    }
}