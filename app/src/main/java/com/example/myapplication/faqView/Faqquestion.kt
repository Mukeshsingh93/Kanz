package com.example.myapplication.faqView

import com.example.myapplication.network.Offers
import com.squareup.moshi.Json

data class Faqquestion(

    @Json(name="name")
    var ques: String? = null,

    @Json(name="offers")
    var offers: MutableList<Offers>? = null


    )