package com.example.myapplication.network

import com.squareup.moshi.Json

data class CouponList(
    @Json(name="id")
    var id: Int? = null,

    @Json(name="coupon_copies")
    var coupon_copies: Coupon_Copies? = null
)