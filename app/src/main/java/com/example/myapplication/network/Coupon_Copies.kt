package com.example.myapplication.network

import com.squareup.moshi.Json

data class Coupon_Copies(


    @Json(name="serial")
    var serial: String? = null,

    @Json(name="coupons")
    var coupons: Coupons? = null
)