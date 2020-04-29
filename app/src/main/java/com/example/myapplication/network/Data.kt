package com.example.myapplication.network

import com.squareup.moshi.Json

data class Data(

    @Json(name="code")
    var code: String? = null,

    @Json(name="token")
    var token: String? = null,

    @Json(name="cart_count")
    var cart_count: String? = null,

    @Json(name="offers")
    var offers: MutableList<Offers>? = null,

    @Json(name="sliders")
    var sliders: MutableList<Slider>? = null,

    @Json(name="offer_product")
    var offerProducts: MutableList<OfferProducts>? = null

)