package com.example.myapplication.network

import com.squareup.moshi.Json

data class HomeProductModel(
    @Json(name="id")
    var id: String = ""
)