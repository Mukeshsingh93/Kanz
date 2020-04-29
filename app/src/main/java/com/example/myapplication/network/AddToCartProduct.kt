package com.example.myapplication.network

import com.squareup.moshi.Json

class AddToCartProduct (
    @Json(name="id")
    var id: Int? = null,

    @Json(name="name")
    var name: String? = null
)