package com.example.myapplication.network

import com.squareup.moshi.Json

data class User(


    @Json(name="id")
    var id: Int? = null,

    @Json(name="name")
    var name: String? = null,


    @Json(name="email")
    var email: String? = null,


    @Json(name="phone")
    var phone: String? = null,


    @Json(name="address")
    var address: String? = null,

    @Json(name="image")
    var image: String? = null,


    @Json(name="product_id")
    var product_id: Product? = null
)