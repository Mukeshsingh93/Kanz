package com.example.myapplication.homeview

import com.squareup.moshi.Json
import java.io.Serializable

data class Advertisement(

    @Json(name="banner")
    var banner: String = "",

    @Json(name="position")
    var position: String? = null,

    @Json(name="start_date")
    var start_date: String? = null,

    @Json(name="exp_date")
    var exp_date: String? = null,

    @Json(name="url")
    var url: String? = null

)