package com.example.myapplication.network

import com.squareup.moshi.Json
import java.io.Serializable

data class SliderImages(

    @Json(name="id")
    var id: Int? = null,

    @Json(name="image")
    var image: String? = null
):Serializable