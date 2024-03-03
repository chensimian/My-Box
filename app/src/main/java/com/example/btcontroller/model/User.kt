package com.example.btcontroller.model

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

class User : LitePalSupport(), Serializable {
    var username: String? = null
    var password: String? = null
    @Column(unique = true)
    var phone: String? = null
    var gender: Int = 0
}