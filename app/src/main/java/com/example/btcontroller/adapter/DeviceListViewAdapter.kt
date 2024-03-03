package com.example.btcontroller.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.btcontroller.R
import com.example.btcontroller.model.DeviceItem
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter

class DeviceListViewAdapter(context: Context?) : RecyclerArrayAdapter<DeviceItem?>(context) {
    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return StairViewHolder(parent)
    }

    private inner class StairViewHolder(item: ViewGroup?) : BaseViewHolder<DeviceItem>(item, R.layout.device_list_item) {
        private val title: TextView
        private val id: TextView
        private val protocol: TextView
        private val createTime: TextView
        private val online: TextView
        override fun setData(data: DeviceItem) {
            super.setData(data)
            //helper.addOnClickListener()
            title.text = data.title
            id.text = "设备Id：" + data.id
            protocol.text = "设备接入协议：" + data.protocol
            createTime.text = "创建时间：" + data.createTime
            if ("HTTP".equals(data.protocol, ignoreCase = true)) {
                online.visibility = View.INVISIBLE
            } else {
                online.visibility = View.VISIBLE
                online.text = if (data.isOnline) "在线" else "离线"
            }
        }

        init {
            title = itemView.findViewById(R.id.title)
            id = itemView.findViewById(R.id.id)
            protocol = itemView.findViewById(R.id.protocol)
            createTime = itemView.findViewById(R.id.create_time)
            online = itemView.findViewById(R.id.online)
        }
    }
}