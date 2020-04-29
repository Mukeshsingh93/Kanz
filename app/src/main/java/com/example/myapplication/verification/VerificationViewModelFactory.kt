package com.example.myapplication.verification

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.userreg.RegisterViewModel

class VerificationViewModelFactory (val sharedPreferences: SharedPreferences,
                                    val otp: String,
                                    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerificationViewModel::class.java)) {
            return VerificationViewModel(sharedPreferences,otp,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}