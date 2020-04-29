package com.example.myapplication.network

import com.squareup.moshi.Json
import java.io.Serializable

data class OfferProducts(

    @Json(name="id")
    var id: Int? = null,

    @Json(name="name")
    var name: String? = null,

    @Json(name="description")
    var description: String? = null,

    @Json(name="image")
    var image: String? = null,

   @Json(name="product")
    var product: Slider? = null,

    @Json(name="price_per_unit")
    var price_per_unit: String? = null,

    @Json(name="total_quantity")
    var total_quantity: String? = null,

    @Json(name="quantity")
    var quantity: Int? = 1,

    @Json(name="offers")
    var offers: Offers? = null,

    @Json(name="in_wishlist")
    var in_wishlist: Boolean? = null,

    @Json(name="transactions")
    var transactions: Transaction? = null,

    @Json(name="images")
    var images: MutableList<SliderImages>? = null,

    @Json(name="like")
    var like: Int? = 0

):Serializable