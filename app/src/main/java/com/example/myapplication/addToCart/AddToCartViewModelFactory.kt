package com.example.myapplication.addToCart

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.forgetpassword.ForgetPasswordViewModel

class AddToCartViewModelFactory (val sharedPreferences: SharedPreferences,
                                 private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddToCartViewModel::class.java)) {
            return AddToCartViewModel(sharedPreferences,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}