package com.example.myapplication.wishlist

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.verification.VerificationViewModel

class WishlistViewModelFactory (val sharedPreferences: SharedPreferences,
                                private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
            return WishlistViewModel(sharedPreferences,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}