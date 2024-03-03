package com.example.btcontroller.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptySupportRecyclerView : RecyclerView {
    /**
     * 当数据为空时展示的View
     */
    var emptyView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    /**
     * 创建一个观察者
     * 为什么要在onChanged里面写？
     * 因为每次notifyDataChanged的时候，系统都会调用这个观察者的onChange函数
     * 我们大可以在这个观察者这里判断我们的逻辑，就是显示隐藏
     */
    private val emptyObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            onChanged()
        }

        override fun onChanged() {
            checkEmptyView()
        }
    }

    fun checkEmptyView() {
        if (adapter != null && emptyView != null) {
            setIsEmpty(adapter!!.itemCount == 0)
        }
    }

    fun setIsEmpty(value: Boolean) {
        if (value) {
            emptyView!!.visibility = View.VISIBLE
            visibility = View.GONE
        } else {
            emptyView!!.visibility = View.GONE
            visibility = View.VISIBLE
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(emptyObserver)
        //当setAdapter的时候也调一次
        emptyObserver.onChanged()
    }
}