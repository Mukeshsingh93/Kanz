package com.example.myapplication.network

import com.squareup.moshi.Json
import java.io.Serializable

data class Offers(

    @Json(name="id")
    var id: Int? = null,

    @Json(name="name")
    var name: String? = null,

    @Json(name="description")
    var description: String? = null,

    @Json(name="image")
    var image: String? = null
):Serializable