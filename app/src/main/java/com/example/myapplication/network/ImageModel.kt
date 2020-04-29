package com.example.myapplication.network

import com.squareup.moshi.Json
import java.io.Serializable

data class ImageModel(
    @Json(name="type")
    var type: String = "",

    @Json(name="image")
    var image: String = ""
):Serializable