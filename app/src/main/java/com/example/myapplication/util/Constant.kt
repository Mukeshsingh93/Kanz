package com.example.myapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object Constant
{

    const val ADDPRODUCTBTN = "ADDPRODUCTBTN"

    const val EMPTY ="EMPTY"
    const val SUCCEESSSTATUS = 200
    const val TOKENEXPIRED = 505
    const val TOKEN = ""
    const val LANGUAGAE = "LANGUAGAE"
    const val WEBVIEWTYPE = "WEBVIEWTYPE"
    const val HOWITWORKS = "HOWITWORKS"
    const val TERMCONDITION = "TERMCONDITION"
    const val ABOUTUS = "ABOUTUS"
    const val LOGINGVIEW = "LOGINGVIEW"
    const val APIRESPONSE = "APIRESPONSE"
    const val ADDTOCARTLISTVIEW = "ADDTOCARTLISTVIEW"
    const val PRODUCTVIEW = "PRODUCTVIEW"
    const val WISHLISTVIEW = "WISHLISTVIEW"
    const val ORDERVIEW = "ORDERVIEW"
    const val USERID = "USERID"
    const val FAQ = "FAQ"
    const val CONTACTUS = "CONTACTUS"
    const val PROFILEVIEW = "PROFILEVIEW"
    const val WEBVIEW = "WEBVIEW"
    const val       PRODUCTDETAIL = "PRODUCTDETAIL"
    const val ALLPRODUCTDETAIL = "ALLPRODUCTDETAIL"
    const val OFFERDETAIL = "OFFERDETAIL"

    fun connected(context:Context) : Boolean
    {
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return  isConnected
      //  return true
    }

}

