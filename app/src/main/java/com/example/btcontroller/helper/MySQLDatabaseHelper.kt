package com.example.btcontroller.helper

import android.content.ContentValues
import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import javax.sql.DataSource

abstract class MySQLDatabaseHelper<T>(private val url: String, private val username: String, private val password: String, private val mainTable : String) {
    private var mConnection: Connection? = null

    abstract fun itemToContentValues(item: T): ContentValues

    abstract fun contentValuesToItem(values: ContentValues): T

    private fun getColumns(values: ContentValues): String {
        var columns = ""
        for (key in values.keySet()) {
            columns += "$key, "
        }
        columns = columns.removeSuffix(", ")
        return columns
    }

    private fun getValues(values: ContentValues): String {
        var valuesStr = ""
        for (key in values.keySet()) {
            valuesStr += "'${values.getAsString(key)}', "
        }
        valuesStr = valuesStr.removeSuffix(", ")
        return valuesStr
    }

    private fun generateSelectStatement(values: ContentValues, table: String): String {
        return "SELECT ${getColumns(values)} FROM $table"
    }

    private fun generateInsertStatement(values: ContentValues, table: String): String {
        return "INSERT INTO $table (${getColumns(values)}) VALUES (${getValues(values)})"
    }

    fun add(value: T): Boolean {
        if (mConnection == null) return false
        val statement = mConnection!!.createStatement()
        val values = itemToContentValues(value)
        val result = statement.execute(generateInsertStatement(values, mainTable))
        statement.close()
        return result
    }

    fun connect(): Int {
        try {
            mConnection = DriverManager.getConnection(url, username, password)
            Log.i(TAG, "Database connect successful")
            return 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    fun disconnect() {
        mConnection?.close()
    }

    companion object {
        private const val TAG = "MySQLDatabaseHelper"
    }
}