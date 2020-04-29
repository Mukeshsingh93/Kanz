package com.example.myapplication.network

import com.squareup.moshi.Json

data class Coupons(

    @Json(name="image")
    var image: String? = null
)