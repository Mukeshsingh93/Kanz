package com.example.myapplication.util

import com.example.myapplication.network.Data
import com.squareup.moshi.Json

data class Response(
    @Json(name="status")
    var status: Int? = null,

    @Json(name="msg")
    var message: String? = null,

    @Json(name="cart_count")
    var cart_count: String? = null,

    @Json(name="data")
    var data: Data? = null
)