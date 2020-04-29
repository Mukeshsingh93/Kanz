package com.example.myapplication.network

import com.squareup.moshi.Json

data class GetCart(
    @Json(name="status")
    var status: Int? = null,

    @Json(name="msg")
    var message: String? = null


)