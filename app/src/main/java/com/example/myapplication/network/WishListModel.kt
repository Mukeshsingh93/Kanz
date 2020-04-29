package com.example.myapplication.network

import com.squareup.moshi.Json

data class WishListModel(

    @Json(name="id")
    var id: Int? = null,

    @Json(name="user_id")
    var user_id: String? = null,


    @Json(name="product")
    var product: Product? = null
)