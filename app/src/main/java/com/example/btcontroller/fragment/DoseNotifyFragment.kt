package com.example.btcontroller.fragment

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.btcontroller.activity.EditActivity
import com.example.btcontroller.R
import com.example.btcontroller.adapter.MedicineRecyclerViewAdapter
import com.example.btcontroller.entity.Medicine
import com.example.btcontroller.utils.Preferences
import com.qx.mylibrary.OneNetApi
import com.qx.mylibrary.OneNetApiCallback
import java9.util.concurrent.CompletableFuture
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class DoseNotifyFragment : Fragment() {
    private lateinit var iv_edit: ImageView
    private lateinit var tv_call: TextView
    private var contentRv: RecyclerView? = null
    private lateinit var adapter: MedicineRecyclerViewAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var loadMedicinesTask: LoadMedicinesTask? = null
    private var mMedicines: ArrayList<Medicine> = ArrayList()
    private var isFirstVisible: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_fu_yao_ti_xing, container, false)
        iv_edit = rootView.findViewById(R.id.iv_edit)
        tv_call = rootView.findViewById(R.id.tv_call)
        contentRv = rootView.findViewById(R.id.content_rv)
        swipeRefresh = rootView.findViewById(R.id.swipe_refresh)
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            adapter.clearMedicines()
            loadMedicine()
        }
        (iv_edit.parent as View).setOnClickListener {
            val i = Intent(activity, EditActivity::class.java)
            startActivity(i)
        }
        (tv_call.parent as View).setOnClickListener { callPhone() }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = object : MedicineRecyclerViewAdapter(requireContext(), mMedicines) {
            override fun callOnRemove(medicine: Medicine): Boolean {
                val deviceId = Preferences.getInstance(activity)!!.getString("devices_id", "626364051")
                OneNetApi.deleteDatastream(deviceId, medicine.id, object : OneNetApiCallback {
                    override fun onSuccess(response: String) {
                        Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show()
                    }

                    override fun onFailed(e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "删除失败", Toast.LENGTH_LONG).show()
                    }
                })
                return true
            }
        }
        contentRv!!.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context)
        contentRv!!.layoutManager = linearLayoutManager
        val animator = DefaultItemAnimator()
        with (animator) {
            addDuration = 300
            removeDuration = 300
            moveDuration = 300
        }
        contentRv!!.itemAnimator = animator
    }

    override fun onResume() {
        super.onResume()
        if (isFirstVisible) {
            loadMedicine()
            isFirstVisible = false
        }
    }

    private fun loadMedicine() {
        if (loadMedicinesTask == null) {
            loadMedicinesTask = LoadMedicinesTask()
            loadMedicinesTask!!.execute()
        } else {
            loadMedicinesTask!!.cancel(true)
            loadMedicinesTask = LoadMedicinesTask()
            loadMedicinesTask!!.execute()
        }
    }

    private fun callPhone() {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:" + "13620192601")
        intent.data = data
        startActivity(intent)
    }

    private inner class LoadMedicinesTask : AsyncTask<Any, Any, List<Medicine>>() {
        override fun onPreExecute() {
            swipeRefresh.isRefreshing = true
        }

        override fun doInBackground(vararg params: Any?): List<Medicine> {
            val waiter = CompletableFuture<List<Medicine>>()
            OneNetApi.queryMultiDataStreams(Preferences.getInstance(activity)!!.getString("devices_id", "626364051"), object : OneNetApiCallback {
                override fun onSuccess(response: String) {
                    Log.e(Tag, "success =$response")
                    waiter.complete(parseMedicines(response))
                }

                override fun onFailed(e: Exception) {
                    Log.e(Tag, "onFailed =" + e.message)
                    Toast.makeText(context, "请求失败", Toast.LENGTH_LONG).show()
                    waiter.complete(ArrayList())
                }
            })
            return waiter.get()
        }

        override fun onPostExecute(result: List<Medicine>) {
            val diffResult = DiffUtil.calculateDiff(MedicinesDiffCallback(mMedicines, result), true)
            diffResult.dispatchUpdatesTo(adapter)
            adapter.setMedicines(result)
            mMedicines = result as ArrayList<Medicine>
            loadMedicinesTask = null
            swipeRefresh.isRefreshing = false
        }
    }

    private fun parseMedicines(data: String): ArrayList<Medicine> {
        val medicines : ArrayList<Medicine> = ArrayList()
        try {
            val `object` = JSONObject(data)
            val items = `object`.getJSONArray("data")
            for (i in 0 until items.length()) {
                val med = Medicine(items.getJSONObject(i).optJSONObject("current_value"))
                if (med.name.isEmpty()) continue
                medicines.add(med)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return medicines
    }

    private inner class MedicinesDiffCallback(val oldItems : List<Medicine>, val newItems : List<Medicine>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].name == newItems[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem.name == newItem.name && oldItem.repeat == newItem.repeat && oldItem.count == newItem.count && oldItem.time == newItem.time
        }

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

    }

    companion object {
        private const val PICK_CONTACT_REQUEST = 1
        private var doseNotifyFragment: DoseNotifyFragment? = null
        private const val Tag = "DoseNotifyFragment"
        fun newInstance(): DoseNotifyFragment? {
            if (doseNotifyFragment == null) {
                doseNotifyFragment = DoseNotifyFragment()
            }
            return doseNotifyFragment
        }
    }
}