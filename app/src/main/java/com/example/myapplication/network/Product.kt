package com.example.myapplication.network

import com.squareup.moshi.Json
import java.io.Serializable

data class Product(

    @Json(name="id")
    var id: Int? = null,

    @Json(name="name")
    var name: String? = null,

    @Json(name="description")
    var description: String? = null,

    @Json(name="price_per_unit")
    var price_per_unit: String? = null,

    @Json(name="total_quantity")
    var total_quantity: String? = null,

    @Json(name="category_id")
    var category_id: String? = null,

    @Json(name="images")
    var images: MutableList<SliderImages>? = null,

   /* @Json(name="images")
    var allimages: MutableList<SliderImages>? = null,*/

    @Json(name="transaction")
    var transactions: Transaction? = null,

  /*  @Json(name="Images")
    var Images: MutableList<ImageModel>? = null,*/

    @Json(name="offer")
    var offer: Offers? = null

    ): Serializable