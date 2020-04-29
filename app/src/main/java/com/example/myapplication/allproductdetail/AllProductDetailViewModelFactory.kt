package com.example.myapplication.allproductdetail

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.Product
import com.example.myapplication.productdetail.ProductDetailViewModel

class AllProductDetailViewModelFactory (val sharedPreferences: SharedPreferences,
                                        val offerProducts: Product,
                                        private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllProductDetailViewModel::class.java)) {
            return AllProductDetailViewModel(sharedPreferences,offerProducts,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}