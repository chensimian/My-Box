package com.example.btcontroller.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.SharedPreferencesCompat

class Preferences private constructor(context: Context) {
    private val mShareferences: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    private val mEditor: SharedPreferences.Editor
    fun putInt(key: String?, value: Int) {
        mEditor.putInt(key, value)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor)
    }

    fun putLong(key: String?, value: Long) {
        mEditor.putLong(key, value)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor)
    }

    fun putString(key: String?, value: String?) {
        mEditor.putString(key, value)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor)
    }

    fun putFloat(key: String?, value: Float) {
        mEditor.putFloat(key, value)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor)
    }

    fun putBoolean(key: String?, value: Boolean) {
        mEditor.putBoolean(key, value)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun putStringSet(key: String?, values: Set<String?>?) {
        mEditor.putStringSet(key, values)
        SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor)
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return mShareferences.getInt(key, defaultValue)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return mShareferences.getLong(key, defaultValue)
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return mShareferences.getString(key, defaultValue)
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        return mShareferences.getFloat(key, defaultValue)
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return mShareferences.getBoolean(key, defaultValue)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String?): Set<String>? {
        return mShareferences.getStringSet(key, null)
    }

    companion object {
        const val API_KEY = "api_key"
        private var sPreferences: Preferences? = null
        @Synchronized
        fun getInstance(context: Context?): Preferences? {
            if (null == sPreferences) {
                sPreferences = Preferences(context!!.applicationContext)
            }
            return sPreferences
        }
    }

    init {
        mEditor = mShareferences.edit()
    }
}