package com.example.btcontroller.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btcontroller.R
import com.example.btcontroller.entity.Medicine

open class MedicineRecyclerViewAdapter(context: Context, medicines: List<Medicine>) : RecyclerView.Adapter<MedicineRecyclerViewAdapter.ViewHolder>() {
    private var mContext: Context? = context
    private var mMedicines: ArrayList<Medicine> = medicines as ArrayList<Medicine>

    fun addMedicine(medicine: Medicine) {
        notifyItemInserted(mMedicines.size - 1)
        mMedicines.add(medicine)
    }

    fun getMedicine(position: Int): Medicine {
        return mMedicines[position]
    }

    fun setMedicine(position: Int, medicine: Medicine) {
        notifyItemChanged(position)
        mMedicines[position] = medicine
    }

    fun setMedicines(medicines: List<Medicine>) {
        mMedicines = medicines as ArrayList<Medicine>
    }

    fun clearMedicines() {
        notifyItemRangeRemoved(0, mMedicines.size)
        mMedicines.clear()
    }

    open fun callOnRemove(medicine: Medicine): Boolean {
        // do nothing
        return true
    }

    open fun callOnClick(position: Int, medicine: Medicine) {
        // do nothing
        return
    }

    fun removeMedicine(position: Int) {
        if (!callOnRemove(mMedicines[position])) return
        mMedicines.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return mMedicines.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.form_medicine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = mMedicines[position].name
        holder.repeat.text = mMedicines[position].repeat
        holder.count.text = mMedicines[position].count
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.med_name)
        val repeat: TextView = itemView.findViewById(R.id.med_repeat)
        val count: TextView = itemView.findViewById(R.id.med_count)
        val delete: ImageView = itemView.findViewById(R.id.btn_delete)

        init {
            (delete.parent as View).setOnClickListener {
                AlertDialog.Builder(mContext)
                        .setTitle("删除药品")
                        .setMessage("您确定要删除该药品吗")
                        .setPositiveButton(R.string.action_ok) { _, _ ->
                            removeMedicine(adapterPosition)
                        }
                        .setNegativeButton("取消", null)
                        .show()
            }
            itemView.setOnClickListener { callOnClick(adapterPosition, mMedicines[adapterPosition]) }
        }
    }
}