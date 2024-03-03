package com.example.btcontroller.utils

import com.example.btcontroller.model.ActivateCode
import com.example.btcontroller.model.DeviceItem
import com.example.btcontroller.model.Location
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class DeviceItemDeserializer : JsonDeserializer<DeviceItem> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DeviceItem {
        val jsonObject = json.asJsonObject
        val deviceItem = DeviceItem()
        deviceItem.id = jsonObject["id"].asString
        deviceItem.title = jsonObject["title"].asString
        val desc = jsonObject["desc"]
        deviceItem.desc = if (desc != null) desc.asString else ""
        val isPrivate = jsonObject["private"]
        deviceItem.isPrivate = isPrivate?.asBoolean ?: true
        val protocol = jsonObject["protocol"]
        deviceItem.protocol = if (protocol != null) protocol.asString else "HTTP"
        deviceItem.isOnline = jsonObject["online"].asBoolean
        val locationElement = jsonObject["location"]
        if (locationElement != null) {
            val locationObject = locationElement.asJsonObject
            if (locationObject != null) {
                val location = Location()
                location.lat = locationObject["lat"].asString
                location.lon = locationObject["lon"].asString
                deviceItem.location = location
            }
        }
        deviceItem.createTime = jsonObject["create_time"].asString
        val authInfo = jsonObject["auth_info"]
        if (authInfo != null) {
            if (authInfo.isJsonObject) {
                deviceItem.authInfo = authInfo.asJsonObject.toString()
            } else {
                deviceItem.authInfo = authInfo.asString
            }
        }
        val activateCodeElement = jsonObject["activate_code"]
        if (activateCodeElement != null) {
            val activateCodeObject = activateCodeElement.asJsonObject
            if (activateCodeObject != null) {
                val activateCode = ActivateCode()
                activateCode.mt = activateCodeObject["mt"].asString
                activateCode.mid = activateCodeObject["mid"].asString
                deviceItem.activateCode = activateCode
            }
        }
        return deviceItem
    }
}