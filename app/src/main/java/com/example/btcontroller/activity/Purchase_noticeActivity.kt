package com.example.btcontroller.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.btcontroller.R

class Purchase_noticeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_notice)
        findViewById<TextView>(R.id.top_title).text = "提醒买药"
    }
}