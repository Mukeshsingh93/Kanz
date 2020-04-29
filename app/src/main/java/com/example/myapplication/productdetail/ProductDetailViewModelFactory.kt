package com.example.myapplication.productdetail

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.order.OrderViewModel

class ProductDetailViewModelFactory (val sharedPreferences: SharedPreferences,
                                     val offerProducts: OfferProducts,
                                     private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            return ProductDetailViewModel(sharedPreferences,offerProducts,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}