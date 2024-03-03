package com.example.btcontroller.entity

import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.util.*

class Medicine : Serializable {
    var name = ""
    var repeat = ""
    var count = ""
    var time = ""
    var id : String = ""

    constructor(name: String, repeat: String, count: String, time: String, id: String? = null) {
        this.name = name
        this.repeat = repeat
        this.count = count
        this.time = time
        this.id = id ?: UUID.randomUUID().toString()
    }

    constructor(data: String) {
        parse(data)
    }

    constructor(json: JSONObject?) {
        if (json == null) return
        parse(json)
    }

    fun dump(): String {
        return dumpObject().toString()
    }

    fun dumpObject(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("name", name)
        json.put("repeat", repeat)
        json.put("count", count)
        json.put("time", time)
        return json
    }

    fun parse(json: JSONObject) {
        try {
            id = json.getString("id")
            name = json.getString("name")
            repeat = json.getString("repeat")
            count = json.getString("count")
            time = json.getString("time")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun parse(data: String) {
        try {
            val json = JSONObject(data)
            parse(json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}