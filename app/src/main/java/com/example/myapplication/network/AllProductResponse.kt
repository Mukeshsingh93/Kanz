package com.example.myapplication.network

import com.squareup.moshi.Json

data class AllProductResponse(

    @Json(name="products")
    var products: MutableList<Product>? = null
)