package com.example.myapplication.network

import com.squareup.moshi.Json

data class ProductModel (

    @Json(name="price")
    var price: String? = null,

    @Json(name="mrp")
    var mrp: String? = null,

    @Json(name="main_image")
    var main_image: String? = null,

    @Json(name="id")
    var id: Int? = null,

    @Json(name="name")
    var name: String? = null,

    @Json(name="image_path")
    var image_path: String? = null,

    @Json(name="message")
    var message: String? = null,

    @Json(name="status")
    var status: Int? = null
)