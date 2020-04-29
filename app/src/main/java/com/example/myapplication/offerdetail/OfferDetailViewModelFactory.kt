package com.example.myapplication.offerdetail

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.network.OfferProducts
import com.example.myapplication.network.Offers

class OfferDetailViewModelFactory (val sharedPreferences: SharedPreferences,
                                   val offerProducts: Offers,
                                   private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OfferDetailViewModel::class.java)) {
            return OfferDetailViewModel(sharedPreferences,offerProducts,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}