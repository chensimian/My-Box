package com.example.btcontroller.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.btcontroller.R

class fenxiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fenxi)
        findViewById<TextView>(R.id.top_title).text = "药理分析"
    }
}