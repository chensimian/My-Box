package com.example.btcontroller.api

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.btcontroller.utils.OkHttpUtil
import org.json.JSONObject

class BarcodeApi(val context: Context) {
    private val apiKey = "aca620f5109e9b79"
    private val apiAddress = "https://api.jisuapi.com/barcode2/query?appkey=$apiKey&barcode="

    fun getBarcodeInfo(code: String?): String? {
        try {
            val response = OkHttpUtil.instance.GET("$apiAddress$code", "") ?: return null
            val `object` = JSONObject(response.body!!.string())
            return if (`object`.getInt("status") == 0) {
                `object`.getJSONObject("result").getString("name")
            } else null
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }
}