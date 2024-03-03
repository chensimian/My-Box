package com.example.btcontroller.model

import com.google.gson.annotations.SerializedName

class DeviceItem {
    var id: String? = null
    var title: String? = null
    var desc: String? = null

    @SerializedName("private")
    var isPrivate = false
    var protocol = "HTTP"
    var isOnline = false
    var location: Location? = null

    @SerializedName("create_time")
    var createTime: String? = null

    @SerializedName("auth_info")
    var authInfo: String? = null

    @SerializedName("activite_code")
    var activateCode: ActivateCode? = null
}