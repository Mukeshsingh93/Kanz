package com.example.myapplication.network

import com.squareup.moshi.Json

data class GetCartList(

    @Json(name="id")
    var id: Int? = null,

    @Json(name="user_id")
    var user_id: Int? = null,

    @Json(name="product_id")
    var product_id: String? = null,

    @Json(name="quantity")
    var quantity: String? = null,

    @Json(name="coupon_numbers")
    var coupon_numbers: String? = null,

    @Json(name="status")
    var status: String? = null,

    @Json(name="products")
    var products: Product? = null

)