package com.example.myapplication.util

import com.example.myapplication.network.*
import com.squareup.moshi.Json

data class ResponseModel(
    @Json(name="status")
    var status: Int? = null,

    @Json(name="msg")
    var message: String? = null,

    @Json(name="data")
    var datas: Data? = null,

    @Json(name="otp_code")
    var otp_code: String? = null,

    @Json(name="token")
    var token: String? = null,

    @Json(name="tickets_numbers")
    var tickets_numbers: String? = null,

    @Json(name="wish_lists")
    var wish_lists: MutableList<WishListModel>? = null,

    @Json(name="cart_list")
    var cartlist: MutableList<GetCartList>? = null,

    @Json(name="orders")
    var orders: MutableList<GetCartList>? = null,

    @Json(name="products")
    var products: MutableList<ProductModel>? = null,

    @Json(name="Coupons")
    var Coupons: MutableList<CouponList>? = null,

    @Json(name="user")
    var user: User? = null


    /*@Json(name="data")
    var data: Data? = null*/







)