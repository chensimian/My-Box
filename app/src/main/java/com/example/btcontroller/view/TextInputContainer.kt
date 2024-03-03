package com.example.btcontroller.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputLayout

class TextInputContainer @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextInputLayout(context!!, attrs, defStyleAttr) {
    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (editText != null) {
            editText!!.addTextChangedListener(mTextWatcher)
        }
    }

    private val mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            if (s.isNotEmpty()) {
                error = null
                isErrorEnabled = false
            }
        }
    }
}