package com.example.myapplication.network

import com.squareup.moshi.Json
import java.io.Serializable

data class Transaction(
    @Json(name="sold_quantity")
    var sold_quantity: String? = null
): Serializable