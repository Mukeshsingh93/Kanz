package com.example.myapplication.network

import com.squareup.moshi.Json
import java.io.Serializable

data class Slider(
    @Json(name="id")
    var id: Int? = null,

    @Json(name="name")
    var name: String? = null,

    @Json(name="description")
    var description: String? = null,

    @Json(name="is_active")
    var is_active: String? = null,

    @Json(name="total_quantity")
    var total_quantity: String? = null,

    @Json(name="price_per_unit")
    var price_per_unit: String? = null,


    @Json(name="images")
    var imageslist: MutableList<SliderImages>? = null,

    @Json(name="Images")
    var Images: MutableList<SliderImages>? = null,

    @Json(name="transactions")
    var transactions: Transaction? = null

):Serializable