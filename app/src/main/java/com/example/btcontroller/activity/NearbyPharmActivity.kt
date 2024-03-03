package com.example.btcontroller.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.btcontroller.R

class NearbyPharmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_pharm)
        findViewById<TextView>(R.id.top_title).text = "周边药店"
    }

}